package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.Adapter.RecyclerAdapter_proclasscheck;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.InClassHowVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Proclasscheck extends Activity {

    private RecyclerView recyclerView;
    private RecyclerAdapter_proclasscheck adapter;
    private View parent_view;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Gson gson = new Gson();
    String name;
    String title;
    String classname;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.proclasscheck);
        parent_view = findViewById(android.R.id.content);
        Intent intent = getIntent();
        name = intent.getStringExtra("name"); //req.getParameter("id")'
        classname = intent.getStringExtra("classname");
        imageUrl = intent.getStringExtra("imageUrl");
        Log.d("받은 이름 값",name);

        Proclasscheck.InnerTask task = new Proclasscheck.InnerTask();
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        task.execute(map);

    }

    private class InnerTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/proclasscheck"); //@RequestMapping url
            http.addOrReplace("name", name);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("JSON_RESULT", (String)o);
            try {
                jsonArray = new JSONArray(o.toString());
//                LinearLayout ll = (LinearLayout) findViewById(R.id.rv_recyclerview);

                int list_cnt = jsonArray.length();
                String[] gettitle = new String[list_cnt];
                String[] getstudent = new String[list_cnt];
                List<String> listTitle;
                List<String> listStudent;

                recyclerView = findViewById(R.id.rv_recyclerview);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Proclasscheck.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new RecyclerAdapter_proclasscheck();
                recyclerView.setAdapter(adapter);

                for (int i = 0; i < jsonArray.length(); ++i) {

                    // Extract values from JSON row:
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.e("JsonObject", "" + jsonObject);

                    gettitle[i] = jsonObject.getString("co_name");
                    getstudent[i] = jsonObject.getString("student");
                    Log.e("JSON Object", jsonObject + "");
                    Log.e("JsonParsing", gettitle[i] + "," + getstudent[i]);
                    title = gettitle[i];

                    listTitle = Arrays.asList(gettitle[i]);
                    listStudent = Arrays.asList(getstudent[i]);

                    for (int j = 0; j < listTitle.size(); j++) {
                        // 각 List의 값들을 data 객체에 set 해줍니다.
                        InClassHowVO data = new InClassHowVO();
                        data.setCo_name(listTitle.get(j));
                        data.setStudent(listStudent.get(j));

                        // 각 값이 들어간 data를 adapter에 추가합니다.
                        adapter.addItem(data);
                    }

                    Log.e("강의명", String.valueOf(listTitle));

                    // adapter의 값이 변경되었다는 것을 알려줍니다.
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView name1 = (TextView) findViewById(R.id.mainname);
            TextView className = (TextView) findViewById(R.id.className);
            ImageView imageView =  (ImageView) findViewById(R.id.mainstuimg);
            name1.setText(name + " 님");
            className.setText(classname);
            Glide.with(Proclasscheck.this).load(imageUrl).into(imageView);
        }
    }
}