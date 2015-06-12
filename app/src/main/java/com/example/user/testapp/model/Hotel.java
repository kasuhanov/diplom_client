package com.example.user.testapp.model;


import com.google.gson.annotations.SerializedName;

public class Hotel {
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("star")
    private long star;
    @SerializedName("id")
    private long id;

    public Hotel(String description, long id, String name, long star) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.star = star;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStar() {
        return star;
    }

    public void setStar(long star) {
        this.star = star;
    }
}
