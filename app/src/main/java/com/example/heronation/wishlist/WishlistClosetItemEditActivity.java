package com.example.heronation.wishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.heronation.R;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WishlistClosetItemEditActivity extends AppCompatActivity {
    @BindView(R.id.wishlist_closet_item_category) TextView category;
    @BindView(R.id.wishlist_closet_item_name) TextView item_name;
    @BindView(R.id.wishlist_closet_item_date) TextView date;
    @BindView(R.id.wishlist_closet_item_shop_name) TextView shop_name;
    @BindView(R.id.wishlist_closet_item_measurement_type) TextView measurement_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_closet_item_edit);
        ButterKnife.bind(this);

        //옷장에서 선택한 리사이클러뷰의 아이템 정보를 받아오기 위한 인텐트
        Intent intent=getIntent();

        category.setText(intent.getStringExtra("category"));
        item_name.setText(intent.getStringExtra("item_name"));
        date.setText(intent.getStringExtra("date"));
        shop_name.setText(intent.getStringExtra("shop_name"));
        measurement_type.setText(intent.getStringExtra("measurement_type"));
    }

    //back 버튼 눌렀을 때, 액티비티 종료
    public void click_back_button(View view){
        finish();
    }
}
