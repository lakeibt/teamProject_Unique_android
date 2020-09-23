package com.example.ko_desk.myex_10;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by psn on 2018-01-18.
 */

public class SignInActivity extends AppCompatActivity {

    EditText edtId, edtPwd;
    Button btnSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // 엑션바 삭제 시작
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // 엑션바 삭제 끝

        edtId = (EditText) findViewById(R.id.edt_id);
        edtPwd = (EditText) findViewById(R.id.edt_pwd);
        btnSignIn = (Button) findViewById(R.id.btn_signin);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                InnerTask task = new InnerTask();
                Map<String, String> map = new HashMap<>();
                map.put("id", edtId.getText().toString());
                map.put("pwd", edtPwd.getText().toString());
                task.execute(map);
            }
        });

        actionBar();
    }

    public void actionBar() {
        ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(R.layout.custom_bar);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.custom_bar, null);
//        TextView tv_bar = (TextView) v.findViewById(R.id.tv_bar);
//        tv_bar.setText("로그인");
        bar.setCustomView(v);
    }

    //각 Activity 마다 Task 작성
    public class InnerTask extends AsyncTask<Map, Integer, String> {

        //doInBackground 실행되기 이전에 동작
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //작업을 쓰레드로 처리
        @Override
        protected String doInBackground(Map... maps) {
            //HTTP 요청 준비
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "androidSignIn"); //스프링 url
            //파라미터 전송
            http.addAllParameters(maps[0]);

            //HTTP 요청 전송
            HttpClient post = http.create();
            post.request(); // jsp의 submit 역할

            String body = post.getBody(); //Web의 Controller에서 리턴한 값
            System.out.println("---" + body);
            return body;
        }

        //doInBackground 종료되면 동작
        /**
         * @param s : doInBackground에서 리턴한 body. JSON 데이터
         */
        @Override
        protected void onPostExecute(String s) {
            Log.d("JSON_RESULT", s);
            //JSON으로 받은 데이터를 VO Obejct로 바꿔준다.
            System.out.println("-----" + s);


            if(s.length() > 12) {
                Gson gson = new Gson();
                String go = Character.toString(s.charAt(20));

                try {
                    JSONObject job = new JSONObject(s);
                    String pass = job.getString("id").substring(0,1);

                    if(pass.equals("s")){

                        StudentVO st = gson.fromJson(s, StudentVO.class);

                        System.out.println("아이디 : "+st.getId());
                        System.out.println("비밀번호 : "+st.getPwd());

                        if (st.getId() != null && st.getId().substring(0,1).equals("s")) {
                            // 페이지 이동
                            Intent intent = new Intent(SignInActivity.this, MainActivity2.class);
                            intent.putExtra("id", st.getId());
                            startActivity(intent);
                        }

                        if (st.getId() != null && st.getId().substring(0,1).equals("a")) {
                            // 페이지 이동
                            Intent intent = new Intent(SignInActivity.this, MainActivity3.class);
                            intent.putExtra("id", st.getId());
                            startActivity(intent);
                        }
                    } else if (pass.equals("a")) {
                        Manager ad = gson.fromJson(s, Manager.class);

                        System.out.println("아이디 : " + ad.getId());
                        System.out.println("비밀번호 : " + ad.getPwd());

                        if (ad.getPwd() == null) {
                            Toast.makeText(getApplicationContext(), "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                        }

                        if (ad.getId() != null && ad.getId().substring(0, 1).equals("s")) {
                            // 페이지 이동
                            Intent intent = new Intent(SignInActivity.this, MainActivity2.class);
                            intent.putExtra("id", ad.getId());
                            startActivity(intent);
                        }

                        if (ad.getId() != null && ad.getId().substring(0, 1).equals("a")) {
                            // 페이지 이동
                            Intent intent = new Intent(SignInActivity.this, MainActivity3.class);
                            intent.putExtra("id", ad.getId());
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "아이디 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
