package com.example.heronation.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.heronation.R;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.WishlistClosetDeleteAdapter;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.ClosetItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WishlistClosetDeleteActivity extends AppCompatActivity {

    /* 리사이클러뷰*/
    @BindView(R.id.recycler_view_delete_closet_item) RecyclerView closet_recyclerView;
    private ArrayList<ClosetItem> item_list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_closet_delete);
        ButterKnife.bind(this);
        item_list=(ArrayList<ClosetItem>)getIntent().getSerializableExtra("closet item");
        /* 리사이클러뷰 객체 생성 */
        WishlistClosetDeleteAdapter wishlistClosetAdapter=new WishlistClosetDeleteAdapter(this,item_list);
        /* 레이아웃 매니저 수평으로 지정 */
        closet_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        /* 리사이클러뷰에 어댑터 지정 */
        closet_recyclerView.setAdapter(wishlistClosetAdapter);
    }


    //뒤로가기 버튼을 눌렀을 때
    public void click_back_button(View view){
        finish();
    }

    //삭제 버튼을 눌렀을 때
    public void delete_closet_item_button(View view){

    }
}
