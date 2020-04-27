package com.example.heronation.home.ItemDetailPage.Wardrobe;

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
import com.example.heronation.home.ItemDetailPage.ItemMeasurementActivity;
import com.example.heronation.home.dataClass.CompareWithBody;
import com.example.heronation.home.dataClass.CompareWithWardrobe;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class ItemCompareItemSizeActivity extends AppCompatActivity {
    @BindView(R.id.compare_item_image) ImageView compare_item_image;
    @BindView(R.id.compare_item_name) TextView compare_item_name;

    @BindView(R.id.comparison_size_S) Button comparison_size_S;
    @BindView(R.id.comparison_size_M) Button comparison_size_M;
    @BindView(R.id.comparison_size_L) Button comparison_size_L;
    @BindView(R.id.comparison_size_XL) Button comparison_size_XL;
    @BindView(R.id.recommendation_size_textview) TextView recommendation_size_textview;

    @BindView(R.id.measurement_result_item) LinearLayout measurement_result_item;
    @BindView(R.id.measurement_result_distance) LinearLayout measurement_result_distance;
    @BindView(R.id.measurement_result_cm) LinearLayout measurement_result_cm;

    String itemId;
    String selectWardrobeId;
    String imageURL;
    String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_compare_item_size);
        ButterKnife.bind(this);

        itemId=getIntent().getStringExtra("item_id"); // 보고 있는 상품 아이디
        selectWardrobeId=getIntent().getStringExtra("select_wardrobe_id"); // 비교할 옷장에 있는 상품 아이디
        imageURL=getIntent().getStringExtra("image_url"); // 비교할 옷장에 있는 상품 이미지
        itemName=getIntent().getStringExtra("item_name"); // 비교할 옷장에 있는 상품 이름

        // 비교하려는 아이템 이미지 상단에 표시
        compare_item_name.setText(itemName);
        compare_item_image.setBackground(new ShapeDrawable(new OvalShape()));
        compare_item_image.setClipToOutline(true);
        Glide.with(getApplicationContext()).load(imageURL).error(R.drawable.shop_item_example_img_2).crossFade().into(compare_item_image);

        compare_product_size_with_body();
    }

    public void click_back_button(View view){
        finish();
    }

    public void click_close_button(View view){
        finish();
        ItemSelectItemForComparisonAcitivity.itemSelectItemForComparisonAcitivity.finish();
    }


    public void compare_product_size_with_body(){
        String authorization = "Bearer "+ MainActivity.access_token;
        String accept="application/json";

        APIInterface.CompareProductSizeWithWardrobeService compareProductSizeWithWardrobeService = ServiceGenerator.createService(APIInterface.CompareProductSizeWithWardrobeService.class);
        retrofit2.Call<CompareWithWardrobe> request = compareProductSizeWithWardrobeService.CompareProductSizeWithWardrobe(Integer.parseInt(itemId),Integer.parseInt(selectWardrobeId),authorization,accept);
        request.enqueue(new Callback<CompareWithWardrobe>() {
            @Override
            public void onResponse(Call<CompareWithWardrobe> call, Response<CompareWithWardrobe> response) {
                if(response.isSuccessful()) {
                    CompareWithWardrobe compareWithWardrobe = response.body();

                    // 사이즈 추천 값에 따라 첫화면 생성 - 일치 확률 최대값 찾기
                    Double max = compareWithWardrobe.getGoodsResponses().get(0).getCompareResultDerive();
                    Integer max_num = 0;
                    for (int i = 0; i < compareWithWardrobe.getGoodsResponses().size(); i++) {
                        if (max < compareWithWardrobe.getGoodsResponses().get(i).getCompareResultDerive()) {
                            max_num = i;
                        }
                    }
                    // max_num에 따라 처음 화면에 뿌려지는 수치값이 다름
                    switch (max_num) {
                        case 0:
                            view_size_S(compareWithWardrobe);
                            recommendation_size_textview.setText("S size ");
                            break;
                        case 1:
                            view_size_M(compareWithWardrobe);
                            recommendation_size_textview.setText("M size ");
                            break;
                        case 2:
                            view_size_L(compareWithWardrobe);
                            recommendation_size_textview.setText("L size ");
                            break;
                        case 3:
                            view_size_XL(compareWithWardrobe);
                            recommendation_size_textview.setText("XL size ");
                            break;
                    }

                    if (compareWithWardrobe.getGoodsResponses().size() >= 1) { // S 사이즈 버튼 활성화
                        comparison_size_S.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view_size_S(compareWithWardrobe);
                            }
                        });
                    }

                    if (compareWithWardrobe.getGoodsResponses().size() >= 2) { // M 사이즈 버튼 활성화
                        comparison_size_M.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view_size_M(compareWithWardrobe);
                            }
                        });
                    }

                    if (compareWithWardrobe.getGoodsResponses().size() >= 3) { // L 사이즈 버튼 활성화
                        comparison_size_L.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view_size_L(compareWithWardrobe);
                            }
                        });
                    }

                    if (compareWithWardrobe.getGoodsResponses().size() >= 4) { // XL 사이즈 버튼 활성화
                        comparison_size_XL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view_size_XL(compareWithWardrobe);
                            }
                        });
                    }
                }else if(response.code()==401) {
                    backgroundThreadShortToast(getApplicationContext(), "세션이 만료되어 재로그인이 필요합니다."); // 토스트 메시지 ( 메인 쓰레드에서 실행되어야하므로 사용 )
                    Intent intent=new Intent(ItemCompareItemSizeActivity.this, IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<CompareWithWardrobe> call, Throwable t) {

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


    public void view_size_S(CompareWithWardrobe compareWithWardrobe){
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
        TextView[]  result_item=new TextView[compareWithWardrobe.getGoodsResponses().get(0).getGoodsScmmValues().size()];
        TextView[]  result_distance=new TextView[compareWithWardrobe.getGoodsResponses().get(0).getGoodsScmmValues().size()];
        TextView[]  result_cm=new TextView[compareWithWardrobe.getGoodsResponses().get(0).getGoodsScmmValues().size()];

        // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
        for (int i = 0; i < compareWithWardrobe.getGoodsResponses().get(0).getGoodsScmmValues().size(); i++) {

            result_item[i] = new TextView(getApplicationContext());
            result_distance[i] = new TextView(getApplicationContext());
            result_cm[i] = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            result_item[i].setLayoutParams(layoutParams);
            result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_item[i].setTextSize(16);
            result_item[i].setText(compareWithWardrobe.getGoodsResponses().get(0).getGoodsScmmValues().get(i).getMeasureItemName()+ "\n");
            result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

            String distance="";
            for(int a=0;a<compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().size();a++){
                if(compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().get(a).getMeasureItemId()==compareWithWardrobe.getGoodsResponses().get(0).getGoodsScmmValues().get(i).getMeasureItemId()){
                    distance=Double.toString(compareWithWardrobe.getGoodsResponses().get(0).getGoodsScmmValues().get(i).getValue()-compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().get(a).getValue());
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

    public void view_size_M(CompareWithWardrobe compareWithWardrobe){
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
        TextView[]  result_item=new TextView[compareWithWardrobe.getGoodsResponses().get(1).getGoodsScmmValues().size()];
        TextView[]  result_distance=new TextView[compareWithWardrobe.getGoodsResponses().get(1).getGoodsScmmValues().size()];
        TextView[]  result_cm=new TextView[compareWithWardrobe.getGoodsResponses().get(1).getGoodsScmmValues().size()];

        // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
        for (int i = 0; i < compareWithWardrobe.getGoodsResponses().get(1).getGoodsScmmValues().size(); i++) {

            result_item[i] = new TextView(getApplicationContext());
            result_distance[i] = new TextView(getApplicationContext());
            result_cm[i] = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            result_item[i].setLayoutParams(layoutParams);
            result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_item[i].setTextSize(16);
            result_item[i].setText(compareWithWardrobe.getGoodsResponses().get(1).getGoodsScmmValues().get(i).getMeasureItemName() + "\n");
            result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

            String distance = "";
            for (int a = 0; a < compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().size(); a++) {
                if (compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().get(a).getMeasureItemId() == compareWithWardrobe.getGoodsResponses().get(1).getGoodsScmmValues().get(i).getMeasureItemId()) {
                    distance = Double.toString(compareWithWardrobe.getGoodsResponses().get(1).getGoodsScmmValues().get(i).getValue() - compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().get(a).getValue());
                }
            }
            result_distance[i].setLayoutParams(layoutParams);
            result_distance[i].setTextSize(16);
            if (Double.parseDouble(distance) > 0) {
                result_distance[i].setText("+" + distance + "\n");
            } else if (Double.parseDouble(distance) == 0) {
                result_distance[i].setText("="+distance+"\n");
            } else {
                result_distance[i].setText(distance + "\n");
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

    public void view_size_L(CompareWithWardrobe compareWithWardrobe){
        // 기존 화면 삭제
        measurement_result_item.removeAllViews();
        measurement_result_distance.removeAllViews();
        measurement_result_cm.removeAllViews();

        // 버튼 색깔 설정
        comparison_size_L.setTextColor(Color.parseColor("#656aed"));
        comparison_size_L.setBackground(getDrawable(R.drawable.button_background_purple));

        comparison_size_M.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_M.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_S.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_S.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_XL.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_XL.setBackground(getDrawable(R.drawable.button_background));

        // 동적으로 띄워줄 텍스트뷰 인덱스 값 할당
        TextView[]  result_item=new TextView[compareWithWardrobe.getGoodsResponses().get(2).getGoodsScmmValues().size()];
        TextView[]  result_distance=new TextView[compareWithWardrobe.getGoodsResponses().get(2).getGoodsScmmValues().size()];
        TextView[]  result_cm=new TextView[compareWithWardrobe.getGoodsResponses().get(2).getGoodsScmmValues().size()];

        // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
        for (int i = 0; i < compareWithWardrobe.getGoodsResponses().get(2).getGoodsScmmValues().size(); i++) {

            result_item[i] = new TextView(getApplicationContext());
            result_distance[i] = new TextView(getApplicationContext());
            result_cm[i] = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            result_item[i].setLayoutParams(layoutParams);
            result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_item[i].setTextSize(16);
            result_item[i].setText(compareWithWardrobe.getGoodsResponses().get(2).getGoodsScmmValues().get(i).getMeasureItemName() + "\n");
            result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

            String distance = "";
            for (int a = 0; a < compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().size(); a++) {
                if (compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().get(a).getMeasureItemId() == compareWithWardrobe.getGoodsResponses().get(2).getGoodsScmmValues().get(i).getMeasureItemId()) {
                    distance = Double.toString(compareWithWardrobe.getGoodsResponses().get(2).getGoodsScmmValues().get(i).getValue() - compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().get(a).getValue());
                }
            }
            result_distance[i].setLayoutParams(layoutParams);
            result_distance[i].setTextSize(16);
            if (Double.parseDouble(distance) > 0) {
                result_distance[i].setText("+" + distance + "\n");
            } else if (Double.parseDouble(distance) == 0) {
                result_distance[i].setText("=" + distance+"\n");
            } else {
                result_distance[i].setText(distance + "\n");
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

    public void view_size_XL(CompareWithWardrobe compareWithWardrobe){
        // 기존 화면 삭제
        measurement_result_item.removeAllViews();
        measurement_result_distance.removeAllViews();
        measurement_result_cm.removeAllViews();

        // 버튼 색깔 설정
        comparison_size_XL.setTextColor(Color.parseColor("#656aed"));
        comparison_size_XL.setBackground(getDrawable(R.drawable.button_background_purple));

        comparison_size_M.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_M.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_L.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_L.setBackground(getDrawable(R.drawable.button_background));
        comparison_size_S.setTextColor(Color.parseColor("#dddddd"));
        comparison_size_S.setBackground(getDrawable(R.drawable.button_background));

        // 동적으로 띄워줄 텍스트뷰 인덱스 값 할당
        TextView[]  result_item=new TextView[compareWithWardrobe.getGoodsResponses().get(3).getGoodsScmmValues().size()];
        TextView[]  result_distance=new TextView[compareWithWardrobe.getGoodsResponses().get(3).getGoodsScmmValues().size()];
        TextView[]  result_cm=new TextView[compareWithWardrobe.getGoodsResponses().get(3).getGoodsScmmValues().size()];

        // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
        for (int i = 0; i < compareWithWardrobe.getGoodsResponses().get(3).getGoodsScmmValues().size(); i++) {

            result_item[i] = new TextView(getApplicationContext());
            result_distance[i] = new TextView(getApplicationContext());
            result_cm[i] = new TextView(getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            result_item[i].setLayoutParams(layoutParams);
            result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_item[i].setTextSize(16);
            result_item[i].setText(compareWithWardrobe.getGoodsResponses().get(3).getGoodsScmmValues().get(i).getMeasureItemName() + "\n");
            result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

            String distance = "";
            for (int a = 0; a < compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().size(); a++) {
                if (compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().get(a).getMeasureItemId() == compareWithWardrobe.getGoodsResponses().get(3).getGoodsScmmValues().get(i).getMeasureItemId()) {
                    distance = Double.toString(compareWithWardrobe.getGoodsResponses().get(3).getGoodsScmmValues().get(i).getValue() - compareWithWardrobe.getWardrobeResponse().getWardrobeScmmValueResponses().get(a).getValue());
                }
            }
            result_distance[i].setLayoutParams(layoutParams);
            result_distance[i].setTextSize(16);
            if (Double.parseDouble(distance) > 0) {
                result_distance[i].setText("+" + distance + "\n");
            } else if (Double.parseDouble(distance) == 0) {
                result_distance[i].setText("="+distance+"\n");
            } else {
                result_distance[i].setText(distance + "\n");
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
