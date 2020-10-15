package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.StudentVO;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Shopnew extends Activity {

    private final int GET_GALLERY_IMAGE = 200;
    EditText tel, email, address, address2;
    String id;
    ImageView imageview;
    Button getphoto, btn_infosave, btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopnew);

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'

        Shopnew.InnerTask task = new Shopnew.InnerTask();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        task.execute(map);

        btn_infosave = findViewById(R.id.btn_infosave);
        btn_infosave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Shop.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Shop.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    @Override
    public void onBackPressed() { }

    private class InnerTask extends AsyncTask<Map, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/stuinfo"); //@RequestMapping url
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(String o) {
            Log.d("JSON_RESULT", o);
            Gson gson = new Gson();
            StudentVO data = gson.fromJson(o, StudentVO.class);

            try {
                TextView name = (TextView) findViewById(R.id.name);
                TextView className = (TextView) findViewById(R.id.className);
                ImageView imageView =  (ImageView) findViewById(R.id.stuimg);
                TextView grade = (TextView) findViewById(R.id.grade);
                TextView year = (TextView) findViewById(R.id.year);
                TextView birth = (TextView) findViewById(R.id.birth);
                TextView id = (TextView) findViewById(R.id.id);
                EditText tel = (EditText) findViewById(R.id.tel);
                EditText email = (EditText) findViewById(R.id.email);
                EditText address = (EditText) findViewById(R.id.address);
                EditText address2 = (EditText) findViewById(R.id.address2);

                name.setText(data.getName() + " 님");
                className.setText(data.getM_code());
                String imageUrl = "" + Web.servletURL + "resources/img/profile_photo/student/"+data.getPhoto();
                Glide.with(Shopnew.this).load(imageUrl).into(imageView);
                grade.setText(String.valueOf(data.getGrade()));
                year.setText(String.valueOf(data.getEntrancedate()));
                birth.setText(String.valueOf(data.getJumin1()));
                id.setText(data.getId());
                tel.setText(data.getTel());
                email.setText(data.getEmail());
                address.setText(data.getAddress());
                address2.setText(data.getDe_address());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class InnerTask2 extends AsyncTask<Map, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/stuinfosave"); //@RequestMapping url
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(String o) {
            Log.d("JSON_RESULT", o);

            Toast.makeText(getApplicationContext(), "수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Shopnew.this, Shopnew.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
}