package com.example.heronation.measurement.AR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.measurement.AR.MeasurementARActivity;
import com.example.heronation.measurement.AR.MeasurementArFragment;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.Typeface.BOLD;

public class MeasurementResultActivity extends AppCompatActivity {
    @BindView(R.id.measurement_result_imageview) ImageView measurement_result_imageview;
    @BindView(R.id.measurement_again_button) Button measurement_again_button;
    @BindView(R.id.save_measurement_result_button) Button save_measurement_result_button;
    @BindView(R.id.measurement_result_item) LinearLayout measurement_result_item;
    @BindView(R.id.measurement_result_distance) LinearLayout measurement_result_distance;
    @BindView(R.id.measurement_result_cm) LinearLayout measurement_result_cm;

    public ArrayList<String> MeasureItem;
    public static Double[] measurement_items_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_result);
        ButterKnife.bind(this);

        MeasureItem= MeasurementArFragment.Measure_item; // 측정 항목을 받아옴
        measurement_items_distance=MeasurementARActivity.measurement_items_distance; // 측정 항목의 거리를 받아옴

        TextView result_measure_item[]=new TextView[MeasureItem.size()];
        TextView result_distance[]=new TextView[MeasureItem.size()];
        TextView result_cm[]=new TextView[MeasureItem.size()];


        // 측정 시작 시에 촬영한 사진 or 갤러리에서 받아온 사진을 띄워줌
        Glide.with(this).load(MeasurementArFragment.file.getAbsolutePath()).into(measurement_result_imageview);

        // 측정 결과값을 예쁘게 출력하기 위한 작업
        for(int i=0;i<MeasureItem.size();i++){
            result_measure_item[i]=new TextView(this);
            result_distance[i]=new TextView(this);
            result_cm[i]=new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity= Gravity.CENTER;
            result_measure_item[i].setLayoutParams(layoutParams);
            result_measure_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_measure_item[i].setTextSize(16);
            result_measure_item[i].setText(MeasureItem.get(i) + "\n");
            result_measure_item[i].setTextColor(Color.parseColor("#1d1d1d"));
            result_distance[i].setLayoutParams(layoutParams);
            result_distance[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_distance[i].setTextSize(16);
            String distanceString=String.format(Locale.getDefault(),"%.2f",measurement_items_distance[i]*100);
            result_distance[i].setText(distanceString + "\n");
            result_distance[i].setTextAppearance(BOLD);
            result_distance[i].setTextColor(Color.parseColor("#3464ff"));
            result_cm[i].setLayoutParams(layoutParams);
            result_cm[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_cm[i].setTextSize(16);
            result_cm[i].setText("cm\n");
            result_cm[i].setTextColor(Color.parseColor("#777777"));
            measurement_result_item.addView(result_measure_item[i]);
            measurement_result_distance.addView(result_distance[i]);
            measurement_result_cm.addView(result_cm[i]);
        }



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
