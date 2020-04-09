package com.example.heronation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import com.example.heronation.R;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.RecentlyViewedItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailActivity extends AppCompatActivity {
    @BindView(R.id.item_detail_close_button) ImageButton item_detail_close_button;
    @BindView(R.id.item_detail_size_button) ImageButton item_detail_size_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);

        String item_id=getIntent().getStringExtra("item_id");
        String item_image=getIntent().getStringExtra("item_image");
        String item_name=getIntent().getStringExtra("item_name");
        String item_price=getIntent().getStringExtra("item_price");

        // SharedPreferences 생성
        SharedPreferences sharedPreferences=getSharedPreferences("RecentlyViewedItem",MODE_PRIVATE);
        // GSON 생성
        Gson gson=new GsonBuilder().create();

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
        final PopupWindow mPopupWindow;
        View popupView = getLayoutInflater().inflate(R.layout.measurement_pop_up, null);
        mPopupWindow = new PopupWindow(popupView);
        mPopupWindow.setWindowLayoutMode(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //팝업 터치 가능
        mPopupWindow.setTouchable(true);
        //팝업 외부 터치 가능(외부 터치시 나갈 수 있게)
        mPopupWindow.setOutsideTouchable(true);
        //외부터치 인식을 위한 추가 설정 : 미 설정시 외부는 null로 생각하고 터치 인식 X
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //애니메이션 활성화

        // PopUp 창 띄우기
        mPopupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        /* 블러 처리 -- 대체 코드 있으면 대체하기 (불안정함)
         *  ViewGroup.LayoutParams를 WindowManager.LayoutParams에 캐스팅하기 위함인데,
         * 버전에 따라 ViewGroup의 자식인 다른 예를 들면 FrameLayout.LayoutParams를 가리키기도 함.
         * 하지만, WindowManager.LayoutParams에 FrameLayout.LayoutParams는 캐스팅 되지 않으므로 오류가 발생함
         * 이를 처리하기 위해서 if문으로 분기를 해주었으나, 불안정한 코드라서 대체 방법이 있다면 대체해야할 것 같음.
         * */
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
    }
}
