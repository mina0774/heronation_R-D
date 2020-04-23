package com.example.heronation.home.dataClass;

import java.util.List;

public class WardrobeResponse {
    private Integer id;
    private String name;
    private String image;
    private Integer subCategoryId;
    private String subCategoryName;
    private String registerType;
    private List<WardrobeScmmValueResponse> wardrobeScmmValueResponses = null;
    private Integer wardrobesCount;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public List<WardrobeScmmValueResponse> getWardrobeScmmValueResponses() {
        return wardrobeScmmValueResponses;
    }

    public void setWardrobeScmmValueResponses(List<WardrobeScmmValueResponse> wardrobeScmmValueResponses) {
        this.wardrobeScmmValueResponses = wardrobeScmmValueResponses;
    }

    public Integer getWardrobesCount() {
        return wardrobesCount;
    }

    public void setWardrobesCount(Integer wardrobesCount) {
        this.wardrobesCount = wardrobesCount;
    }
}
