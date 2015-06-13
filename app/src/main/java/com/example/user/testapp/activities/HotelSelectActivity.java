package com.example.user.testapp.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.testapp.R;
import com.example.user.testapp.REST.MarkAPI;
import com.example.user.testapp.model.Hotel;
import com.example.user.testapp.util.HotelListAdapter;
import com.example.user.testapp.util.NaviDrawer;
import com.example.user.testapp.util.ServerUrl;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class HotelSelectActivity extends ActionBarActivity {
    ListView hotelListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_select);

        hotelListView=(ListView)findViewById(R.id.hotelListView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("Select hotel in "+getIntent().getStringExtra("name"));
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);
        getHotels(getIntent().getExtras().getLong("id"));
        hotelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HotelSelectActivity.this, RoomSelectActivity.class);
                intent.putExtra("name",((Hotel)parent.getItemAtPosition(position)).getName());
                Bundle bundle = new Bundle();
                bundle.putLong("id", ((Hotel) parent.getItemAtPosition(position)).getId());
                intent.putExtras(bundle);
                startActivity(intent);
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
            public void success(List<Hotel> h, Response response) {
                ArrayAdapter<Hotel> adapter = new HotelListAdapter(getApplicationContext(),h);
                hotelListView.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
