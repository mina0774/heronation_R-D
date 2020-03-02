package com.example.heronation.shop.ShoplistRecyclerViewAdapter.dataClass;

import java.util.HashMap;
import java.util.Map;

public class StyleTagResponse {
    private Integer id;
    private String name;

    public StyleTagResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
