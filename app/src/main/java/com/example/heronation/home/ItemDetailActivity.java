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
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.RecentlyViewedItem;
import com.example.heronation.main.MainActivity;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    PopupWindow mPopupWindow;
    View popupViewMeasurement;
    // 측정의 popup view에 해당하는 레이아웃 요소들
    RelativeLayout popup_no_size_info_in_item;
    Button popup_shopping_button;

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

        String item_info="";
        RecentlyViewedItem recentlyViewedItem=new RecentlyViewedItem(item_id,item_image,item_name,item_price);
        item_info=gson.toJson(recentlyViewedItem,RecentlyViewedItem.class);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(item_id,item_info);
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

        get_item_size_info();

    }

    public void get_item_size_info(){
        String authorization="bearer "+ MainActivity.access_token;
        String accept="application/json";

        APIInterface.GetItemSizeInfoService itemSizeInfoService= ServiceGenerator.createService(APIInterface.GetItemSizeInfoService.class);
        retrofit2.Call<ItemSizeInfo> request=itemSizeInfoService.GetItemSizeInfo(Integer.parseInt(item_id),authorization,accept);
        request.enqueue(new Callback<ItemSizeInfo>() {
            @Override
            public void onResponse(Call<ItemSizeInfo> call, Response<ItemSizeInfo> response) {
                if(response.isSuccessful()){
                    popup_no_size_info_in_item.setVisibility(View.GONE);

                }else if(!response.isSuccessful()){
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
}
