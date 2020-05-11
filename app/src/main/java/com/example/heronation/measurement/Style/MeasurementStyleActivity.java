package com.example.heronation.measurement.Style;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.heronation.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasurementStyleActivity extends AppCompatActivity {
    Boolean[] style=new Boolean[14];
    @BindView(R.id.style_1) ImageButton style_1;
    @BindView(R.id.style_2) ImageButton style_2;
    @BindView(R.id.style_3) ImageButton style_3;
    @BindView(R.id.style_4) ImageButton style_4;
    @BindView(R.id.style_5) ImageButton style_5;
    @BindView(R.id.style_6) ImageButton style_6;
    @BindView(R.id.style_7) ImageButton style_7;
    @BindView(R.id.style_8) ImageButton style_8;
    @BindView(R.id.style_9) ImageButton style_9;
    @BindView(R.id.style_10) ImageButton style_10;
    @BindView(R.id.style_11) ImageButton style_11;
    @BindView(R.id.style_12) ImageButton style_12;
    @BindView(R.id.style_13) ImageButton style_13;
    @BindView(R.id.style_14) ImageButton style_14;

    @BindView(R.id.measurement_style_finish_button) Button measurement_style_finish_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_style);
        ButterKnife.bind(this);
        Arrays.fill(style,false);

        style_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[0]==false) {
                    style_1.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[0]=true;
                }else {
                    style_1.clearColorFilter();
                    style[0]=false;
                }
            }
        });

        style_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[1]==false) {
                    style_2.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[1]=true;
                }else {
                    style_2.clearColorFilter();
                    style[1]=false;
                }
            }
        });

        style_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[2]==false) {
                    style_3.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[2]=true;
                }else {
                    style_3.clearColorFilter();
                    style[2]=false;
                }
            }
        });

        style_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[3]==false) {
                    style_4.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[3]=true;
                }else {
                    style_4.clearColorFilter();
                    style[3]=false;
                }
            }
        });

        style_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[4]==false) {
                    style_5.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[4]=true;
                }else {
                    style_5.clearColorFilter();
                    style[4]=false;
                }
            }
        });

        style_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[5]==false) {
                    style_6.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[5]=true;
                }else {
                    style_6.clearColorFilter();
                    style[5]=false;
                }
            }
        });

        style_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[6]==false) {
                    style_7.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[6]=true;
                }else {
                    style_7.clearColorFilter();
                    style[6]=false;
                }
            }
        });

        style_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[7]==false) {
                    style_8.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[7]=true;
                }else {
                    style_8.clearColorFilter();
                    style[7]=false;
                }
            }
        });

        style_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[8]==false) {
                    style_9.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[8]=true;
                }else {
                    style_9.clearColorFilter();
                    style[8]=false;
                }
            }
        });

        style_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[9]==false) {
                    style_10.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[9]=true;
                }else {
                    style_10.clearColorFilter();
                    style[9]=false;
                }
            }
        });

        style_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[10]==false) {
                    style_11.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[10]=true;
                }else {
                    style_11.clearColorFilter();
                    style[10]=false;
                }
            }
        });

        style_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[11]==false) {
                    style_12.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[11]=true;
                }else {
                    style_12.clearColorFilter();
                    style[11]=false;
                }
            }
        });

        style_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[12]==false) {
                    style_13.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[12]=true;
                }else {
                    style_13.clearColorFilter();
                    style[12]=false;
                }
            }
        });

        style_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style[13]==false) {
                    style_14.setColorFilter(Color.parseColor("#747474"), PorterDuff.Mode.MULTIPLY);
                    style[13]=true;
                }else {
                    style_14.clearColorFilter();
                    style[13]=false;
                }
            }
        });

        // 완료 버튼을 누를 시
        measurement_style_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 아이템이 2개인지 확인, 아니면 2개만 선택해달라는 Dialog를 띄움
                int select_count=0;
                for(Boolean style_bool: style){
                    if(style_bool==true)
                        select_count++;
                }

                if(select_count!=2){
                    Toast.makeText(getApplicationContext(),"아이템을 2개 선택해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    
                }
           }
        });

    }

    /* 뒤로가기 버튼을 눌렀을 때 */
    public void click_back_button(View view){
        finish();
    }
}
