package com.example.demologinsignup.Data;

public class DataclassCategory {
    private String categoryId;

    private String categoryImage;
    private String categoryName;
    public String getCategoryId() {
        return categoryId;
    }
    public String getCategoryImage() {
        return categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public DataclassCategory(String categoryId,String categoryName, String categoryImage){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }
    public  DataclassCategory(){}
}
