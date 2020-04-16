package com.example.heronation.measurement.Body;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.heronation.R;

import butterknife.BindView;

public class MeasurementBodySizeInfoActivity extends AppCompatActivity {
    @BindView(R.id.add_button) ImageButton add_button_all;
    @BindView(R.id.check_button_shoulder) ImageButton check_button_shoulder;
    @BindView(R.id.check_button_chest) ImageButton check_button_chest;
    @BindView(R.id.check_button_waist) ImageButton check_button_waist;
    @BindView(R.id.check_button_hip) ImageButton check_button_hip;
    @BindView(R.id.check_button_thigh) ImageButton check_button_thigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_body_size_info);
    }

    public void click_back_button(View view){
        finish();
    }
}
