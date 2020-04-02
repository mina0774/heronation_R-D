package com.example.heronation.measurement.Style;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.heronation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasurementStyleActivity extends AppCompatActivity {
    @BindView(R.id.style_1) ImageButton style_1;
    @BindView(R.id.style_2) ImageButton style_2;
    @BindView(R.id.style_3) ImageButton style_3;
    @BindView(R.id.style_4) ImageButton style_4;
    @BindView(R.id.style_5) ImageButton style_5;
    @BindView(R.id.style_6) ImageButton style_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_style);
        ButterKnife.bind(this);

        style_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                style_1.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
            }
        });


    }

    /* 뒤로가기 버튼을 눌렀을 때 */
    public void click_back_button(View view){
        finish();
    }
}
