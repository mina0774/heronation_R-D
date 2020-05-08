package com.example.heronation.measurement.Body;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.heronation.FCM.FirebaseMessagingServiceTest;
import com.example.heronation.R;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.measurement.Body.dataClass.BodySizeDetail;
import com.example.heronation.measurement.Body.dataClass.UserBodySizeDetail;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeasurementBodySizeDetailInfoActivity extends AppCompatActivity {
    @BindView(R.id.add_button) ImageButton add_button;
    @BindView(R.id.finish_button) Button activity_finish_button;

    /* 팝업창 */
    PopupWindow mPopupWindow;

    Button top_size_button;
    Button bottom_size_button;
    LinearLayout top_size_layout;
    LinearLayout bottom_size_layout;
    EditText measure_item_id_2_et;
    EditText measure_item_id_3_et;
    EditText measure_item_id_4_et;
    EditText measure_item_id_5_et;
    EditText measure_item_id_6_et;
    EditText measure_item_id_7_et;
    EditText measure_item_id_8_et;
    EditText measure_item_id_9_et;
    EditText measure_item_id_11_et;
    EditText measure_item_id_12_et;
    Button finish_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_body_size_detail_info);
        ButterKnife.bind(this);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_panel();
            }
        });

        activity_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MeasurementBodySizeInfoActivity.measurementBodySizeInfoActivity.finish();
                MeasurementBodyActivity.measurementBodyActivity.finish();
            }
        });
    }

    public void click_back_button(View view) {
        finish();
    }

    public void open_panel() {

        /* 필터 PopUp창 띄우기 */
        View popupView = getLayoutInflater().inflate(R.layout.size_detail_pop_up, null);
        mPopupWindow = new PopupWindow(popupView);
        mPopupWindow.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //팝업 터치 가능
        mPopupWindow.setTouchable(true);
        //팝업 외부 터치 가능(외부 터치시 나갈 수 있게)
        mPopupWindow.setOutsideTouchable(true);
        //외부터치 인식을 위한 추가 설정 : 미 설정시 외부는 null로 생각하고 터치 인식 X
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //애니메이션 활성화

        // PopUp 창 띄우기
        mPopupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        /* 블러 처리 -- 대체 코드 있으면 대체하기 (불안정함)
         *  ViewGroup.LayoutParams를 WindowManager.LayoutParams에 캐스팅하기 위함인데,
         * 버전에 따라 ViewGroup의 자식인 다른 예를 들면 FrameLayout.LayoutParams를 가리키기도 함.
         * 하지만, WindowManager.LayoutParams에 FrameLayout.LayoutParams는 캐스팅 되지 않으므로 오류가 발생함
         * 이를 처리하기 위해서 if문으로 분기를 해주었으나, 불안정한 코드라서 대체 방법이 있다면 대체해야할 것 같음.
         * */
        View container;
        if (android.os.Build.VERSION.SDK_INT > 22) {
            container = (View) mPopupWindow.getContentView().getParent().getParent();
        } else {
            container = (View) mPopupWindow.getContentView().getParent();
        }
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        // add flag
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);

        mPopupWindow.setFocusable(true);
        mPopupWindow.update();

        top_size_button=popupView.findViewById(R.id.top_size_button);
        bottom_size_button=popupView.findViewById(R.id.bottom_size_button);
        top_size_layout=popupView.findViewById(R.id.top_size_layout);
        bottom_size_layout=popupView.findViewById(R.id.bottom_size_layout);
        measure_item_id_2_et=popupView.findViewById(R.id.measure_item_id_2_et);
        measure_item_id_3_et=popupView.findViewById(R.id.measure_item_id_3_et);
        measure_item_id_4_et=popupView.findViewById(R.id.measure_item_id_4_et);
        measure_item_id_5_et=popupView.findViewById(R.id.measure_item_id_5_et);
        measure_item_id_6_et=popupView.findViewById(R.id.measure_item_id_6_et);
        measure_item_id_7_et=popupView.findViewById(R.id.measure_item_id_7_et);
        measure_item_id_8_et=popupView.findViewById(R.id.measure_item_id_8_et);
        measure_item_id_9_et=popupView.findViewById(R.id.measure_item_id_9_et);
        measure_item_id_11_et=popupView.findViewById(R.id.measure_item_id_11_et);
        measure_item_id_12_et=popupView.findViewById(R.id.measure_item_id_12_et);
        finish_button=popupView.findViewById(R.id.finish_button);

        get_body_size_detail_info(); // 기존 저장된 체형 정보 나타내기

        top_size_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_size_button.setBackground(getDrawable(R.drawable.btn_background_purple2));
                top_size_button.setTextColor(Color.parseColor("#ffffff"));
                bottom_size_button.setBackground(getDrawable(R.drawable.btn_background_gray));
                bottom_size_button.setTextColor(Color.parseColor("#acacac"));
                top_size_layout.setVisibility(View.VISIBLE);
                bottom_size_layout.setVisibility(View.GONE);
            }
        });

        bottom_size_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_size_button.setBackground(getDrawable(R.drawable.btn_background_purple2));
                bottom_size_button.setTextColor(Color.parseColor("#ffffff"));
                top_size_button.setBackground(getDrawable(R.drawable.btn_background_gray));
                top_size_button.setTextColor(Color.parseColor("#acacac"));
                bottom_size_layout.setVisibility(View.VISIBLE);
                top_size_layout.setVisibility(View.GONE);
            }
        });

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    modify_body_size_detail_info();
            }
        });
    }

    public void get_body_size_detail_info(){
        String authorization="bearer "+ MainActivity.access_token;
        String accept="application/json";

        APIInterface.GetBodySizeDetailInfoService getBodySizeDetailInfoService= ServiceGenerator.createService(APIInterface.GetBodySizeDetailInfoService.class);
        retrofit2.Call<UserBodySizeDetail> request= getBodySizeDetailInfoService.GetBodySizeDetailInfo(authorization,accept);
        request.enqueue(new Callback<UserBodySizeDetail>() {
            @Override
            public void onResponse(Call<UserBodySizeDetail> call, Response<UserBodySizeDetail> response) {
                if(response.isSuccessful()) {
                    UserBodySizeDetail userBodySizeDetail=response.body();
                    if(userBodySizeDetail.getBodyResponses().size()==0){ // 현재 저장된 체형 치수 정보가 없을 때

                    }else{ // 현재 저장된 체형 치수 정보가 있을 때 - 현재 저장된 정보를 띄워줌
                        for(int i=0; i<userBodySizeDetail.getBodyResponses().size(); i++){
                            switch (userBodySizeDetail.getBodyResponses().get(i).getMeasureItemId()){
                                case 2: measure_item_id_2_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                                case 3: measure_item_id_3_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                                case 4: measure_item_id_4_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                                case 5: measure_item_id_5_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                                case 6: measure_item_id_6_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                                case 7: measure_item_id_7_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                                case 8: measure_item_id_8_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                                case 9: measure_item_id_9_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                                case 11: measure_item_id_11_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                                case 12: measure_item_id_12_et.setText(userBodySizeDetail.getBodyResponses().get(i).getValue().toString()); break;
                            }
                        }
                    }
                }else if(response.code()==401){
                    backgroundThreadShortToast(getApplicationContext(), "세션이 만료되어 재로그인이 필요합니다."); // 토스트 메시지 ( 메인 쓰레드에서 실행되어야하므로 사용 )
                    Intent intent=new Intent(MeasurementBodySizeDetailInfoActivity.this, IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserBodySizeDetail> call, Throwable t) {

            }
        });
    }

    public void modify_body_size_detail_info(){
        /* 현재 EditText에 입력 되어있는 정보를 이용하여 치수를 담는 리스트를 만듦 */
        BodySizeDetail bodySizeDetail2=new BodySizeDetail(2,measure_item_id_2_et.getText().toString());
        BodySizeDetail bodySizeDetail3=new BodySizeDetail(3,measure_item_id_3_et.getText().toString());
        BodySizeDetail bodySizeDetail4=new BodySizeDetail(4,measure_item_id_4_et.getText().toString());
        BodySizeDetail bodySizeDetail5=new BodySizeDetail(5,measure_item_id_5_et.getText().toString());
        BodySizeDetail bodySizeDetail6=new BodySizeDetail(6,measure_item_id_6_et.getText().toString());
        BodySizeDetail bodySizeDetail7=new BodySizeDetail(7,measure_item_id_7_et.getText().toString());
        BodySizeDetail bodySizeDetail8=new BodySizeDetail(8,measure_item_id_8_et.getText().toString());
        BodySizeDetail bodySizeDetail9=new BodySizeDetail(9,measure_item_id_9_et.getText().toString());
        BodySizeDetail bodySizeDetail11=new BodySizeDetail(11,measure_item_id_11_et.getText().toString());
        BodySizeDetail bodySizeDetail12=new BodySizeDetail(12,measure_item_id_12_et.getText().toString());

        List<BodySizeDetail> bodySizeDetailList=new ArrayList<>();
        bodySizeDetailList.clear();

        bodySizeDetailList.add(bodySizeDetail2); bodySizeDetailList.add(bodySizeDetail3);
        bodySizeDetailList.add(bodySizeDetail4); bodySizeDetailList.add(bodySizeDetail5);
        bodySizeDetailList.add(bodySizeDetail6); bodySizeDetailList.add(bodySizeDetail7);
        bodySizeDetailList.add(bodySizeDetail8); bodySizeDetailList.add(bodySizeDetail9);
        bodySizeDetailList.add(bodySizeDetail11); bodySizeDetailList.add(bodySizeDetail12);

        String authorization="bearer "+ MainActivity.access_token;
        String accept="application/json";
        String content_type="application/json";

        APIInterface.ModifyBodySizeDetailInfoService modifyBodySizeDetailInfoService=ServiceGenerator.createService(APIInterface.ModifyBodySizeDetailInfoService.class);
        retrofit2.Call<String> request=modifyBodySizeDetailInfoService.ModifyBodySizeDetailInfo(authorization,accept,content_type,bodySizeDetailList);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    // TODO: 2020-05-08 FCM 기존 구독 정보를 삭제하고, 새로운 체형 정보를 바탕으로 구독을 해줌

                    backgroundThreadShortToast(getApplicationContext(),"체형 정보 변경이 완료되었습니다.");
                    mPopupWindow.dismiss();
                }
                else{
                    backgroundThreadShortToast(getApplicationContext(),"올바른 형식으로 입력해주세요.");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //Toast는 비동기 태스크 내에서 처리할 수 없으므로, 메인 쓰레드 핸들러를 생성하여 toast가 메인쓰레드에서 생성될 수 있도록 처리해준다.
    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
