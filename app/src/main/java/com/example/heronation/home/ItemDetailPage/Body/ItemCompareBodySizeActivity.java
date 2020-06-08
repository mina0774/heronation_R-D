package com.example.heronation.home.ItemDetailPage.Body;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.home.ItemDetailPage.Body.ItemMeasurementBodyActivity;
import com.example.heronation.home.ItemDetailPage.Body.ItemMeasurementBodySizeInfoActivity;
import com.example.heronation.home.ItemDetailPage.ItemMeasurementActivity;
import com.example.heronation.home.ItemDetailPage.Wardrobe.ItemSelectItemForComparisonAcitivity;
import com.example.heronation.home.dataClass.CompareWithBody;
import com.example.heronation.home.dataClass.GoodsResponses;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class ItemCompareBodySizeActivity extends AppCompatActivity {
    @BindView(R.id.measurement_item_name) TextView measurement_item_name;
    @BindView(R.id.measurement_item_image) ImageView measurement_item_image;

    @BindView(R.id.recommendation_size_textview) TextView recommendation_size_textview;
    @BindView(R.id.size_button_linear_layout) LinearLayout size_button_linear_layout;
    @BindView(R.id.measurement_result_item) LinearLayout measurement_result_item;
    @BindView(R.id.measurement_result_distance) LinearLayout measurement_result_distance;
    @BindView(R.id.measurement_result_cm) LinearLayout measurement_result_cm;

    String item_id;

    // 사이즈 버튼 이벤트 처리
    int i;
    Button[] measurement_size_button;

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

    public void click_close_button(View view){
        finish();
        ItemMeasurementActivity.itemMeasurementActivity.finish();
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
                if(response.isSuccessful()) {
                    CompareWithBody compareWithBody = response.body();

                    // 현재 보고있는 상품 정보 설정
                    measurement_item_name.setText(compareWithBody.getItemResponse().getName());
                    measurement_item_image.setBackground(new ShapeDrawable(new OvalShape()));
                    measurement_item_image.setClipToOutline(true);
                    Glide.with(getApplicationContext()).load(compareWithBody.getItemResponse().getShopImage()).error(R.drawable.shop_item_example_img_2).crossFade().into(measurement_item_image);

                    // 사이즈 추천 값에 따라 첫화면 생성 - 일치 확률 최대값 찾기
                    Double max = compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(0).getCompareResultDerive();
                    Integer max_num = 0;
                    for (int i = 0; i < compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size(); i++) {
                        if (max < compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(i).getCompareResultDerive()) {
                            max=compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(i).getCompareResultDerive();
                            max_num = i;
                        }
                    }

                    measurement_size_button=new Button[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size()];
                    for(i=0;i<compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size();i++){
                        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(100, 100);
                        lp.setMarginEnd(30);

                        measurement_size_button[i]=new Button(getApplicationContext());
                        measurement_size_button[i].setLayoutParams(lp);

                            measurement_size_button[i].setTextColor(Color.parseColor("#dddddd"));
                            measurement_size_button[i].setBackground(getDrawable(R.drawable.button_background));

                        measurement_size_button[i].setTextSize(11);
                        measurement_size_button[i].setText(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(i).getSizeOptionName());
                        measurement_size_button[i].setTextAppearance(BOLD);

                        size_button_linear_layout.addView(measurement_size_button[i]);
                    }

                    for(i=0;i<compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size();i++) {
                        size_button_onclick(compareWithBody,i,compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().size());
                    }

                    // max_num에 따라 처음 화면에 뿌려지는 수치값이 다름
                    recommendation_size_textview.setText(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(max_num).getSizeOptionName()+" size ");
                    recommend_size(compareWithBody,max_num);

                }else if(response.code()==401){
                    backgroundThreadShortToast(getApplicationContext(), "세션이 만료되어 재로그인이 필요합니다."); // 토스트 메시지 ( 메인 쓰레드에서 실행되어야하므로 사용 )
                    Intent intent=new Intent(ItemCompareBodySizeActivity.this, IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<CompareWithBody> call, Throwable t) {

            }
        });
    }

    public void recommend_size(CompareWithBody compareWithBody,int max_num){
        measurement_size_button[max_num].setTextColor(Color.parseColor("#656aed"));
        measurement_size_button[max_num].setBackground(getDrawable(R.drawable.button_background_purple));

        measurement_result_item.removeAllViews();
        measurement_result_distance.removeAllViews();
        measurement_result_cm.removeAllViews();

        // 동적으로 띄워줄 텍스트뷰 인덱스 값 할당
        TextView[]  result_item=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(max_num).getGoodsScmmValues().size()];
        TextView[]  result_distance=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(max_num).getGoodsScmmValues().size()];
        TextView[]  result_cm=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(max_num).getGoodsScmmValues().size()];

        // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
        for (int i = 0; i < compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(max_num).getGoodsScmmValues().size(); i++) {

            result_item[i] = new TextView(getApplicationContext());
            result_distance[i] = new TextView(getApplicationContext());
            result_cm[i] = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            result_item[i].setLayoutParams(layoutParams);
            result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_item[i].setTextSize(16);
            result_item[i].setText(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(max_num).getGoodsScmmValues().get(i).getMeasureItemName()+ "\n");
            result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

            String distance="";
            DecimalFormat df=new DecimalFormat("######0.00");
            for(int a=0;a<compareWithBody.getBodysResponses().size();a++){
                if(compareWithBody.getBodysResponses().get(a).getMeasureItemId()==compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(max_num).getGoodsScmmValues().get(i).getMeasureItemId()){
                    distance=df.format(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(max_num).getGoodsScmmValues().get(i).getValue()-compareWithBody.getBodysResponses().get(a).getValue());
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

    public void size_button_onclick(CompareWithBody compareWithBody, int num, int size){
        measurement_size_button[num].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(i=0;i<size;i++) {
                    measurement_size_button[i].setTextColor(Color.parseColor("#dddddd"));
                    measurement_size_button[i].setBackground(getDrawable(R.drawable.button_background));
                }
                measurement_size_button[num].setTextColor(Color.parseColor("#656aed"));
                measurement_size_button[num].setBackground(getDrawable(R.drawable.button_background_purple));

                measurement_result_item.removeAllViews();
                measurement_result_distance.removeAllViews();
                measurement_result_cm.removeAllViews();

                // 동적으로 띄워줄 텍스트뷰 인덱스 값 할당
                TextView[]  result_item=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(num).getGoodsScmmValues().size()];
                TextView[]  result_distance=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(num).getGoodsScmmValues().size()];
                TextView[]  result_cm=new TextView[compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(num).getGoodsScmmValues().size()];

                // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
                for (int i = 0; i < compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(num).getGoodsScmmValues().size(); i++) {

                    result_item[i] = new TextView(getApplicationContext());
                    result_distance[i] = new TextView(getApplicationContext());
                    result_cm[i] = new TextView(getApplicationContext());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;

                    result_item[i].setLayoutParams(layoutParams);
                    result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    result_item[i].setTextSize(16);
                    result_item[i].setText(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(num).getGoodsScmmValues().get(i).getMeasureItemName()+ "\n");
                    result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

                    String distance="";
                    DecimalFormat df=new DecimalFormat("######0.00");
                    for(int a=0;a<compareWithBody.getBodysResponses().size();a++){
                        if(compareWithBody.getBodysResponses().get(a).getMeasureItemId()==compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(num).getGoodsScmmValues().get(i).getMeasureItemId()){
                            distance=df.format(compareWithBody.getGoodsAndMeasureItemResponses().getGoodsResponses().get(num).getGoodsScmmValues().get(i).getValue()-compareWithBody.getBodysResponses().get(a).getValue());
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
        });
    }

    //Toast는 비동기 태스크 내에서 처리할 수 없으므로, 메인 쓰레드 핸들러를 생성하여 toast가 메인쓰레드에서 생성될 수 있도록 처리해준다.
    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
