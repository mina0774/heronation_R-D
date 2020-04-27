package com.example.heronation.wishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.mypage.UserModifyActivity;
import com.example.heronation.wishlist.dataClass.ClosetDetailResponse;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class WishlistClosetItemDetailActivity extends AppCompatActivity {
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
    @BindView(R.id.wishlist_closet_edit_button) Button wishlist_closet_edit_button;
    // 측정 상세정보를 나타내기 위함
    TextView[] result_measure_item;
    EditText[] result_distance;
    TextView[] result_cm;
    // 측정한 옷의 카테고리를 받아옴
    Integer result_category_id;
    // 측정 목록 각각의 아이디를 저장하는 변수
    Integer[] result_measurement_items_id;
    // 측정 목록 각각의 거리를 저장하는 변수
    Integer[] result_measurement_items_distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_closet_item_detail);
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

        // 수정 버튼 눌렀을 때
        wishlist_closet_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", getIntent().getStringExtra("id"));
                    jsonObject.put("name", getIntent().getStringExtra("item_name"));
                    jsonObject.put("subCategoryId", result_category_id);

                    JSONObject measurementObject;
                    JSONArray wardrobe = new JSONArray();
                    for (int i = 0; i < result_measurement_items_id.length; i++) {
                        measurementObject = new JSONObject();
                        measurementObject.put("measureItemId", result_measurement_items_id[i]);
                        measurementObject.put("value",result_distance[i].getText().toString());
                        wardrobe.put(measurementObject);
                    }
                    jsonObject.put("wardrobeScmmValueRequests", wardrobe);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                    String authorization="bearer "+MainActivity.access_token;
                    String accept="application/json";
                    String content_type="application/json";
                    APIInterface.EditClosetItemService editClosetItemService=ServiceGenerator.createService(APIInterface.EditClosetItemService.class);
                    retrofit2.Call<JSONObject> request=editClosetItemService.EditClosetItem(authorization,accept,content_type,requestBody);

                    request.enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            if(response.code()==400){
                                backgroundThreadShortToast(WishlistClosetItemDetailActivity.this,"범위에 벗어났습니다.");
                            }else if (response.code()==200){
                                backgroundThreadShortToast(WishlistClosetItemDetailActivity.this, "수정이 완료되었습니다.");
                            }else if(response.code()==401){
                                backgroundThreadShortToast(getApplicationContext(), "세션이 만료되어 재로그인이 필요합니다.");
                                Intent intent=new Intent(getApplicationContext(), IntroActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                        }
                    });
                }catch (JSONException e){ }
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
            }); }
    }

    // 옷장의 특정 아이템의 구체적인 정보를 받아오는 작업
    public void getClosetDetailInfo() {
        String authorization = "bearer " + MainActivity.access_token;
        String accept = "application/json";
        APIInterface.GetClosetDetailInfoService getClosetDetailInfoService = ServiceGenerator.createService(APIInterface.GetClosetDetailInfoService.class);
        retrofit2.Call<ClosetDetailResponse> request = getClosetDetailInfoService.GetClosetDetailInfo(Integer.parseInt(measurement_id.getText().toString()), authorization, accept);
        request.enqueue(new Callback<ClosetDetailResponse>() {
            @Override
            public void onResponse(Call<ClosetDetailResponse> call, Response<ClosetDetailResponse> response) {
                ClosetDetailResponse closetDetailResponse = response.body();
                result_category_id = Integer.parseInt(closetDetailResponse.getSubCategoryId());
                result_measurement_items_id = new Integer[closetDetailResponse.getWardrobeScmmValueResponses().size()];
                result_measurement_items_distance = new Integer[closetDetailResponse.getWardrobeScmmValueResponses().size()];
                result_measure_item = new TextView[closetDetailResponse.getWardrobeScmmValueResponses().size()];
                result_distance = new EditText[closetDetailResponse.getWardrobeScmmValueResponses().size()];
                result_cm = new TextView[closetDetailResponse.getWardrobeScmmValueResponses().size()];

                // 받아온 결과값을 화면에 예쁘게 뿌려주는 작업
                for (int i = 0; i < closetDetailResponse.getWardrobeScmmValueResponses().size(); i++) {

                    result_measure_item[i] = new TextView(getApplicationContext());
                    result_distance[i] = new EditText(getApplicationContext());
                    result_cm[i] = new TextView(getApplicationContext());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;

                    result_measure_item[i].setLayoutParams(layoutParams);
                    result_measure_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    result_measure_item[i].setTextSize(16);
                    result_measure_item[i].setText(closetDetailResponse.getWardrobeScmmValueResponses().get(i).getMeasureItemName() + "\n");
                    result_measure_item[i].setTextColor(Color.parseColor("#1d1d1d"));

                    result_distance[i].setLayoutParams(layoutParams);
                    result_distance[i].setTextSize(16);
                    String distanceString=String.format(Locale.getDefault(),"%d",Math.round(Double.parseDouble(closetDetailResponse.getWardrobeScmmValueResponses().get(i).getValue())));
                    result_distance[i].setText(distanceString);
                    result_distance[i].setTextAppearance(BOLD);
                    result_distance[i].setTextColor(Color.parseColor("#3464ff"));

                    result_cm[i].setLayoutParams(layoutParams);
                    result_cm[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    result_cm[i].setTextSize(16);
                    result_cm[i].setText("cm\n");
                    result_cm[i].setTextColor(Color.parseColor("#777777"));

                    result_measurement_items_id[i] = Integer.parseInt(closetDetailResponse.getWardrobeScmmValueResponses().get(i).getMeasureItemId());

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

    // back 버튼 눌렀을 때, 액티비티 종료
    public void click_back_button(View view){
        finish();
    }


}
