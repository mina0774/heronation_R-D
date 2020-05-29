package com.example.heronation.home.ItemDetailPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.heronation.R;
import com.example.heronation.home.dataClass.ItemSizeInfo;
import com.example.heronation.home.dataClass.RecentlyViewedItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailActivity extends AppCompatActivity {
    @BindView(R.id.item_detail_close_button) ImageButton item_detail_close_button;
    @BindView(R.id.item_detail_size_button) ImageButton item_detail_size_button;
    @BindView(R.id.item_detail_share_button) ImageButton item_detail_share_button;
    @BindView(R.id.webview) WebView webView;

    private String item_id="";
    private String item_image="";
    private String item_name="";
    private String item_price="-";
    private String item_subcategory="";
    private String item_url="";
    private String brand="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);
        if (getIntent().hasExtra("item_id")) {
            item_id = getIntent().getStringExtra("item_id");
        }
        if (getIntent().hasExtra("item_image")) {
            item_image = getIntent().getStringExtra("item_image");
        }
        if (getIntent().hasExtra("item_name")) {
            item_name = getIntent().getStringExtra("item_name");
        }
        if (getIntent().hasExtra("item_price")) {
            item_price = getIntent().getStringExtra("item_price");
        }
        if (getIntent().hasExtra("item_subcategory")) {
            item_subcategory = getIntent().getStringExtra("item_subcategory");
        }
        if(getIntent().hasExtra("brand")){
            brand=getIntent().getStringExtra("brand");
        }
        if (getIntent().hasExtra("item_url")) {
            item_url = getIntent().getStringExtra("item_url");
            if (!item_url.contains("http")) {
                item_url = "http://" + item_url;
            }
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(item_url);
        }

        /* 최근 본 상품 목록을 만들기 위해 해당 아이템의 정보를 SharedPreferences에 저장함 */
        SharedPreferences sharedPreferences=getSharedPreferences("RecentlyViewedItem",MODE_PRIVATE); // SharedPreferences 생성
        Gson gson=new GsonBuilder().create(); // GSON 생성

        String items_info = "";
        String item_info = "";
        RecentlyViewedItem recentlyViewedItem;
        if(getIntent().hasExtra("item_url")){
            recentlyViewedItem=new RecentlyViewedItem(item_image, item_id, item_name, item_price, item_url);
        }else{
            recentlyViewedItem= new RecentlyViewedItem(item_image, item_id, item_name, item_price);
        }

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
                intent.putExtra("brand",brand);
                startActivity(intent);
            }
        });

        // 공유 버튼을 눌렀을 때
        item_detail_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareKakao();
            }
        });
    }

    public void shareKakao(){
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("히어로네이션",
                        item_image,
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption(item_name)
                        .build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("item_id="+item_id+"&item_image="+item_image+"&item_name="+item_name+"&item_price="+item_price+"&item_subcategory="+item_subcategory+"&item_url="+item_url+"&brand="+brand)
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();

        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Toast.makeText(getApplicationContext(),"카카오톡을 다운받거나 업데이트해주세요.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }

}
