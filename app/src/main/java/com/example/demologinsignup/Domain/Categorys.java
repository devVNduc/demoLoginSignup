package com.example.demologinsignup.Domain;
import com.google.firebase.database.PropertyName;

public class Categorys {
    private int id;
    private String ImagePath;
    private String Name;
    public Categorys() {

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @PropertyName("ImagePath")

    public String getImagePath() {
        return ImagePath;
    }
    @PropertyName("ImagePath")
    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
    @PropertyName("Name")
    public String getName() {
        return Name;
    }
    @PropertyName("Name")
    public void setName(String name) {
        Name = name;
    }

}
