package com.example.heronation.adapter.bannerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.heronation.R;

/* 배너 Image를 넘기기 위해 Viewpager를 위한 Adapter */
public class bannerAdapter extends PagerAdapter {
    //배너에 넣을 사진 모음
    private int[] images = {R.drawable.banner_example, R.drawable.banner_example5, R.drawable.banner_example4, R.drawable.banner_example3};

    private LayoutInflater inflater;
    private Context context;

    public bannerAdapter(Context context){
        this.context=context;
    }

    //이미지의 개수를 받아줌
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        //이미지 슬라이더는 뷰페이져에 들어가는 아이템 하나를 나타냄
        View v = inflater.inflate(R.layout.image_slider, container, false);
        //이미지 슬라이더에 있는 이미지뷰 객체 선언
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        //이미지 설정
        imageView.setImageResource(images[position]);

        //이러한 아이템을 담는 컨테이너에 이 뷰를 넣어줌
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }
}
