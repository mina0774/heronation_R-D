package com.example.heronation.home.dataClass;

import com.example.heronation.measurement.AR.dataClass.MeasureItemResponse;

import java.util.List;

public class CompareWithWardrobe {
    private List<GoodsResponses> goodsResponses = null;
    private List<MeasureItemResponse> measureItemResponses = null;
    private WardrobeResponse wardrobeResponse;
    private ItemResponse itemResponse;
    private ItemDmodelResponse itemDmodelResponse;
    private WardrobeDmodelResponse wardrobeDmodelResponse;
    private ItemImageDmodelResponse itemImageDmodelResponse;

    public List<GoodsResponses> getGoodsResponses() {
        return goodsResponses;
    }

    public void setGoodsResponses(List<GoodsResponses> goodsResponses) {
        this.goodsResponses = goodsResponses;
    }

    public List<MeasureItemResponse> getMeasureItemResponses() {
        return measureItemResponses;
    }

    public void setMeasureItemResponses(List<MeasureItemResponse> measureItemResponses) {
        this.measureItemResponses = measureItemResponses;
    }

    public WardrobeResponse getWardrobeResponse() {
        return wardrobeResponse;
    }

    public void setWardrobeResponse(WardrobeResponse wardrobeResponse) {
        this.wardrobeResponse = wardrobeResponse;
    }

    public ItemResponse getItemResponse() {
        return itemResponse;
    }

    public void setItemResponse(ItemResponse itemResponse) {
        this.itemResponse = itemResponse;
    }

    public ItemDmodelResponse getItemDmodelResponse() {
        return itemDmodelResponse;
    }

    public void setItemDmodelResponse(ItemDmodelResponse itemDmodelResponse) {
        this.itemDmodelResponse = itemDmodelResponse;
    }

    public WardrobeDmodelResponse getWardrobeDmodelResponse() {
        return wardrobeDmodelResponse;
    }

    public void setWardrobeDmodelResponse(WardrobeDmodelResponse wardrobeDmodelResponse) {
        this.wardrobeDmodelResponse = wardrobeDmodelResponse;
    }

    public ItemImageDmodelResponse getItemImageDmodelResponse() {
        return itemImageDmodelResponse;
    }

    public void setItemImageDmodelResponse(ItemImageDmodelResponse itemImageDmodelResponse) {
        this.itemImageDmodelResponse = itemImageDmodelResponse;
    }
}
