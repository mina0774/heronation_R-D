package com.example.heronation.home.dataClass;

import java.util.List;

public class GoodsResponses {
    private Integer goodsId;
    private Integer sizeOptionId;
    private String sizeOptionName;
    private List<GoodsScmmValue> goodsScmmValues = null;
    private Double compareResultDerive;
    private Boolean selected;
    private Integer subCategoryId;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSizeOptionId() {
        return sizeOptionId;
    }

    public void setSizeOptionId(Integer sizeOptionId) {
        this.sizeOptionId = sizeOptionId;
    }

    public String getSizeOptionName() {
        return sizeOptionName;
    }

    public void setSizeOptionName(String sizeOptionName) {
        this.sizeOptionName = sizeOptionName;
    }

    public List<GoodsScmmValue> getGoodsScmmValues() {
        return goodsScmmValues;
    }

    public void setGoodsScmmValues(List<GoodsScmmValue> goodsScmmValues) {
        this.goodsScmmValues = goodsScmmValues;
    }

    public Double getCompareResultDerive() {
        return compareResultDerive;
    }

    public void setCompareResultDerive(Double compareResultDerive) {
        this.compareResultDerive = compareResultDerive;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
