package com.example.heronation.home.itemRecyclerViewAdapter.dataClass;

/* 상품 정보를 보여주는 리사이클러뷰를 위해 상품 정보를 받아오기 위한 클래스 */
public class ShopItem {


    private String itemImage_URL;
    private String itemName;
    private String shopName;
    private Integer originalPrice;
    private Integer salePrice;
    
    public ShopItem(String itemImage_URL, String itemName, String shopName, Integer originalPrice, Integer salePrice){
        this.itemImage_URL=itemImage_URL;
        this.itemName=itemName;
        this.shopName=shopName;
        this.originalPrice=originalPrice;
        this.salePrice=salePrice;
    }

    public ShopItem(String itemImage_URL, String itemName, String shopName, Integer originalPrice){
        this.itemImage_URL=itemImage_URL;
        this.itemName=itemName;
        this.shopName=shopName;
        this.originalPrice=originalPrice;;
    }

    public String getItemImage_URL() {
        return itemImage_URL;
    }

    public String getItemName() {
        return itemName;
    }

    public String getShopName() {
        return shopName;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }
}
