package com.example.test;

import com.google.gson.annotations.SerializedName;

public class RetroPhoto {

    @SerializedName("name")
    private char name;
//    @SerializedName("image_number")
//    private Integer image_number;
    @SerializedName("category")
    private char category;
    @SerializedName("detail")
    private char detail;
    @SerializedName("season")
    private char season;
    @SerializedName("Url")
    private char Url;

    public RetroPhoto(char name, char category, char detail, char season, char Url) {
        this.name = name;
//        this.image_number = image_number;
        this.category = category;
        this.detail = detail;
        this.season = season;
        this.Url = Url;
    }

    public char getname() {
        return name;
    }

    public void setname(char name) {
        this.name = name;
    }

//    public Integer getimage_number() {
//        return image_number;
//    }

//    public void setimage_number(Integer image_number) {
//        this.image_number = image_number;
//    }

    public char getcategory() {
        return category;
    }

    public void setcategory(char category) {
        this.category = category;
    }

    public char getdetail() {
        return detail;
    }

    public void setdetail(char detail) {
        this.detail = detail;
    }

    public char getseason() {
        return season;
    }

    public void setseason(char season) {
        this.season = season;
    }

    public char getUrl() {
        return Url;
    }

    public void setUrl(char Url) {
        this.Url = Url;
    }
}