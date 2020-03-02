package com.example.heronation.login_register.dataClass;

import java.util.HashMap;
import java.util.Map;

public class BodyResponse {

    private Integer measureItemId;
    private String measureItemName;
    private String measureMethod;
    private Integer minScope;
    private Integer maxScope;
    private String code;
    private Double value;
    private String partKind;
    private Boolean selected;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public Integer getMinScope() {
        return minScope;
    }

    public void setMinScope(Integer minScope) {
        this.minScope = minScope;
    }

    public Integer getMaxScope() {
        return maxScope;
    }

    public void setMaxScope(Integer maxScope) {
        this.maxScope = maxScope;
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

    public String getPartKind() {
        return partKind;
    }

    public void setPartKind(String partKind) {
        this.partKind = partKind;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}