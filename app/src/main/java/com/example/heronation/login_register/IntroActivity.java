package com.example.heronation.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.heronation.login_register.dataClass.UserLoginInfo;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity {
    public static String style_tag_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // 로그인 세션 유지 위해 RefreshToken이 있는지 확인하고 있으면, Main으로 넘겨주고 값을 갱신함
        SharedPreferences pref = getSharedPreferences("token_management",MODE_PRIVATE);
        String refresh_token=pref.getString("refresh_token",null);

        if(refresh_token!=null){
            String authorization = Credentials.basic("zeyo_user", "iamuser");
            String accept="application/json";
            String content_type="application/x-www-form-urlencoded";
            String api_login_key="66Gc6re4T1Prk5zsnKDsl5RaRVlU7J24VEU=";
            String grant_type="refresh_token";

            APIInterface.RefreshLoginService refreshLoginService= ServiceGenerator.createService(APIInterface.RefreshLoginService.class);
            retrofit2.Call<UserLoginInfo> request=refreshLoginService.RefreshLogin(authorization,accept,content_type,api_login_key,refresh_token,grant_type);

            request.enqueue(new Callback<UserLoginInfo>() {
                @Override
                public void onResponse(Call<UserLoginInfo> call, Response<UserLoginInfo> response) {
                    Log.d("response",response.isSuccessful()+"");
                    if(response.isSuccessful()){
                        UserLoginInfo userLoginInfo=response.body();

                        // 로그인 세션 유지를 위해 SharedPreferences에 토큰값을 저장
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putString("refresh_token",userLoginInfo.getRefresh_token());
                        editor.commit();

                        getStyleTagInfo(userLoginInfo.getAccess_token());
                    }
                }

                @Override
                public void onFailure(Call<UserLoginInfo> call, Throwable t) {
                    System.out.println("error + Connect Server Error is " + t.toString());
                }
            });

        }
    }

    public void getStyleTagInfo(String access_token){
        String authorization = "";
        String accept = "application/json";
        authorization = "bearer " + access_token;
        APIInterface.UserInfoService userInfoService = ServiceGenerator.createService(APIInterface.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request = userInfoService.UserInfo(authorization, accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                UserMyInfo userMyInfo = response.body();
                //스타일 태그 받기
                style_tag_id="";
                if(userMyInfo.getStyleTagResponses()!=null) {
                    for (int i = 0; i < userMyInfo.getStyleTagResponses().size(); i++) {
                        style_tag_id += userMyInfo.getStyleTagResponses().get(i).getId() + ",";
                        if (i == userMyInfo.getStyleTagResponses().size() - 1) {
                            style_tag_id += userMyInfo.getStyleTagResponses().get(i).getId();
                        }
                    }
                }
                Log.d("스타일",style_tag_id);

                Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                intent.putExtra("access_token",access_token);
                intent.putExtra("style_tag_id",style_tag_id);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<UserMyInfo> call, Throwable t) {
            }
        });
    }

    public void go_to_login(View view){
        Intent intent = new Intent(this, loginPageActivity.class);
        startActivity(intent);
    }

    public void go_to_register(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
