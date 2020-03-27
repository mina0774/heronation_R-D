package com.example.heronation.measurement.AR.dataClass;

import java.util.List;

public class MeasurementCloset {
    private Integer id;
    private String name;
    private Integer subCategoryId;
    private String registerType;
    private List<AttachFile> attachFile = null;
    private List<WardrobeScmmValueRequest> wardrobeScmmValueRequests = null;

    public MeasurementCloset(Integer id, String name, Integer subCategoryId, String registerType, List<AttachFile> attachFile, List<WardrobeScmmValueRequest> wardrobeScmmValueRequests) {
        this.id = id;
        this.name = name;
        this.subCategoryId = subCategoryId;
        this.registerType = registerType;
        this.attachFile = attachFile;
        this.wardrobeScmmValueRequests = wardrobeScmmValueRequests;
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

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public List<AttachFile> getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(List<AttachFile> attachFile) {
        this.attachFile = attachFile;
    }

    public List<WardrobeScmmValueRequest> getWardrobeScmmValueRequests() {
        return wardrobeScmmValueRequests;
    }

    public void setWardrobeScmmValueRequests(List<WardrobeScmmValueRequest> wardrobeScmmValueRequests) {
        this.wardrobeScmmValueRequests = wardrobeScmmValueRequests;
    }
}
