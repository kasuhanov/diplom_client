package com.example.user.testapp.model;

import com.google.gson.annotations.SerializedName;

public class MarkImage {
    @SerializedName("lat")
    private double Lat;
    @SerializedName("long")
    private double Long;
    @SerializedName("image")
    private byte[] image;

    public MarkImage(double lat, double aLong, byte[] image) {
        Lat = lat;
        Long = aLong;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }
}
