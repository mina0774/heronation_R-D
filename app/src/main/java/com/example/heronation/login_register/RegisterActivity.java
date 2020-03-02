package com.example.heronation.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heronation.R;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import java.util.Calendar;
import java.util.regex.Pattern;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.userModify_id_text) EditText userModify_id_text;
    @BindView(R.id.userModify_check_pw_et) EditText userModify_check_pw_et;
    @BindView(R.id.userModify_email_text) EditText userModify_email_text;
    @BindView(R.id.userModify_name_text) EditText userModify_name_text;
    @BindView(R.id.userModify_present_pw_et) EditText userModify_present_pw_et;
    @BindView(R.id.userModify_push_check) CheckBox userModify_push_check;
    @BindView(R.id.userModify_male) CheckBox userModify_male;
    @BindView(R.id.userModify_female) CheckBox userModify_female;
    @BindView(R.id.userModify_info_check) CheckBox userModify_info_check;
    @BindView(R.id.textView_register_datepicker) TextView register_datepicker;
    @BindView(R.id.register_next_btn) Button register_next_button;

    //사용자 푸시 알림 체킹 여부
    private String push_check;
    //성별
    private static String gender_info;
    //사용자 생년월일
    private static String user_year;
    private static String user_month;
    private static String user_day;
    //회원가입이 정상적으로 이루어졌는지 확인하는 부분
    private int response_code;
    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,15}$");
    // 아이디 정규식
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{4,15}$");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        /*생년월일 TextView클릭시 showDatePicker가 실행됨*/
       register_datepicker = (TextView) findViewById(R.id.textView_register_datepicker);
        register_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        /* 회원가입 버튼 클릭시, 회원가입 정보가 서버단으로 넘어가고, 스타일 설정 페이지로 넘어감 */
        register_next_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //체크박스가 체크 되어있으면, 푸시알림을 Y로 설정 아니면 N으로 설정, API 처리를 위해 이러한 형태로 바꿔줌
                if (userModify_push_check.isChecked()) {
                    push_check = "Y";
                } else {
                    push_check = "N";
                }

                //성별 여자이면 F, 남자이면 M, API 처리를 위해 이러한 형태로 바꿔줌
                if (userModify_male.isChecked()) {
                    gender_info = "M";
                } else if (userModify_female.isChecked()) {
                    gender_info = "F";
                }

                //입력 조건에 맞지 않을 시 종료시킴
                if(isGender()&&isID()&&isValidPasswd()&&isValidEmail()&&isName()&&isBirth()&&isInfoCheck()){
                    //Retrofit을 이용하여 회원가입을 위한 사용자 정보를 서버로 넘겨주는 작업을 진행함
                    RegisterUserInfo();
                }

            }
        });
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (userModify_email_text.getText().toString().isEmpty()) {
            // 이메일 공백
            Toast.makeText(RegisterActivity.this,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userModify_email_text.getText().toString()).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(RegisterActivity.this,"이메일 형식이 아닙니다.",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //이름 유효성 검사
    private boolean isName() {
        if (userModify_name_text.getText().toString().isEmpty()) {
            //이름 공백
            Toast.makeText(RegisterActivity.this,"이름을 입력해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (userModify_present_pw_et.getText().toString().isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(userModify_present_pw_et.getText().toString()).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(RegisterActivity.this, "잘못된 비밀번호 형식입니다. (영문, 숫자, 특수문자를 포함한 4~15자를 입력해주세요.)", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!userModify_present_pw_et.getText().toString().equals(userModify_check_pw_et.getText().toString())) {
            // 비밀번호 더블체크
            Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //아이디 유효성 검사
    private boolean isID() {
        if (userModify_id_text.getText().toString().isEmpty()) {
            // 아이디 공백
            Toast.makeText(RegisterActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ID_PATTERN.matcher(userModify_id_text.getText().toString()).matches()) {
            // 아이디 형식 불일치
            Toast.makeText(RegisterActivity.this, "잘못된 아이디 형식입니다. (영문, 숫자를 포함한 4~15자를 입력해주세요.)", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //성별 유효성 검사
    private boolean isGender(){
        if(!userModify_male.isChecked()&&!userModify_female.isChecked()) {
            //성별 체크 안함
            Toast.makeText(RegisterActivity.this, "성별을 체크해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(userModify_female.isChecked()&&userModify_male.isChecked()){
            //성별 두개 체크
            Toast.makeText(RegisterActivity.this, "성별을 하나만 체크해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }

    }

    //생년월일 유효성 검사
    private boolean isBirth(){
        if(register_datepicker.getText().toString().isEmpty()){
            //생년월일 입력 안함
            Toast.makeText(RegisterActivity.this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    //개인정보 동의 확인
    private boolean isInfoCheck(){
        if(!userModify_info_check.isChecked()){
            Toast.makeText(RegisterActivity.this, "서비스 이용약관 및 개인 정보 취급 방침에 동의해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
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

            user_year = String.valueOf(year);
            user_month = String.valueOf(monthOfYear + 1);
            user_day = String.valueOf(dayOfMonth);
            register_datepicker.setText(user_year + "-" + user_month
                    + "-" + user_day);
        }
    };

    private void RegisterUserInfo(){
        String authorization="zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept="application/json";
        String content_type="application/x-www-form-urlencoded";

         RegisterService registerService= ServiceGenerator.createService(RegisterService.class);
         Call<String> request=registerService.postInfo(authorization,accept,content_type,userModify_id_text.getText().toString(),
                 userModify_email_text.getText().toString(),
                 userModify_name_text.getText().toString(),
                 userModify_check_pw_et.getText().toString(),
                 gender_info ,push_check,
                 user_year, user_month , user_day);

         //요청 부분
         request.enqueue(new Callback<String>() {
             @Override
             public void onResponse(Call<String> call, Response<String> response) {
                 System.out.println("Response" + response.code()); //204 사이의 값이 나왔을 때는 회원가입이 정상적으로 이루어짐
                 response_code = response.code();
                 //204의 값이 나오지 않으면, 회원가입이 정상적으로 이루어지지 않음
                 if (response.code() != 204) {
                     backgroundThreadShortToast(getApplicationContext(), "이미 등록된 아이디입니다.");
                     return;
                 } else if(response.code() == 204){
                     backgroundThreadShortToast(getApplicationContext(),"회원가입이 완료 되었습니다.");
                 }

                 Intent intent = new Intent(getApplicationContext(), loginPageActivity.class);
                 intent.putExtra("user_id", userModify_id_text.getText().toString());
                 startActivity(intent);
                 finish();
             }

             @Override
             public void onFailure(Call<String> call, Throwable t) {
                 System.out.println("error + Connect Server Error is " + t.toString());
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

    //인터페이스 - 추상 메소드(구현부가 없는 메소드)의 모임
    /* retrofit은 인터페이스에 기술된 명세를 Http API(호출 가능한 객체)로 전환해줌
    => 우리가 요청할 API들에 대한 명세만을 Interface에 기술해두면 됨.
     */
    public interface RegisterService{
        @FormUrlEncoded
        @POST("api/consumers/registry")
        Call<String> postInfo(@Header("Authorization") String authorization,
                              @Header("Accept") String accept,
                              @Header("ShopContent-Type") String content_type,
                              @Field("consumerId") String consumerID, @Field("email") String email,
                              @Field("name") String name, @Field("password") String password,
                              @Field("gender") String gender, @Field("termsAdvertisement") String termsAdvertisement,
                              @Field("birthYear") String birthYear, @Field("birthMonth") String birthMonth, @Field("birthDay") String birthDay);
    }

}

