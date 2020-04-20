package com.example.heronation.home;

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
import com.example.heronation.measurement.Body.dataClass.BodySizeLevel;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeasurementBodyActivity extends AppCompatActivity {
    @BindView(R.id.register_Female) Button register_female;
    @BindView(R.id.register_Male) Button register_male;
    @BindView(R.id.editText_age) EditText editText_age;
    @BindView(R.id.editText_height) EditText editText_height;
    @BindView(R.id.editText_weight) EditText editText_weight;

    String gender="";
    String age="";
    String height="";
    String weight="";

    BodySizeLevel bodySizeLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_body);
        ButterKnife.bind(this);

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
            Toast.makeText(MeasurementBodyActivity.this, "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(MeasurementBodyActivity.this, MeasurementBodySizeInfoActivity.class);
                    intent.putExtra("body_size_default_level",bodySizeLevel);
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
