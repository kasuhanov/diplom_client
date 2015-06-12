package com.example.user.testapp.model;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("country")
    private String name;
    @SerializedName("id")
    private long id;

    public Country(long id, String name) {
        this.id = id;
        this.name = name;
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
}
