package com.example.heronation.measurement.AR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.measurement.AR.InnerGuide.InnerGuideViewPager;
import com.example.heronation.measurement.AR.helper.CameraPermissionHelper;
import com.example.heronation.measurement.AR.helper.DisplayRotationHelper;
import com.example.heronation.measurement.AR.helper.FullScreenHelper;
import com.example.heronation.measurement.AR.helper.SnackbarHelper;
import com.example.heronation.measurement.AR.helper.TapHelper;
import com.example.heronation.measurement.AR.helper.TrackingStateHelper;
import com.example.heronation.measurement.AR.renderer.BackgroundRenderer;
import com.example.heronation.measurement.AR.renderer.ObjectRenderer;
import com.example.heronation.measurement.AR.renderer.PointCloudRenderer;
import com.example.heronation.measurement.AR.renderer.PlaneRenderer;
import com.example.heronation.measurement.AR.renderer.RectanglePolygonRenderer;
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
import com.google.ar.core.exceptions.NotTrackingException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

import java.io.IOException;
import java.util.ArrayList;
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
    @BindView(R.id.measurement_item_guide_button) ImageButton measurement_item_guide_button;
    @BindView(R.id.delete_image_button) ImageButton deleteButton;
    @BindView(R.id.prev_image_button) ImageButton prevButton;
    @BindView(R.id.next_image_button) ImageButton nextButton;
    @BindView(R.id.guide_image_button) ImageButton guideButton;
    @BindView(R.id.measure_item_textview) TextView measureItemTextview;
    @BindView(R.id.measurement_item_guide_layout) RelativeLayout measurement_item_guide_layout;
    @BindView(R.id.measurement_item_guide_imageview) ImageView measurement_item_guide_imageview;
    @BindView(R.id.measurement_item_guide_close_button) ImageView measurement_item_guide_close_button;

    @BindView(R.id.standard_measurement_guide_layout) RelativeLayout standard_measurement_guide_layout;
    @BindView(R.id.indicator) ImageView indicator;
    @BindView(R.id.standard_measurement_guide_close_button) ImageButton standard_measurement_guide_close_button;
    @BindView(R.id.measurement_guide_viewPager) ViewPager measurement_guide_viewPager;
    FragmentPagerAdapter adapterViewPager;

    private Integer measurement_count=0; // 현재 측정된 항목의 개수
    private Integer measurement_item_size; // 전체 측정 항목의 개수
    public static Double[] measurement_items_distance; // 측정 항목을 저장하는 배열
    Integer min_scope; // 해당 측정 항목의 거리의 최소값
    Integer max_scope; // 해당 측정 항목의 거리의 최대값

    // Anchors created from taps used for object placing with a given color.
    private static class ColoredAnchor {
        public final Anchor anchor;
        public final float[] color;

        public ColoredAnchor(Anchor a, float[] color4f) {
            this.anchor = a;
            this.color = color4f;
        }
    }

    private ArrayList<ColoredAnchor> anchors = new ArrayList<>(); // 측정 항목당 두 개의 anchor를 저장하는 배열
    private float[][] mPoints; // anchor의 좌표를 저장하는 배열
    private ArrayList[] allAnchors; // 이전에 찍은 모든 점을 저장하는 리스트

    public static int long_pressed_flag; // anchor를 꾹 누르는 이벤트를 판단하는 flag 변수
    private final int DEFAULT_VALUE=-1; // 현재 터치하고 있는 anchor index의 디폴트값
    private int nowTouchingPointIndex=DEFAULT_VALUE; // 현재 터치하고 있는 anchor의 index
    private float x, y; // 특정 지점을 터치한 좌표를 담는 변수
    private int outOfRange=0; // 거리가 범위 안에 속하는지 판단하는 변수

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_ar);
        ButterKnife.bind(this);
        displayRotationHelper = new DisplayRotationHelper(/*context=*/ this);

        measurement_item_size=MeasurementArFragment.Measure_item.size(); // 전체 측정 항목의 개수
        measurement_count=0; // 현재 측정된 항목의 개수
        measurement_items_distance=new Double[20];
        mPoints=new float[2][3]; // anchor의 좌표를 저장하는 배열, 2개의 anchor, x,y,z 좌표
        allAnchors= new ArrayList[measurement_item_size]; // 이전에 찍은 모든 점을 저장하는 리스트
       long_pressed_flag=0;
       nowTouchingPointIndex=DEFAULT_VALUE;
       outOfRange=0;
       min_scope = Integer.parseInt(MeasurementArFragment.min_scope.get(measurement_count)); // 해당 측정 항목의 거리의 최소값
       max_scope = Integer.parseInt(MeasurementArFragment.max_scope.get(measurement_count)); // 해당 측정 항목의 거리의 최대값

        installRequested = false;

        // Set up tap listener.
        tapHelper = new TapHelper(/*context=*/ this);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:                                               //Touch 감지
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:                                               //Drag 감지
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:                                                 //화면에서 손을 떼는 것 감지
                        long_pressed_flag = 0;
                        break;
                }
                return tapHelper.onTouch(surfaceView,event);
            }
        });
        // Set up renderer.
        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        surfaceView.setRenderer(this);
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        surfaceView.setWillNotDraw(false);

        showLoadingMessage(this); //처음 gif 로딩 화면을 띄워줌

        measureItemTextview.setText(MeasurementArFragment.Measure_item.get(measurement_count)); //첫 측정 항목 설정

        // 왼쪽 상단 가이드 버튼을 눌렀을 경우
        measurement_item_guide_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measurement_item_guide_layout.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(MeasurementArFragment.Image_item.get(measurement_count)).into(measurement_item_guide_imageview);
            }
        });

        // 왼쪽 상단 가이드 닫는 버튼을 눌렀을 경우
        measurement_item_guide_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measurement_item_guide_layout.setVisibility(View.INVISIBLE);
            }
        });

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

        // 이전 버튼을 눌렀을 경우
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(measurement_count > 0) // 첫번째 측정항목이 아닐 경우에, 이전 버튼 활성화
                {
                    measurement_count--; // 이전 버튼 누를 시 measurement_count 1 감소
                    anchors=allAnchors[measurement_count]; // 이전 버튼을 눌렀을 때 이동한 측정항목으로 anchors를 설정
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 측정 항목에 해당하는 anchor 거리 설정, 측정 항목 설정
                            distanceTextview.setText(String.format(Locale.getDefault(),"%.2f",measurement_items_distance[measurement_count]*100)+"cm");
                            measureItemTextview.setText(MeasurementArFragment.Measure_item.get(measurement_count));
                        }
                    });
                    outOfRange=0;
                }

            }
        });

        // 다음 버튼을 눌렀을 경우, 다음 측정 항목으로 넘어감
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 측정 거리가 범위에서 벗어났을 경우
                if(outOfRange==1){
                    Toast.makeText(getApplicationContext(),"측정 가능한 범위에서 측정해주세요.",Toast.LENGTH_SHORT).show();
                }
                // 현재 상태가, anchor의 점이 2개 찍힌 상태이고, 측정 항목의 개수보다 적은 상태에서
                // 다음 측정 항목이 측정이 안되어있는 상태일 경우
                // 다음 버튼을 누를 시에, 원래 찍혀있던 anchor를 초기화함으로써 제거하고,
                // 측정한 횟수인 measurement_count를 1 증가
                // 실제 거리 "수치"로 설정된 textview를 "점을 찍어주세요."로 변경
                // 다음 측정 항목으로 변경
                else if(measurement_count<measurement_item_size-1 && anchors.size()==2 && allAnchors[measurement_count+1]==null){
                    anchors=new ArrayList<>();
                    measurement_count++;

                    distanceTextview.setText("점을 찍어주세요.");
                    measureItemTextview.setText(MeasurementArFragment.Measure_item.get(measurement_count));
                    nowTouchingPointIndex=DEFAULT_VALUE;
                    outOfRange=0;
                }
                // 현재 상태가, anchor의 점이 2개 찍힌 상태이고, 측정 항목의 개수보다 적은 상태에서
                // 다음 측정 항목이 측정이 되어있는 상태일 경우
                // 측정한 횟수인 measurement_count를 1 증가
                // 다음 버튼을 누를 시에, 현재 측정 항목을 allAnchors에서 찾아 anchors에 지정해주고,
                // 현재 측정 항목의 거리로 textview를 변경
                // 현재 측정 항목으로 textview를 변경
                else if(measurement_count<measurement_item_size-1 && anchors.size()==2  && allAnchors[measurement_count+1]!=null){
                    measurement_count++;
                    anchors=allAnchors[measurement_count]; // 이전 버튼을 눌렀을 때 이동한 측정항목으로 anchors를 설정
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 측정 항목에 해당하는 anchor 거리 설정, 측정 항목 설정
                            distanceTextview.setText(String.format(Locale.getDefault(),"%.2f",measurement_items_distance[measurement_count]*100)+"cm");
                            measureItemTextview.setText(MeasurementArFragment.Measure_item.get(measurement_count));
                            nowTouchingPointIndex=DEFAULT_VALUE;
                            outOfRange=0;
                        }
                    });

                }
                // 현재 상태가, 측정이 모두 완료된 상태이면,
                // 각 측정 항목의 거리를 전송
                else if(anchors.size()==2 && measurement_count==measurement_item_size-1){
                    Log.d("측정항목",measurement_items_distance[0]+"");
                    Intent intent=new Intent(getApplicationContext(), MeasurementResultActivity.class);
                    intent.putExtra("distance",measurement_items_distance);
                    startActivity(intent);
                    finish();
                }

            }
        });

       // 오른쪽 하단 가이드 버튼을 눌렀을 경우
       guideButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               standard_measurement_guide_layout.setVisibility(View.VISIBLE);
               adapterViewPager=new InnerGuideViewPager.MyPagerAdapter(getSupportFragmentManager());
               measurement_guide_viewPager.setAdapter(adapterViewPager);
               measurement_guide_viewPager.setBackgroundColor(0x7F000000);
               indicator.setBackgroundResource(R.drawable.indicator1);
               measurement_guide_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                   public void onPageScrollStateChanged(int state) {
                   }

                   public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                   }

                   public void onPageSelected(int position) { //화면 전환 이벤트 처리
                       if (position == 0) {
                           indicator.setBackgroundResource(R.drawable.indicator1);
                       } else if (position == 1) {
                           indicator.setBackgroundResource(R.drawable.indicator2);
                       } else {
                           indicator.setBackgroundResource(R.drawable.indicator3);
                       }
                   }
               });
           }
       });

       // 오른쪽 하단 가이드의 닫기버튼을 눌렀을 경우
       standard_measurement_guide_close_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               standard_measurement_guide_layout.setVisibility(View.INVISIBLE);
           }
       });
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

        nowTouchingPointIndex=DEFAULT_VALUE; // 현재 touching point index를 -1로 설정
    }

    /*
    -      처음에 아무것도 없을 때 – anchor를 두는 로직
1)    화면을 탭하면, handleTap 함수에서, tap한 위치에 anchor가 생성된다.
2)    Anchor가 생성됨과 동시에 Anchor를 화면 상에 그려줌 (virtualObject.draw)
3)    그 후에, Anchor 2개가 찍히는 순간에 거리를 측정함
3-1) 거리가 범위 안에 속할 경우, 범위 플래그 outOfRange=0으로 설정
3-2) 거리가 범위 바깥에 속할 경우, 범위 플래그 outOfRange=1으로 설정 => 여기서 원래는 anchor가 놓이면 안되지만, 로직이 생각이 안나서… anchor를 삭제하지는 않고, 다음 버튼을 눌렀을 때, outOfRange 플래그를 이용하여 다음 버튼이 작동하지 않도록 함
-      점이 2개 놓였을떄, 꾹 눌렀을 떄의 로직
1)    꾹 눌렀을 때, 점이 선택된 nowTouchingPointIndex를 확인.
2)    handleMoveEvent 함수에서 선택된 anchor를 삭제하고, 해당 nowTouchingPointIndex에 드래그한 새로운 위치에 nowSelectedAnchor를 위치하게 함
3)    그 후에, 새롭게 만들어진 anchor의 포인트를 받아, updateDistance 함수를 통해 거리를 업데이트함.
*/

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


            if (anchors != null && !anchors.isEmpty()) {
                if (nowTouchingPointIndex != DEFAULT_VALUE) {
                    // Get the current pose of an Anchor in world space. The Anchor pose is updated
                    // during calls to session.update() as ARCore refines its estimate of the world.
                    anchors.get(nowTouchingPointIndex).anchor.getPose().toMatrix(anchorMatrix, 0);

                    // Update and draw the model and its shadow.
                    virtualObject.updateModelMatrix(anchorMatrix, scaleFactor);
                    virtualObject.draw(viewmtx, projmtx, colorCorrectionRgba, anchors.get(nowTouchingPointIndex).color);
                    checkIfHit(virtualObject, nowTouchingPointIndex);
                }

                    Pose point0 = anchors.get(0).anchor.getPose();
                    point0.toMatrix(anchorMatrix, 0);
                    virtualObject.updateModelMatrix(anchorMatrix, scaleFactor);
                    virtualObject.draw(viewmtx, projmtx, colorCorrectionRgba, anchors.get(0).color);
                    checkIfHit(virtualObject, 0);

                    if(anchors.size()==2){
                    Pose point1 = anchors.get(1).anchor.getPose();
                    point1.toMatrix(anchorMatrix, 0);
                    virtualObject.updateModelMatrix(anchorMatrix, scaleFactor);
                    virtualObject.draw(viewmtx, projmtx, colorCorrectionRgba, anchors.get(1).color);
                    checkIfHit(virtualObject, 1);}

                if(anchors.size()==2){
                    // 두 점이 모두 찍혔을 때, 선을 그려줌
                    drawLine(anchors.get(0).anchor.getPose(),anchors.get(1).anchor.getPose(),viewmtx,projmtx);
                }
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
        MotionEvent tap = tapHelper.queuedSingleTapsPoll();
        if (anchors.size() < 2 && tap != null && camera.getTrackingState() == TrackingState.TRACKING) {
            for (HitResult hit : frame.hitTest(x,y)) {
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
                    // this anchor attached to. - 파란색
                    float[] objColor = new float[]{66.0f, 133.0f, 244.0f, 255.0f};

                    // Adding an Anchor tells ARCore that it should track this position in
                    // space.
                    anchors.add(new ColoredAnchor(hit.createAnchor(), objColor));

                    if (anchors.size() == 1) {
                        float anchorX = anchors.get(0).anchor.getPose().tx();
                        float anchorY = anchors.get(0).anchor.getPose().ty();
                        float anchorZ = anchors.get(0).anchor.getPose().tz();
                        float[] points = new float[]{anchorX, anchorY, anchorZ};
                        mPoints[0] = points; // 해당 점의 좌표를 배열에 저장
                    } else if (anchors.size() == 2) {
                        float anchorX = anchors.get(1).anchor.getPose().tx();
                        float anchorY = anchors.get(1).anchor.getPose().ty();
                        float anchorZ = anchors.get(1).anchor.getPose().tz();
                        float[] points = new float[]{anchorX, anchorY, anchorZ};
                        mPoints[1] = points; // 해당 점의 좌표를 배열에 저장
                        updateDistance(mPoints[0], mPoints[1]); //거리 업데이트
                    }

                    break;
                }
            }
        }
        else if(anchors.size() == 2 && long_pressed_flag==1) // 점이 꾹 눌리면, 점을 이동시킴
        {
            for (HitResult hit : frame.hitTest(x,y)) {
                handleMoveEvent(nowTouchingPointIndex, hit);
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
                double distance = 0.0;
                distance = Math.sqrt((start[0] - end[0]) * (start[0] - end[0]) + (start[1] - end[1]) * (start[1] - end[1]) + (start[2] - end[2]) * (start[2] - end[2])); // 거리 구하기
                String distanceString = String.format(Locale.getDefault(), "%.2f", distance * 100) + "cm";
                // 측정 항목의 범위 안에 속할 경우
                if ((distance * 100 >= min_scope) && (max_scope >= distance * 100)) {
                    distanceTextview.setText(distanceString);
                    // 모든 거리를 저장하는 배열에 해당 측정 항목 번호를 인덱스로 두어 거리를 저장함
                    measurement_items_distance[measurement_count] = distance;
                    // anchors가 2개 찍혔을 때, 모든 anchors를 저장하는 allAnchors 배열에 해당 측정 항목 번호를 인덱스로 두어 저장함
                    allAnchors[measurement_count] = anchors;
                    outOfRange=0; // 범위에 벗어나지 않았으므로 0 으로 설정
                }
                // 측정 항목의 범위 안에 속하지 않을 경우
                else {
                    distanceTextview.setText(distanceString + "\n"
                            + "측정 가능한 범위에서 측정해주세요 \n"
                            + "측정 가능한 범위는 " + min_scope + "cm" + "~" + max_scope + "cm 입니다.");
                    outOfRange=1; // 범위에 벗어났으므로 1로 설정 - 다음 버튼을 눌렀을 때 넘어가는 것 방지하기
                }
            }
        });
    }

    // anchor를 꾹 눌렀을 때, 이동이 가능하게 하는 이벤트 처리
    private void handleMoveEvent(int nowSelectedIndex, HitResult hit) {
        try {
            // anchor가 선택되지 않았으므로 움직이면 안됨
            if (nowSelectedIndex == DEFAULT_VALUE) {
                return;
            }

            if (anchors.size() > nowSelectedIndex && long_pressed_flag == 1) {
                // 해당 점을 삭제
                anchors.get(nowSelectedIndex).anchor.detach();
                anchors.remove(nowSelectedIndex);

                // 현재 hit 부분에 앵커 생성
                float[] objColor = new float[]{66.0f, 133.0f, 244.0f, 255.0f};
                anchors.add(nowSelectedIndex,new ColoredAnchor(hit.createAnchor(), objColor));
                Log.d("anchors",anchors.size()+"");

                // 앵커 포인트를 받아옴
                Pose point0 = anchors.get(0).anchor.getPose();
                float anchorX = point0.tx();
                float anchorY = point0.ty();
                float anchorZ = point0.tz();
                float[] points0 = new float[]{anchorX, anchorY, anchorZ};
                mPoints[0]=points0;
                Pose point1 = anchors.get(1).anchor.getPose();
                anchorX = point1.tx();
                anchorY = point1.ty();
                anchorZ = point1.tz();
                float[] points1 = new float[]{anchorX, anchorY, anchorZ};
                mPoints[1]=points1;

                // 거리 업데이트
                updateDistance(mPoints[0], mPoints[1]);
            }
        } catch (NotTrackingException e1) {
            e1.printStackTrace();
        }
    }

    // 현재 선택된 점을 판단하는데 이용
    private void checkIfHit(ObjectRenderer renderer, int anchorIndex) {
        if (isMVPMatrixHitMotionEvent(renderer.getModelViewProjectionMatrix(), tapHelper.longPressPeek())) {
            nowTouchingPointIndex = anchorIndex;
            tapHelper.longPressPoll();
            long_pressed_flag = 1;
        } else if (isMVPMatrixHitMotionEvent(renderer.getModelViewProjectionMatrix(), tapHelper.queuedSingleTapsPeek())) {
            nowTouchingPointIndex = anchorIndex;
            tapHelper.queuedSingleTapsPoll();
        }
    }

    // 현재 선택된 점을 판단하는데 이용
    private boolean isMVPMatrixHitMotionEvent(float[] ModelViewProjectionMatrix, MotionEvent event) {
        float[] vertexResult = new float[4];
        float[] centerVertexOfAnchor = {0f, 0f, 0f, 1};
        float AnchorHitAreaRadius = 0.08f;
        if (event == null) {
            return false;
        }
        Matrix.multiplyMV(vertexResult, 0, ModelViewProjectionMatrix, 0, centerVertexOfAnchor, 0);
        float radius = (viewWidth / 2) * (AnchorHitAreaRadius / vertexResult[3]);
        float dx = event.getX() - (viewWidth / 2) * (1 + vertexResult[0] / vertexResult[3]);
        float dy = event.getY() - (viewHeight / 2) * (1 - vertexResult[1] / vertexResult[3]);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < radius;
    }

}

