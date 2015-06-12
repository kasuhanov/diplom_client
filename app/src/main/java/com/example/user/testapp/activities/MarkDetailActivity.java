package com.example.user.testapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.testapp.REST.MarkAPI;
import com.example.user.testapp.model.Comment;
import com.example.user.testapp.model.Mark;
import com.example.user.testapp.model.MarkComments;

import com.example.user.testapp.util.CommentListAdapter;
import com.example.user.testapp.util.MarkListAdapter;
import com.example.user.testapp.util.NaviDrawer;
import com.example.user.testapp.R;
import com.example.user.testapp.util.ServerUrl;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


public class MarkDetailActivity extends ActionBarActivity {
    TextView markName;
    TextView markDesc;
    TextView commentTxt;
    Button showMapBtn;
    Button commBtn;
    EditText editComment;
    Button sendButton;
    Button showDirBtn;
    //String BASE_URL = "http://10.0.3.2:8080";
    String BASE_URL = ServerUrl.BASE_SERVER_URL;
    ListView commentsListView;
    DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_detail);

        dialogFragment=new LoginDialogFragment();

        markDesc=(TextView)findViewById(R.id.markDescText);
        markName=(TextView)findViewById(R.id.markNameText);
        showMapBtn=(Button)findViewById(R.id.showMapBtn);
        showDirBtn=(Button)findViewById(R.id.showDirBtn);
        //commentTxt=(TextView)findViewById(R.id.commentTxt);

        sendButton=(Button)findViewById(R.id.sendBtn);
        editComment=(EditText)findViewById(R.id.editComment);
        commentsListView=(ListView)findViewById(R.id.commentsListView);
        getComments();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);

        markName.setText(getIntent().getStringExtra("name"));
        //markDesc.setText(String.valueOf(getIntent().getExtras().getLong("id")));
        markDesc.setText(getIntent().getStringExtra("description"));
        //ArrayAdapter<MarkComments> adapter = new CommentListAdapter(getApplicationContext(),getComments());
        //commentsListView.setAdapter(adapter);
    }
    public void getComments(){
        //commentTxt.setText("");
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.getComments(getIntent().getExtras().getLong("id"), new Callback<List<MarkComments>>() {
            @Override
            public void success(List<MarkComments> s, Response response) {
                //Toast.makeText(getApplicationContext(),s.get(0).getComments().get(0), Toast.LENGTH_SHORT).show();
                String result="";
                //for (int i=0;i<s.get(0).getComments().size();i++){
                //    result+="   "+s.get(0).getUsers().get(i)+" :\n "+s.get(0).getComments().get(i)+"\n\n";
                //}
                //commentTxt.setText(result);
                //Toast.makeText(getApplicationContext(),s.get(0).toComennts().get(0).getComment(), Toast.LENGTH_SHORT).show();
                ArrayAdapter<Comment> adapter = new CommentListAdapter(getApplicationContext(),s.get(0).toComennts());
                commentsListView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(),"unable to load comments", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onShowMapBtnClick(View view){
        Intent intent = new Intent(MarkDetailActivity.this, MapActivity.class);
        intent.putExtra("from","detail");
        intent.putExtra("name", getIntent().getStringExtra("name"));
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",getIntent().getExtras().getDouble("lat"));
        bundle.putDouble("long", getIntent().getExtras().getDouble("long"));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void onShowDirectionClick(View view){
        Intent intent = new Intent(MarkDetailActivity.this, MapActivity.class);
        intent.putExtra("from","direction");
        intent.putExtra("name", getIntent().getStringExtra("name"));
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",getIntent().getExtras().getDouble("lat"));
        bundle.putDouble("long", getIntent().getExtras().getDouble("long"));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void onCommBtnClick(View view){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.getComments(getIntent().getExtras().getLong("id"), new Callback<List<MarkComments>>() {
            @Override
            public void success(List<MarkComments> s, Response response) {
                //Toast.makeText(getApplicationContext(),s.get(0).getComments().get(0), Toast.LENGTH_SHORT).show();
                String result = "";
                for (int i = 0; i < s.get(0).getComments().size(); i++) {
                    result += "   " + s.get(0).getUsers().get(i) + " :\n " + s.get(0).getComments().get(i) + "\n\n";
                }
                commentTxt.setText(result);
                //Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onSendCommentBtnClick(View view){
        String comment_text= editComment.getText().toString();
        String username =isLogged();
        if(username.equals("")){
            //if(!ServerUrl.BASE_SERVER_URL.equals(ServerUrl.BASE_SERVER_URL_GENY))
                dialogFragment.show(getFragmentManager(),"dialogFragment");
            return;
        }

        editComment.setText("");
        if (comment_text.matches("")) {
            //progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Write your comment", Toast.LENGTH_SHORT).show();
            return;
        }

        Comment body = new Comment(comment_text,getIntent().getExtras().getLong("id"),username);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.createComment(body, new Callback<JSONArray>() {
            @Override
            public void success(JSONArray s, Response response) {
                //Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
                getComments();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String isLogged(){
        try{
            FileInputStream fin = openFileInput("login");
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }

            if(temp.equals("")){
                //dialogFragment.show(getFragmentManager(),"dialogFragment");
               /* AlertDialog.Builder ad;
                ad = new AlertDialog.Builder(MarkDetailActivity.this);
                ad.setTitle("You're not logged in");  // заголовок
                ad.setMessage("Only logged users can add marks."); // сообщение
                ad.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        //Toast.makeText(getApplicationContext(), "Вы сделали правильный выбор",
                        //       Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MarkDetailActivity.this, AccountActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                ad.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        //Toast.makeText(getApplicationContext(), "Возможно вы правы", Toast.LENGTH_LONG)
                        //        .show();
                        finish();
                    }
                });
                ad.show();*/
                return "";
            }else {
                return temp;
            }

        }
        catch(Exception e){
            return "";
        }

    }
    public class LoginDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You must log in to post comments")
                    .setCancelable(false)
                    .setPositiveButton("login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(MarkDetailActivity.this, AccountActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
