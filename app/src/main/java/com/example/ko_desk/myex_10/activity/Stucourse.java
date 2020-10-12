package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.Adapter.RecyclerAdapter_studentCourse;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stucourse extends Activity {
    private RecyclerView recyclerView;
    private RecyclerAdapter_studentCourse adapter;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String id;
    Spinner spinner1, spinner2, spinner3;

    Button btnselect, btnback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stucourse);

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'

        Stucourse.InnerTask task = new Stucourse.InnerTask();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("year", "2020");
        map.put("semester", "2학기");
        map.put("day", "월요일");

        task.execute(map);

        btnselect = (Button) findViewById(R.id.btn_select);
        btnback = (Button) findViewById(R.id.btn_back);

        btnselect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                spinner1 = (Spinner)findViewById(R.id.selectYear);
                spinner2 = (Spinner)findViewById(R.id.selectSemester);
                spinner3 = (Spinner)findViewById(R.id.selectDay);
                String text1 = spinner1.getSelectedItem().toString();
                String text2 = spinner2.getSelectedItem().toString();
                String text3 = spinner3.getSelectedItem().toString();

                Stucourse.InnerTask task = new Stucourse.InnerTask();
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("year", text1);
                map.put("semester", text2);
                map.put("day", text3);

                task.execute(map);
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Stucourse.this, MainActivity2.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        // Spinner
        Spinner yearSpinner = (Spinner)findViewById(R.id.selectYear);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        Spinner semesterSpinner = (Spinner)findViewById(R.id.selectSemester);
        ArrayAdapter semesterAdapter = ArrayAdapter.createFromResource(this, R.array.semester, android.R.layout.simple_spinner_item);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(semesterAdapter);

        Spinner daySpinner = (Spinner)findViewById(R.id.selectDay);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
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
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/stuCourse"); //@RequestMapping url
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(String o) {
            JSONObject jsonObject = null;
            Log.d("JSON_RESULT : STUDENT COURSE :", o);

            ImageView imageView =  (ImageView) findViewById(R.id.stuimg);
            TextView name = findViewById(R.id.name);
            TextView major = findViewById(R.id.className);

            String imageUrl = "";

            try {
                jsonArray = new JSONArray(o);

                int length = jsonArray.length();
                String[] course = new String[length];
                String[] professor = new String[length];
                String[] day = new String[length];
                String[] time = new String[length];
                String[] grade = new String[length];

                List<String> listCourse = null;
                List<String> listProfessor = null;
                List<String> listDay = null;
                List<String> listTime = null;
                List<String> listGrade = null;

                recyclerView = findViewById(R.id.rv_recyclerview);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Stucourse.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new RecyclerAdapter_studentCourse();
                recyclerView.setAdapter(adapter);

                for(int i = 0; i < length; i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    imageUrl = "" + Web.servletURL + "resources/img/profile_photo/student/"+jsonObject.getString("PHOTO");
                    Glide.with(Stucourse.this).load(imageUrl).into(imageView);
                    name.setText(jsonObject.getString("NAME"));
                    major.setText(jsonObject.getString("M_NAME"));

                    course[i] = jsonObject.getString("CO_NAME");
                    professor[i] = jsonObject.getString("P_NAME");
                    day[i] = jsonObject.getString("DAY");
                    time[i] = jsonObject.getString("TIME");
                    grade[i] = jsonObject.getString("GRADE");

                    listCourse = Arrays.asList(course[i]);
                    listProfessor = Arrays.asList(professor[i]);
                    listDay = Arrays.asList(day[i]);
                    listTime = Arrays.asList(time[i]);
                    listGrade = Arrays.asList(grade[i]);

                    for(int j = 0; j < listCourse.size(); j++) {
                        Map<String, String> data = new HashMap<>();
                        data.put("course", listCourse.get(j));
                        data.put("professor", listProfessor.get(j));
                        data.put("day", listDay.get(j));
                        data.put("time", listTime.get(j));
                        data.put("grade", listGrade.get(j));

                        adapter.addItem(data);
                    }
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}