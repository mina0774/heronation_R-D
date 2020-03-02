package com.example.heronation.shop;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heronation.R;

public class ShopRankingSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_ranking_search);
    }

    public void click_back_button(View view){
        finish();
    }
}
