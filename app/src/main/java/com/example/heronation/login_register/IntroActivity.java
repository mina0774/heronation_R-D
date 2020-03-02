package com.example.heronation.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.heronation.main.MainActivity;
import com.example.heronation.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }
    public void go_to_main(View view){
        Intent intent = new Intent(this, MainActivity.class);
        //비회원 사용자일 때
        intent.putExtra("access_token","null");
        startActivity(intent);
        finish();
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
