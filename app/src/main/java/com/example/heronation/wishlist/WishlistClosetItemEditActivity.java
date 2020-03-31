package com.example.heronation.wishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.main.MainActivity;
import com.example.heronation.measurement.AR.MeasurementArFragment;
import com.example.heronation.wishlist.dataClass.ClosetDetailResponse;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class WishlistClosetItemEditActivity extends AppCompatActivity {
    @BindView(R.id.wishlist_closet_item) ImageView image;
    @BindView(R.id.wishlist_closet_item_category) TextView category;
    @BindView(R.id.wishlist_closet_item_name) TextView item_name;
    @BindView(R.id.wishlist_closet_item_date) TextView date;
    @BindView(R.id.wishlist_closet_item_shop_name) TextView shop_name;
    @BindView(R.id.wishlist_closet_item_measurement_type) TextView measurement_type;
    @BindView(R.id.wishlist_closet_item_measurement_id) TextView measurement_id;
    @BindView(R.id.measurement_result_item) LinearLayout measurement_result_item;
    @BindView(R.id.measurement_result_distance) LinearLayout measurement_result_distance;
    @BindView(R.id.measurement_result_cm) LinearLayout measurement_result_cm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_closet_item_edit);
        ButterKnife.bind(this);

        // 옷장에서 선택한 리사이클러뷰의 아이템 정보를 받아오기 위한 인텐트
        Intent intent=getIntent();

        Glide.with(this).load(intent.getStringExtra("image")).into(image);
        category.setText(intent.getStringExtra("category"));
        item_name.setText(intent.getStringExtra("item_name"));
        date.setText(intent.getStringExtra("date"));
        shop_name.setText(intent.getStringExtra("shop_name"));
        measurement_type.setText(intent.getStringExtra("measurement_type"));
        measurement_id.setText(intent.getStringExtra("id"));

        // 옷장 아이템의 측정 정보를 받아와 화면에 뿌리는 함수
        getClosetDetailInfo();

    }

    // back 버튼 눌렀을 때, 액티비티 종료
    public void click_back_button(View view){
        finish();
    }

    // 옷장의 특정 아이템의 구체적인 정보를 받아오는 작업
    public void getClosetDetailInfo(){
        String authorization="bearer "+ MainActivity.access_token;
        String accept="application/json";
        APIInterface.GetClosetDetailInfoService getClosetDetailInfoService= ServiceGenerator.createService(APIInterface.GetClosetDetailInfoService.class);
        retrofit2.Call<ClosetDetailResponse> request=getClosetDetailInfoService.GetClosetDetailInfo(Integer.parseInt(measurement_id.getText().toString()),authorization,accept);
        request.enqueue(new Callback<ClosetDetailResponse>() {
            @Override
            public void onResponse(Call<ClosetDetailResponse> call, Response<ClosetDetailResponse> response) {
                ClosetDetailResponse closetDetailResponse = response.body();

                // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
                for (int i=0; i<closetDetailResponse.getWardrobeScmmValueResponses().size();i++){
                    TextView result_measure_item[]=new TextView[closetDetailResponse.getWardrobeScmmValueResponses().size()];
                    TextView result_distance[]=new TextView[closetDetailResponse.getWardrobeScmmValueResponses().size()];
                    TextView result_cm[]=new TextView[closetDetailResponse.getWardrobeScmmValueResponses().size()];
                    result_measure_item[i]=new TextView(getApplicationContext());
                    result_distance[i]=new TextView(getApplicationContext());
                    result_cm[i]=new TextView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity= Gravity.CENTER;
                    result_measure_item[i].setLayoutParams(layoutParams);
                    result_measure_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    result_measure_item[i].setTextSize(16);
                    result_measure_item[i].setText(closetDetailResponse.getWardrobeScmmValueResponses().get(i).getMeasureItemName() + "\n");
                    result_measure_item[i].setTextColor(Color.parseColor("#1d1d1d"));
                    result_distance[i].setLayoutParams(layoutParams);
                    result_distance[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    result_distance[i].setTextSize(16);
                    result_distance[i].setText(closetDetailResponse.getWardrobeScmmValueResponses().get(i).getValue() + "\n");
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
            }

            @Override
            public void onFailure(Call<ClosetDetailResponse> call, Throwable t) {

            }
        });


    }
}
