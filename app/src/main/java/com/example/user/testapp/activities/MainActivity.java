package com.example.user.testapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.testapp.util.NaviDrawer;
import com.example.user.testapp.R;
import com.example.user.testapp.util.MarkListAdapter;
import com.example.user.testapp.model.Mark;
import com.example.user.testapp.REST.MarkAPI;
import com.example.user.testapp.util.ServerUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {
    String BASE_URL = ServerUrl.BASE_SERVER_URL;
    //String BASE_URL = "http://192.168.0.19:8080";
    //String BASE_URL = "http://10.0.3.2:8080";
    Button loadBtn;
    ListView listView;

    ArrayAdapter<Mark> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadBtn =(Button)findViewById(R.id.loadBtn);
        listView =(ListView)findViewById(R.id.listView);

        //adapter = new MarkListAdapter(getApplicationContext(),getMarksFromPreferenses());

        adapter = new MarkListAdapter(getApplicationContext(),getMarks());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, MarkDetailActivity.class);
                intent.putExtra("name",((Mark)parent.getItemAtPosition(position)).getName());
                intent.putExtra("description",((Mark)parent.getItemAtPosition(position)).getDescription());
                Bundle bundle = new Bundle();
                bundle.putLong("id",((Mark) parent.getItemAtPosition(position)).getId());
                bundle.putDouble("lat",((Mark)parent.getItemAtPosition(position)).getLat());
                bundle.putDouble("long",((Mark)parent.getItemAtPosition(position)).getLong());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("TURISTICHESKAYA DEYTEL`NOST");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);
    }

    public void onLoadMarksClick(View view){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);

        markAPI.getMarks(new Callback<List<Mark>>() {
            @Override
            public void success(List<Mark> marks, Response response) {
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                String value = gson.toJson(marks);

                Toast.makeText(getApplicationContext(),"marks loaded", Toast.LENGTH_SHORT).show();
                //
                //
                saveList(marks);

                adapter = new MarkListAdapter(getApplicationContext(),getMarks());
                listView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveList(List<Mark> list) {

        try {
            FileOutputStream fOut = openFileOutput("marks",MODE_WORLD_READABLE);
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            gson.toJson(list);
            fOut.write(gson.toJson(list).getBytes());
            fOut.close();
            //Toast.makeText(getBaseContext(),"file saved",Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {

            e.printStackTrace();
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
            List<Mark> marks ;
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            marks = gson.fromJson(temp, listType);

            return marks;
        }
        catch(Exception e){

        }
        List<Mark> m = new ArrayList<Mark>();
        m.add(new Mark("", "", 0, 0,0));
        listView.setVisibility(View.INVISIBLE);
        return m;
    }
    private List<Mark> getMarksFromPreferenses(){
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        String value = sPref.getString("list", null);
        Type listType = new TypeToken<List<Mark>>() {}.getType();
        List<Mark> marks ;
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        marks = gson.fromJson(value, listType);
        return marks;
    }
    private AccountHeader.Result createAccontHeader() {
        IProfile profile = new ProfileDrawerItem()
                .withEmail("kasuhanov@gmail.com")
                .withName("Cyril Suhanov");
        return new AccountHeader()

                    .withActivity(this)
                    .addProfiles(profile)
                    .build();
    }


}
