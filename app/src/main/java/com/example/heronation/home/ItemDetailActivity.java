package com.example.heronation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.heronation.R;
import com.example.heronation.home.dataClass.ItemSizeInfo;
import com.example.heronation.home.dataClass.RecentlyViewedItem;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.wishlist.dataClass.ClosetResponse;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity {
    @BindView(R.id.item_detail_close_button) ImageButton item_detail_close_button;
    @BindView(R.id.item_detail_size_button) ImageButton item_detail_size_button;

    private String item_id;
    private String item_image;
    private String item_name;
    private String item_price;

    // popup view를 나타냄
    PopupWindow mPopupWindow;
    View popupViewMeasurement;
    // 측정의 popup view에 해당하는 레이아웃 요소들
    RelativeLayout popup_no_size_info_in_item;
    Button popup_shopping_button;

    ItemSizeInfo itemSizeInfo; // 해당 상품의 사이즈 정보를 담는 변수

    // 해당 아이템 사이즈 정보, 신체 사이즈 정보, 기존에 측정한 옷 사이즈 정보의 여부를 확인하는 변수
    Boolean item_size_info=false;
    Boolean body_size_info=false;
    Boolean measurement_cloth_size_info=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);

        item_id=getIntent().getStringExtra("item_id");
        item_image=getIntent().getStringExtra("item_image");
        item_name=getIntent().getStringExtra("item_name");
        item_price=getIntent().getStringExtra("item_price");

        /* 최근 본 상품 목록을 만들기 위해 해당 아이템의 정보를 SharedPreferences에 저장함 */
        SharedPreferences sharedPreferences=getSharedPreferences("RecentlyViewedItem",MODE_PRIVATE); // SharedPreferences 생성
        Gson gson=new GsonBuilder().create(); // GSON 생성

        String items_info = "";
        String item_info = "";
        RecentlyViewedItem recentlyViewedItem = new RecentlyViewedItem(item_image, item_id, item_name, item_price);
        item_info = gson.toJson(recentlyViewedItem, RecentlyViewedItem.class);
        LinkedHashMap linkedHashMap=new LinkedHashMap();

        if(sharedPreferences.getAll().isEmpty()) { // 최근 본 상품이 비어있을 때
            linkedHashMap.put(item_id,item_info);
            items_info=gson.toJson(linkedHashMap,LinkedHashMap.class);
        } else{ // 최근 본 상품이 있을 때
            linkedHashMap=gson.fromJson(sharedPreferences.getString("items",""),LinkedHashMap.class);
            linkedHashMap.put(item_id,item_info);
            items_info=gson.toJson(linkedHashMap,LinkedHashMap.class);
        }

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("items",items_info);
        editor.commit();

        // 닫는 버튼을 눌렀을 때
        item_detail_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ruler 버튼을 눌렀을 떄
        item_detail_size_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_measurement_panel();
            }
        });
    }

    public void open_measurement_panel(){
        /* 필터 PopUp창 띄우기 */
        popupViewMeasurement = getLayoutInflater().inflate(R.layout.measurement_pop_up, null);
        mPopupWindow = new PopupWindow(popupViewMeasurement);
        mPopupWindow.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //팝업 터치 가능
        mPopupWindow.setTouchable(true);
        //팝업 외부 터치 가능(외부 터치시 나갈 수 있게)
        mPopupWindow.setOutsideTouchable(true);
        //외부터치 인식을 위한 추가 설정 : 미 설정시 외부는 null로 생각하고 터치 인식 X
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        // PopUp 창 띄우기
        mPopupWindow.showAtLocation(popupViewMeasurement, Gravity.BOTTOM, 0, 0);
        // 블러 처리
        View container;
        if (android.os.Build.VERSION.SDK_INT > 22) {
            container = (View) mPopupWindow.getContentView().getParent().getParent();
        }else{
            container = (View) mPopupWindow.getContentView().getParent();
        }
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams)container.getLayoutParams();
        // add flag
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);

        // 레이아웃 요소
        popup_no_size_info_in_item=popupViewMeasurement.findViewById(R.id.popup_no_size_info_in_item);
        popup_shopping_button=popupViewMeasurement.findViewById(R.id.popup_shopping_button);

        getItemSizeInfo();


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
                    popup_no_size_info_in_item.setVisibility(View.GONE);

                    itemSizeInfo = response.body();

                }else if(!response.isSuccessful()){ // 아이템에 대한 사이즈 정보가 없을 때
                    item_size_info=false;
                    popup_no_size_info_in_item.setVisibility(View.VISIBLE);
                    popup_shopping_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopupWindow.dismiss();
                        }
                    });
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
        String authorization="bearer "+MainActivity.access_token;
        String accept="application/json";

        APIInterface.GetClosetListService getClosetListService=ServiceGenerator.createService(APIInterface.GetClosetListService.class);
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
