package com.example.heronation.home.dataClass;

public class GoodsScmmValue {
    private Integer goodsScmmValueId;
    private Integer measureItemId;
    private String measureItemName;
    private String measureMethod;
    private String code;
    private Double value;

    public Integer getGoodsScmmValueId() {
        return goodsScmmValueId;
    }

    public void setGoodsScmmValueId(Integer goodsScmmValueId) {
        this.goodsScmmValueId = goodsScmmValueId;
    }

    public Integer getMeasureItemId() {
        return measureItemId;
    }

    public void setMeasureItemId(Integer measureItemId) {
        this.measureItemId = measureItemId;
    }

    public String getMeasureItemName() {
        return measureItemName;
    }

    public void setMeasureItemName(String measureItemName) {
        this.measureItemName = measureItemName;
    }

    public String getMeasureMethod() {
        return measureMethod;
    }

    public void setMeasureMethod(String measureMethod) {
        this.measureMethod = measureMethod;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
