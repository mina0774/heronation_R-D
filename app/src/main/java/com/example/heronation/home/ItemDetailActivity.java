package com.example.heronation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.heronation.R;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.RecentlyViewedItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailActivity extends AppCompatActivity {
    @BindView(R.id.item_detail_close_button) ImageButton item_detail_close_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);

        String item_id=getIntent().getStringExtra("item_id");
        String item_image=getIntent().getStringExtra("item_image");
        String item_name=getIntent().getStringExtra("item_name");
        String item_price=getIntent().getStringExtra("price");

        // SharedPreferences 생성
        SharedPreferences sharedPreferences=getSharedPreferences("RecentlyViewedItem",MODE_PRIVATE);
        // GSON 생성
        Gson gson=new GsonBuilder().create();

        String item_info="";
        RecentlyViewedItem recentlyViewedItem=new RecentlyViewedItem(item_id,item_image,item_name,item_price);
        item_image=gson.toJson(recentlyViewedItem,RecentlyViewedItem.class);



        // 닫는 버튼을 눌렀을 때
        item_detail_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
