package com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass;

import java.io.Serializable;

/* Serializable은 intent로 데이터를 넘겨줄 때
* 객체를 직렬화 시키기 위함 */
public class ClosetItem implements Serializable {
    String image_url="";
    String category="";
    String item_name="";
    String date="";
    Integer category_id;
    String shop_name="";
    String measurement_type="";
    String id="";
    Boolean delete_check;


    public ClosetItem(String category, String item_name, String date, String shop_name, String measurement_type, Boolean delete_check) {
        this.category = category;
        this.item_name = item_name;
        this.date = date;
        this.shop_name = shop_name;
        this.measurement_type = measurement_type;
        this.delete_check = delete_check;
    }


    public ClosetItem(String image_url, String category, String item_name, String date, String shop_name, String measurement_type,String id,Integer category_id) {
        this.image_url = image_url;
        this.category = category;
        this.item_name = item_name;
        this.date = date;
        this.shop_name = shop_name;
        this.measurement_type=measurement_type;
        this.id=id;
        this.category_id=category_id;
    }


    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getImage_url() { return image_url; }

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

    public String getId() { return id; }

}
