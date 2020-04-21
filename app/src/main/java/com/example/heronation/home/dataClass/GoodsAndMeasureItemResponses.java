package com.example.heronation.home.dataClass;

import java.util.List;

public class GoodsAndMeasureItemResponses {
    private List<GoodsResponses> goodsResponses = null;
    private List<MeasureItem> measureItems = null;

    public List<GoodsResponses> getGoodsResponses() {
        return goodsResponses;
    }

    public void setGoodsResponses(List<GoodsResponses> goodsResponses) {
        this.goodsResponses = goodsResponses;
    }

    public List<MeasureItem> getMeasureItems() {
        return measureItems;
    }

    public void setMeasureItems(List<MeasureItem> measureItems) {
        this.measureItems = measureItems;
    }

}
