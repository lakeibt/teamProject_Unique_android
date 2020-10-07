package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.Adapter.RecyclerAdapter;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.nfcVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WorkCheck extends Activity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerAdapter adapter;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Gson gson = new Gson();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workcheck);

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'
        Log.d("받은 아이디 값",id);

        WorkCheck.InnerTask task = new WorkCheck.InnerTask();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        task.execute(map);

    }

    private class InnerTask extends AsyncTask {

        //MypageRecyAdapter adapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/workcheck"); //@RequestMapping url
            http.addOrReplace("id", id);

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
                String[] getdate = new String[list_cnt];
                String[] getintime = new String[list_cnt];
                String[] getouttime = new String[list_cnt];
                List<String> listDate;
                List<String> listIntime;
                List<String> listOuttime;

                recyclerView = findViewById(R.id.rv_recyclerview);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkCheck.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new RecyclerAdapter();
                recyclerView.setAdapter(adapter);

                for (int i = 0; i < jsonArray.length(); ++i) {

                    // Extract values from JSON row:
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.e("JsonObject", "" + jsonObject);

                    getdate[i] = jsonObject.getString("tagintime").substring(2,14);
                    getintime[i] = jsonObject.getString("tagintime").substring(15);
                    getouttime[i] = jsonObject.getString("tagouttime");
                    if(!getouttime[i].equals("null")){
                        getouttime[i] = jsonObject.getString("tagouttime").substring(15);
                    } else {
                        getouttime[i] = "미퇴근";
                    }

                    Log.e("JSON Object", jsonObject + "");
                    Log.e("JsonParsing", getdate[i] + "," + getintime[i] + "," + getouttime[i]);

                    listDate = Arrays.asList(getdate[i]);
                    listIntime = Arrays.asList(getintime[i]);
                    listOuttime = Arrays.asList(getouttime[i]);

                    for (int j = 0; j < listIntime.size(); j++) {
                        // 각 List의 값들을 data 객체에 set 해줍니다.
                        nfcVO data = new nfcVO();
                        data.setDate(listDate.get(j));
                        data.setTagintime(listIntime.get(j));
                        data.setTagouttime(listOuttime.get(j));

                        // 각 값이 들어간 data를 adapter에 추가합니다.
                        adapter.addItem(data);
                    }
                    Log.e("일자", String.valueOf(listDate));
                    Log.e("인타임", String.valueOf(listIntime));
                    Log.e("아웃타임", String.valueOf(listOuttime));

                    // adapter의 값이 변경되었다는 것을 알려줍니다.
                    adapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}