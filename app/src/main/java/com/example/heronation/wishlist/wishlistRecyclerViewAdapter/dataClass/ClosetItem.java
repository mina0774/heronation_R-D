package com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass;

import java.io.Serializable;

/* Serializable은 intent로 데이터를 넘겨줄 때
* 객체를 직렬화 시키기 위함 */
public class ClosetItem implements Serializable {
    String category;
    String item_name;
    String date;
    String shop_name;
    String measurement_type;
    Boolean delete_check;

    public ClosetItem(String category, String item_name, String date, String shop_name, String measurement_type) {
        this.category = category;
        this.item_name = item_name;
        this.date = date;
        this.shop_name = shop_name;
        this.measurement_type = measurement_type;
    }

    public ClosetItem(String category, String item_name, String date, String shop_name, String measurement_type, Boolean delete_check) {
        this.category = category;
        this.item_name = item_name;
        this.date = date;
        this.shop_name = shop_name;
        this.measurement_type = measurement_type;
        this.delete_check = delete_check;
    }

    public String getCategory() {
        return category;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getDate() {
        return date;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getMeasurement_type() {
        return measurement_type;
    }

    public Boolean getDelete_check() {
        return delete_check;
    }
}
