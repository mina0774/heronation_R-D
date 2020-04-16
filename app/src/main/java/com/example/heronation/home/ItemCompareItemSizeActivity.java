package com.example.heronation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.heronation.R;

public class ItemCompareItemSizeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_compare_item_size);
    }

    public void click_back_button(View view){
        finish();
    }
}
