package com.example.heronation.measurement.Body;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.heronation.R;
import com.example.heronation.home.MeasurementBodyActivity;
import com.example.heronation.home.MeasurementBodySizeInfoActivity;
import com.example.heronation.home.dataClass.BodySizeLevel;
import com.example.heronation.main.MainActivity;
import com.example.heronation.measurement.Body.dataClass.BodySizeDetail;
import com.example.heronation.measurement.Body.dataClass.UserBodySizeDetail;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeasurementBodySizeDetailInfoActivity extends AppCompatActivity {
    @BindView(R.id.add_button) ImageButton add_button;

    /* 팝업창 */
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
    }

    public void click_back_button(View view) {
        finish();
    }

    public void open_panel() {

        /* 필터 PopUp창 띄우기 */
        final PopupWindow mPopupWindow;
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
                }
            }

            @Override
            public void onFailure(Call<UserBodySizeDetail> call, Throwable t) {

            }
        });

    }

}
