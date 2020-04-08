package com.example.heronation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.heronation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailActivity extends AppCompatActivity {
    @BindView(R.id.item_detail_close_button) ImageButton item_detail_close_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences=getSharedPreferences("RecentlyViewedItem",MODE_PRIVATE);

        String item_id=getIntent().getStringExtra("item_id");
        Log.d("아이디",item_id);

        // 닫는 버튼을 눌렀을 때
        item_detail_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
