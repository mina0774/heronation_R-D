package com.example.heronation.mypage.dataClass;

import java.util.HashMap;
import java.util.Map;

public class UserModifyInfo {
    private String name;
    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;
    private String gender;
    private String termsAdvertisement;


    public UserModifyInfo(String name, Integer birthYear, Integer birthMonth, Integer birthDay, String gender, String termsAdvertisement) {
        this.name = name;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.gender = gender;
        this.termsAdvertisement = termsAdvertisement;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTermsAdvertisement() {
        return termsAdvertisement;
    }

    public void setTermsAdvertisement(String termsAdvertisement) {
        this.termsAdvertisement = termsAdvertisement;
    }
}
