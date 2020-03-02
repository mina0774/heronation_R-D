package com.example.heronation.shop.ShoplistRecyclerViewAdapter.dataClass;

import java.util.List;

public class ShopContent {
    private Integer id;
    private String name;
    private String createDt;
    private String storeType;
    private String storeId;
    private String url;
    private String useYn;
    private String memberId;
    private Integer billingPolicyId;
    private String billingPolicyName;
    private List<AgeTagResponse> ageTagResponses = null;
    private List<StyleTagResponse> styleTagResponses = null;

    public ShopContent(Integer id, String name, String createDt, String storeType, String storeId, String url, String useYn, String memberId, Integer billingPolicyId, String billingPolicyName, List<AgeTagResponse> ageTagResponses, List<StyleTagResponse> styleTagResponses) {
        this.id = id;
        this.name = name;
        this.createDt = createDt;
        this.storeType = storeType;
        this.storeId = storeId;
        this.url = url;
        this.useYn = useYn;
        this.memberId = memberId;
        this.billingPolicyId = billingPolicyId;
        this.billingPolicyName = billingPolicyName;
        this.ageTagResponses = ageTagResponses;
        this.styleTagResponses = styleTagResponses;
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

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getBillingPolicyId() {
        return billingPolicyId;
    }

    public void setBillingPolicyId(Integer billingPolicyId) {
        this.billingPolicyId = billingPolicyId;
    }

    public String getBillingPolicyName() {
        return billingPolicyName;
    }

    public void setBillingPolicyName(String billingPolicyName) {
        this.billingPolicyName = billingPolicyName;
    }

    public List<AgeTagResponse> getAgeTagResponses() {
        return ageTagResponses;
    }

    public void setAgeTagResponses(List<AgeTagResponse> ageTagResponses) {
        this.ageTagResponses = ageTagResponses;
    }

    public List<StyleTagResponse> getStyleTagResponses() {
        return styleTagResponses;
    }

    public void setStyleTagResponses(List<StyleTagResponse> styleTagResponses) {
        this.styleTagResponses = styleTagResponses;
    }

}
