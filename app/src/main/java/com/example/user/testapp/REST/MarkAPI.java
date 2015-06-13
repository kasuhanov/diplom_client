package com.example.user.testapp.REST;

import com.example.user.testapp.model.Comment;
import com.example.user.testapp.model.Country;
import com.example.user.testapp.model.Hotel;
import com.example.user.testapp.model.Mark;
import com.example.user.testapp.model.MarkComments;
import com.example.user.testapp.model.MarkImage;
import com.example.user.testapp.model.Room;
import com.example.user.testapp.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public interface MarkAPI {
    @GET("/?par=marks")
     void getMarks(Callback<List<Mark>> response);

    @GET("/?par=comments")
    void getComments(@Header("id") long mark_id, Callback<List<MarkComments>> response);

    @GET("/?par=countries")
    void getCountries( Callback<List<Country>> response);

    @GET("/?par=countries")
    List<Country> getCountriesSync();

    @GET("/?par=hotel")
    void getHotels(@Header("country") long country_id, Callback<List<Hotel>> response);

    @GET("/?par=room")
    void getRooms(@Header("hotel") long hotel_id, Callback<List<Room>> response);

    @POST("/?par=addmark")
    void createMark(@Body Mark mark, Callback<Mark> cb);

    @POST("/?par=addcomment")
    void createComment(@Body Comment body, Callback<JSONArray> cb);

    @POST("/?par=login")
    void logIn(@Body User user, Callback<Response> cb);

    @POST("/?par=addimage")
    void createImage(@Body MarkImage markImage, Callback<String> cb);

    @Multipart
    @POST("/?par=addimage")
    void addImage(@Part("file")TypedFile typedFile,Callback<Response> cb);

}
