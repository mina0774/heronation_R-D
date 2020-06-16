package com.example.heronation.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heronation.R;
import com.example.heronation.home.ItemDetailPage.Wardrobe.ItemSelectItemForComparisonAcitivity;
import com.example.heronation.login_register.DatePickerFragment;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.mypage.dataClass.UserModifyInfo;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserModifyActivity extends AppCompatActivity {
    @BindView(R.id.textView_modify_datepicker)
    TextView modify_datepicker;
    @BindView(R.id.userModify_edit_btn) TextView userModify_edit_btn;
    @BindView(R.id.userModify_id_text) TextView userModify_id_text;
    @BindView(R.id.userModify_email_text) TextView userModify_email_text;
    @BindView(R.id.userModify_name_text)
    EditText userModify_name_text;
    @BindView(R.id.userModify_female) TextView userModify_female;
    @BindView(R.id.userModify_male) TextView userModify_male;
    @BindView(R.id.userModify_ad_check)
    CheckBox userModify_ad_check;
    @BindView(R.id.userModify_hi_nickname_text) TextView userModify_hi_nickname_text;

    Integer modify_year;
    Integer modify_month;
    Integer modify_date;
    String gender;
    String termsAdvertisement;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modify);

        ButterKnife.bind(this);

        // 현재 유저 정보를 받아와 화면에 표시
        getCurrentUserInfo();

        //날짜 변경
        modify_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        //성별 변경 - 여자
        userModify_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModify_female.setTextColor(Color.parseColor("#000000"));
                userModify_male.setTextColor(Color.parseColor("#d3d3d3"));
                gender="F";
            }
        });
        //성별 변경 - 남자
        userModify_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModify_female.setTextColor(Color.parseColor("#d3d3d3"));
                userModify_male.setTextColor(Color.parseColor("#000000"));
                gender="M";
            }
        });

        // 수정 버튼을 눌렀을 때
        userModify_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyUserInfo(); //회원 정보 수정
            }
        });
    }

    // 뒤로가기 버튼을 눌렀을 경우
    public void click_back_button(View view){
        finish();
    }

    //회원 정보를 수정
    private void modifyUserInfo(){
        //변경된 정보를 담은 객체 생성
        user_name=userModify_name_text.getText().toString();
        if(userModify_ad_check.isChecked()){
            termsAdvertisement="Y";
        }else if(!userModify_ad_check.isChecked()){
            termsAdvertisement="N";
        }
        UserModifyInfo userModifyInfo=new UserModifyInfo(user_name,modify_year,modify_month,modify_date,gender,termsAdvertisement);

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
                    MainActivity.backgroundThreadShortToast(UserModifyActivity.this, "수정되었습니다.");
                    finish();
                }
                else if(response.code()==401){ //토큰 만료기한이 끝나, 재로그인이 필요할 때
                    MainActivity.backgroundThreadShortToast(UserModifyActivity.this, "세션이 만료되어 재로그인이 필요합니다.");
                    Intent intent=new Intent(UserModifyActivity.this, IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    // 현재 유저 정보를 받아와 화면에 표시
    private void getCurrentUserInfo(){
        //회원정보를 받아옴
        String authorization="bearer " + MainActivity.access_token;
        String accept="application/json";
        APIInterface.UserInfoService userInfoService=ServiceGenerator.createService(APIInterface.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request=userInfoService.UserInfo(authorization,accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                /* 정상적으로 로그인이 되었을 때 */
                if (response.code() == 200) {
                    UserMyInfo userMyInfo = response.body();
                    /* 현재 유저 정보값 설정 */
                    userModify_hi_nickname_text.setText("안녕하세요!\n"+userMyInfo.getName()+" 님");
                    userModify_id_text.setText(userMyInfo.getConsumerId());
                    userModify_email_text.setText(userMyInfo.getEmail());
                    user_name=userMyInfo.getName();
                    userModify_name_text.setText(user_name);
                    modify_year=userMyInfo.getBirthYear();
                    modify_month=userMyInfo.getBirthMonth();
                    modify_date=userMyInfo.getBirthDay();
                    gender=userMyInfo.getGender();
                    modify_datepicker.setText(modify_year+"-"+modify_month+"-"+modify_date);
                    if(gender.matches("F")){
                        userModify_female.setTextColor(Color.parseColor("#000000"));
                    }else if(gender.matches("M")){
                        userModify_male.setTextColor(Color.parseColor("#000000"));
                    }
                    termsAdvertisement=userMyInfo.getTermsAdvertisement();
                    if(termsAdvertisement!=null) {
                        if (termsAdvertisement.matches("Y")) {
                            userModify_ad_check.setChecked(true);
                        } else if (termsAdvertisement.matches("N")) {
                            userModify_ad_check.setChecked(false);
                        }
                    }
                }
                /*토큰 만료기한이 끝나, 재로그인이 필요할 때*/
                else if(response.code()==401) {
                    MainActivity.backgroundThreadShortToast(UserModifyActivity.this, "세션이 만료되어 재로그인이 필요합니다.");
                    Intent intent=new Intent(UserModifyActivity.this, IntroActivity.class);
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

    /*각 생년 월일 입력받음*/
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    /*입력 받은것 출력*/
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            TextView textView_modify_datepicker = (TextView)findViewById(R.id.textView_modify_datepicker);
            modify_year=year;
            modify_month=monthOfYear+1;
            modify_date=dayOfMonth;
            textView_modify_datepicker.setText(modify_year + "-" + modify_month + "-" + modify_date);
        }
    };
}
