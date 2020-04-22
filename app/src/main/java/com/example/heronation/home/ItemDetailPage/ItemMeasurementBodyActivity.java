package com.example.heronation.home.ItemDetailPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heronation.R;
import com.example.heronation.home.dataClass.BodySizeLevel;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemMeasurementBodyActivity extends AppCompatActivity {
    @BindView(R.id.register_Female) Button register_female;
    @BindView(R.id.register_Male) Button register_male;
    @BindView(R.id.editText_age) EditText editText_age;
    @BindView(R.id.editText_height) EditText editText_height;
    @BindView(R.id.editText_weight) EditText editText_weight;

    String gender="";
    String age="";
    String height="";
    String weight="";
    String item_id;

    BodySizeLevel bodySizeLevel;

    public static ItemMeasurementBodyActivity itemMeasurementBodyActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_body);
        ButterKnife.bind(this);
        itemMeasurementBodyActivity=this;
        item_id=getIntent().getStringExtra("item_id");

        // 기존에 저장된 값을 불러옴
        getUserInfo(MainActivity.access_token);

        /* 성별 - 여성을 클릭했을 때 */
        register_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="F";
                register_female.setBackground(getDrawable(R.drawable.btn_background_click));
                register_female.setTextColor(Color.parseColor("#ffffff"));
                register_male.setBackground(getDrawable(R.drawable.btn_background));
                register_male.setTextColor(Color.parseColor("#000000"));
            }
        });

        /* 성별 - 남성을 클릭했을 때 */
        register_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="M";
                register_male.setBackground(getDrawable(R.drawable.btn_background_click));
                register_male.setTextColor(Color.parseColor("#ffffff"));
                register_female.setBackground(getDrawable(R.drawable.btn_background));
                register_female.setTextColor(Color.parseColor("#000000"));
            }
        });
    }

    /* 뒤로가기 버튼을 눌렀을 때 */
    public void click_back_button(View view){
        finish();
    }

    /* 다음 버튼을 눌렀을 때 */
    public void click_next_button(View view){
        age=editText_age.getText().toString();
        height=editText_height.getText().toString();
        weight=editText_weight.getText().toString();

        if(!age.equals("") && !gender.equals("") && !height.equals("") && !weight.equals("") ) {
            get_measurement_default_value();
        }else{
            Toast.makeText(ItemMeasurementBodyActivity.this, "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void get_measurement_default_value(){
        String authorization="zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept="application/json";

        APIInterface.GetMeasurementDefaultValueService getMeasurementDefaultValueService= ServiceGenerator.createService(APIInterface.GetMeasurementDefaultValueService.class);
        retrofit2.Call<BodySizeLevel> request= getMeasurementDefaultValueService.GetMeasurementDefaultValue(gender,age,height,weight,authorization,accept);
        request.enqueue(new Callback<BodySizeLevel>() {
            @Override
            public void onResponse(Call<BodySizeLevel> call, Response<BodySizeLevel> response) {
                if(response.isSuccessful()) {
                    bodySizeLevel = response.body();
                    Intent intent = new Intent(ItemMeasurementBodyActivity.this, ItemMeasurementBodySizeInfoActivity.class);
                    intent.putExtra("body_size_default_level",bodySizeLevel);
                    intent.putExtra("gender",gender);
                    intent.putExtra("age",age);
                    intent.putExtra("height",height);
                    intent.putExtra("weight",weight);
                    intent.putExtra("item_id",item_id);
                    startActivity(intent);
                }else{ // 입력 형식이 잘못 되었을 때
                    backgroundThreadShortToast(getApplicationContext(),"형식에 맞게 입력해주세요.");
                }
            }

            @Override
            public void onFailure(Call<BodySizeLevel> call, Throwable t) {

            }
        });

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
                    UserMyInfo userMyInfo = response.body();

                    // 성별
                    if(userMyInfo.getGender().equals("F")){
                        gender="F";
                        register_female.setBackground(getDrawable(R.drawable.btn_background_click));
                        register_female.setTextColor(Color.parseColor("#ffffff"));
                        register_male.setBackground(getDrawable(R.drawable.btn_background));
                        register_male.setTextColor(Color.parseColor("#000000"));
                    }
                    else if(userMyInfo.getGender().equals("M")){
                        gender="M";
                        register_male.setBackground(getDrawable(R.drawable.btn_background_click));
                        register_male.setTextColor(Color.parseColor("#ffffff"));
                        register_female.setBackground(getDrawable(R.drawable.btn_background));
                        register_female.setTextColor(Color.parseColor("#000000"));
                    }

                    // 나이
                    Calendar cal=Calendar.getInstance();
                    SimpleDateFormat formats = new SimpleDateFormat ( "yyyy");
                    int current_time = Integer.parseInt(formats.format(cal.getTime()));
                    editText_age.setText(Integer.toString(current_time-userMyInfo.getBirthYear()+1));

                    // 키
                    if(userMyInfo.getHeight()!=null){
                        editText_height.setText(userMyInfo.getHeight().toString());
                    }

                    // 몸무게
                    if(userMyInfo.getWeight()!=null){
                        editText_weight.setText(userMyInfo.getWeight().toString());
                    }

                }
            }
            @Override
            public void onFailure(Call<UserMyInfo> call, Throwable t) {
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
