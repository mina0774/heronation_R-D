package com.example.heronation.home.ItemDetailPage.Body;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.heronation.R;
import com.example.heronation.home.dataClass.BodySizeLevel;
import com.example.heronation.home.dataClass.BodySizeLevelForSizeInfo;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemMeasurementBodySizeInfoActivity extends AppCompatActivity {
    @BindView(R.id.add_button) ImageButton add_button_all;
    @BindView(R.id.check_button_shoulder) ImageButton check_button_shoulder;
    @BindView(R.id.check_button_chest) ImageButton check_button_chest;
    @BindView(R.id.check_button_waist) ImageButton check_button_waist;
    @BindView(R.id.check_button_hip) ImageButton check_button_hip;
    @BindView(R.id.check_button_thigh) ImageButton check_button_thigh;

    /* 지정된 부위별 사이즈 레벨 민감도 */
    Integer shoulder_sensibility_level;
    Integer chest_sensibility_level;
    Integer waist_sensibility_level;
    Integer hip_sensibility_level;
    Integer thigh_sensibility_level;
    /* 비교하려는 상품 아이디 */
    String product_id;
    String gender;
    String age;
    String height;
    String weight;
    BodySizeLevel bodySizeLevel;

    UserMyInfo userMyInfo;
    public static ItemMeasurementBodySizeInfoActivity itemMeasurementBodySizeInfoActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_body_size_info);
        ButterKnife.bind(this);
        itemMeasurementBodySizeInfoActivity=this;

        // 기존 저장된 값을 받아옴
        getUserInfo(MainActivity.access_token);

        bodySizeLevel=(BodySizeLevel)getIntent().getSerializableExtra("body_size_default_level");
        product_id=getIntent().getStringExtra("item_id");
        gender=getIntent().getStringExtra("gender");
        age=getIntent().getStringExtra("age");
        height=getIntent().getStringExtra("height");
        weight=getIntent().getStringExtra("weight");

        add_button_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { open_panel(); }
        });
        check_button_shoulder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { open_panel(); }
        });
        check_button_chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { open_panel(); }
        });
        check_button_waist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { open_panel(); }
        });
        check_button_hip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { open_panel(); }
        });
        check_button_thigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { open_panel(); }
        });
    }

    public void click_back_button(View view){
        finish();
    }

    public void click_next_button(View view){
      generate_body_size_info();
    }

    public void generate_body_size_info(){
        String authorization = "Bearer "+ MainActivity.access_token;
        String heronation_api_uniqId_key="1580197357282_xerh4r4k5xkpgbo";
        String accept="application/json";
        String content_type="application/json";

        BodySizeLevelForSizeInfo bodySizeLevelForSizeInfo = new BodySizeLevelForSizeInfo("heronation","cafe24",
                product_id,gender,Integer.parseInt(age),Integer.parseInt(height),Integer.parseInt(weight),
                bodySizeLevel.getShoulderLevel(),bodySizeLevel.getChestLevel(),bodySizeLevel.getHubLevel(),bodySizeLevel.getHipLevel(),bodySizeLevel.getThighLevel(),
                shoulder_sensibility_level,chest_sensibility_level,waist_sensibility_level,hip_sensibility_level,thigh_sensibility_level);

        APIInterface.GenerateBodySizeInfoService generateBodySizeInfoService= ServiceGenerator.createService(APIInterface.GenerateBodySizeInfoService.class);
        retrofit2.Call<String> request=generateBodySizeInfoService.GenerateBodySizeInfo(authorization,heronation_api_uniqId_key,accept,content_type,bodySizeLevelForSizeInfo);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ItemMeasurementBodySizeInfoActivity.this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("잠시만 기다려주세요");
        progressDoalog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        progressDoalog.show();
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    progressDoalog.dismiss();
                    backgroundThreadShortToast(getApplicationContext(),"입력하신 값이 반영되었습니다.");
                    Intent intent=new Intent(ItemMeasurementBodySizeInfoActivity.this, ItemCompareBodySizeActivity.class);
                    intent.putExtra("item_id",product_id);
                    startActivity(intent);
                }else{
                    progressDoalog.dismiss();
                    backgroundThreadShortToast(getApplicationContext(),"값을 모두 입력해주세요.");
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

    // 기존 회원정보를 받아옴
    private void getUserInfo(String access_token){
        String authorization="bearer " + access_token;
        String accept="application/json";
        APIInterface.UserInfoService userInfoService= ServiceGenerator.createService(APIInterface.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request=userInfoService.UserInfo(authorization,accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                if (response.code()==200) {
                    userMyInfo = response.body();
                }
            }
            @Override
            public void onFailure(Call<UserMyInfo> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    public void open_panel() {
        /* 필터 PopUp창 띄우기 */
        final PopupWindow mPopupWindow;
        View popupView = getLayoutInflater().inflate(R.layout.size_pop_up, null);
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

        /* 각 부위별 사이즈 레벨 버튼 */
        Button shoulder_level_size_1=(Button)popupView.findViewById(R.id.shoulder_level_size_1);
        Button shoulder_level_size_2=(Button)popupView.findViewById(R.id.shoulder_level_size_2);
        Button shoulder_level_size_3=(Button)popupView.findViewById(R.id.shoulder_level_size_3);
        Button shoulder_level_size_4=(Button)popupView.findViewById(R.id.shoulder_level_size_4);
        Button shoulder_level_size_5=(Button)popupView.findViewById(R.id.shoulder_level_size_5);

        Button chest_level_size_1=(Button)popupView.findViewById(R.id.chest_level_size_1);
        Button chest_level_size_2=(Button)popupView.findViewById(R.id.chest_level_size_2);
        Button chest_level_size_3=(Button)popupView.findViewById(R.id.chest_level_size_3);
        Button chest_level_size_4=(Button)popupView.findViewById(R.id.chest_level_size_4);
        Button chest_level_size_5=(Button)popupView.findViewById(R.id.chest_level_size_5);

        Button waist_level_size_1=(Button)popupView.findViewById(R.id.waist_level_size_1);
        Button waist_level_size_2=(Button)popupView.findViewById(R.id.waist_level_size_2);
        Button waist_level_size_3=(Button)popupView.findViewById(R.id.waist_level_size_3);
        Button waist_level_size_4=(Button)popupView.findViewById(R.id.waist_level_size_4);
        Button waist_level_size_5=(Button)popupView.findViewById(R.id.waist_level_size_5);

        Button hip_level_size_1=(Button)popupView.findViewById(R.id.hip_level_size_1);
        Button hip_level_size_2=(Button)popupView.findViewById(R.id.hip_level_size_2);
        Button hip_level_size_3=(Button)popupView.findViewById(R.id.hip_level_size_3);
        Button hip_level_size_4=(Button)popupView.findViewById(R.id.hip_level_size_4);
        Button hip_level_size_5=(Button)popupView.findViewById(R.id.hip_level_size_5);

        Button thigh_level_size_1=(Button)popupView.findViewById(R.id.thigh_level_size_1);
        Button thigh_level_size_2=(Button)popupView.findViewById(R.id.thigh_level_size_2);
        Button thigh_level_size_3=(Button)popupView.findViewById(R.id.thigh_level_size_3);
        Button thigh_level_size_4=(Button)popupView.findViewById(R.id.thigh_level_size_4);
        Button thigh_level_size_5=(Button)popupView.findViewById(R.id.thigh_level_size_5);

        Button finish_button=(Button)popupView.findViewById(R.id.finish_button);

        /* 기존 default 부위별 사이즈 레벨이 존재하면 그 값을 받아옴 */
        if(userMyInfo.getShoulderLevel()!=0 && shoulder_sensibility_level==null){
            shoulder_sensibility_level=userMyInfo.getShoulderLevel();
            switch (userMyInfo.getShoulderLevel()) {
                case 1:  shoulder_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  shoulder_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  shoulder_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  shoulder_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  shoulder_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }
        }

        if(userMyInfo.getChestLevel()!=0 && chest_sensibility_level==null){
            chest_sensibility_level=userMyInfo.getChestLevel();
            switch (userMyInfo.getChestLevel()) {
                case 1:  chest_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  chest_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  chest_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  chest_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  chest_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }
        }

        if(userMyInfo.getHubLevel()!=0 && waist_sensibility_level==null){
            waist_sensibility_level=userMyInfo.getHubLevel();
            switch (userMyInfo.getHubLevel()) {
                case 1:  waist_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  waist_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  waist_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  waist_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  waist_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }
        }

        if(userMyInfo.getHipLevel()!=0 && hip_sensibility_level==null){
            hip_sensibility_level=userMyInfo.getHipLevel();
            switch (userMyInfo.getHipLevel()) {
                case 1:  hip_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  hip_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  hip_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  hip_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  hip_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }
        }

        if(userMyInfo.getThighLevel()!=0 && thigh_sensibility_level==null){
            thigh_sensibility_level=userMyInfo.getThighLevel();
            switch (userMyInfo.getChestLevel()) {
                case 1:  thigh_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  thigh_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  thigh_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  thigh_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  thigh_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }
        }

        /* 팝업창을 열었을 때, sensibility의 입력 기록이 있다면 */
        if(shoulder_sensibility_level!=null){
            switch (shoulder_sensibility_level) {
                case 1:  shoulder_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  shoulder_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  shoulder_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  shoulder_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  shoulder_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }
        }
        if(chest_sensibility_level!=null){
            switch (chest_sensibility_level) {
                case 1:  chest_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  chest_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  chest_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  chest_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  chest_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }
        }
        if(waist_sensibility_level!=null){
            switch (waist_sensibility_level) {
                case 1:  waist_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  waist_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  waist_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  waist_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  waist_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }
        }
        if(hip_sensibility_level!=null){
            switch (hip_sensibility_level) {
                case 1:  hip_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  hip_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  hip_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  hip_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  hip_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }

        }
        if(thigh_sensibility_level!=null){
            switch (thigh_sensibility_level) {
                case 1:  thigh_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 2:  thigh_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 3:  thigh_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 4:  thigh_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple)); break;
                case 5:  thigh_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple)); break;
            }
        }

        /* 각각의 부위별 사이즈 버튼 입력 이벤트 */
        shoulder_level_size_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoulder_sensibility_level=1;
                shoulder_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple));
                shoulder_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        shoulder_level_size_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoulder_sensibility_level=2;
                shoulder_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple));
                shoulder_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        shoulder_level_size_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoulder_sensibility_level=3;
                shoulder_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple));
                shoulder_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        shoulder_level_size_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoulder_sensibility_level=4;
                shoulder_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple));
                shoulder_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        shoulder_level_size_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoulder_sensibility_level=5;
                shoulder_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple));
                shoulder_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                shoulder_level_size_1.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        chest_level_size_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chest_sensibility_level=1;
                chest_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple));
                chest_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        chest_level_size_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chest_sensibility_level=2;
                chest_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple));
                chest_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        chest_level_size_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chest_sensibility_level=3;
                chest_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple));
                chest_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        chest_level_size_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chest_sensibility_level=4;
                chest_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple));
                chest_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        chest_level_size_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chest_sensibility_level=5;
                chest_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple));
                chest_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                chest_level_size_1.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        waist_level_size_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waist_sensibility_level=1;
                waist_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple));
                waist_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        waist_level_size_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waist_sensibility_level=2;
                waist_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple));
                waist_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        waist_level_size_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waist_sensibility_level=3;
                waist_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple));
                waist_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        waist_level_size_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waist_sensibility_level=4;
                waist_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple));
                waist_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        waist_level_size_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waist_sensibility_level=5;
                waist_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple));
                waist_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                waist_level_size_1.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        hip_level_size_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hip_sensibility_level=1;
                hip_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple));
                hip_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        hip_level_size_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hip_sensibility_level=2;
                hip_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple));
                hip_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        hip_level_size_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hip_sensibility_level=3;
                hip_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple));
                hip_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        hip_level_size_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hip_sensibility_level=4;
                hip_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple));
                hip_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        hip_level_size_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hip_sensibility_level=5;
                hip_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple));
                hip_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                hip_level_size_1.setBackground(getDrawable(R.drawable.button_background));
            }
        });

         thigh_level_size_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thigh_sensibility_level=1;
                thigh_level_size_1.setBackground(getDrawable(R.drawable.button_background_purple));
                thigh_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        thigh_level_size_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thigh_sensibility_level=2;
                thigh_level_size_2.setBackground(getDrawable(R.drawable.button_background_purple));
                thigh_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        thigh_level_size_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thigh_sensibility_level=3;
                thigh_level_size_3.setBackground(getDrawable(R.drawable.button_background_purple));
                thigh_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        thigh_level_size_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thigh_sensibility_level=4;
                thigh_level_size_4.setBackground(getDrawable(R.drawable.button_background_purple));
                thigh_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_1.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_5.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        thigh_level_size_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thigh_sensibility_level=5;
                thigh_level_size_5.setBackground(getDrawable(R.drawable.button_background_purple));
                thigh_level_size_2.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_3.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_4.setBackground(getDrawable(R.drawable.button_background));
                thigh_level_size_1.setBackground(getDrawable(R.drawable.button_background));
            }
        });

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shoulder_sensibility_level!=null){
                    check_button_shoulder.setImageDrawable(getDrawable(R.drawable.ic_check));
                }
                if(chest_sensibility_level!=null){
                    check_button_chest.setImageDrawable(getDrawable(R.drawable.ic_check));
                }
                if(waist_sensibility_level!=null){
                    check_button_waist.setImageDrawable(getDrawable(R.drawable.ic_check));
                }
                if(hip_sensibility_level!=null){
                    check_button_hip.setImageDrawable(getDrawable(R.drawable.ic_check));
                }
                if(thigh_sensibility_level!=null){
                    check_button_thigh.setImageDrawable(getDrawable(R.drawable.ic_check));
                }
                mPopupWindow.dismiss();
            }
        });


    }
}
