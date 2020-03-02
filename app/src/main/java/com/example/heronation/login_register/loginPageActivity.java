package com.example.heronation.login_register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.login_register.dataClass.UserLoginInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class  loginPageActivity extends AppCompatActivity {
    @BindView(R.id.login_id_et) EditText login_id_et;
    @BindView(R.id.login_password_et) EditText login_password_et;
    @BindView(R.id.login_button) Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        ButterKnife.bind(this);

        login_id_et.setText( getIntent().getStringExtra("user_id") );

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    public void Login(){
        String accept="application/json";
        String content_type="application/x-www-form-urlencoded";
        String heronation_api_login_key="66Gc6re4T1Prk5zsnKDsl5RaRVlU7J24VEU=";
        String heronation_api_uniqId_key="jvvzfj7p";
        String autorization= "Basic emV5b191c2VyOmlhbXVzZXI=";

        LoginService loginService= ServiceGenerator.createService(LoginService.class);
        retrofit2.Call<UserLoginInfo> request=loginService.LoginInfo(accept,content_type,heronation_api_login_key,heronation_api_uniqId_key,autorization,
                login_id_et.getText().toString(),login_password_et.getText().toString(),"password");

        request.enqueue(new retrofit2.Callback<UserLoginInfo>() {
            @Override
            public void onResponse(retrofit2.Call<UserLoginInfo> call, retrofit2.Response<UserLoginInfo> response) {
                UserLoginInfo userLoginInfo=response.body();

                if(response.code()!=200){
                    backgroundThreadShortToast(getApplicationContext(), "등록되지 않은 아이디거나 아이디 또는 비밀번호가 일치하지 않습니다.");
                    return;
                }
                else if(response.code()==200){
                    //JSON 파일의 값이 이렇게 Parsing 되어 값이 나옴
                    System.out.println("Response" + userLoginInfo.access_token+","+userLoginInfo.refresh_token+","+userLoginInfo.member_id+","+userLoginInfo.member_name);
                    backgroundThreadShortToast(getApplicationContext(), "로그인이 완료되었습니다.");
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("access_token",userLoginInfo.access_token);
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

    //인터페이스 - 추상 메소드(구현부가 없는 메시드)의 모임
    /* retrofit은 인터페이스에 기술된 명세를 Http API(호출 가능한 객체)로 전환해줌
    => 우리가 요청할 API들에 대한 명세만을 Interface에 기술해두면 됨.
     */
    public interface LoginService{
        @FormUrlEncoded
        @POST("oauth/token")
        retrofit2.Call<UserLoginInfo> LoginInfo(@Header("Accept") String accept,
                                        @Header("ShopContent-Type") String content_type,
                                        @Header("heronation-api-login-key") String heronation_api_login_key,
                                        @Header("heronation-api-uniqId-key") String heronation_api_uniqId_key,
                                        @Header("Authorization") String authorization,
                                        @Field("username") String username,
                                        @Field("password") String password,
                                        @Field("grant_type") String grant_type);
    }



}
