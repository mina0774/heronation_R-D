package com.example.heronation.measurement.Body;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.heronation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasurementBodyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_body);
        ButterKnife.bind(this);
    }

    /* 뒤로가기 버튼을 눌렀을 때 */
    public void click_back_button(View view){
        finish();
    }

    /* 다음 버튼을 눌렀을 때 */
    public void click_next_button(View view){
        Intent intent=new Intent(MeasurementBodyActivity.this,MeasurementBodySizeInfoActivity.class);
        startActivity(intent);
    }
}
