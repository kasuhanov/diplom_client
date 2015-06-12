package com.example.user.testapp.model;


import com.google.gson.annotations.SerializedName;

public class Room {
    @SerializedName("date_start")
    private String dateStart;
    @SerializedName("description")
    private String description;
    @SerializedName("type")
    private String type;
    @SerializedName("date_long")
    private long dateLong;
    @SerializedName("id")
    private long id;

    public Room(long dateLong, String dateStart, String description, long id, String type) {
        this.dateLong = dateLong;
        this.dateStart = dateStart;
        this.description = description;
        this.id = id;
        this.type = type;
    }

    public long getDateLong() {
        return dateLong;
    }

    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
