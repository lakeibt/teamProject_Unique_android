package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ko_desk.myex_10.Adapter.RecyclerAdapter_proclasscheck;
import com.example.ko_desk.myex_10.Adapter.RecyclerAdapter_proclasschecknext;
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
    private RecyclerView.Adapter mAdapter;
    private RecyclerAdapter_proclasscheck adapter;
    private RecyclerAdapter_proclasschecknext adapter2;
    private View parent_view;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Gson gson = new Gson();
    String name;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.proclasscheck);
        parent_view = findViewById(android.R.id.content);
        Intent intent = getIntent();
        name = intent.getStringExtra("name"); //req.getParameter("id")'
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
                    Proclasscheck.InnerTask2 task2 = new Proclasscheck.InnerTask2();
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("title", title);
                    task2.execute(map2);



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
        }
    }

    private class InnerTask2 extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/proclasschecknext"); //@RequestMapping url
            http.addOrReplace("title", title);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("JSON_RESULT2", (String)o);
            try {
                jsonArray = new JSONArray(o.toString());
//                LinearLayout ll = (LinearLayout) findViewById(R.id.rv_recyclerview);

                int list_cnt = jsonArray.length();
                String[] getStudent = new String[list_cnt];
                String[] getId = new String[list_cnt];
                String[] getM_name = new String[list_cnt];
                List<String> listStudent;
                List<String> listId;
                List<String> listM_name;

                recyclerView = findViewById(R.id.rv_recyclerview);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Proclasscheck.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter2 = new RecyclerAdapter_proclasschecknext();
                recyclerView.setAdapter(adapter2);

                for (int i = 0; i < jsonArray.length(); ++i) {

                    // Extract values from JSON row:
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.e("JsonObject2", "" + jsonObject);

                    getStudent[i] = jsonObject.getString("student");
                    getId[i] = jsonObject.getString("id");
                    getM_name[i] = jsonObject.getString("m_name");

                    Log.e("JSON Object2", jsonObject + "");
                    Log.e("JsonParsing2", getStudent[i] + "," + getId[i]+","+getM_name[i]);

                    listStudent = Arrays.asList(getStudent[i]);
                    listId = Arrays.asList(getId[i]);
                    listM_name = Arrays.asList(getM_name[i]);

                    for (int j = 0; j < listStudent.size(); j++) {
                        // 각 List의 값들을 data 객체에 set 해줍니다.
                        InClassHowVO data = new InClassHowVO();
                        data.setStudent(listStudent.get(j));
                        data.setId(listId.get(j));
                        data.setM_name(listM_name.get(j));

                    }
                    Log.e("학생2", String.valueOf(listStudent));
                    Log.e("학번2", String.valueOf(listId));
                    Log.e("학과2", String.valueOf(listM_name));

                    // adapter의 값이 변경되었다는 것을 알려줍니다.
                    adapter2.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}