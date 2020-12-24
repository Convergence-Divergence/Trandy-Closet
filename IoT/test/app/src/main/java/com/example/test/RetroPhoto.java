package com.example.test;

import com.google.gson.annotations.SerializedName;

public class RetroPhoto {

    @SerializedName("name")
    private String name;
    @SerializedName("category")
    private String category;
    @SerializedName("detail")
    private String detail;
    @SerializedName("color")
    private String color;
    @SerializedName("season")
    private String season;
    @SerializedName("Url")
    private String Url;

    public RetroPhoto(String name, String category, String color, String detail, String season, String Url) {
        this.name = name;
//        this.image_number = image_number;
        this.category = category;
        this.color = color;
        this.detail = detail;
        this.season = season;
        this.Url = Url;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

//    public Integer getimage_number() {
//        return image_number;
//    }

//    public void setimage_number(Integer image_number) {
//        this.image_number = image_number;
//    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }

    public String getcolor() {
        return color;
    }

    public void setcolor(String color) { this.color = color; }

    public String getdetail() {
        return detail;
    }

    public void setdetail(String detail) {
        this.detail = detail;
    }

    public String getseason() {
        return season;
    }

    public void setseason(String season) {
        this.season = season;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }
}