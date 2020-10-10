package com.example.ko_desk.myex_10.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.Data;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    MainActivity.InnerTask task = null;
    View proinfo;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activitypro);
        setToolbar();

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'

        task = new MainActivity.InnerTask();
        task.execute(id);

        proinfo = findViewById(R.id.stuinfo);
        proinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Proinfo.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        new AlertDialog.Builder(this).setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(MainActivity.this, SignInActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();

        if (id == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class InnerTask extends AsyncTask {

        //MypageRecyAdapter adapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/androidMyPageMain"); //@RequestMapping url
            http.addOrReplace("id", (String) objects[0]);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("JSON_RESULT", (String) o);
            Gson gson = new Gson();
            Data data = gson.fromJson((String) o, Data.class);
            try {
                TextView name = (TextView) findViewById(R.id.name);
                TextView className = (TextView) findViewById(R.id.className);
                ImageView imageView =  (ImageView) findViewById(R.id.stuimg);
                name.setText(data.getData2() + " 님");
                className.setText(data.getData5());
                String imageUrl = "http://choihyun7000.synology.me/profile_photo/professor/"+data.getData4();
                Glide.with(MainActivity.this).load(imageUrl).into(imageView);
                Log.d("pic", "사진 = " + data.getData4());
                Log.d("JSON_RESULT", "이름 = " + data.getData2());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
