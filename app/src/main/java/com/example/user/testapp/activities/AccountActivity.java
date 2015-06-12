package com.example.user.testapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.testapp.R;
import com.example.user.testapp.REST.MarkAPI;
import com.example.user.testapp.model.User;
import com.example.user.testapp.util.NaviDrawer;
import com.example.user.testapp.util.ServerUrl;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountActivity extends ActionBarActivity {
    EditText loginEdit;
    EditText passEdit;
    Button loginBtn;
    Button logoutBtn;
    TextView loggedTxt;
    //String BASE_URL = "http://10.0.3.2:8080";
    String BASE_URL = ServerUrl.BASE_SERVER_URL;
    String username="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        loggedTxt=(TextView)findViewById(R.id.logedTxt);
        loginBtn=(Button)findViewById(R.id.log_inBtn);
        logoutBtn=(Button)findViewById(R.id.logoutBtn);
        passEdit=(EditText)findViewById(R.id.passEdit);
        loginEdit=(EditText)findViewById(R.id.loginEdit);

        if(isLogged()){
            passEdit.setVisibility(View.INVISIBLE);
            loginEdit.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
        }else {
            loggedTxt.setVisibility(View.INVISIBLE);
            logoutBtn.setVisibility(View.INVISIBLE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);
    }

    private boolean isLogged(){
        try{
            FileInputStream fin = openFileInput("login");
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }

            if(temp.equals("")){
                return false;
            }else {
                loggedTxt.setText("You're logged as "+temp);
                return true;
            }

        }
        catch(Exception e){
            return false;
        }

    }
    private void saveLogin(String login) {

        try {
            FileOutputStream fOut = openFileOutput("login", MODE_WORLD_READABLE);

            fOut.write(login.getBytes());
            fOut.close();
            //Toast.makeText(getBaseContext(),"file saved",Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {

            e.printStackTrace();
        }
    }
    public void onLogInBtnClick(View view){

        if (loginEdit.getText().toString().matches("")) {
            Toast.makeText(this, "please enter your login", Toast.LENGTH_SHORT).show();
            return;
        }
        username=loginEdit.getText().toString();
        User user =new User(loginEdit.getText().toString(),passEdit.getText().toString());
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);

        markAPI.logIn(user, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                //Toast.makeText(getApplicationContext(), String.valueOf(response.getBody().length()), Toast.LENGTH_SHORT).show();
                if(response.getBody().length()==6){
                    saveLogin(username);
                    loggedTxt.setText("You're logged as " + username);
                    passEdit.setVisibility(View.INVISIBLE);
                    loginEdit.setVisibility(View.INVISIBLE);
                    loginBtn.setVisibility(View.INVISIBLE);

                    loggedTxt.setVisibility(View.VISIBLE);
                    logoutBtn.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(getApplicationContext(), "Login or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onLogOutBtnClick(View view){
        saveLogin("");
        passEdit.setVisibility(View.VISIBLE);
        loginEdit.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.VISIBLE);

        loggedTxt.setVisibility(View.INVISIBLE);
        logoutBtn.setVisibility(View.INVISIBLE);
    }
}
