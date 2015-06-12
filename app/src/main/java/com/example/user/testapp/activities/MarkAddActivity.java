package com.example.user.testapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.testapp.model.MarkImage;
import com.example.user.testapp.util.NaviDrawer;
import com.example.user.testapp.R;
import com.example.user.testapp.model.Mark;
import com.example.user.testapp.REST.MarkAPI;
import com.example.user.testapp.util.RequestCode;
import com.example.user.testapp.util.ServerUrl;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedFile;


public class MarkAddActivity extends ActionBarActivity {
    //String BASE_URL = "http://10.0.3.2:8080";
    String BASE_URL = ServerUrl.BASE_SERVER_URL;
    String encodedString;
    byte[] bytes;
    private EditText markEdit;
    private EditText latEdit;
    private EditText longEdit;
    private EditText descEdit;
    private Button sendBtn;
    private Button selectCoordBtn;
    private Button loadImgBtn;
    private ProgressBar progressBar;
    private Button b;
    DialogFragment dialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_add);

        markEdit = (EditText)findViewById(R.id.markEditText);
        latEdit = (EditText)findViewById(R.id.latEditText);
        longEdit = (EditText)findViewById(R.id.longEditText);
        descEdit = (EditText)findViewById(R.id.descEditText);
        sendBtn = (Button)findViewById(R.id.sendBtn);
        selectCoordBtn = (Button)findViewById(R.id.currLatLongBtn);
        loadImgBtn = (Button)findViewById(R.id.loadImgBtn);
        progressBar = (ProgressBar)findViewById(R.id.progBar);

        progressBar.setVisibility(View.INVISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            toolbar.setTitle("Add Mark");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        }
        NaviDrawer nd = new NaviDrawer(this,toolbar);
        dialogFragment=new LoginDialogFragment();
        if(!isLogged()){
            dialogFragment.show(getFragmentManager(), "dialogFragment");

        }
        b=(Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String LatSTR = latEdit.getText().toString();
                String LongSTR = longEdit.getText().toString();
                Double Lat;
                Double Long;
                if(LatSTR == null || LatSTR.isEmpty()) {
                    Lat = 0.0;
                } else {
                    Lat = Double.parseDouble(LatSTR);
                }
                if(LongSTR == null || LongSTR.isEmpty()) {
                    Long = 0.0;
                } else {
                    Long = Double.parseDouble(LongSTR);
                }
                Mark testMark = new Mark(markEdit.getText().toString(),descEdit.getText().toString(),
                        Long,Lat,0);
                sendImage(testMark,bytes);
            }
        });
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
                /*AlertDialog.Builder ad;
                ad = new AlertDialog.Builder(MarkAddActivity.this);
                ad.setTitle("You're not logged in");  // заголовок
                ad.setMessage("Only logged users can add marks."); // сообщение
                ad.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        //Toast.makeText(getApplicationContext(), "Вы сделали правильный выбор",
                         //       Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MarkAddActivity.this,AccountActivity.class);
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
                return false;
            }else {
                return true;
            }

        }
        catch(Exception e){
            return false;
        }

    }
    public void onSendButtonClick(View view){
        progressBar.setVisibility(View.VISIBLE);
        String LatSTR = latEdit.getText().toString();
        String LongSTR = longEdit.getText().toString();
        Double Lat;
        Double Long;
        if(LatSTR == null || LatSTR.isEmpty()) {
            Lat = 0.0;
        } else {
            Lat = Double.parseDouble(LatSTR);
        }
        if(LongSTR == null || LongSTR.isEmpty()) {
            Long = 0.0;
        } else {
            Long = Double.parseDouble(LongSTR);
        }
        //sUsername = usernameEditText.getText().toString();
        if (markEdit.getText().toString().matches("")) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "You did not enter a mark name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descEdit.getText().toString().matches("")) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "You did not enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        final Mark testMark = new Mark(markEdit.getText().toString(),descEdit.getText().toString(),
                Long,Lat,0);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        markAPI.createMark(testMark, new Callback<Mark>() {
            @Override
            public void success(Mark mark, Response response) {
                Toast.makeText(getApplicationContext(), "Mark added", Toast.LENGTH_SHORT).show();
                latEdit.setText("");
                longEdit.setText("");
                markEdit.setText("");
                descEdit.setText("");
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void sendImage(Mark mark, byte[] image){
        progressBar.setVisibility(View.VISIBLE);

        //MarkImage markImage = new MarkImage(mark.getLat(),mark.getLong(),image);


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)

                .build();
        MarkAPI markAPI = restAdapter.create(MarkAPI.class);
        TypedFile typedFile = new TypedFile("image/jpg", new File(encodedString));
        markAPI.addImage(typedFile, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast.makeText(getApplicationContext(),"succsess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onSelectCoordClick(View view){
        Intent intent = new Intent(MarkAddActivity.this,LatLongSelectActivity.class);
        startActivityForResult(intent, RequestCode.REQUEST_CODE_SELECT_LATLONG);
    }
    public void onLoadImageClick(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, RequestCode.REQUEST_CODE_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((resultCode == RESULT_OK)&&(requestCode==RequestCode.REQUEST_CODE_SELECT_LATLONG)){
            //Toast.makeText(this, String.valueOf((double) data.getExtras().getDouble("lat")), Toast.LENGTH_SHORT).show();
            latEdit.setText(String.valueOf((double) data.getExtras().getDouble("lat")));
            longEdit.setText(String.valueOf((double) data.getExtras().getDouble("long")));
        }
        if((resultCode == RESULT_OK)&&(requestCode==RequestCode.REQUEST_CODE_LOAD_IMAGE)&& null != data){

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
             bytes = baos.toByteArray();
            encodedString = imgDecodableString;
            //encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
            //Toast.makeText(this, selectedImage.toString(),Toast.LENGTH_LONG).show();

            //ImageView imgView = (ImageView) findViewById(R.id.imgView);
            // Set the Image in ImageView after decoding the String
            //imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
        }
        if((resultCode == RESULT_OK)&&(requestCode==RequestCode.REQUEST_CODE_LOAD_IMAGE)&& null == data){
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
    public class LoginDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You must log in to post comments")
                    .setCancelable(true)
                    .setPositiveButton("login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(MarkAddActivity.this, AccountActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        @Override
        public void onDestroy() {
            finish();
            super.onDestroy();
        }
    }
}
