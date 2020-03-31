package com.example.heronation.wishlist.dataClass;
import java.util.ArrayList;
import java.util.List;

public class ClosetResponse {
    public List<WardrobeResponse> wardrobeResponses = null;
    private Integer totalPages;
    private Integer totalElements;
    private Integer number;
    private Integer size;


    public List<WardrobeResponse> getWardrobeResponses() {
        return wardrobeResponses;
    }

    public void setWardrobeResponses(List<WardrobeResponse> wardrobeResponses) {
        this.wardrobeResponses = wardrobeResponses;
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


    public class WardrobeResponse{
        private Integer id;
        private String name="";
        private Integer subCategoryId;
        private String subCategoryName="";
        private String registerType="";
        private String createDt="";
        private String image="";
        private String sizeOptionName="";
        private String shopmallName="";

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

        public String getCreateDt() {
            return createDt;
        }

        public void setCreateDt(String createDt) {
            this.createDt = createDt;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSizeOptionName() {
            return sizeOptionName;
        }

        public void setSizeOptionName(String sizeOptionName) {
            this.sizeOptionName = sizeOptionName;
        }

        public String getShopmallName() {
            return shopmallName;
        }

        public void setShopmallName(String shopmallName) {
            this.shopmallName = shopmallName;
        }
    }
}