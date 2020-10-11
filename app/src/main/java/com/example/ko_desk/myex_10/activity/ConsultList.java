package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ko_desk.myex_10.Adapter.ConsultAdapter;
import com.example.ko_desk.myex_10.Adapter.RecyclerAdapter;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.ConsultVO;
import com.example.ko_desk.myex_10.vo.nfcVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConsultList extends Activity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private ConsultAdapter adapter;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Gson gson = new Gson();
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.consultlist);

        Intent intent = getIntent();
        name = intent.getStringExtra("name"); //req.getParameter("id")'
        Log.d("받은 이름 값",name);

        ConsultList.InnerTask task = new ConsultList.InnerTask();
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
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
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/consultList"); //@RequestMapping url
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
                String[] name = new String[list_cnt];
                String[] number = new String[list_cnt];
                String[] subject = new String[list_cnt];
                String[] type = new String[list_cnt];
                String[] exp = new String[list_cnt];
                String[] content = new String[list_cnt];
                String[] date = new String[list_cnt];
                List<String> listName;
                List<String> listNumber;
                List<String> listType;
                List<String> listExp;
                List<String> listDate;
                List<String> listSubject;
                List<String> listContent;

                recyclerView = findViewById(R.id.rv_recyclerview);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConsultList.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new ConsultAdapter();
                recyclerView.setAdapter(adapter);

                for (int i = 0; i < jsonArray.length(); ++i) {

                    // Extract values from JSON row:
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.e("JsonObject", "" + jsonObject);

                    name[i] = jsonObject.getString("stuName");
                    number[i] = jsonObject.getString("stuNumber");
                    type[i] = jsonObject.getString("consultType");
                    exp[i] = jsonObject.getString("consultExp");
                    date[i] = jsonObject.getString("date");
                    subject[i] = jsonObject.getString("subject");
                    content[i] = jsonObject.getString("content");

                    Log.e("JSON Object", jsonObject + "");
                    Log.e("JsonParsing", name[i] + "," + number[i] + "," + type[i] + "," + exp[i] + "," + date[i] + "," + subject[i] + "," + content[i]);

                    listName = Arrays.asList(name[i]);
                    listNumber = Arrays.asList(number[i]);
                    listType = Arrays.asList(type[i]);
                    listExp = Arrays.asList(exp[i]);
                    listDate = Arrays.asList(date[i]);
                    listSubject = Arrays.asList(subject[i]);
                    listContent = Arrays.asList(content[i]);

                    for (int j = 0; j < listName.size(); j++) {
                        // 각 List의 값들을 data 객체에 set 해줍니다.
                        ConsultVO consult = new ConsultVO();
                        consult.setStuName(listName.get(j));
                        consult.setStuNumber(listNumber.get(j));
                        consult.setConsultType(listType.get(j));
                        consult.setConsultExp(listExp.get(j));
                        consult.setDate(listDate.get(j));
                        consult.setSubject(listSubject.get(j));
                        consult.setContent(listContent.get(j));

                        // 각 값이 들어간 data를 adapter에 추가합니다.
                        adapter.addItem(consult);
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