package com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass;

public class FavoriteItem {

    private Integer id;
    private String name;
    private Integer price;
    private String shopImage;
    private String shopmallName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopmallName() {
        return shopmallName;
    }

    public void setShopmallName(String shopmallName) {
        this.shopmallName = shopmallName;
    }
}
