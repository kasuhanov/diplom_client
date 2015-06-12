package com.example.user.testapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.user.testapp.R;
import com.example.user.testapp.direction.GoogleDirection;
import com.example.user.testapp.model.Mark;
import com.example.user.testapp.util.NaviDrawer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;

import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity {
    private GoogleMap mMap;
    List<Mark> marks ;
    LatLng start;
    LatLng end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);



        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String name = "";
                String desc = "";
                for (Mark mark : marks) {
                    if ((mark.getLat() == marker.getPosition().latitude) && (mark.getLong() == marker.getPosition().longitude)) {
                        name = mark.getName();
                        desc = mark.getDescription();
                    }
                }
                Intent intent = new Intent(MapActivity.this, MarkDetailActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("description", desc);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", marker.getPosition().latitude);
                bundle.putDouble("long", marker.getPosition().longitude);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private Location getBestKnownLocation(){
        LocationManager locationManager;
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            Log.d("l kn loc pror: , loc: ", provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                Log.d("found bestlocation: ",provider);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    private void setUpMap() {
        if(getIntent().getStringExtra("from").equals("detail")){
            double lati = getIntent().getExtras().getDouble("lat");
            double longi = getIntent().getExtras().getDouble("long");

            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(new LatLng(lati, longi));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(6);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }

        List<Mark> marks;
        marks = getMarks();
        if(marks!=null){
            for (Mark mark:marks){
                mMap.addMarker(new MarkerOptions().position(new LatLng(mark.getLat(), mark.getLong()))
                    .title(mark.getName()).snippet("click for more info"));
            }
        }
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        final Location bestLocation= getBestKnownLocation();

        if ((bestLocation!=null)&&(!getIntent().getStringExtra("from").equals("detail"))) {
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(6);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            if(getIntent().getStringExtra("from").equals("direction")){
                final double lati = getIntent().getExtras().getDouble("lat");
                final double longi = getIntent().getExtras().getDouble("long");

                GoogleDirection gd;
                gd = new GoogleDirection(this);
                start=new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
                end=new LatLng(lati,longi);
                gd.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
                    public void onResponse(String status, Document doc, GoogleDirection gd) {

                        mMap.addPolyline(gd.getPolyline(doc, 3, Color.RED));
                        mMap.addMarker(new MarkerOptions().position(start)
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN)));

                        mMap.addMarker(new MarkerOptions().position(end)
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN)));
                    }
                });
                gd.setLogging(true);
                gd.request(start, end, GoogleDirection.MODE_DRIVING);

            }
        }


    }

    private List<Mark> getMarks() {
        try{
            FileInputStream fin = openFileInput("marks");
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }

            Type listType = new TypeToken<List<Mark>>() {}.getType();


            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            marks = gson.fromJson(temp, listType);

            return marks;
        }
        catch(Exception e){

        }
        List<Mark> m = new ArrayList<Mark>();
        m.add(new Mark("", "", 0, 0,0));
        return m;
    }

}
