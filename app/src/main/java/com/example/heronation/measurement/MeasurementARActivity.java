package com.example.heronation.measurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heronation.R;
import com.example.heronation.measurement.helper.CameraPermissionHelper;
import com.example.heronation.measurement.helper.DisplayRotationHelper;
import com.example.heronation.measurement.helper.FullScreenHelper;
import com.example.heronation.measurement.helper.SnackbarHelper;
import com.example.heronation.measurement.helper.TapHelper;
import com.example.heronation.measurement.helper.TrackingStateHelper;
import com.example.heronation.measurement.renderer.BackgroundRenderer;
import com.example.heronation.measurement.renderer.ObjectRenderer;
import com.example.heronation.measurement.renderer.PointCloudRenderer;
import com.example.heronation.measurement.renderer.PlaneRenderer;
import com.example.heronation.measurement.renderer.RectanglePolygonRenderer;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Point;
import com.google.ar.core.PointCloud;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;

/* 측정 클래스, MeasureFragment에서 카테고리에 따른 측정항목이 넘어오면, 해당 항목을 측정해서 결과값을 MeasureResult로 넘겨줌. */
public class MeasurementARActivity extends AppCompatActivity implements GLSurfaceView.Renderer {
    private static final String TAG = MeasurementARActivity.class.getSimpleName();

    // Rendering. 여기서 렌더러가 생성, GL surface가 생성 될 때 초기화
    @BindView(R.id.surfaceview) GLSurfaceView surfaceView;

    private boolean installRequested; // ARCore 설치 유무

    private Session session;
    private final SnackbarHelper messageSnackbarHelper = new SnackbarHelper(); // Snackbar 관련
    private DisplayRotationHelper displayRotationHelper; // Device 각도 관련
    private final TrackingStateHelper trackingStateHelper = new TrackingStateHelper(this); // 현재 상태를 텍스트로 알려줌
    private TapHelper tapHelper; // Gesture 관련

    private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
    private final ObjectRenderer virtualObject = new ObjectRenderer(); // 화면에 보여줄 Object
    private final PlaneRenderer planeRenderer = new PlaneRenderer();
    private final PointCloudRenderer pointCloudRenderer = new PointCloudRenderer();  // Rendering 됐을 시 그려줄 Object
    private RectanglePolygonRenderer lineRenderer=new RectanglePolygonRenderer(); // 점과 점 사이의 선을 그리는 Object

    private int viewHeight=0;
    private int viewWidth=0;
    float[] projmtx = new float[16]; // projection matrix
    float[] viewmtx = new float[16]; // camera matrix

    // Temporary matrix allocated here to reduce number of allocations for each frame.
    private final float[] anchorMatrix = new float[16];
    private static final float[] DEFAULT_COLOR = new float[] {0f, 0f, 0f, 0f};

    private static final String SEARCHING_PLANE_MESSAGE = "평면을 찾는 중입니다.";

    @BindView(R.id.gifImageView) pl.droidsonroids.gif.GifImageView gifImageView;
    @BindView(R.id.distance_textview) TextView distanceTextview;
    @BindView(R.id.delete_image_button) ImageButton deleteButton;
    @BindView(R.id.prev_image_button) ImageButton prevButton;
    @BindView(R.id.next_image_button) ImageButton nextButton;
    @BindView(R.id.guide_image_button) ImageButton guideButton;
    @BindView(R.id.measure_item_textview) TextView measureItemTextview;

    private Integer measurement_count=0; // 현재 측정된 항목의 개수
    private Integer measurement_item_size; // 전체 측정 항목의 개수
    public static Double[] measurement_items_distance; // 측정 항목을 저장하는 배열

    // Anchors created from taps used for object placing with a given color.
    private static class ColoredAnchor {
        public final Anchor anchor;
        public final float[] color;

        public ColoredAnchor(Anchor a, float[] color4f) {
            this.anchor = a;
            this.color = color4f;
        }
    }

    private ArrayList<ColoredAnchor> anchors = new ArrayList<>();
    private List<float[]> mPoints=new ArrayList<float[]>(); // anchor의 좌표를 저장하는 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_ar);
        ButterKnife.bind(this);
        displayRotationHelper = new DisplayRotationHelper(/*context=*/ this);

        measurement_item_size=MeasurementArFragment.Measure_item.size(); // 전체 측정 항목의 개수
        measurement_count=0; // 현재 측정된 항목의 개수
        measurement_items_distance=new Double[20];
        mPoints=new ArrayList<float[]>(); // anchor의 좌표를 저장하는 배열

        installRequested = false;

        // Set up tap listener.
        tapHelper = new TapHelper(/*context=*/ this);
        surfaceView.setOnTouchListener(tapHelper);
        // Set up renderer.
        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        surfaceView.setRenderer(this);
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        surfaceView.setWillNotDraw(false);

