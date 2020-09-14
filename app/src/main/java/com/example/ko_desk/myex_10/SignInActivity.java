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
            if(s.length() > 0) {
                Gson gson = new Gson();
                MemberVO m = gson.fromJson(s, MemberVO.class);

                System.out.println("111111111111111111"+m.getAuthority());
                m.setAuthority("ROLE_ADMIN");
                System.out.println("222222222222222222"+m.getUserId());
                System.out.println("333333333333333333"+m.getUsername());
                if (m.getUserId() != null && m.getAuthority() == "ROLE_STUDENT") {
                    // 페이지 이동
                    Intent intent = new Intent(SignInActivity.this, MainActivity2.class);
                    intent.putExtra("id", m.getUserId());
                    startActivity(intent);
                } else if (m.getAuthority() != "ROLE_STUDENT") {
                    Toast.makeText(getApplicationContext(), "회원 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "가입 인증이 필요한 회원입니다.", Toast.LENGTH_SHORT).show();
                }

                if (m.getUserId() != null && m.getAuthority() == "ROLE_ADMIN") {
                    // 페이지 이동
                    Intent intent = new Intent(SignInActivity.this, MainActivity3.class);
                    intent.putExtra("id", m.getUserId());
                    startActivity(intent);
                } else if (m.getAuthority() != "ROLE_ADMIN") {
                    Toast.makeText(getApplicationContext(), "회원 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "가입 인증이 필요한 회원입니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
