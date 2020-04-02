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
    Boolean style1=false,style2=false,style3=false,style4=false,style5=false,style6=false;
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
                if(style1==false) {
                    style_1.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style1=true;
                }else {
                    style_1.clearColorFilter();
                    style1=false;
                }
            }
        });

        style_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style2==false) {
                    style_2.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style2=true;
                }else {
                    style_2.clearColorFilter();
                    style2=false;
                }
            }
        });

        style_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style3==false) {
                    style_3.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style3=true;
                }else {
                    style_3.clearColorFilter();
                    style3=false;
                }
            }
        });

        style_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style4==false) {
                    style_4.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style4=true;
                }else {
                    style_4.clearColorFilter();
                    style4=false;
                }
            }
        });

        style_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style5==false) {
                    style_5.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style5=true;
                }else {
                    style_5.clearColorFilter();
                    style5=false;
                }
            }
        });

        style_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style6==false) {
                    style_6.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style6=true;
                }else {
                    style_6.clearColorFilter();
                    style6=false;
                }
            }
        });


    }

    /* 뒤로가기 버튼을 눌렀을 때 */
    public void click_back_button(View view){
        finish();
    }
}
