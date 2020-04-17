package com.example.heronation.measurement.Body;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.heronation.R;

public class MeasurementBodySizeDetailInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_body_size_detail_info);
    }

    public void click_back_button(View view){
        finish();
    }
}
