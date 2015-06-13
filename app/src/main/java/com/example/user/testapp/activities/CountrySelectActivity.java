package com.example.user.testapp.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.testapp.HotelSelectActivity;
import com.example.user.testapp.R;
import com.example.user.testapp.REST.MarkAPI;
import com.example.user.testapp.model.Country;
import com.example.user.testapp.model.Hotel;
import com.example.user.testapp.model.Mark;
import com.example.user.testapp.model.Room;
import com.example.user.testapp.util.CountryListAdapter;
import com.example.user.testapp.util.NaviDrawer;
import com.example.user.testapp.util.ServerUrl;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CountrySelectActivity extends ActionBarActivity {
    List<Country> countries;
    List<Hotel> hotels;
    List<Room> rooms;
    ListView countryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_select);

        countryListView=(ListView)findViewById(R.id.countryListView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("Select destination country");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);

        countries = new ArrayList<Country>();
        getCountries();
        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CountrySelectActivity.this, HotelSelectActivity.class);
                intent.putExtra("name",((Country)parent.getItemAtPosition(position)).getName());
                Bundle bundle = new Bundle();
                bundle.putLong("id", ((Country) parent.getItemAtPosition(position)).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }


    public void getCountries(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerUrl.BASE_SERVER_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.getCountries(new Callback<List<Country>>() {
            @Override
            public void success(List<Country> c, Response response) {

                //Toast.makeText(getApplicationContext(), countries.get(0).getName(), Toast.LENGTH_SHORT).show();
                countries=c;
                //ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(getApplicationContext(),R.layout.country_spinner_item,countries);
                ArrayAdapter<Country> adapter = new CountryListAdapter(getApplicationContext(),c);
                countryListView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public List<Country> getCountriesSync(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerUrl.BASE_SERVER_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        return markAPI.getCountriesSync();
    }
    public void getHotels(long country_id){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerUrl.BASE_SERVER_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.getHotels(country_id, new Callback<List<Hotel>>() {
            @Override
            public void success(List<Hotel> h, Response response) {
                //Toast.makeText(getApplicationContext(), hotels.get(0).getName(), Toast.LENGTH_SHORT).show();
                hotels=h;
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
            public void success(List<Room> r, Response response) {
                //Toast.makeText(getApplicationContext(), hotels.get(0).getDescription(), Toast.LENGTH_SHORT).show();
                rooms=r;
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
