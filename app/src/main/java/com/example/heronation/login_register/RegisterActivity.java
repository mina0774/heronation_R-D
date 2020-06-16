package com.example.heronation.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heronation.R;
import com.example.heronation.mypage.PolicyActivity;
import com.example.heronation.zeyoAPI.APIInterface;
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
    @BindView(R.id.btn_gender_woman) Button buttonWoman;
    @BindView(R.id.btn_gender_man) Button buttonMan;

    @BindView(R.id.edt_id) EditText editTextID;
    @BindView(R.id.edt_pw) EditText editTextPW;
    @BindView(R.id.edt_pw_check) EditText editTextPWCheck;
    @BindView(R.id.edt_email) EditText editTextEmail;
    @BindView(R.id.edt_name) EditText editTextName;

    @BindView(R.id.btn_require) ImageButton buttonRequire;
    @BindView(R.id.btn_option)  ImageButton buttonOption;
    @BindView(R.id.date_picker) TextView editTextdate;

    @BindView(R.id.txt_pw) TextView textViewPW;
    @BindView(R.id.txt_pw_check) TextView textViewPWCheck;
    @BindView(R.id.txt_id_check) TextView textViewIDCheck;
    @BindView(R.id.txt_email_check) TextView textViewEmailCheck;
    @BindView(R.id.txt_name_check) TextView textViewNameCheck;
    @BindView(R.id.txt_birth_check) TextView textViewBirthCheck;
    @BindView(R.id.txt_gender_check) TextView textViewGenderCheck;

    @BindView(R.id.btn_signup_success) Button buttonSignupSuccess;

    private boolean require=false;
    private String advertisement="N";
    private Boolean passwd_bool=false;
    private Boolean passwd_check_bool=false;
    private Boolean id_bool=false;
    private Boolean email_bool=false;
    private Boolean name_bool=false;
    private Boolean gender_bool=false;
    private Boolean date_bool=false;
    //성별
    private static String gender_info="";
    //사용자 생년월일
    private static String user_year;
    private static String user_month;
    private static String user_day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        /*생년월일 TextView클릭시 showDatePicker가 실행됨*/
        editTextdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        buttonWoman.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                buttonWoman.setBackgroundResource(R.drawable.rounded_rectangle_pink);
                buttonWoman.setTextColor(Color.parseColor("#ff576c"));
                buttonMan.setBackgroundResource(R.drawable.rounded_rectangle_gray);
                buttonMan.setTextColor(Color.parseColor("#777777"));
                gender_info = "W";
                textViewGenderCheck.setTextColor(Color.parseColor("#1048ff"));
                textViewGenderCheck.setText("성별이 입력되었습니다.");
                gender_bool=true;
            }
        });

        buttonMan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                buttonMan.setBackgroundResource(R.drawable.rounded_rectangle_pink);
                buttonMan.setTextColor(Color.parseColor("#ff576c"));
                buttonWoman.setBackgroundResource(R.drawable.rounded_rectangle_gray);
                buttonWoman.setTextColor(Color.parseColor("#777777"));
                gender_info = "M";
                textViewGenderCheck.setTextColor(Color.parseColor("#1048ff"));
                textViewGenderCheck.setText("성별이 입력되었습니다.");
                gender_bool=true;
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    if (Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-z]{2,6}$", editTextEmail.getText())) {
                        textViewEmailCheck.setText("올바른 이메일입니다.");
                        textViewEmailCheck.setTextColor(Color.parseColor("#1048ff"));
                        email_bool=true;
                    } else {
                        textViewEmailCheck.setText("올바르지 못한 이메일입니다.");
                        textViewEmailCheck.setTextColor(Color.parseColor("#ff576c"));
                        email_bool=false;
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    if (Pattern.matches("^[ㄱ-ㅎ가-힣]{2,10}|[a-zA-Z]{2,20}$", editTextName.getText())) {
                        textViewNameCheck.setText("올바른 이름입니다.");
                        textViewNameCheck.setTextColor(Color.parseColor("#1048ff"));
                        name_bool=true;
                    } else {
                        textViewNameCheck.setText("올바르지 못한 이름입니다.");
                        textViewNameCheck.setTextColor(Color.parseColor("#ff576c"));
                        name_bool=false;
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        //입력 조건은 EditText에 글자를 타이핑할때마다 체크함.
        editTextPW.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    if (Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", editTextPW.getText())) {
                        textViewPW.setText("올바른 비밀번호입니다.");
                        textViewPW.setTextColor(Color.parseColor("#1048ff"));
                        passwd_bool=true;
                    } else {
                        textViewPW.setText("8~20자의 영문 소문자, 숫자와 특수기호를 혼합해야 합니다.");
                        textViewPW.setTextColor(Color.parseColor("#ff576c"));
                        passwd_bool=false;
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        editTextPWCheck.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    if (editTextPW.getText().toString().equals(editTextPWCheck.getText().toString()) && !editTextPW.getText().toString().equals("")) {
                        textViewPWCheck.setText("비밀번호가 일치합니다.");
                        textViewPWCheck.setTextColor(Color.parseColor("#1048ff"));
                        passwd_check_bool=true;
                    } else {
                        textViewPWCheck.setText("비밀번호가 불일치합니다.");
                        textViewPWCheck.setTextColor(Color.parseColor("#ff576c"));
                        passwd_check_bool=false;
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        editTextID.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    if (Pattern.matches("^[A-Za-z0-9]{4,12}$", editTextID.getText())) {
                        textViewIDCheck.setText("올바른 아이디입니다.");
                        textViewIDCheck.setTextColor(Color.parseColor("#1048ff"));
                        id_bool=true;
                    } else {
                        textViewIDCheck.setText("4~12자의 영문 소문자,숫자를 혼합해서 사용 가능합니다.");
                        textViewIDCheck.setTextColor(Color.parseColor("#ff576c"));
                        id_bool=false;
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        buttonRequire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!require) {
                    buttonRequire.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    require = true;
                } else {
                    buttonRequire.setImageResource(R.drawable.ic_unchecked);
                    require = false;
                }
            }
        });

        buttonOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (advertisement == "N") {
                    buttonOption.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    advertisement = "Y";
                } else {
                    buttonOption.setImageResource(R.drawable.ic_unchecked);
                    advertisement = "N";
                }
            }
        });

        TextView txt_privacy = findViewById(R.id.txt_privacy_sign_up);
        txt_privacy.setPaintFlags(txt_privacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_privacy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(RegisterActivity.this, PolicyActivity.class);
                startActivity(intent);
            }
        });
        TextView txt_terms_of_service = findViewById(R.id.txt_terms_of_service);
        txt_terms_of_service.setPaintFlags(txt_terms_of_service.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_terms_of_service.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(RegisterActivity.this, PolicyActivity.class);
                startActivity(intent);
            }
        });

        /* 회원가입 버튼 클릭시, 회원가입 정보가 서버단으로 넘어가고, 스타일 설정 페이지로 넘어감 */
        buttonSignupSuccess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //체크박스가 체크 되어있으면, 푸시알림을 Y로 설정 아니면 N으로 설정, API 처리를 위해 이러한 형태로 바꿔줌


                //입력 조건에 맞지 않을 시 종료시킴
                if(passwd_check_bool&&passwd_bool&&name_bool&&email_bool&&date_bool&&id_bool&&gender_bool&&require){
                    //Retrofit을 이용하여 회원가입을 위한 사용자 정보를 서버로 넘겨주는 작업을 진행함
                    RegisterUserInfo();
                }else{
                    Toast.makeText(getApplicationContext(),"모든 정보를 올바르게 입력했는지 확인해주세요.",Toast.LENGTH_SHORT).show();
                }

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
            user_year = String.valueOf(year);
            user_month = String.valueOf(monthOfYear + 1);
            user_day = String.valueOf(dayOfMonth);
            editTextdate.setText(user_year + "-" + user_month
                    + "-" + user_day);
            textViewBirthCheck.setText("생년월일이 입력되었습니다.");
            textViewBirthCheck.setTextColor(Color.parseColor("#1048ff"));
            date_bool=true;
        }
    };

    private void RegisterUserInfo(){
        String authorization="zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept="application/json";
        String content_type="application/x-www-form-urlencoded";

        APIInterface.RegisterService registerService= ServiceGenerator.createService(APIInterface.RegisterService.class);
         Call<String> request=registerService.postInfo(authorization,accept,content_type,editTextID.getText().toString(),
                 editTextEmail.getText().toString(),
                 editTextName.getText().toString(),
                 editTextPW.getText().toString(),
                 gender_info ,advertisement,
                 user_year, user_month , user_day);

         //요청 부분
         request.enqueue(new Callback<String>() {
             @Override
             public void onResponse(Call<String> call, Response<String> response) {
                 System.out.println("Response" + response.code()); //204 사이의 값이 나왔을 때는 회원가입이 정상적으로 이루어짐
                 //204의 값이 나오지 않으면, 회원가입이 정상적으로 이루어지지 않음

                     if(response.code() == 204){
                         backgroundThreadShortToast(getApplicationContext(), "회원가입이 완료 되었습니다.");
                         Intent intent = new Intent(getApplicationContext(), loginPageActivity.class);
                         intent.putExtra("user_id", editTextID.getText().toString());
                         startActivity(intent);
                         finish(); }
                     else {
                         backgroundThreadShortToast(getApplicationContext(), "이미 등록된 아이디입니다.");
                         return;
                     }

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

}

