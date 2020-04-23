package com.example.heronation.home.dataClass;

public class WardrobeScmmValueResponse {
    private Integer wardrobeScmmValueId;
    private String measureItemName;
    private Integer measureItemId;
    private String code;
    private Float value;

    public Integer getWardrobeScmmValueId() {
        return wardrobeScmmValueId;
    }

    public void setWardrobeScmmValueId(Integer wardrobeScmmValueId) {
        this.wardrobeScmmValueId = wardrobeScmmValueId;
    }

    public String getMeasureItemName() {
        return measureItemName;
    }

    public void setMeasureItemName(String measureItemName) {
        this.measureItemName = measureItemName;
    }

    public Integer getMeasureItemId() {
        return measureItemId;
    }

    public void setMeasureItemId(Integer measureItemId) {
        this.measureItemId = measureItemId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
