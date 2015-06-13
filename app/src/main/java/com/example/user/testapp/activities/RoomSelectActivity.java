package com.example.user.testapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.testapp.R;
import com.example.user.testapp.REST.MarkAPI;
import com.example.user.testapp.model.Room;
import com.example.user.testapp.util.NaviDrawer;
import com.example.user.testapp.util.RoomListAdapter;
import com.example.user.testapp.util.ServerUrl;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RoomSelectActivity extends ActionBarActivity {
    ListView roomListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_select);

        roomListView=(ListView)findViewById(R.id.roomListView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("Select room in "+getIntent().getStringExtra("name"));
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);
        getRooms(getIntent().getExtras().getLong("id"));
    }

    public void getRooms(long hotel_id){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerUrl.BASE_SERVER_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.getRooms(hotel_id, new Callback<List<Room>>() {
            @Override
            public void success(List<Room> r, Response response) {
                ArrayAdapter<Room> adapter = new RoomListAdapter(getApplicationContext(),r);
                roomListView.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
