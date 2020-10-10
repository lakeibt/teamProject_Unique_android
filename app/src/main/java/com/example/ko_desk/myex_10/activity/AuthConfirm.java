package com.example.ko_desk.myex_10.activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.FingerPrintVO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AuthConfirm extends AppCompatActivity {

    String id;
    String Unique;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Unique = UUID.getDeviceId(null);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        Map<String, String> map = new HashMap<>();
        checkTask check = new checkTask();
        map.put("id", id);
        map.put("uuid", Unique);
        check.execute(map);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint);

        // activity_main.xml에서 정의한 LinearLayout 객체 할당
        LinearLayout inflatedLayout = (LinearLayout)findViewById(R.id.inflatedLayout);
        // LayoutInflater 객체 생성
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
        inflater.inflate(R.layout.fingerprint, inflatedLayout);
    }

    //키 확인 task
    public class checkTask extends AsyncTask<Map, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... map) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/android_bio_check"); //스프링 url
            http.addAllParameters(map[0]);

            System.out.println("[Map > ID] : + "+map[0].get("id"));
            System.out.println("[Map > UU] : + "+map[0].get("uuid"));

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            System.out.println("---" + body);

            return body;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            Gson gson = new Gson();
            FingerPrintVO fp = gson.fromJson(jsonData, FingerPrintVO.class);
            System.out.println("아이디 : "+fp.getId());
            System.out.println("키값 : "+fp.getUuid());

            if (jsonData.length() > 0) {
                Toast.makeText(getApplicationContext(), "지문정보 로드 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), AuthFingerprintActivity.class);
                intent.putExtra("uuid", fp.getUuid());
                intent.putExtra("id", fp.getId());
                startActivity(intent);

            }else {
                Toast.makeText(getApplicationContext(), "지문정보 로드 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
}