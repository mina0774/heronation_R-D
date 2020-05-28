package com.example.heronation.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.heronation.home.ItemDetailPage.ItemDetailActivity;
import com.example.heronation.login_register.dataClass.UserLoginInfo;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.Set;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity {
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


                        Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                        intent.putExtra("access_token",userLoginInfo.getAccess_token());
                        if (getIntent().getData() != null) {
                            intent.putExtra("kakao_share_data",getIntent().getData().toString());
                        }
                        if(getIntent().getStringExtra("firebase_push")!=null){
                            intent.putExtra("firebase_push","firebase_push");
                            intent.putExtra("item_id", getIntent().getStringExtra("item_id"));
                            intent.putExtra("item_image", getIntent().getStringExtra("item_image"));
                            intent.putExtra("item_name",getIntent().getStringExtra("item_name"));
                            intent.putExtra("item_price", getIntent().getStringExtra("item_price"));
                            intent.putExtra("item_url", getIntent().getStringExtra("item_url"));
                        }
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UserLoginInfo> call, Throwable t) {
                    System.out.println("error + Connect Server Error is " + t.toString());
                }
            });

        }
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
