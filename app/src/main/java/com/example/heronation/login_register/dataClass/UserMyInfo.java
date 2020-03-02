package com.example.heronation.login_register.dataClass;

import java.io.Serializable;
import java.util.List;

//수정 필요
public class UserMyInfo implements Serializable {

    private String consumerId;
    private String name;
    private String email;
    private String gender;
    private Integer height;
    private Integer weight;
    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;
    private String termsAdvertisement;
    private String lastAccessDt;
    private String corpType;
    private Integer wardrobeViewerCount;
    private Integer shoulderLevel;
    private Integer chestLevel;
    private Integer hubLevel;
    private Integer hipLevel;
    private Integer thighLevel;
    private Integer shoulderSensibilityLevel;
    private Integer chestSensibilityLevel;
    private Integer hubSensibilityLevel;
    private Integer hipSensibilityLevel;
    private Integer thighSensibilityLevel;
    private List<BodyResponse> bodyResponses = null;

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(Integer birthMonth) {
        this.birthMonth = birthMonth;
    }

    public Integer getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Integer birthDay) {
        this.birthDay = birthDay;
    }

    public String getTermsAdvertisement() {
        return termsAdvertisement;
    }

    public void setTermsAdvertisement(String termsAdvertisement) {
        this.termsAdvertisement = termsAdvertisement;
    }

    public String getLastAccessDt() {
        return lastAccessDt;
    }

    public void setLastAccessDt(String lastAccessDt) {
        this.lastAccessDt = lastAccessDt;
    }

    public String getCorpType() {
        return corpType;
    }

    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    public Integer getWardrobeViewerCount() {
        return wardrobeViewerCount;
    }

    public void setWardrobeViewerCount(Integer wardrobeViewerCount) {
        this.wardrobeViewerCount = wardrobeViewerCount;
    }

    public Integer getShoulderLevel() {
        return shoulderLevel;
    }

    public void setShoulderLevel(Integer shoulderLevel) {
        this.shoulderLevel = shoulderLevel;
    }

    public Integer getChestLevel() {
        return chestLevel;
    }

    public void setChestLevel(Integer chestLevel) {
        this.chestLevel = chestLevel;
    }

    public Integer getHubLevel() {
        return hubLevel;
    }

    public void setHubLevel(Integer hubLevel) {
        this.hubLevel = hubLevel;
    }

    public Integer getHipLevel() {
        return hipLevel;
    }

    public void setHipLevel(Integer hipLevel) {
        this.hipLevel = hipLevel;
    }

    public Integer getThighLevel() {
        return thighLevel;
    }

    public void setThighLevel(Integer thighLevel) {
        this.thighLevel = thighLevel;
    }

    public Integer getShoulderSensibilityLevel() {
        return shoulderSensibilityLevel;
    }

    public void setShoulderSensibilityLevel(Integer shoulderSensibilityLevel) {
        this.shoulderSensibilityLevel = shoulderSensibilityLevel;
    }

    public Integer getChestSensibilityLevel() {
        return chestSensibilityLevel;
    }

    public void setChestSensibilityLevel(Integer chestSensibilityLevel) {
        this.chestSensibilityLevel = chestSensibilityLevel;
    }

    public Integer getHubSensibilityLevel() {
        return hubSensibilityLevel;
    }

    public void setHubSensibilityLevel(Integer hubSensibilityLevel) {
        this.hubSensibilityLevel = hubSensibilityLevel;
    }

    public Integer getHipSensibilityLevel() {
        return hipSensibilityLevel;
    }

    public void setHipSensibilityLevel(Integer hipSensibilityLevel) {
        this.hipSensibilityLevel = hipSensibilityLevel;
    }

    public Integer getThighSensibilityLevel() {
        return thighSensibilityLevel;
    }

    public void setThighSensibilityLevel(Integer thighSensibilityLevel) {
        this.thighSensibilityLevel = thighSensibilityLevel;
    }

    public List<BodyResponse> getBodyResponses() {
        return bodyResponses;
    }

    public void setBodyResponses(List<BodyResponse> bodyResponses) {
        this.bodyResponses = bodyResponses;
    }

}

/*
  "consumerId": "syh8088",
  "name": "서양훈",
  "email": "syh8088@nate.com",
  "gender": "M",
  "height": 171,
  "weight": 60,
  "birthYear": 1988,
  "birthMonth": 8,
  "birthDay": 8,
  "termsAdvertisement": "Y",
  "lastAccessDt": "2020-02-04T17:00:15",
  "corpType": "normal",
  "wardrobeViewerCount": 0,
  "shoulderLevel": 1,
  "chestLevel": 1,
  "hubLevel": 1,
  "hipLevel": 1,
  "thighLevel": 1,
  "shoulderSensibilityLevel": 3,
  "chestSensibilityLevel": 3,
  "hubSensibilityLevel": 3,
  "hipSensibilityLevel": 3,
  "thighSensibilityLevel": 3
  */