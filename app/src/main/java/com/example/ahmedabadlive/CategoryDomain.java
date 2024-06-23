package com.example.ahmedabadlive;

public class CategoryDomain {


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    private String title;
    private int id;
    private String picUrl;

    public  CategoryDomain(String title,int id, String picUrl){
        this.id = id;
        this.title = title;
        this.picUrl = picUrl;
    }

    public  CategoryDomain(){

    }

}
