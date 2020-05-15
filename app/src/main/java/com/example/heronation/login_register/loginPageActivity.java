package com.example.heronation.login_register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.heronation.login_register.dataClass.BodyResponse;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.login_register.dataClass.UserLoginInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

import static java.lang.Integer.parseInt;

public class  loginPageActivity extends AppCompatActivity {
    @BindView(R.id.login_id_et) EditText login_id_et;
    @BindView(R.id.login_password_et) EditText login_password_et;
    @BindView(R.id.login_button) Button login_button;
    /* access token을 Package 내에서 공유 , access token은 로그인할 때 한번만 받음 */
    public String access_token;
    public String topic1="";
    public String topic2="";
    public String topic3="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        ButterKnife.bind(this);

        login_id_et.setText( getIntent().getStringExtra("user_id") );

        /* 로그인 버튼을 클릭했을 때 */
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        /* 파이어베이스 구독 설정 */
        // Topic 방식
        FirebaseMessaging.getInstance().subscribeToTopic("rnd")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceId failed", task.getException());
                            return;
                        }
                    }
                });
    }

    public void Login(){
        String authorization = Credentials.basic("zeyo_user", "iamuser");
        String accept="application/json";
        String content_type="application/x-www-form-urlencoded";
        String heronation_api_login_key="66Gc6re4T1Prk5zsnKDsl5RaRVlU7J24VEU=";
        String heronation_api_uniqId_key="1580197357282_xerh4r4k5xkpgbo";
        String heronation_api_comparedType_key="brand";
        String heronation_api_comparedSubCategoryId_key="";

        APIInterface.LoginService loginService= ServiceGenerator.createService(APIInterface.LoginService.class);
        retrofit2.Call<UserLoginInfo> request=loginService.LoginInfo(authorization,accept,content_type,heronation_api_login_key,heronation_api_uniqId_key,
                heronation_api_comparedType_key, heronation_api_comparedSubCategoryId_key,
                login_id_et.getText().toString(),login_password_et.getText().toString(),"password");

        SharedPreferences pref=getSharedPreferences("token_management",MODE_PRIVATE);

        request.enqueue(new retrofit2.Callback<UserLoginInfo>() {
            @Override
            public void onResponse(retrofit2.Call<UserLoginInfo> call, retrofit2.Response<UserLoginInfo> response) {
                UserLoginInfo userLoginInfo=response.body();
                if(response.code()!=200){
                    backgroundThreadShortToast(getApplicationContext(), "등록되지 않은 아이디거나 아이디 또는 비밀번호가 일치하지 않습니다.");
                    return;
                }
                else if(response.code()==200){

                    // 로그인 세션 유지를 위해 SharedPreferences에 토큰값을 저장
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString("refresh_token",userLoginInfo.getRefresh_token());
                    editor.commit();

                    GetUserInfo(userLoginInfo.getAccess_token());

                    //JSON 파일의 값이 이렇게 Parsing 되어 값이 나옴
                    System.out.println("Response" + userLoginInfo.access_token+","+userLoginInfo.refresh_token+","+userLoginInfo.member_id+","+userLoginInfo.member_name);
                    backgroundThreadShortToast(getApplicationContext(), "로그인이 완료되었습니다.");
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }

                access_token=userLoginInfo.access_token;
                intent.putExtra("access_token",access_token);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(retrofit2.Call<UserLoginInfo> call, Throwable t) {
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

    /* 사용자 체형 정보 받아와서 구독하기 */
    public void GetUserInfo(String access_token) {
        String authorization = "";
        String accept = "application/json";
        authorization = "bearer " + access_token;
        APIInterface.UserInfoService userInfoService = ServiceGenerator.createService(APIInterface.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request = userInfoService.UserInfo(authorization, accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                UserMyInfo userMyInfo = response.body();

                // 체형 정보가 있을 시에 구독하기
                if(userMyInfo.getBodyResponses().size()!=0){
                    userMyInfo.getGender();
                    long shoulder_value = Math.round(userMyInfo.getBodyResponses().get(0).getValue()); // 어깨 너비
                    long waist_value =  Math.round(userMyInfo.getBodyResponses().get(4).getValue()); // 허리 둘레

                    topic1=userMyInfo.getGender()+"_T_"+shoulder_value;
                    topic2=userMyInfo.getGender()+"_B_"+waist_value;
                    topic3=userMyInfo.getGender()+"_A_"+shoulder_value+"_"+waist_value;

                    FirebaseMessaging.getInstance().subscribeToTopic(topic1);
                    FirebaseMessaging.getInstance().subscribeToTopic(topic2);
                    FirebaseMessaging.getInstance().subscribeToTopic(topic3);
                }
            }

            @Override
            public void onFailure(Call<UserMyInfo> call, Throwable t) {
            }
        });

    }

}
