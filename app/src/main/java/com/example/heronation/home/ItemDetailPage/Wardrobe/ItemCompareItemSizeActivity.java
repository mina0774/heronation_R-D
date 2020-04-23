package com.example.heronation.home.ItemDetailPage.Wardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heronation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemCompareItemSizeActivity extends AppCompatActivity {
    @BindView(R.id.compare_item_image) ImageView compare_item_image;
    @BindView(R.id.compare_item_name) TextView compare_item_name;

    @BindView(R.id.comparison_size_S) Button comparison_size_S;
    @BindView(R.id.comparison_size_M) Button comparison_size_M;
    @BindView(R.id.comparison_size_L) Button comparison_size_L;
    @BindView(R.id.comparison_size_XL) Button comparison_size_XL;
    @BindView(R.id.recommendation_size_textview) TextView recommendation_size_textview;

    @BindView(R.id.measurement_result_item) LinearLayout measurement_result_item;
    @BindView(R.id.measurement_result_distance) LinearLayout measurement_result_distance;
    @BindView(R.id.measurement_result_cm) LinearLayout measurement_result_cm;

    String selectItemId;
    String imageURL;
    String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_compare_item_size);
        ButterKnife.bind(this);

        selectItemId=getIntent().getStringExtra("select_item_id");
        imageURL=getIntent().getStringExtra("image_url");
        itemName=getIntent().getStringExtra("item_name");

        // 비교하려는 아이템 이미지 상단에 표시
        compare_item_name.setText(itemName);
        compare_item_image.setBackground(new ShapeDrawable(new OvalShape()));
        compare_item_image.setClipToOutline(true);
        Glide.with(getApplicationContext()).load(imageURL).error(R.drawable.shop_item_example_img_2).crossFade().into(compare_item_image);
    }

    public void click_back_button(View view){
        finish();
    }

    public void click_close_button(View view){
        finish();
        ItemSelectItemForComparisonAcitivity.itemSelectItemForComparisonAcitivity.finish();
    }
}
