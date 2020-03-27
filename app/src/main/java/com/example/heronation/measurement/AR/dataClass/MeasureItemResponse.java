package com.example.heronation.measurement.AR.dataClass;

import java.io.Serializable;

public class MeasureItemResponse implements Serializable {
    private String id;
    private boolean bodyDisplay;
    private String bodyMinScope;
    private String bodyMaxScope;
    private String itemMinScope;
    private String itemMaxScope;
    private String bodyName;
    private String itemName;
    private String bodyImage;
    private String itemImage;
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBodyDisplay() {
        return bodyDisplay;
    }

    public void setBodyDisplay(boolean bodyDisplay) {
        this.bodyDisplay = bodyDisplay;
    }

    public String getBodyMinScope() {
        return bodyMinScope;
    }

    public void setBodyMinScope(String bodyMinScope) {
        this.bodyMinScope = bodyMinScope;
    }

    public String getBodyMaxScope() {
        return bodyMaxScope;
    }

    public void setBodyMaxScope(String bodyMaxScope) {
        this.bodyMaxScope = bodyMaxScope;
    }

    public String getItemMinScope() {
        return itemMinScope;
    }

    public void setItemMinScope(String itemMinScope) {
        this.itemMinScope = itemMinScope;
    }

    public String getItemMaxScope() {
        return itemMaxScope;
    }

    public void setItemMaxScope(String itemMaxScope) {
        this.itemMaxScope = itemMaxScope;
    }

    public String getBodyName() {
        return bodyName;
    }

    public void setBodyName(String bodyName) {
        this.bodyName = bodyName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBodyImage() {
        return bodyImage;
    }

    public void setBodyImage(String bodyImage) {
        this.bodyImage = bodyImage;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
