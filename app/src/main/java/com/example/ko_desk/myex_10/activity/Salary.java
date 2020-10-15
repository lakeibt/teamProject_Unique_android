package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.Adapter.RecyclerAdapter_studentCourse;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.StudentVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Salary extends Activity {
    JSONObject jsonObject;
    String id;
    Spinner spinner1, spinner2;

    Button btnsalary, btnback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salary);

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'

        Salary.InnerTask task = new Salary.InnerTask();
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("year", "2020");
        map.put("month", "1");

        task.execute(map);

        btnsalary = (Button) findViewById(R.id.btn_salary);
        btnback = (Button) findViewById(R.id.btn_back);

        btnsalary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                spinner1 = (Spinner)findViewById(R.id.yearList);
                spinner2 = (Spinner)findViewById(R.id.monthList);
                String text1 = spinner1.getSelectedItem().toString();
                String text2 = spinner2.getSelectedItem().toString();

                Salary.InnerTask task = new Salary.InnerTask();
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("year", text1);
                map.put("month", text2);

                task.execute(map);
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Salary.this, MainActivity3.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        // Spinner
        Spinner yearSpinner = (Spinner)findViewById(R.id.yearList);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.yearList, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        Spinner monthSpinner = (Spinner)findViewById(R.id.monthList);
        ArrayAdapter monthAdaper = ArrayAdapter.createFromResource(this, R.array.monthList, android.R.layout.simple_spinner_item);
        monthAdaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdaper);
    }

    private class InnerTask extends AsyncTask<Map, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/Salary"); //@RequestMapping url
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(String o) {
            Log.d("JSON_RESULT : SALARY :", o);
            Gson gson = new Gson();
            Map data = gson.fromJson(o, Map.class);

            try {
                TextView name = (TextView) findViewById(R.id.name);
                TextView className = (TextView) findViewById(R.id.className);
                ImageView imageView =  (ImageView) findViewById(R.id.stuimg);
                TextView month_salary = (TextView) findViewById(R.id.month_salay);
                TextView food_salary = (TextView) findViewById(R.id.food_salay);
                TextView car_salary = (TextView) findViewById(R.id.car_salay);
                TextView total = (TextView) findViewById(R.id.total_salay);

                name.setText(data.get("NAME") + " 님");
                className.setText(String.valueOf(data.get("DEPART")));
                String imageUrl = "" + Web.servletURL + "resources/img/profile_photo/admin/"+data.get("PHOTO");
                Glide.with(Salary.this).load(imageUrl).into(imageView);
                month_salary.setText(String.valueOf(data.get("PAY")));
                food_salary.setText(String.valueOf(data.get("MEAL")));
                car_salary.setText(String.valueOf(data.get("CAR")));
                total.setText(String.valueOf(data.get("TOTAL")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}