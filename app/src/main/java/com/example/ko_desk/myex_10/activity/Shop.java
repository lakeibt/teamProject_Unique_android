package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ko_desk.myex_10.Adapter.RecyclerAdapter_Shop;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.TableinfoVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop extends Activity {

    private RecyclerView recyclerView;
    private RecyclerAdapter_Shop adapter;
    private View parent_view;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Gson gson = new Gson();
    String id, name;
    String title;
    String classname;
    String imageUrl;
    Button btn_back , btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shop);
        parent_view = findViewById(android.R.id.content);

        Shop.InnerTask task = new Shop.InnerTask();
        Map<String, String> map = new HashMap<>();
//        map.put("id", id);
        task.execute(map);

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'
        name = intent.getStringExtra("name");

        btn_back = findViewById(R.id.btn_back);
        btn_insert = findViewById(R.id.btn_insert);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity2.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Shopnew.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

    }

    private class InnerTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/shoplist"); //@RequestMapping url
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
                String[] gettitle = new String[list_cnt];
                String[] getstudent = new String[list_cnt];
                String[] getnum = new String[list_cnt];
                String[] getid = new String[list_cnt];
                String[] getname = new String[list_cnt];
                List<String> listTitle;
                List<String> listStudent;
                List<String> listNum;
                List<String> listId;
                List<String> listName;

                recyclerView = findViewById(R.id.rv_recyclerview);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Shop.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new RecyclerAdapter_Shop();
                recyclerView.setAdapter(adapter);

                for (int i = 0; i < jsonArray.length(); ++i) {

                    // Extract values from JSON row:
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.e("JsonObject", "" + jsonObject);

                    gettitle[i] = jsonObject.getString("title");
                    getstudent[i] = jsonObject.getString("id");
                    getnum[i] = jsonObject.getString("num");
                    getid[i] = id;
                    getname[i] = name;
                    Log.e("JSON Object", jsonObject + "");
                    Log.e("JsonParsing", gettitle[i] + "," + getstudent[i]+","+getnum[i]+","+getid[i]+","+getname[i]);
                    title = gettitle[i];

                    listTitle = Arrays.asList(gettitle[i]);
                    listStudent = Arrays.asList(getstudent[i]);
                    listNum = Arrays.asList(getnum[i]);
                    listId = Arrays.asList(getid[i]);
                    listName = Arrays.asList(getname[i]);

                    for (int j = 0; j < listTitle.size(); j++) {
                        // 각 List의 값들을 data 객체에 set 해줍니다.
                        TableinfoVO data = new TableinfoVO();
                        data.setTitle(listTitle.get(j));
                        data.setName(listStudent.get(j));
                        data.setNum(Integer.parseInt(listNum.get(j)));
                        data.setId(listId.get(j));
                        data.setMyname(listName.get(j));

                        // 각 값이 들어간 data를 adapter에 추가합니다.
                        adapter.addItem(data);
                    }

                    // adapter의 값이 변경되었다는 것을 알려줍니다.
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}