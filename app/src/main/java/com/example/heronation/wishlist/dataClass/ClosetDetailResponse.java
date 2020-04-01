package com.example.heronation.wishlist.dataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ClosetDetailResponse {
    private String id="";
    private String name="";
    private String image="";
    private String subCategoryId="";
    private String subCategoryName="";
    private String registerType="";
    public List<wardrobeScmmValueResponses> wardrobeScmmValueResponses = new ArrayList<>();

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public String getImage(){
        return image;
    }
    public String getSubCategoryId(){
        return subCategoryId;
    }
    public String getSubCategoryName(){
        return subCategoryName;
    }
    public String getRegisterType(){
        return registerType;
    }
    public String getCreateDt(){
        return registerType;
    }
    public List<wardrobeScmmValueResponses> getWardrobeScmmValueResponses(){
        return wardrobeScmmValueResponses;
    }

    public class wardrobeScmmValueResponses{


        private String wardrobeScmmValueId;
        private String measureItemName;
        private String measureItemId;
        private String code;
        private String value;

        public String getWardrobeScmmValueId(){
            return wardrobeScmmValueId;
        }
        public String getMeasureItemName(){
            return measureItemName;
        }
        public String getMeasureItemId(){
            return measureItemId;
        }
        public String getCode(){
            return code;
        }
        public String getValue(){
            return value;
        }
    }

}
