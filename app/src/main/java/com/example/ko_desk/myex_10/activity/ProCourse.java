package com.example.ko_desk.myex_10.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;

import java.util.HashMap;
import java.util.Map;

public class ProCourse extends AppCompatActivity {

    String id;
    Button btnback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_course);


        btnback = (Button) findViewById(R.id.btn_back);

        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ProCourse.this, MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }

    private class InnerTask extends AsyncTask<Map, Integer, String> {

        @Override
        protected String doInBackground(Map... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/procourse"); //@RequestMapping url
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }
    }
}