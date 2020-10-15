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
    EditText title, textarea;
    String id;
    ImageView imageview;
    Button getphoto, btn_infosave, btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopnew);

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'

        btn_infosave = findViewById(R.id.btn_infosave);
        btn_infosave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                title = findViewById(R.id.title);
                textarea = findViewById(R.id.textarea);
                Shopnew.InnerTask task = new Shopnew.InnerTask();
                Map<String, String> map = new HashMap<>();
                map.put("id",id);
                map.put("title", title.getText().toString());
                map.put("text", textarea.getText().toString());
                task.execute(map);

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
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/tableinfosave"); //@RequestMapping url
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(String o) {
            Log.d("JSON_RESULT", o);

            Intent intent = new Intent(getBaseContext(), Shop.class);
            intent.putExtra("id", id);
            startActivity(intent);

        }
    }
}