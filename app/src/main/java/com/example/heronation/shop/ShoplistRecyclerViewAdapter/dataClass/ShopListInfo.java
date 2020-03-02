package com.example.heronation.shop.ShoplistRecyclerViewAdapter.dataClass;


import java.util.List;

public class ShopListInfo {
    private List<ShopContent> content = null;
    private Integer totalPages;
    private Integer totalElements;
    private Integer number;
    private Integer size;

    public ShopListInfo(List<ShopContent> content, Integer totalPages, Integer totalElements, Integer number, Integer size) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.number = number;
        this.size = size;
    }

    public List<ShopContent> getShopContent() {
        return content;
    }

    public void setShopContent(List<ShopContent> content) {
        this.content = content;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
