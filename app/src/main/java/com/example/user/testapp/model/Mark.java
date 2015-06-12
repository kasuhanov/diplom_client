package com.example.user.testapp.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Mark {
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("lat")
    private double Lat;
    @SerializedName("long")
    private double Long;
    @SerializedName("id")
    private long id;

    public Mark(String name,String description, double aLong, double lat,long id) {
        this.description = description;
        Long = aLong;
        Lat = lat;
        this.name = name;
        this.id=id;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("name", this.name);
            jsonObject.put("description", this.description);
            jsonObject.put("lat", this.Lat);
            jsonObject.put("long", this.Long);
            Log.d("Mark",name+" converted to JSON");
        }
        catch (Exception e){
            Log.e("Mark",e.getMessage());
        }

        return jsonObject;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public long getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
