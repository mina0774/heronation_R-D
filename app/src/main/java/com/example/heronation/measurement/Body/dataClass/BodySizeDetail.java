package com.example.heronation.measurement.Body.dataClass;

public class BodySizeDetail {
    private Integer measureItemId;
    private String value;

    public BodySizeDetail(Integer measureItemId, String value) {
        this.measureItemId = measureItemId;
        this.value = value;
    }

    public Integer getMeasureItemId() {
        return measureItemId;
    }

    public void setMeasureItemId(Integer measureItemId) {
        this.measureItemId = measureItemId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