        showLoadingMessage(this); //처음 gif 로딩 화면을 띄워줌

        measureItemTextview.setText(MeasurementArFragment.Measure_item.get(measurement_count)); //첫 측정 항목 설정

        // 삭제 버튼을 눌렀을 경우
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anchors.size()>0) // 이미 찍힌 점이 있다면,
                {
                    anchors.get(anchors.size()-1).anchor.detach();
                    anchors.remove(anchors.size()-1); // 해당 점을 삭제
                    distanceTextview.setText("점을 찍어주세요."); // textview 설정
                }
            }
        });

        // 다음 버튼을 눌렀을 경우, 다음 측정 항목으로 넘어감
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 상태가, anchor의 점이 2개 찍힌 상태이고, 측정 항목의 개수보다 적은 상태에서
                // 다음 버튼을 누를 시에, 원래 찍혀있던 anchor를 초기화함으로써 제거하고,
                // anchor의 좌표를 저장하는 mPoints 또한 초기화함으로써 제거,
                // 실제 거리 "수치"로 설정된 textview를 "거리"로 변경
                // 다음 측정 항목으로 변경
                // 측정한 횟수인 count 를 1 +
                if(measurement_count<measurement_item_size-1 && anchors.size()==2){
                    anchors=new ArrayList<>();
                    mPoints=new ArrayList<>();

                    measurement_count++;

                    distanceTextview.setText("점을 찍어주세요.");
                    measureItemTextview.setText(MeasurementArFragment.Measure_item.get(measurement_count));
                }
                // 현재 상태가, 측정이 모두 완료된 상태이면,
                // 각 측정 항목의 거리를 전송
                else if(measurement_count==measurement_item_size-1){
                    Log.d("측정항목",measurement_items_distance[0]+"");
                    Intent intent=new Intent(getApplicationContext(),MeasurementResultActivity.class);
                    intent.putExtra("distance",measurement_items_distance);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //저장되는 값을 초기화시켜줌
        measurement_count=0; // 현재 측정된 항목의 개수
        measurement_items_distance=new Double[20];
        mPoints=new ArrayList<float[]>(); // anchor의 좌표를 저장하는 배열
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (session == null) {
            Exception exception = null;
            String message = null;
            try {
                switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
                    case INSTALL_REQUESTED:
                        installRequested = true;
                        return;
                    case INSTALLED:
                        break;
                }

                // ARCore 는 카메라 권한이 필요. 아직 Android M 이상에서 런타임 권한을 얻지 못한 경우에 사용자에게 권한 요청
                if (!CameraPermissionHelper.hasCameraPermission(this)) {
                    CameraPermissionHelper.requestCameraPermission(this);
                    return;
                }

                // Create the session.
                session = new Session(/* context= */ this);

            } catch (UnavailableArcoreNotInstalledException
                    | UnavailableUserDeclinedInstallationException e) {
                message = "ARCore를 설치해 주세요.";
                exception = e;
            } catch (UnavailableApkTooOldException e) {
                message = "ARCore를 업데이트 해 주세요.";
                exception = e;
            } catch (UnavailableSdkTooOldException e) {
                message = "앱을 업데이트 해 주세요.";
                exception = e;
            } catch (UnavailableDeviceNotCompatibleException e) {
                message = "AR을 지원하지 않는 단말입니다.";
                exception = e;
            } catch (Exception e) {
                message = "AR 생성에 실패했습니다.";
                exception = e;
            }

            if (message != null) {
                messageSnackbarHelper.showError(this, message);
                Log.e(TAG, "에러가 발생했습니다.", exception);
                return;
            }
        }

        // Note that order matters - see the note in onPause(), the reverse applies here.
        try {
            session.resume();
        } catch (CameraNotAvailableException e) {
            messageSnackbarHelper.showError(this, "카메라를 사용할 수 없습니다. 앱을 다시 시작하세요.");
            session = null;
            return;
        }
        surfaceView.onResume();
        displayRotationHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (session != null) {
            // Note that the order matters - GLSurfaceView is paused first so that it does not try
            // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
            // still call session.update() and get a SessionPausedException.
            displayRotationHelper.onPause();
            surfaceView.onPause();
            session.pause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus);
    }

    public void showLoadingMessage(MeasurementARActivity measurementARActivity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gifImageView.setBackgroundResource(R.drawable.mobile_moving);
                messageSnackbarHelper.showMessage(MeasurementARActivity.this, "측정을 시작하려면 스마트폰을\n좌우로 기울여주세요");
            }
        });
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
        try {
            // Create the texture and pass it to ARCore session to be filled during update().
            backgroundRenderer.createOnGlThread(/*context=*/ this);
            planeRenderer.createOnGlThread(/*context=*/ this, "models/trigrid.png");
            pointCloudRenderer.createOnGlThread(/*context=*/ this);
            lineRenderer=new RectanglePolygonRenderer();

            virtualObject.createOnGlThread(/*context=*/ this, "models/dot.obj", "models/andy.png");
            virtualObject.setMaterialProperties(0.0f, 2.0f, 0.5f, 6.0f);
        } catch (IOException e) {
            Log.e(TAG, "Failed to read an asset file", e);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        displayRotationHelper.onSurfaceChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
        viewWidth=width;
        viewHeight=height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear screen to notify driver it should not load any pixels from previous frame.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (session == null) {
            return;
        }
        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        displayRotationHelper.updateSessionIfNeeded(session);

        try {
            session.setCameraTextureName(backgroundRenderer.getTextureId());

            // Obtain the current frame from ARSession. When the configuration is set to
            // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
            // camera framerate.
            Frame frame = session.update();
            Camera camera = frame.getCamera();

            // Handle one tap per frame.
            handleTap(frame, camera);

            // If frame is ready, render camera preview image to the GL surface.
            backgroundRenderer.draw(frame);

            // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
            trackingStateHelper.updateKeepScreenOnFlag(camera.getTrackingState());

            // If not tracking, don't draw 3D objects, show tracking failure reason instead.
            if (camera.getTrackingState() == TrackingState.PAUSED) {
                messageSnackbarHelper.showMessage(
                        this, TrackingStateHelper.getTrackingFailureReasonString(camera));
                return;
            }

            // Get projection matrix.
            projmtx = new float[16];
            camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f);

            // Get camera matrix and draw.
            viewmtx = new float[16];
            camera.getViewMatrix(viewmtx, 0);

            // Compute lighting from average intensity of the image.
            // The first three components are color scaling factors.
            // The last one is the average pixel intensity in gamma space.
            final float[] colorCorrectionRgba = new float[4];
            frame.getLightEstimate().getColorCorrection(colorCorrectionRgba, 0);

            // Visualize tracked points.
            // Use try-with-resources to automatically release the point cloud.
            try (PointCloud pointCloud = frame.acquirePointCloud()) {
                pointCloudRenderer.update(pointCloud);
                pointCloudRenderer.draw(viewmtx, projmtx);
            }

            // 이 시점에서 추적 오류 X. 평면이 감지되면 메시지 UI 숨김.
            // 그렇지 않으면 검색 Plane 메시지를 표시.
            if (hasTrackingPlane()) {
                //평면이 그려질 때, 가이드를 숨김
                gifImageView.setVisibility(View.INVISIBLE);
                messageSnackbarHelper.hide(this);
            } else {
                messageSnackbarHelper.showMessage(this, SEARCHING_PLANE_MESSAGE);
            }

            // Visualize planes.
            planeRenderer.drawPlanes(
                    session.getAllTrackables(Plane.class), camera.getDisplayOrientedPose(), projmtx);

            // Visualize anchors created by touch.
            float scaleFactor = 0.15f;
            for (ColoredAnchor coloredAnchor : anchors) {
                if (coloredAnchor.anchor.getTrackingState() != TrackingState.TRACKING) {
                    continue;
                }
                // Get the current pose of an Anchor in world space. The Anchor pose is updated
                // during calls to session.update() as ARCore refines its estimate of the world.
                coloredAnchor.anchor.getPose().toMatrix(anchorMatrix, 0);
                // Update and draw the model and its shadow.
                virtualObject.updateModelMatrix(anchorMatrix, scaleFactor);
                virtualObject.draw(viewmtx, projmtx, colorCorrectionRgba, coloredAnchor.color);
            }

            if(anchors.size()==2){
                // 두 점이 모두 찍혔을 때, 선을 그려줌
                drawLine(anchors.get(0).anchor.getPose(),anchors.get(1).anchor.getPose(),viewmtx,projmtx);
            }

        } catch (Throwable t) {
            // Avoid crashing the application due to unhandled exceptions.
            Log.e(TAG, "Exception on the OpenGL thread", t);
        }
    }

    // 두 점 사이의 선을 그리는 함수
    private void drawLine(Pose pose0, Pose pose1, float[] viewmtx, float[] projmtx) {
        float lineWidth = 0.004f;
        float lineWidthH = lineWidth / viewHeight * viewWidth;
        lineRenderer.setVerts(
                pose0.tx() - lineWidth, pose0.ty() + lineWidthH, pose0.tz() - lineWidth,
                pose0.tx() + lineWidth, pose0.ty() + lineWidthH, pose0.tz() + lineWidth,
                pose1.tx() + lineWidth, pose1.ty() + lineWidthH, pose1.tz() + lineWidth,
                pose1.tx() - lineWidth, pose1.ty() + lineWidthH, pose1.tz() - lineWidth,
                pose0.tx() - lineWidth, pose0.ty() - lineWidthH, pose0.tz() - lineWidth,
                pose0.tx() + lineWidth, pose0.ty() - lineWidthH, pose0.tz() + lineWidth,
                pose1.tx() + lineWidth, pose1.ty() - lineWidthH, pose1.tz() + lineWidth,
                pose1.tx() - lineWidth, pose1.ty() - lineWidthH, pose1.tz() - lineWidth
        );
        lineRenderer.draw(viewmtx, projmtx);
    }

    // Handle only one tap per frame, as taps are usually low frequency compared to frame rate.
    private void handleTap(Frame frame, Camera camera) {
        if(anchors.size()<2) {
            MotionEvent tap = tapHelper.poll();
            if (tap != null && camera.getTrackingState() == TrackingState.TRACKING) {
                for (HitResult hit : frame.hitTest(tap)) {
                    // Check if any plane was hit, and if it was hit inside the plane polygon
                    Trackable trackable = hit.getTrackable();
                    // Creates an anchor if a plane or an oriented point was hit.
                    if ((trackable instanceof Plane
                            && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())
                            && (PlaneRenderer.calculateDistanceToPlane(hit.getHitPose(), camera.getPose()) > 0))
                            || (trackable instanceof Point
                            && ((Point) trackable).getOrientationMode()
                            == Point.OrientationMode.ESTIMATED_SURFACE_NORMAL)) {

                        // Assign a color to the object for rendering based on the trackable type
                        // this anchor attached to. For AR_TRACKABLE_POINT, it's blue color, and
                        // for AR_TRACKABLE_PLANE, it's green color.
                        float[] objColor;
                        if (trackable instanceof Point) {
                            objColor = new float[]{66.0f, 133.0f, 244.0f, 255.0f};
                        } else if (trackable instanceof Plane) {
                            objColor = new float[]{139.0f, 195.0f, 74.0f, 255.0f};
                        } else {
                            objColor = DEFAULT_COLOR;
                        }

                        // Adding an Anchor tells ARCore that it should track this position in
                        // space. This anchor is created on the Plane to place the 3D model
                        // in the correct position relative both to the world and to the plane.
                        anchors.add(new ColoredAnchor(hit.createAnchor(), objColor));

                        if (anchors.size() == 1) {
                            float anchorX = anchors.get(0).anchor.getPose().tx();
                            float anchorY = anchors.get(0).anchor.getPose().ty();
                            float anchorZ = anchors.get(0).anchor.getPose().tz();
                            float[] points = new float[]{anchorX, anchorY, anchorZ};
                            mPoints.add(0, points); // 해당 점의 좌표를 배열에 저장
                        } else if (anchors.size() == 2) {
                            float anchorX = anchors.get(1).anchor.getPose().tx();
                            float anchorY = anchors.get(1).anchor.getPose().ty();
                            float anchorZ = anchors.get(1).anchor.getPose().tz();
                            float[] points = new float[]{anchorX, anchorY, anchorZ};
                            mPoints.add(1,points);
                            //거리 업데이트
                            updateDistance(mPoints.get(0),mPoints.get(1));
                        }

                        break;
                    }
                }
            }
        }
    }

    /** Checks if we detected at least one plane. */
    private boolean hasTrackingPlane() {
        for (Plane plane : session.getAllTrackables(Plane.class)) {
            if (plane.getTrackingState() == TrackingState.TRACKING) {
                return true;
            }
        }
        return false;
    }

    public void updateDistance(float[] start, float[] end){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Integer min_scope= Integer.parseInt(MeasurementArFragment.min_scope.get(measurement_count)); // 해당 측정 항목의 거리의 최소값
                Integer max_scope = Integer.parseInt(MeasurementArFragment.max_scope.get(measurement_count)); // 해당 측정 항목의 거리의 최대값
                double distance=0.0;
                if(mPoints.size()>=2){
                    distance=Math.sqrt((start[0]-end[0])*(start[0]-end[0])+(start[1]-end[1])*(start[1]-end[1])+(start[2]-end[2])*(start[2]-end[2])); // 거리 구하기
                    String distanceString=String.format(Locale.getDefault(),"%.2f",distance*100)+"cm";
                    // 측정 항목의 범위 안에 속할 경우
                    if((distance*100 >= min_scope) && (max_scope >= distance*100)) {
                        distanceTextview.setText(distanceString);
                        measurement_items_distance[measurement_count]=distance;
                    }
                    // 측정 항목의 범위 안에 속하지 않을 경우
                    else{
                        distanceTextview.setText(distanceString+"\n"
                                +"측정 가능한 범위에서 측정해주세요 \n"
                                +"측정 가능한 범위는 "+min_scope+"cm"+"~"+max_scope+"cm 입니다.");
                        anchors.get(1).anchor.detach();
                        anchors.remove(1);
                    }
                }
            }
        });
    }
}

