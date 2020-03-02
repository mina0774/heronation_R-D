package com.example.heronation.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.heronation.R;
import com.example.heronation.login_register.RegisterActivity;
import com.example.heronation.login_register.RegisterAiActivity;

public class RegisterBodyActivity extends AppCompatActivity {
    /*키, 무게, 나이 변수*/
    public int number_height = 0;
    public int number_weight = 0;
    public int number_age = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_body);
        /*SeekBar - height 값 바꾸는건 xml 파일에서 바꿀 수 있어용 */
        final TextView height = (TextView) findViewById(R.id.height_text);
        SeekBar seekBar_height = (SeekBar) findViewById(R.id.seekBar_height);
        seekBar_height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar_height, int progress, boolean fromUser) {
                height.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_height) {
                number_height = seekBar_height.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_height) {
                number_height = seekBar_height.getProgress();
            }
        });
        /*SeekBar - weight 값 바꾸는건 xml 파일에서 바꿀 수 있어용 */
        final TextView weight = (TextView) findViewById(R.id.weight_text);
        SeekBar seekBar_weight = (SeekBar) findViewById(R.id.seekBar_weight);
        seekBar_weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar_weight, int progress, boolean fromUser) {
                weight.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_weight) {
                number_weight = seekBar_weight.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_weight) {
                number_weight = seekBar_weight.getProgress();
            }
        });
        /*SeekBar - age 값 바꾸는건 xml 파일에서 바꿀 수 있어용 */
        final TextView age = (TextView) findViewById(R.id.age_text);
        SeekBar seekBar_age = (SeekBar) findViewById(R.id.seekBar_age);
        seekBar_age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar_age, int progress, boolean fromUser) {
                age.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_age) {
                number_age = seekBar_age.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_age) {
                number_age = seekBar_age.getProgress();
            }
        });
    }

/*이전 버튼 클릭시 이동*/
    public void click_registerbody_previous(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
/*다음 버튼 클릭시 이동*/
    public void click_registerbody_next(View view) {
        Intent intent = new Intent(this, RegisterAiActivity.class);
        startActivity(intent);
    }
}

