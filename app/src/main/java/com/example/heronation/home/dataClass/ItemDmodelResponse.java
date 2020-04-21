package com.example.heronation.home.dataClass;

public class ItemDmodelResponse {
    private Integer id;
    private Integer subCategoryId;
    private String subCategoryName;
    private String title;
    private Float scale;
    private Integer lineThick;
    private String lineColor;
    private String ctrlData;
    private String svgData;
    private String dmodelData;
    private Integer xaxis;
    private Integer yaxis;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public Integer getLineThick() {
        return lineThick;
    }

    public void setLineThick(Integer lineThick) {
        this.lineThick = lineThick;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public String getCtrlData() {
        return ctrlData;
    }

    public void setCtrlData(String ctrlData) {
        this.ctrlData = ctrlData;
    }

    public String getSvgData() {
        return svgData;
    }

    public void setSvgData(String svgData) {
        this.svgData = svgData;
    }

    public String getDmodelData() {
        return dmodelData;
    }

    public void setDmodelData(String dmodelData) {
        this.dmodelData = dmodelData;
    }

    public Integer getXaxis() {
        return xaxis;
    }

    public void setXaxis(Integer xaxis) {
        this.xaxis = xaxis;
    }

    public Integer getYaxis() {
        return yaxis;
    }

    public void setYaxis(Integer yaxis) {
        this.yaxis = yaxis;
    }
}
