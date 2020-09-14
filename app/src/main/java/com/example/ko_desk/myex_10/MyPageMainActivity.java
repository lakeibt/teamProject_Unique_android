package com.example.ko_desk.myex_10;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by psn on 2018-02-07.
 */

public class MyPageMainActivity extends AppCompatActivity {

    InnerTask task = null;
    String id;

    /*@Override
    protected void onResume() {
        super.onResume();

        if(task != null) {
            task.cancel(true);
            task = null;

            task = new InnerTask();
            task.execute(id);
        }
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'

        task = new InnerTask();
        task.execute(id);

        actionBar();
    }

    private void actionBar() {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.custom_bar, null);
//        TextView tv_bar = v.findViewById(R.id.tv_bar);
//        tv_bar.setText("MyProject");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(v);
    }

    private class InnerTask extends AsyncTask {

        //MypageRecyAdapter adapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "androidMyPageMain"); //@RequestMapping url
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
                TextView name = (TextView) findViewById(R.id.tv_name);
                name.setText(data.getData1() + "님");

                Log.d("JSON_RESULT", "이름 = " + data.getMember().get("member_name"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}