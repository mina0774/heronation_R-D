package com.example.heronation.home.dataClass;

import java.io.Serializable;

public class BodySizeLevel implements Serializable {
    private Integer shoulderLevel;
    private Integer chestLevel;
    private Integer hubLevel;
    private Integer hipLevel;
    private Integer thighLevel;

    public BodySizeLevel(Integer shoulderLevel, Integer chestLevel, Integer hubLevel, Integer hipLevel, Integer thighLevel) {
        this.shoulderLevel = shoulderLevel;
        this.chestLevel = chestLevel;
        this.hubLevel = hubLevel;
        this.hipLevel = hipLevel;
        this.thighLevel = thighLevel;
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
}
