package com.example.heronation.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.heronation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopWebViewActivity extends AppCompatActivity {

    @BindView(R.id.shop_web_view) WebView shop_web_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_web_view);
        ButterKnife.bind(this);
        String site_link;
        if(!getIntent().getStringExtra("shop_link").contains("http")){ // 링크에 http가 포함되어있지 않을 경우에
            site_link="http://"+getIntent().getStringExtra("shop_link"); // 링크 앞에 http를 포함시켜줌
        }else{ //링크에 http가 포함되어 있을 경우에
            site_link=getIntent().getStringExtra("shop_link"); //그대로 링크를 받음
        }

        shop_web_view.getSettings().setJavaScriptEnabled(true); //자바스크립트 허용
        shop_web_view.loadUrl(site_link); //웹뷰 실행
        shop_web_view.setWebChromeClient(new WebChromeClient()); //웹뷰에 크롬 사용 허용, 이 부분이 없으면 크롬에서 alert가 뜨지 않음
        shop_web_view.setWebViewClient(new WebViewClientClass()); //새 창 열기 없이 웹뷰 내에서 다시 열기, 페이지 이동 원활히 하기위해 사용

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && shop_web_view.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로감
            shop_web_view.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }
}
