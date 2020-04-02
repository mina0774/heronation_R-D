package com.example.heronation.measurement.Body;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.heronation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasurementBodyActivity extends AppCompatActivity {
    @BindView(R.id.height_text) TextView heigt_text;
    @BindView(R.id.seekBar_height) SeekBar seekBar_height;
    @BindView(R.id.weight_text) TextView weight_text;
    @BindView(R.id.seekBar_weight) SeekBar seekBar_weight;
    @BindView(R.id.age_text) TextView age_text;
    @BindView(R.id.seekBar_age) SeekBar seekBar_age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_body);
        ButterKnife.bind(this);

        /* 키 Seekbar 설정 */
        seekBar_height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar_weight, int progress, boolean fromUser) {
                heigt_text.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_weight) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_weight) {
            }
        });

        /* 몸무게 Seekbar 설정 */
        seekBar_weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar_weight, int progress, boolean fromUser) {
                weight_text.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_weight) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_weight) {
            }
        });

        /* 나이 Seekbar 설정 */
        seekBar_age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar_weight, int progress, boolean fromUser) {
                age_text.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_weight) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_weight) {
            }
        });

    }

    /* 뒤로가기 버튼을 눌렀을 때 */
    public void click_back_button(View view){
        finish();
    }
}
