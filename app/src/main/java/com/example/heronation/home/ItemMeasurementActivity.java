package com.example.heronation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.home.dataClass.ItemSizeInfo;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.wishlist.dataClass.ClosetResponse;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemMeasurementActivity extends AppCompatActivity {

    // 해당 아이템 사이즈 정보, 신체 사이즈 정보, 기존에 측정한 옷 사이즈 정보의 여부를 확인하는 변수
    Boolean item_size_info=false;
    Boolean body_size_info=false;
    Boolean measurement_cloth_size_info=false;

    String item_id; // 현재 보고 있는 상품의 아이템 아이디
    String item_name; // 현재 보고 있는 상품의 아이템명
    String item_image; // 현재 보고 있는 상품의 이미지 URL

    @BindView(R.id.size_info_in_item) LinearLayout size_info_in_item;
    @BindView(R.id.no_size_info_in_item) RelativeLayout no_size_info_in_item;
    @BindView(R.id.body_compare_button) Button body_compare_button;
    @BindView(R.id.item_compare_button) Button item_compare_button;
    @BindView(R.id.measurement_item_name) TextView measurement_item_name;
    @BindView(R.id.measurement_item_image) ImageView measurement_item_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_measurement);
        ButterKnife.bind(this);

        item_id=getIntent().getStringExtra("item_id");
        item_name=getIntent().getStringExtra("item_name");
        measurement_item_name.setText(item_name);
        item_image=getIntent().getStringExtra("item_image");
        measurement_item_image.setBackground(new ShapeDrawable(new OvalShape()));
        measurement_item_image.setClipToOutline(true);
        Glide.with(getApplicationContext()).load(item_image).error(R.drawable.shop_item_example_img_2).crossFade().into(measurement_item_image);
        getItemSizeInfo();

        // 신체와 비교하기 버튼 눌렀을 때
        body_compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemMeasurementActivity.this,ItemCompareBodySizeActivity.class);
                startActivity(intent);
            }
        });

        // 등록한 옷과 비교하기 버튼 눌렀을 때
        item_compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemMeasurementActivity.this,ItemCompareItemSizeActivity.class);
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

                }else if(!response.isSuccessful()){ // 아이템에 대한 사이즈 정보가 없을 때
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

}
