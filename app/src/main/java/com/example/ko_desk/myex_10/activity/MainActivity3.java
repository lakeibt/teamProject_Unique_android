package com.example.ko_desk.myex_10.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.Data;
import com.example.ko_desk.myex_10.vo.FingerPrintVO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class MainActivity3 extends AppCompatActivity {

    Toolbar toolbar;
    MainActivity3.InnerTask task = null;
    String id;
    View parking;
    View workcheck;
    View fingerprint;
    View myinfo;
    View salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity3);
        setToolbar();

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'
        task = new MainActivity3.InnerTask();
        task.execute(id);

        parking = findViewById(R.id.parking);
        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Parking.class);
                startActivity(intent);
            }
        });

        workcheck = findViewById(R.id.workcheck);
        workcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), WorkCheck.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        myinfo = findViewById(R.id.myinfo);
        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Maninfo.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        fingerprint = findViewById(R.id.fingerprint);
        fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Unique = UUID.getDeviceId(null);

                Map<String, String> map = new HashMap<>();
                MainActivity3.checkTask check = new MainActivity3.checkTask();
                map.put("uuid", Unique);
                check.execute(map);

            }
        });

        salary = findViewById(R.id.salary);
        salary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Salary.class);
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
                        Intent i = new Intent(MainActivity3.this, SignInActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
            Log.d("JSON_RESULTALL", (String) o);
            Gson gson = new Gson();
            Data data = gson.fromJson((String) o, Data.class);
            try {
                TextView name = (TextView) findViewById(R.id.name);
                TextView className = (TextView) findViewById(R.id.className);
                ImageView imageView = (ImageView) findViewById(R.id.stuimg);
                name.setText(data.getData2() + " 님");
                className.setText(data.getData5() + " / " + data.getData3());
                String imageUrl = "" + Web.servletURL + "resources/img/profile_photo/admin/" + data.getData4();
                Glide.with(MainActivity3.this).load(imageUrl).into(imageView);
                Log.d("pic", "사진 = " + data.getData4());
                Log.d("JSON_RESULT", "이름 = " + data.getMember().get("member_name"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //키 확인 task
    public class checkTask extends AsyncTask<Map, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... map) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/android_bio_signCheck"); //스프링 url
            http.addAllParameters(map[0]);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            System.out.println("---" + body);

            return body;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            Log.d("JSON_RESULT", jsonData);
            Gson gson = new Gson();
            FingerPrintVO fp = gson.fromJson(jsonData, FingerPrintVO.class);
            String fp_id = fp.getId();
            String fp_pwd = fp.getPwd();
            String fp_uu = fp.getUuid();
            if(fp_pwd!=null){
            if (fp_pwd.length() == 60 && fp_uu.length() == 19) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
                builder.setTitle("알림");
                builder.setMessage("이미 지문이 등록 되었습니다.");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity3.this, MainActivity3.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        })
                        .show();
            }
            } else{
                Intent intent = new Intent(MainActivity3.this, AuthFingerprintActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        }
    }
}
