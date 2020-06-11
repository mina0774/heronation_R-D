package com.example.heronation.measurement.Style;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.heronation.R;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.mypage.UserModifyActivity;
import com.example.heronation.mypage.dataClass.UserModifyInfo;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeasurementStyleActivity extends AppCompatActivity {
    @BindView(R.id.table_layout_female) TableLayout table_layout_female;
    @BindView(R.id.table_layout_male) TableLayout table_layout_male;
    Boolean[] style=new Boolean[14];
    Boolean[] style_m=new Boolean[11];
    String[] style_tag_name={"심플베이직","페미닌","러블리","캐주얼","섹시글램","시크","유니크","캠퍼스룩","오피스룩","커플룩","로맨틱","빈티지","럭셔리","스트릿"};
    String[] style_tag_name_m={"심플베이직","캐주얼","시크","유니크","캠퍼스룩","오피스룩","커플룩","빈티지","럭셔리","스트릿","댄디"};

    public String gender;

    /* 순서대로 - 여자
    * 심플베이직 페미닌 러블리 캐주얼 섹시글램 시크 유니크 캠퍼스룩 오피스룩 커플룩 로맨틱 빈티지 럭셔리 스트릿 */
    @BindView(R.id.style_1) ImageButton style_1;
    @BindView(R.id.style_2) ImageButton style_2;
    @BindView(R.id.style_3) ImageButton style_3;
    @BindView(R.id.style_4) ImageButton style_4;
    @BindView(R.id.style_5) ImageButton style_5;
    @BindView(R.id.style_6) ImageButton style_6;
    @BindView(R.id.style_7) ImageButton style_7;
    @BindView(R.id.style_8) ImageButton style_8;
    @BindView(R.id.style_9) ImageButton style_9;
    @BindView(R.id.style_10) ImageButton style_10;
    @BindView(R.id.style_11) ImageButton style_11;
    @BindView(R.id.style_12) ImageButton style_12;
    @BindView(R.id.style_13) ImageButton style_13;
    @BindView(R.id.style_14) ImageButton style_14;

    /* 순서대로 - 남자
     * 심플베이직 캐주얼 시크 유니크 캠퍼스룩 오피스룩 커플룩 빈티지 럭셔리 스트릿 댄디 */
    @BindView(R.id.style_1_m) ImageButton style_1_m;
    @BindView(R.id.style_2_m) ImageButton style_2_m;
    @BindView(R.id.style_3_m) ImageButton style_3_m;
    @BindView(R.id.style_4_m) ImageButton style_4_m;
    @BindView(R.id.style_5_m) ImageButton style_5_m;
    @BindView(R.id.style_6_m) ImageButton style_6_m;
    @BindView(R.id.style_7_m) ImageButton style_7_m;
    @BindView(R.id.style_8_m) ImageButton style_8_m;
    @BindView(R.id.style_9_m) ImageButton style_9_m;
    @BindView(R.id.style_10_m) ImageButton style_10_m;
    @BindView(R.id.style_11_m) ImageButton style_11_m;


    @BindView(R.id.measurement_style_finish_button) Button measurement_style_finish_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_style);
        ButterKnife.bind(this);

        Arrays.fill(style,false);
        Arrays.fill(style_m,false);

        getGenderInfo();

        style_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[0]==false) {
                    style_1.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[0]=true;
                }else {
                    style_1.clearColorFilter();
                    style[0]=false;
                }
            }
        });

        style_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[1]==false) {
                    style_2.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[1]=true;
                }else {
                    style_2.clearColorFilter();
                    style[1]=false;
                }
            }
        });

        style_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[2]==false) {
                    style_3.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[2]=true;
                }else {
                    style_3.clearColorFilter();
                    style[2]=false;
                }
            }
        });

        style_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[3]==false) {
                    style_4.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[3]=true;
                }else {
                    style_4.clearColorFilter();
                    style[3]=false;
                }
            }
        });

        style_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[4]==false) {
                    style_5.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[4]=true;
                }else {
                    style_5.clearColorFilter();
                    style[4]=false;
                }
            }
        });

        style_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[5]==false) {
                    style_6.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[5]=true;
                }else {
                    style_6.clearColorFilter();
                    style[5]=false;
                }
            }
        });

        style_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[6]==false) {
                    style_7.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[6]=true;
                }else {
                    style_7.clearColorFilter();
                    style[6]=false;
                }
            }
        });

        style_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[7]==false) {
                    style_8.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[7]=true;
                }else {
                    style_8.clearColorFilter();
                    style[7]=false;
                }
            }
        });

        style_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[8]==false) {
                    style_9.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[8]=true;
                }else {
                    style_9.clearColorFilter();
                    style[8]=false;
                }
            }
        });

        style_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[9]==false) {
                    style_10.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[9]=true;
                }else {
                    style_10.clearColorFilter();
                    style[9]=false;
                }
            }
        });

        style_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[10]==false) {
                    style_11.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[10]=true;
                }else {
                    style_11.clearColorFilter();
                    style[10]=false;
                }
            }
        });

        style_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[11]==false) {
                    style_12.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[11]=true;
                }else {
                    style_12.clearColorFilter();
                    style[11]=false;
                }
            }
        });

        style_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[12]==false) {
                    style_13.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[12]=true;
                }else {
                    style_13.clearColorFilter();
                    style[12]=false;
                }
            }
        });

        style_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[13]==false) {
                    style_14.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[13]=true;
                }else {
                    style_14.clearColorFilter();
                    style[13]=false;
                }
            }
        });

        style_1_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[0]==false) {
                    style_1_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[0]=true;
                }else {
                    style_1_m.clearColorFilter();
                    style_m[0]=false;
                }
            }
        });

        style_2_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[1]==false) {
                    style_2_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[1]=true;
                }else {
                    style_2_m.clearColorFilter();
                    style_m[1]=false;
                }
            }
        });

        style_3_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[2]==false) {
                    style_3_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[2]=true;
                }else {
                    style_3_m.clearColorFilter();
                    style_m[2]=false;
                }
            }
        });

        style_4_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[3]==false) {
                    style_4_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[3]=true;
                }else {
                    style_4_m.clearColorFilter();
                    style_m[3]=false;
                }
            }
        });

        style_5_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[4]==false) {
                    style_5_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[4]=true;
                }else {
                    style_5_m.clearColorFilter();
                    style_m[4]=false;
                }
            }
        });

        style_6_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[5]==false) {
                    style_6_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[5]=true;
                }else {
                    style_6_m.clearColorFilter();
                    style_m[5]=false;
                }
            }
        });

        style_7_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[6]==false) {
                    style_7_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[6]=true;
                }else {
                    style_7_m.clearColorFilter();
                    style_m[6]=false;
                }
            }
        });

        style_8_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[7]==false) {
                    style_8_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[7]=true;
                }else {
                    style_8_m.clearColorFilter();
                    style_m[7]=false;
                }
            }
        });

        style_9_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[8]==false) {
                    style_9_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[8]=true;
                }else {
                    style_9_m.clearColorFilter();
                    style_m[8]=false;
                }
            }
        });

        style_10_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[9]==false) {
                    style_10_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[9]=true;
                }else {
                    style_10_m.clearColorFilter();
                    style_m[9]=false;
                }
            }
        });

        style_11_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style_m[10]==false) {
                    style_11_m.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style_m[10]=true;
                }else {
                    style_11_m.clearColorFilter();
                    style_m[10]=false;
                }
            }
        });

        // 완료 버튼을 누를 시
        measurement_style_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 아이템이 2개인지 확인, 아니면 2개만 선택해달라는 Dialog를 띄움
                int select_count=0;

                List<String> styleTags=new ArrayList<>();
                styleTags.clear();

                if(gender.equals("F")) {
                    for (int i = 0; i < style.length; i++) {
                        if (style[i] == true) {
                            styleTags.add(style_tag_name[i]);
                            select_count++;
                        }
                    }
                }else if(gender.equals("M")){
                    for (int i = 0; i < style_m.length; i++) {
                        if (style_m[i] == true) {
                            styleTags.add(style_tag_name_m[i]);
                            select_count++;
                        }
                    }
                }

                if(select_count!=2){
                    Toast.makeText(getApplicationContext(),"아이템을 2개 선택해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    getUserInfo(styleTags);
                }
           }
        });

    }

    public void getUserInfo(List<String> styleTags){
        String authorization="bearer " + MainActivity.access_token;
        String accept="application/json";
        APIInterface.UserInfoService userInfoService= ServiceGenerator.createService(APIInterface.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request=userInfoService.UserInfo(authorization,accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                /* 정상적으로 로그인이 되었을 때 */
                if (response.code() == 200) {
                    UserMyInfo userMyInfo = response.body();
                    setStyleTagInfo(userMyInfo.getName(),userMyInfo.getBirthYear(),userMyInfo.getBirthMonth(),userMyInfo.getBirthDay(),userMyInfo.getGender(),styleTags);
                }
                /*토큰 만료기한이 끝나, 재로그인이 필요할 때*/
                else if(response.code()==401) {
                    MainActivity.backgroundThreadShortToast(getApplicationContext(), "세션이 만료되어 재로그인이 필요합니다.");
                    Intent intent=new Intent(getApplicationContext(), IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<UserMyInfo> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    public void setStyleTagInfo(String name,Integer year,Integer month, Integer day,String gender,List<String> styleTags){
        UserModifyInfo userModifyInfo=new UserModifyInfo(name,year,month,day,gender,styleTags);

        String authorization="bearer "+ MainActivity.access_token;
        String accept="application/json";
        String content_type="application/json";
        APIInterface.ModifyUserInfoService modifyUserInfoService= ServiceGenerator.createService(APIInterface.ModifyUserInfoService.class);
        retrofit2.Call<String> request=modifyUserInfoService.ModifyUserInfo(authorization,accept,content_type,userModifyInfo);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code()==204) { //정상적으로 로그인이 되었을 때
                    Bundle bundle1=new Bundle();
                    bundle1.putString("access_token", MainActivity.access_token);
                    MainActivity.backgroundThreadShortToast(MeasurementStyleActivity.this, "스타일 정보가 등록되었습니다.");
                    finish();
                }
                else if(response.code()==401){ //토큰 만료기한이 끝나, 재로그인이 필요할 때
                    MainActivity.backgroundThreadShortToast(MeasurementStyleActivity.this, "세션이 만료되어 재로그인이 필요합니다.");
                    Intent intent=new Intent(MeasurementStyleActivity.this, IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    /* 뒤로가기 버튼을 눌렀을 때 */
    public void click_back_button(View view){
        finish();
    }

    public void getGenderInfo() {
        String authorization = "bearer " + MainActivity.access_token;
        String accept = "application/json";
        APIInterface.UserInfoService userInfoService = ServiceGenerator.createService(APIInterface.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request = userInfoService.UserInfo(authorization, accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                if (response.code() == 200) { //정상적으로 로그인이 되었을 때
                    UserMyInfo userMyInfo = response.body(); // 사용자 정보를 받아온 후에
                    gender=userMyInfo.getGender();
                   Log.d("성별",gender);
                    if(gender.equals("F")){
                        table_layout_female.setVisibility(View.VISIBLE);
                        table_layout_male.setVisibility(View.GONE);
                    }else{
                        table_layout_male.setVisibility(View.VISIBLE);
                        table_layout_female.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMyInfo> call, Throwable t) {
            }
        });
    }
}
