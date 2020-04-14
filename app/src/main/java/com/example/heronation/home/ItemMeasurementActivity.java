package com.example.heronation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.heronation.R;
import com.example.heronation.home.dataClass.ItemSizeInfo;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.wishlist.dataClass.ClosetResponse;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemMeasurementActivity extends AppCompatActivity {

    // 해당 아이템 사이즈 정보, 신체 사이즈 정보, 기존에 측정한 옷 사이즈 정보의 여부를 확인하는 변수
    Boolean item_size_info=false;
    Boolean body_size_info=false;
    Boolean measurement_cloth_size_info=false;

    String item_id; // 현재 보고 있는 상품의 아이템명

    @BindView(R.id.no_size_info_in_item) RelativeLayout no_size_info_in_item;
    @BindView(R.id.body_compare_button) Button body_compare_button;
    @BindView(R.id.item_compare_button) Button item_compare_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_measurement);
        ButterKnife.bind(this);

        item_id=getIntent().getStringExtra("item_id");

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
                }else if(!response.isSuccessful()){ // 아이템에 대한 사이즈 정보가 없을 때
                    item_size_info=false;
                }
            }

            @Override
            public void onFailure(Call<ItemSizeInfo> call, Throwable t) {

            }
        });
    }

    public void getBodyInfo(){
        String authorization="";
        String accept="application/json";

        if(!MainActivity.access_token.matches("null")) { //회원 사용자일 때
            authorization="bearer " +MainActivity.access_token;
            APIInterface.UserInfoService userInfoService= ServiceGenerator.createService(APIInterface.UserInfoService.class);
            retrofit2.Call<UserMyInfo> request=userInfoService.UserInfo(authorization,accept);
            request.enqueue(new Callback<UserMyInfo>() {
                @Override
                public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                    if(response.isSuccessful()) { //정상적으로 로그인이 되었을 때
                        UserMyInfo userMyInfo=response.body();
                        if(userMyInfo.getBodyResponses().size()!=0){ // 신체 정보가 있을 때
                            body_size_info=true;
                        }else{ // 신체 정보가 없을 때
                            body_size_info=false;
                        }
                    }

                }
                @Override
                public void onFailure(Call<UserMyInfo> call, Throwable t) {
                }
            });
        }
    }

    public void getMeasurementClothInfo(){
        String authorization="bearer "+ MainActivity.access_token;
        String accept="application/json";

        APIInterface.GetClosetListService getClosetListService= ServiceGenerator.createService(APIInterface.GetClosetListService.class);
        retrofit2.Call<ClosetResponse> request= getClosetListService.GetClosetList(1,200,"id,desc",authorization,accept);
        request.enqueue(new Callback<ClosetResponse>() {
            @Override
            public void onResponse(Call<ClosetResponse> call, Response<ClosetResponse> response) {
                if(response.isSuccessful()){
                    ClosetResponse closetResponse=response.body();
                    if(closetResponse.getSize()!=0){ // 기존에 측정한 옷의 정보가 있을 때
                        measurement_cloth_size_info=true;

                    }else{ // 기존에 측정한 옷의 정보가 없을 때
                        measurement_cloth_size_info=false;
                    }
                }
            }

            @Override
            public void onFailure(Call<ClosetResponse> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }
}
