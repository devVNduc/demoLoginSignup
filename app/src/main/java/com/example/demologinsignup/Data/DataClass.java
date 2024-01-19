package com.example.demologinsignup.Data;

public class DataClass {

    private String dataTitle;
    private String dataDesc;
    private String dataPrice;
    private String dataOrigin;
    private String dataImage;

    private String dataQuantity;
    private String dataStatus;
    private  String key;

    private String categoryId;

    public String getKey() {
        return key;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getDataTitle() {
        return dataTitle;
    }
    public String getDataPrice() {
        return dataPrice;
    }

    public String getDataOrigin() {
        return dataOrigin;
    }
    public String getDataDesc() {
        return dataDesc;
    }


    public String getDataImage() {
        return dataImage;
    }
    public String getDataQuantity() {
        return dataQuantity;
    }

    public String getDataStatus() {
        return dataStatus;
    }
    public DataClass(String dataTitle,String dataPrice,String dataOrigin, String dataDesc, String dataQuantity, String dataStatus, String dataImage, String categoryId) {
        this.categoryId = categoryId;
        this.dataTitle = dataTitle;
        this.dataPrice = dataPrice;
        this.dataOrigin = dataOrigin;
        this.dataDesc = dataDesc;
        this.dataQuantity = dataQuantity;
        this.dataStatus = dataStatus;
        this.dataImage = dataImage;
    }
    public DataClass(){}
}
