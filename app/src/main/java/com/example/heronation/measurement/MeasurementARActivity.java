package com.example.heronation.measurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.example.heronation.R;

/* 측정 클래스, MeasureFragment에서 카테고리에 따른 측정항목이 넘어오면, 해당 항목을 측정해서 결과값을 MeasureResult로 넘겨줌. */
public class MeasurementARActivity extends AppCompatActivity {
    public static Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_ar);
    }
}
