package com.example.user.testapp.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.user.testapp.R;
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

import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class LatLongSelectActivity extends ActionBarActivity {
    private GoogleMap mMap;
    TextView txtLat;
    TextView txtLong;
    Double lati=0.0;
    Double longi=0.0;
    boolean drawn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lat_long_select);

        txtLat=(TextView)findViewById(R.id.txtLat);
        txtLong=(TextView)findViewById(R.id.txtLong);

        setUpMapIfNeeded();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("Select location");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                txtLat.setText("Lat: "+String.valueOf((double)marker.getPosition().latitude));
                txtLong.setText("Long: "+String.valueOf((double)marker.getPosition().longitude));
                lati=marker.getPosition().latitude;
                longi=marker.getPosition().longitude;
            }
        });
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
        //mMap.setMyLocationEnabled(true);
        List<Mark> marks;
        marks = getMarks();
        if(marks!=null){
            for (Mark mark:marks){
                mMap.addMarker(new MarkerOptions().position(new LatLng(mark.getLat(), mark.getLong()))
                        .title(mark.getName()));
            }
        }
        //LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        double lati=0.0;
        double longi=0.0;

        if(drawn==false) {
            Location bestLocation=getBestKnownLocation();
            if (bestLocation!=null){
                lati=bestLocation.getLatitude();
                longi=bestLocation.getLongitude();
            }


            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lati, longi))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title("New Mark")
                    .draggable(true));
            txtLat.setText("Lat: " + String.valueOf((double) lati));
            txtLong.setText("Long: " + String.valueOf((double) longi));
            drawn=true;
            if (bestLocation!=null) {
                CameraUpdate center =
                        CameraUpdateFactory.newLatLng(new LatLng(lati, longi));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }

        }
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);


       /* */

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",lati);
        bundle.putDouble("long", longi);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void onSelectPressed(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",lati);
        bundle.putDouble("long",longi);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
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
            List<Mark> marks ;
            marks = gson.fromJson(temp, listType);

            return marks;
        }
        catch(Exception e){

        }
        List<Mark> m = new ArrayList<Mark>();
        m.add(new Mark("", "", 0, 0, 0));
        return m;
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


}
