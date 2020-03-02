package com.example.heronation.home.itemRecyclerViewAdapter.dataClass;

import android.graphics.drawable.Drawable;

/* 아이템 베스트 프래그먼트의 카테고리 리사이클러뷰에 들어가는 아이템 정의*/
public class ItemBestCategory {
    private Drawable icon_drawable;
    private String item_name;

    public ItemBestCategory(Drawable icon_drawable, String item_name) {
        this.icon_drawable = icon_drawable;
        this.item_name = item_name;
    }


    public Drawable getIcon_drawable() {
        return icon_drawable;
    }

    public String getItem_name() {
        return item_name;
    }
}
