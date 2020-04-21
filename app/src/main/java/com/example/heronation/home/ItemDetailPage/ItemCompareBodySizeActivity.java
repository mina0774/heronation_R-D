package com.example.heronation.home.ItemDetailPage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.home.dataClass.CompareWithBody;
import com.example.heronation.main.MainActivity;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class ItemCompareBodySizeActivity extends AppCompatActivity {
    @BindView(R.id.measurement_item_name) TextView measurement_item_name;
    @BindView(R.id.measurement_item_image) ImageView measurement_item_image;

    @BindView(R.id.comparison_size_S) Button comparison_size_S;
    @BindView(R.id.comparison_size_M) Button comparison_size_M;
    @BindView(R.id.comparison_size_L) Button comparison_size_L;
    @BindView(R.id.comparison_size_XL) Button comparison_size_XL;
    @BindView(R.id.recommendation_size_textview) TextView recommendation_size_textview;

    @BindView(R.id.measurement_result_item) LinearLayout measurement_result_item;
    @BindView(R.id.measurement_result_distance) LinearLayout measurement_result_distance;
    @BindView(R.id.measurement_result_cm) LinearLayout measurement_result_cm;

    String item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_compare_body_size);
        ButterKnife.bind(this);

        item_id=getIntent().getStringExtra("item_id");
        compare_product_size_with_body();
    }

    public void click_back_button(View view){
        finish();
    }

    public void compare_product_size_with_body(){
        String authorization = "Bearer "+ MainActivity.access_token;
        String accept="application/json";
        String content_type="application/json";

        APIInterface.CompareProductSizeWithBodyService compareProductSizeWithBodyService = ServiceGenerator.createService(APIInterface.CompareProductSizeWithBodyService.class);
        retrofit2.Call<CompareWithBody> request = compareProductSizeWithBodyService.CompareProductSizeWithBody(Integer.parseInt(item_id),authorization,accept,content_type);
        request.enqueue(new Callback<CompareWithBody>() {
            @Override
            public void onResponse(Call<CompareWithBody> call, Response<CompareWithBody> response) {
                CompareWithBody compareWithBody=response.body();

                // 현재 보고있는 상품 정보 설정
                measurement_item_name.setText(compareWithBody.getItemResponse().getName());
                measurement_item_image.setBackground(new ShapeDrawable(new OvalShape()));
                measurement_item_image.setClipToOutline(true);
                Glide.with(getApplicationContext()).load(compareWithBody.getItemResponse().getShopImage()).error(R.drawable.shop_item_example_img_2).crossFade().into(measurement_item_image);

                // 사이즈 추천 값에 따라 첫화면 생성 - 일치 확률 최대값 찾기
                Double max=compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(0).getCompareResultDerive();
                Integer max_num=0;
                for(int i=0; i<compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size(); i++){
                    if(max< compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(i).getCompareResultDerive()){
                        max_num=i;
                    }
                }
                // max_num에 따라 처음 화면에 뿌려지는 수치값이 다름
                switch(max_num){
                    case 0: view_size_S(compareWithBody); recommendation_size_textview.setText("S size "); break;
                    case 1: view_size_M(compareWithBody); recommendation_size_textview.setText("M size "); break;
                    case 2: view_size_L(compareWithBody); recommendation_size_textview.setText("L size "); break;
                    case 3: view_size_XL(compareWithBody); recommendation_size_textview.setText("XL size "); break;
                }

                if(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size()>=1){ // S 사이즈 버튼 활성화
                    comparison_size_S.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view_size_S(compareWithBody);
                            }
                    });
                }

                if(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size()>=2){ // M 사이즈 버튼 활성화
                    comparison_size_M.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            view_size_M(compareWithBody);
                        }
                    });
                }

                if(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size()>=3){ // L 사이즈 버튼 활성화
                    comparison_size_L.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           view_size_L(compareWithBody);
                        }
                    });
                }

                if(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size()>=4){ // XL 사이즈 버튼 활성화
                    comparison_size_XL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           view_size_XL(compareWithBody);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CompareWithBody> call, Throwable t) {

            }
        });
    }

    public void view_size_S(CompareWithBody compareWithBody){
        // 기존 화면 삭제
        measurement_result_item.removeAllViews();
        measurement_result_distance.removeAllViews();
        measurement_result_cm.removeAllViews();

        // 버튼 색깔 설정
        comparison_size_S.setTextColor(Color.parseColor("#656aed"));
        comparison_size_S.setBackground(getDrawable(R.drawable.button_background_purple));

        comparison_size_M.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_M.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_L.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_L.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_XL.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_XL.setBackground(getDrawable(R.drawable.button_background));

        // 동적으로 띄워줄 텍스트뷰 인덱스 값 할당
        TextView[]  result_item=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(0).getGoodsScmmValues().size()];
        TextView[]  result_distance=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(0).getGoodsScmmValues().size()];
        TextView[]  result_cm=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(0).getGoodsScmmValues().size()];

        // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
        for (int i = 0; i < compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(0).getGoodsScmmValues().size(); i++) {

            result_item[i] = new TextView(getApplicationContext());
            result_distance[i] = new TextView(getApplicationContext());
            result_cm[i] = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            result_item[i].setLayoutParams(layoutParams);
            result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_item[i].setTextSize(16);
            result_item[i].setText(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(0).getGoodsScmmValues().get(i).getMeasureItemName()+ "\n");
            result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

            String distance="";
            for(int a=0;a<compareWithBody.getBodysResponses().size();a++){
                if(compareWithBody.getBodysResponses().get(a).getMeasureItemId()==compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(0).getGoodsScmmValues().get(i).getMeasureItemId()){
                    distance=Double.toString(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(0).getGoodsScmmValues().get(i).getValue()-compareWithBody.getBodysResponses().get(a).getValue());
                }
            }
            result_distance[i].setLayoutParams(layoutParams);
            result_distance[i].setTextSize(16);
            if(Double.parseDouble(distance)>0) {
                result_distance[i].setText("+"+distance+"\n");
            }else if(Double.parseDouble(distance)==0){
                result_distance[i].setText("="+distance+"\n");
            }else{
                result_distance[i].setText(distance+"\n");
            }
            result_distance[i].setTextAppearance(BOLD);
            result_distance[i].setTextColor(Color.parseColor("#1d1d1d"));

            result_cm[i].setLayoutParams(layoutParams);
            result_cm[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_cm[i].setTextSize(16);
            result_cm[i].setText("cm\n");
            result_cm[i].setTextColor(Color.parseColor("#777777"));

            measurement_result_item.addView(result_item[i]);
            measurement_result_distance.addView(result_distance[i]);
            measurement_result_cm.addView(result_cm[i]);
        }
    }

    public void view_size_M(CompareWithBody compareWithBody){
        // 기존 화면 삭제
        measurement_result_item.removeAllViews();
        measurement_result_distance.removeAllViews();
        measurement_result_cm.removeAllViews();

        // 버튼 색깔 설정
        comparison_size_M.setTextColor(Color.parseColor("#656aed"));
        comparison_size_M.setBackground(getDrawable(R.drawable.button_background_purple));

        comparison_size_S.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_S.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_L.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_L.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_XL.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_XL.setBackground(getDrawable(R.drawable.button_background));

        // 동적으로 띄워줄 텍스트뷰 인덱스 값 할당
        TextView[]  result_item=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(1).getGoodsScmmValues().size()];
        TextView[]  result_distance=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(1).getGoodsScmmValues().size()];
        TextView[]  result_cm=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(1).getGoodsScmmValues().size()];

        // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
        for (int i = 0; i < compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(1).getGoodsScmmValues().size(); i++) {

            result_item[i] = new TextView(getApplicationContext());
            result_distance[i] = new TextView(getApplicationContext());
            result_cm[i] = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            result_item[i].setLayoutParams(layoutParams);
            result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_item[i].setTextSize(16);
            result_item[i].setText(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(1).getGoodsScmmValues().get(i).getMeasureItemName()+ "\n");
            result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

            String distance="";
            for(int a=0;a<compareWithBody.getBodysResponses().size();a++){
                if(compareWithBody.getBodysResponses().get(a).getMeasureItemId()==compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(1).getGoodsScmmValues().get(i).getMeasureItemId()){
                    distance=Double.toString(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(1).getGoodsScmmValues().get(i).getValue()-compareWithBody.getBodysResponses().get(a).getValue());
                }
            }
            result_distance[i].setLayoutParams(layoutParams);
            result_distance[i].setTextSize(16);
            if(Double.parseDouble(distance)>0) {
                result_distance[i].setText("+"+distance+"\n");
            }else if(Double.parseDouble(distance)==0){
                result_distance[i].setText("="+distance+"\n");
            }else{
                result_distance[i].setText(distance+"\n");
            }
            result_distance[i].setTextAppearance(BOLD);
            result_distance[i].setTextColor(Color.parseColor("#1d1d1d"));

            result_cm[i].setLayoutParams(layoutParams);
            result_cm[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_cm[i].setTextSize(16);
            result_cm[i].setText("cm\n");
            result_cm[i].setTextColor(Color.parseColor("#777777"));

            measurement_result_item.addView(result_item[i]);
            measurement_result_distance.addView(result_distance[i]);
            measurement_result_cm.addView(result_cm[i]);
        }
    }

    public void view_size_L(CompareWithBody compareWithBody){
        // 기존 화면 삭제
        measurement_result_item.removeAllViews();
        measurement_result_distance.removeAllViews();
        measurement_result_cm.removeAllViews();

        // 버튼 색깔 설정
        comparison_size_L.setTextColor(Color.parseColor("#656aed"));
        comparison_size_L.setBackground(getDrawable(R.drawable.button_background_purple));

        comparison_size_S.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_S.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_M.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_M.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_XL.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_XL.setBackground(getDrawable(R.drawable.button_background));

        // 동적으로 띄워줄 텍스트뷰 인덱스 값 할당
        TextView[]  result_item=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(2).getGoodsScmmValues().size()];
        TextView[]  result_distance=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(2).getGoodsScmmValues().size()];
        TextView[]  result_cm=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(2).getGoodsScmmValues().size()];

        // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
        for (int i = 0; i < compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(2).getGoodsScmmValues().size(); i++) {

            result_item[i] = new TextView(getApplicationContext());
            result_distance[i] = new TextView(getApplicationContext());
            result_cm[i] = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            result_item[i].setLayoutParams(layoutParams);
            result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_item[i].setTextSize(16);
            result_item[i].setText(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(2).getGoodsScmmValues().get(i).getMeasureItemName()+ "\n");
            result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

            String distance="";
            for(int a=0;a<compareWithBody.getBodysResponses().size();a++){
                if(compareWithBody.getBodysResponses().get(a).getMeasureItemId()==compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(2).getGoodsScmmValues().get(i).getMeasureItemId()){
                    distance=Double.toString(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(2).getGoodsScmmValues().get(i).getValue()-compareWithBody.getBodysResponses().get(a).getValue());
                }
            }
            result_distance[i].setLayoutParams(layoutParams);
            result_distance[i].setTextSize(16);
            if(Double.parseDouble(distance)>0) {
                result_distance[i].setText("+"+distance+"\n");
            }else if(Double.parseDouble(distance)==0){
                result_distance[i].setText("="+distance+"\n");
            }else{
                result_distance[i].setText(distance+"\n");
            }
            result_distance[i].setTextAppearance(BOLD);
            result_distance[i].setTextColor(Color.parseColor("#1d1d1d"));

            result_cm[i].setLayoutParams(layoutParams);
            result_cm[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_cm[i].setTextSize(16);
            result_cm[i].setText("cm\n");
            result_cm[i].setTextColor(Color.parseColor("#777777"));

            measurement_result_item.addView(result_item[i]);
            measurement_result_distance.addView(result_distance[i]);
            measurement_result_cm.addView(result_cm[i]);
        }
    }

    public void view_size_XL(CompareWithBody compareWithBody){
        // 기존 화면 삭제
        measurement_result_item.removeAllViews();
        measurement_result_distance.removeAllViews();
        measurement_result_cm.removeAllViews();

        // 버튼 색깔 설정
        comparison_size_XL.setTextColor(Color.parseColor("#656aed"));
        comparison_size_XL.setBackground(getDrawable(R.drawable.button_background_purple));

        comparison_size_S.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_S.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_M.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_M.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_L.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_L.setBackground(getDrawable(R.drawable.button_background));

        // 동적으로 띄워줄 텍스트뷰 인덱스 값 할당
        TextView[]  result_item=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(3).getGoodsScmmValues().size()];
        TextView[]  result_distance=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(3).getGoodsScmmValues().size()];
        TextView[]  result_cm=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(3).getGoodsScmmValues().size()];

        // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
        for (int i = 0; i < compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(3).getGoodsScmmValues().size(); i++) {

            result_item[i] = new TextView(getApplicationContext());
            result_distance[i] = new TextView(getApplicationContext());
            result_cm[i] = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            result_item[i].setLayoutParams(layoutParams);
            result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_item[i].setTextSize(16);
            result_item[i].setText(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(3).getGoodsScmmValues().get(i).getMeasureItemName()+ "\n");
            result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

            String distance="";
            for(int a=0;a<compareWithBody.getBodysResponses().size();a++){
                if(compareWithBody.getBodysResponses().get(a).getMeasureItemId()==compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(3).getGoodsScmmValues().get(i).getMeasureItemId()){
                    distance=Double.toString(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(3).getGoodsScmmValues().get(i).getValue()-compareWithBody.getBodysResponses().get(a).getValue());
                }
            }
            result_distance[i].setLayoutParams(layoutParams);
            result_distance[i].setTextSize(16);
            if(Double.parseDouble(distance)>0) {
                result_distance[i].setText("+"+distance+"\n");
            }else if(Double.parseDouble(distance)==0){
                result_distance[i].setText("="+distance+"\n");
            }else{
                result_distance[i].setText(distance+"\n");
            }
            result_distance[i].setTextAppearance(BOLD);
            result_distance[i].setTextColor(Color.parseColor("#1d1d1d"));

            result_cm[i].setLayoutParams(layoutParams);
            result_cm[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_cm[i].setTextSize(16);
            result_cm[i].setText("cm\n");
            result_cm[i].setTextColor(Color.parseColor("#777777"));

            measurement_result_item.addView(result_item[i]);
            measurement_result_distance.addView(result_distance[i]);
            measurement_result_cm.addView(result_cm[i]);
        }
    }
}
