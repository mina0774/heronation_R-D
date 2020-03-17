package com.example.heronation.home.itemRecyclerViewAdapter.dataClass;

import java.util.HashMap;
import java.util.Map;

public class BodyRecommendation {

    private Integer itemId;
    private String itemName;
    private String code;
    private Integer price;
    private String shopImage;
    private Integer hit;
    private Integer subCategoryId;
    private String itemCreateDt;
    private String partKind;
    private String mensurations;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getItemCreateDt() {
        return itemCreateDt;
    }

    public void setItemCreateDt(String itemCreateDt) {
        this.itemCreateDt = itemCreateDt;
    }

    public String getPartKind() {
        return partKind;
    }

    public void setPartKind(String partKind) {
        this.partKind = partKind;
    }

    public String getMensurations() {
        return mensurations;
    }

    public void setMensurations(String mensurations) {
        this.mensurations = mensurations;
    }
}
