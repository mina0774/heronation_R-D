package com.example.heronation.home.ItemDetailPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.heronation.R;
import com.example.heronation.home.dataClass.ItemSizeInfo;
import com.example.heronation.home.dataClass.RecentlyViewedItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailActivity extends AppCompatActivity {
    @BindView(R.id.item_detail_close_button) ImageButton item_detail_close_button;
    @BindView(R.id.item_detail_size_button) ImageButton item_detail_size_button;

    private String item_id;
    private String item_image;
    private String item_name;
    private String item_price;
    private String item_subcategory="";

    ItemSizeInfo itemSizeInfo; // 해당 상품의 사이즈 정보를 담는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);

        item_id=getIntent().getStringExtra("item_id");
        item_image=getIntent().getStringExtra("item_image");
        item_name=getIntent().getStringExtra("item_name");
        item_price=getIntent().getStringExtra("item_price");
        if(getIntent().hasExtra("item_subcategory")){
            item_subcategory=getIntent().getStringExtra("item_subcategory");
        }

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

        // ruler 버튼을 눌렀을 때 - ItemMeasurementActivity로 이동
        item_detail_size_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailActivity.this, ItemMeasurementActivity.class);
                intent.putExtra("item_name",item_name);
                intent.putExtra("item_image",item_image);
                intent.putExtra("item_id",item_id);
                intent.putExtra("item_subcategory",item_subcategory);
                startActivity(intent);
            }
        });
    }


}
