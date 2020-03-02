package com.example.heronation.home.itemRecyclerViewAdapter.dataClass;

import java.util.ArrayList;

public class ShopItemPackage {
/* all best, 하의 best ... 등의 아이템 묶음의 대표 제목과, 그에 속하는 각각의 상품들을 나타내는 클래스 */
    private String packageName;
    private ArrayList<ItemContent> shopItems;

    public ShopItemPackage(String packageName, ArrayList<ItemContent> shopItems) {
        this.packageName = packageName;
        this.shopItems = shopItems;
    }

    public String getPackageName() {
        return packageName;
    }

    public ArrayList<ItemContent> getShopItems() {
        return shopItems;
    }
}
