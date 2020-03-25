package com.example.heronation.measurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.main.MainActivity;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasurementResultActivity extends AppCompatActivity {
    @BindView(R.id.measurement_result_imageview) ImageView measurement_result_imageview;
    @BindView(R.id.measurement_result_textview) TextView measurement_result_textview;
    @BindView(R.id.measurement_again_button) Button measurement_again_button;
    @BindView(R.id.save_measurement_result_button) Button save_measurement_result_button;

    public ArrayList<String> MeasureItem=MeasurementArFragment.Measure_item;
    public static Double[] measurement_items_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_result);
        ButterKnife.bind(this);

        measurement_items_distance=MeasurementARActivity.measurement_items_distance; // 측정 항목의 거리를 받아옴

        // 측정 시작 시에 촬영한 사진 or 갤러리에서 받아온 사진을 띄워줌
        Glide.with(this).load(MeasurementArFragment.file.getAbsolutePath()).into(measurement_result_imageview);

        // 측정 결과값을 받아오는 작업
        String result="";
        for(int i=0;i<MeasureItem.size();i++){
            String distanceString=String.format(Locale.getDefault(),"%.2f",measurement_items_distance[i]*100)+"cm";
            result+=MeasureItem.get(i)+": "+distanceString+"\n";
        }

        // 측정 결과값을 텍스트뷰에 설정
        measurement_result_textview.setText(result);

        // 재촬영 버튼을 눌렀을 경우, AR로 돌아감
        measurement_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MeasurementARActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 저장 버튼을 눌렀을 경우 ? 일단 Main으로 돌아감 (수정 필요)
        save_measurement_result_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
