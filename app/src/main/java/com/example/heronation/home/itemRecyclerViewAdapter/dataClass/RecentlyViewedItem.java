package com.example.heronation.home.itemRecyclerViewAdapter.dataClass;

public class RecentlyViewedItem {
    String item_id;
    String image_url;
    String item_name;
    String item_price;

    public RecentlyViewedItem(String item_id, String image_url, String item_name, String item_price) {
        this.item_id = item_id;
        this.image_url = image_url;
        this.item_name = item_name;
        this.item_price = item_price;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

}
