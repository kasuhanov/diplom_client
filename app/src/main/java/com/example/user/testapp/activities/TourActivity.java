package com.example.user.testapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.testapp.R;
import com.example.user.testapp.REST.MarkAPI;
import com.example.user.testapp.model.Country;
import com.example.user.testapp.model.Hotel;
import com.example.user.testapp.model.Mark;
import com.example.user.testapp.model.Room;
import com.example.user.testapp.util.NaviDrawer;
import com.example.user.testapp.util.ServerUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TourActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("Tour");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);
    }

    public void getReferenses(View view){
       getRooms(1);

    }
    public void getCountries(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerUrl.BASE_SERVER_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.getCountries(new Callback<List<Country>>() {
            @Override
            public void success(List<Country> countries, Response response) {

                //Toast.makeText(getApplicationContext(), countries.get(0).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getHotels(long country_id){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerUrl.BASE_SERVER_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.getHotels(country_id, new Callback<List<Hotel>>() {
            @Override
            public void success(List<Hotel> hotels, Response response) {
                Toast.makeText(getApplicationContext(), hotels.get(0).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void getRooms(long hotel_id){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerUrl.BASE_SERVER_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.getRooms(hotel_id, new Callback<List<Room>>() {
            @Override
            public void success(List<Room> hotels, Response response) {
                Toast.makeText(getApplicationContext(), hotels.get(0).getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
