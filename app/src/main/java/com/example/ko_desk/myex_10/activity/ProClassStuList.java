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


public class ProClassStuList extends Activity {

    private RecyclerView recyclerView;
    private RecyclerAdapter_proclasschecknext adapter;
    private View parent_view;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Gson gson = new Gson();
    String title;
    String student;
    String id;
    String m_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.proclasschecknext);
        parent_view = findViewById(android.R.id.content);
        Intent intent = getIntent();
        title = intent.getStringExtra("title"); //req.getParameter("id")'
        Log.d("받은 과목명 값",title);

        ProClassStuList.InnerTask task = new ProClassStuList.InnerTask();
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        task.execute(map);

    }

    private class InnerTask extends AsyncTask {

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
            Log.d("JSON_RESULT", (String)o);
            try {
                jsonArray = new JSONArray(o.toString());
//                LinearLayout ll = (LinearLayout) findViewById(R.id.rv_recyclerview);

                int list_cnt = jsonArray.length();
                String[] getstudent = new String[list_cnt];
                String[] getid = new String[list_cnt];
                String[] getm_name = new String[list_cnt];
                List<String> listStudent;
                List<String> listId;
                List<String> listM_name;

                recyclerView = findViewById(R.id.rv_recyclerview);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProClassStuList.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new RecyclerAdapter_proclasschecknext();
                recyclerView.setAdapter(adapter);

                for (int i = 0; i < jsonArray.length(); ++i) {

                    // Extract values from JSON row:
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.e("JsonObject", "" + jsonObject);

                    getstudent[i] = jsonObject.getString("student");
                    getid[i] = jsonObject.getString("id");
                    getm_name[i] = jsonObject.getString("m_name");
                    Log.e("JSON Object", jsonObject + "");
                    Log.e("JsonParsing", getstudent[i] + "," + getid[i]+","+getm_name[i]);

                    listStudent = Arrays.asList(getstudent[i]);
                    listId = Arrays.asList(getid[i]);
                    listM_name = Arrays.asList(getm_name[i]);

                    for (int j = 0; j < listStudent.size(); j++) {
                        // 각 List의 값들을 data 객체에 set 해줍니다.
                        InClassHowVO data = new InClassHowVO();
                        data.setStudent(listStudent.get(j));
                        data.setId(listId.get(j));
                        data.setM_name(listM_name.get(j));

                        // 각 값이 들어간 data를 adapter에 추가합니다.
                        adapter.addItem(data);
                    }

                    Log.e("학생명", String.valueOf(listStudent));

                    // adapter의 값이 변경되었다는 것을 알려줍니다.
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView title1 = (TextView) findViewById(R.id.title);
            title1.setText(title);
        }
    }
}