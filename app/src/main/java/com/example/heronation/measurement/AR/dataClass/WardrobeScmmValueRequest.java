package com.example.heronation.measurement.AR.dataClass;

import java.util.HashMap;
import java.util.Map;

public class WardrobeScmmValueRequest {
    private Integer measureItemId;
    private Integer value;

    public WardrobeScmmValueRequest(Integer measureItemId, Integer value) {
        this.measureItemId = measureItemId;
        this.value = value;
    }

    public Integer getMeasureItemId() {
        return measureItemId;
    }

    public void setMeasureItemId(Integer measureItemId) {
        this.measureItemId = measureItemId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
