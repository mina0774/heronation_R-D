package com.example.heronation.home.dataClass;

import java.util.List;

public class ItemSizeInfo {
    private List<GoodsResponse> goodsResponses = null;
    private List<MeasureItem> measureItems = null;

    public List<GoodsResponse> getGoodsResponses() {
        return goodsResponses;
    }

    public void setGoodsResponses(List<GoodsResponse> goodsResponses) {
        this.goodsResponses = goodsResponses;
    }

    public List<MeasureItem> getMeasureItems() {
        return measureItems;
    }

    public void setMeasureItems(List<MeasureItem> measureItems) {
        this.measureItems = measureItems;
    }

}
