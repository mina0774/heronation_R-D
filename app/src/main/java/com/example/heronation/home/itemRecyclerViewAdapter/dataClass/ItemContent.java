package com.example.heronation.home.itemRecyclerViewAdapter.dataClass;

import java.util.HashMap;
import java.util.Map;

public class ItemContent {
    private Integer id;
    private String name;
    private String linkDt;
    private String linkYn;
    private String sizeInputStatusYn;
    private String subCategoryName;
    private String shopImage;
    private String code;
    private String useYn;
    private String createDt;
    private String shopmallCreateDt;
    private String shopmallUpdateDt;

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

    public String getLinkDt() {
        return linkDt;
    }

    public void setLinkDt(String linkDt) {
        this.linkDt = linkDt;
    }

    public String getLinkYn() {
        return linkYn;
    }

    public void setLinkYn(String linkYn) {
        this.linkYn = linkYn;
    }

    public String getSizeInputStatusYn() {
        return sizeInputStatusYn;
    }

    public void setSizeInputStatusYn(String sizeInputStatusYn) {
        this.sizeInputStatusYn = sizeInputStatusYn;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getShopmallCreateDt() {
        return shopmallCreateDt;
    }

    public void setShopmallCreateDt(String shopmallCreateDt) {
        this.shopmallCreateDt = shopmallCreateDt;
    }

    public String getShopmallUpdateDt() {
        return shopmallUpdateDt;
    }

    public void setShopmallUpdateDt(String shopmallUpdateDt) {
        this.shopmallUpdateDt = shopmallUpdateDt;
    }
}
