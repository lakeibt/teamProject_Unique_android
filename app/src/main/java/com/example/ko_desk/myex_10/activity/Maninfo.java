package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.Data;
import com.example.ko_desk.myex_10.vo.Manager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Maninfo extends Activity {

    EditText tel, email, address, address2;
    String id;

    Button btnInfoSave;
    Button btnback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personinfo3);

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'

        Maninfo.InnerTask task = new Maninfo.InnerTask();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        task.execute(map);

        tel = (EditText) findViewById(R.id.tel);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);
        address2 = (EditText) findViewById(R.id.address2);
        btnInfoSave = (Button) findViewById(R.id.btn_infosave);
        btnback = (Button) findViewById(R.id.btn_back);

        btnInfoSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Maninfo.InnerTask2 task = new Maninfo.InnerTask2();
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("tel", tel.getText().toString());
                map.put("email", email.getText().toString());
                map.put("address", address.getText().toString());
                map.put("address2", address2.getText().toString());
                task.execute(map);
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Maninfo.this, MainActivity3.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private class InnerTask extends AsyncTask<Map, Integer, String> {

        //MypageRecyAdapter adapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/maninfo"); //@RequestMapping url
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
            Manager data = gson.fromJson(o, Manager.class);

            try {
                TextView name = (TextView) findViewById(R.id.mainname);
                TextView className = (TextView) findViewById(R.id.className);
                ImageView imageView =  (ImageView) findViewById(R.id.mainstuimg);
                TextView id = (TextView) findViewById(R.id.id);
                EditText tel = (EditText) findViewById(R.id.tel);
                EditText email = (EditText) findViewById(R.id.email);
                EditText address = (EditText) findViewById(R.id.address);
                EditText address2 = (EditText) findViewById(R.id.address2);

                name.setText(data.getName() + " 님");
                className.setText(data.getDepart() +" / "+ data.getRank());
                String imageUrl = "" + Web.servletURL + "resources/img/profile_photo/admin/"+data.getPhoto();
                Glide.with(Maninfo.this).load(imageUrl).into(imageView);
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

        //MypageRecyAdapter adapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/maninfosave"); //@RequestMapping url
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
            Intent intent = new Intent(Maninfo.this, Maninfo.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
}