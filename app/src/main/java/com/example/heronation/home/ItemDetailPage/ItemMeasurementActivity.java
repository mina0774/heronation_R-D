package com.example.heronation.home.ItemDetailPage;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.home.ItemDetailPage.Body.ItemCompareBodySizeActivity;
import com.example.heronation.home.ItemDetailPage.Body.ItemMeasurementBodyActivity;
import com.example.heronation.home.ItemDetailPage.Wardrobe.ItemCompareItemSizeActivity;
import com.example.heronation.home.ItemDetailPage.Wardrobe.ItemSelectItemForComparisonAcitivity;
import com.example.heronation.home.dataClass.GoodsResponses;
import com.example.heronation.home.dataClass.ItemSizeInfo;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class ItemMeasurementActivity extends AppCompatActivity {

    // 해당 아이템 사이즈 정보, 신체 사이즈 정보, 기존에 측정한 옷 사이즈 정보의 여부를 확인하는 변수
    Boolean item_size_info=false;
    Boolean body_size_info=false;
    Boolean measurement_cloth_size_info=false;

    String item_id; // 현재 보고 있는 상품의 아이템 아이디
    String item_name; // 현재 보고 있는 상품의 아이템명
    String item_image; // 현재 보고 있는 상품의 이미지 URL
    String item_subcategory;

    @BindView(R.id.size_info_in_item) LinearLayout size_info_in_item;
    @BindView(R.id.no_size_info_in_item) RelativeLayout no_size_info_in_item;

    @BindView(R.id.body_compare_button) Button body_compare_button;
    @BindView(R.id.item_compare_button) Button item_compare_button;

    @BindView(R.id.measurement_item_name) TextView measurement_item_name;
    @BindView(R.id.measurement_brand_name) TextView measurement_brand_name;
    @BindView(R.id.measurement_item_image) ImageView measurement_item_image;

    @BindView(R.id.size_button_linear_layout) LinearLayout size_button_linear_layout;

    @BindView(R.id.measurement_result_item) LinearLayout measurement_result_item;
    @BindView(R.id.measurement_result_distance) LinearLayout measurement_result_distance;
    @BindView(R.id.measurement_result_cm) LinearLayout measurement_result_cm;

    // 사이즈 버튼 이벤트 처리
    int i;
    Button[] measurement_size_button;

    public static ItemMeasurementActivity itemMeasurementActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_measurement);
        itemMeasurementActivity=this;
        ButterKnife.bind(this);

        item_subcategory=getIntent().getStringExtra("item_subcategory");
        item_id=getIntent().getStringExtra("item_id");
        item_name=getIntent().getStringExtra("item_name");
        measurement_item_name.setText(item_name);
        item_image=getIntent().getStringExtra("item_image");
        measurement_item_image.setBackground(new ShapeDrawable(new OvalShape()));
        measurement_item_image.setClipToOutline(true);
        if(getIntent().hasExtra("brand")){
            measurement_brand_name.setText(getIntent().getStringExtra("brand"));
        }
        Glide.with(getApplicationContext()).load(item_image).error(R.drawable.shop_item_example_img_2).crossFade().into(measurement_item_image);

        getItemSizeInfo();

        // 신체와 비교하기 버튼 눌렀을 때
        body_compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemMeasurementActivity.this, ItemCompareBodySizeActivity.class);
                intent.putExtra("item_id",item_id);
                startActivity(intent);
            }
        });

        // 등록한 옷과 비교하기 버튼 눌렀을 때
        item_compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemMeasurementActivity.this, ItemSelectItemForComparisonAcitivity.class);
                intent.putExtra("item_id",item_id);
                intent.putExtra("item_subcategory",item_subcategory);
                startActivity(intent);
            }
        });
    }

    // 뒤로 가기 버튼 눌렀을 때
    public void click_back_button(View view){
        finish();
    }

    // 쇼핑 계속하기 버튼 눌렀을 때
    public void click_shop_button(View view){
        finish();
    }

    public void getItemSizeInfo(){
        String authorization="bearer "+ MainActivity.access_token;
        String accept="application/json";

        APIInterface.GetItemSizeInfoService itemSizeInfoService= ServiceGenerator.createService(APIInterface.GetItemSizeInfoService.class);
        retrofit2.Call<ItemSizeInfo> request=itemSizeInfoService.GetItemSizeInfo(Integer.parseInt(item_id),authorization,accept);
        request.enqueue(new Callback<ItemSizeInfo>() {
            @Override
            public void onResponse(Call<ItemSizeInfo> call, Response<ItemSizeInfo> response) {
                if(response.isSuccessful()){ // 아이템에 대한 사이즈 정보가 있을 때
                    item_size_info=true;
                    size_info_in_item.setVisibility(View.VISIBLE);
                    no_size_info_in_item.setVisibility(View.GONE);

                    ItemSizeInfo itemSizeInfo=response.body();
                    List<GoodsResponses> goodsResponses=itemSizeInfo.getGoodsResponses();

                    /* 처음 값 S로 설정 */
                    TextView[]  result_item=new TextView[goodsResponses.get(0).getGoodsScmmValues().size()];
                    TextView[]  result_distance=new TextView[goodsResponses.get(0).getGoodsScmmValues().size()];
                    TextView[]  result_cm=new TextView[goodsResponses.get(0).getGoodsScmmValues().size()];

                    // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
                    for (int i = 0; i < goodsResponses.get(0).getGoodsScmmValues().size(); i++) {
                        result_item[i] = new TextView(getApplicationContext());
                        result_distance[i] = new TextView(getApplicationContext());
                        result_cm[i] = new TextView(getApplicationContext());

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER;

                        result_item[i].setLayoutParams(layoutParams);
                        result_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
                        result_item[i].setTextSize(16);
                        result_item[i].setText(goodsResponses.get(0).getGoodsScmmValues().get(i).getMeasureItemName() + "\n");
                        result_item[i].setTextColor(Color.parseColor("#1d1d1d"));

                        result_distance[i].setLayoutParams(layoutParams);
                        result_distance[i].setTextSize(16);
                        result_distance[i].setText(goodsResponses.get(0).getGoodsScmmValues().get(i).getValue().toString()+ "\n");
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

                    measurement_size_button=new Button[goodsResponses.size()];
                    for(i=0;i<goodsResponses.size();i++){
                        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(100, 100);
                        lp.setMarginEnd(30);

                        measurement_size_button[i]=new Button(getApplicationContext());
                        measurement_size_button[i].setLayoutParams(lp);
                        if(i==0){
                            measurement_size_button[i].setTextColor(Color.parseColor("#656aed"));
                            measurement_size_button[i].setBackground(getDrawable(R.drawable.button_background_purple));
                        }else {
                            measurement_size_button[i].setTextColor(Color.parseColor("#dddddd"));
                            measurement_size_button[i].setBackground(getDrawable(R.drawable.button_background));
                        }
                        measurement_size_button[i].setTextSize(11);
                        measurement_size_button[i].setText(goodsResponses.get(i).getSizeOptionName());
                        measurement_size_button[i].setTextAppearance(BOLD);

                        size_button_linear_layout.addView(measurement_size_button[i]);
                    }

                    for(i=0;i<goodsResponses.size();i++) {
                        size_button_onclick(goodsResponses,i,goodsResponses.size());
                    }

                }else if(response.code()==401){ // 로그인 세션이 완료되었을 때
                    backgroundThreadShortToast(getApplicationContext(), "세션이 만료되어 재로그인이 필요합니다."); // 토스트 메시지 ( 메인 쓰레드에서 실행되어야하므로 사용 )
                    Intent intent=new Intent(ItemMeasurementActivity.this, IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{ // 아이템에 대한 사이즈 정보가 없을 때
                    item_size_info=false;
                    size_info_in_item.setVisibility(View.GONE);
                    no_size_info_in_item.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ItemSizeInfo> call, Throwable t) {

            }
        });
    }

    public void size_button_onclick(List<GoodsResponses> goodsResponses,int num,int size){
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

                TextView[]  result_item=new TextView[goodsResponses.get(num).getGoodsScmmValues().size()];
                TextView[]  result_distance=new TextView[goodsResponses.get(num).getGoodsScmmValues().size()];
                TextView[]  result_cm=new TextView[goodsResponses.get(num).getGoodsScmmValues().size()];

                // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
                for (int j = 0; j < goodsResponses.get(num).getGoodsScmmValues().size(); j++) {

                    result_item[j] = new TextView(getApplicationContext());
                    result_distance[j] = new TextView(getApplicationContext());
                    result_cm[j] = new TextView(getApplicationContext());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;

                    result_item[j].setLayoutParams(layoutParams);
                    result_item[j].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    result_item[j].setTextSize(16);
                    result_item[j].setText(goodsResponses.get(num).getGoodsScmmValues().get(j).getMeasureItemName() + "\n");
                    result_item[j].setTextColor(Color.parseColor("#1d1d1d"));

                    result_distance[j].setLayoutParams(layoutParams);
                    result_distance[j].setTextSize(16);
                    result_distance[j].setText(goodsResponses.get(num).getGoodsScmmValues().get(j).getValue().toString()+ "\n");
                    result_distance[j].setTextAppearance(BOLD);
                    result_distance[j].setTextColor(Color.parseColor("#1d1d1d"));

                    result_cm[j].setLayoutParams(layoutParams);
                    result_cm[j].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    result_cm[j].setTextSize(16);
                    result_cm[j].setText("cm\n");
                    result_cm[j].setTextColor(Color.parseColor("#777777"));

                    measurement_result_item.addView(result_item[j]);
                    measurement_result_distance.addView(result_distance[j]);
                    measurement_result_cm.addView(result_cm[j]);
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
