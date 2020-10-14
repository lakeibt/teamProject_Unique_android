package com.example.ko_desk.myex_10.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.FingerPrintVO;
import com.example.ko_desk.myex_10.vo.HumanVO;
import com.example.ko_desk.myex_10.vo.Manager;
import com.example.ko_desk.myex_10.vo.StudentVO;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by psn on 2018-01-18.
 */

public class SignInActivity extends AppCompatActivity {

    EditText edtId, edtPwd;
    Button btnSignIn;
    Button fingerPrint;
    String Unique;
    String fp_id;
    String fp_pwd;
    String fp_uu;
    BiometricPrompt.AuthenticationResult biocheck;
    private Executor executor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // 엑션바 삭제 시작
        ActionBar actionBar = getSupportActionBar();
        // 엑션바 삭제 끝
        Intent intent = getIntent();
        edtId = (EditText) findViewById(R.id.edt_id);
        edtPwd = (EditText) findViewById(R.id.edt_pwd);
        btnSignIn = (Button) findViewById(R.id.btn_signin);
        fingerPrint = (Button) findViewById(R.id.btn_fpSignin);

        executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {

            @Override   //error
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        R.string.auth_error_message, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override   //sucess > uuid 생성 / 확인
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Unique = UUID.getDeviceId(null);
                if(fp_pwd!=null && fp_uu!=null) {
                    Toast.makeText(getApplicationContext(),
                            R.string.auth_success_message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, MainActivity3.class);
                    intent.putExtra("id", fp_id);
                    startActivity(intent);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                    builder.setTitle("알림");
                    builder.setMessage("등록 된 지문이 없습니다. 로그인 후 지문을 등록해주세요.");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                }
                            }).show();
                }
            };
            @Override   //fail
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), R.string.auth_fail_message,
                        Toast.LENGTH_SHORT).show();
            }
        });
        fingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricPrompt.PromptInfo bio = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("본인 인증")
                        .setSubtitle("지문으로 인증해 주세요.")
                        .setNegativeButtonText("취소")
                        .build();
                Unique = UUID.getDeviceId(null);
                biometricPrompt.authenticate(bio);

                Map<String, String> map = new HashMap<>();
                checkTask check = new checkTask();
                map.put("uuid", Unique);
                check.execute(map);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InnerTask task = new InnerTask();
                Map<String, String> map = new HashMap<>();
                map.put("id", edtId.getText().toString());
                map.put("pwd", edtPwd.getText().toString());
                task.execute(map);
            }
        });
    }

    public void actionBar() {
        ActionBar bar = getSupportActionBar();

        LayoutInflater inflater = LayoutInflater.from(this);
//        TextView tv_bar = (TextView) v.findViewById(R.id.tv_bar);
//        tv_bar.setText("로그인");
    }

    //각 Activity 마다 Task 작성
    public class InnerTask extends AsyncTask<Map, String, String> {

        //doInBackground 실행되기 이전에 동작
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //작업을 쓰레드로 처리
        @Override
        protected String doInBackground(Map... maps) {
            //HTTP 요청 준비
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/androidSignIn"); //스프링 url
            //파라미터 전송
            System.out.println("** : " + http);
            http.addAllParameters(maps[0]);

            //HTTP 요청 전송
            HttpClient post = http.create();
            System.out.println("*** : " + post);
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

                try {
                    JSONObject job = new JSONObject(s);
                    String pass = job.getString("id").substring(0,1);

                    if(pass.equals("s")){

                        StudentVO st = gson.fromJson(s, StudentVO.class);

                        System.out.println("아이디 : "+st.getId());
                        System.out.println("비밀번호 : "+st.getPwd());

                        if (st.getPwd() == null) {
                            Toast.makeText(getApplicationContext(), "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                        }

                        if (st.getId() != null && st.getId().substring(0,1).equals("s")) {
                            // 페이지 이동
                            Intent intent = new Intent(SignInActivity.this, MainActivity2.class);
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

                        if (ad.getId() != null && ad.getId().substring(0, 1).equals("a")) {
                            // 페이지 이동
                            Intent intent = new Intent(SignInActivity.this, MainActivity3.class);
                            intent.putExtra("id", ad.getId());
                            startActivity(intent);
                        }
                    } else if (pass.equals("p")) {
                        HumanVO ad = gson.fromJson(s, HumanVO.class);

                        System.out.println("아이디 : " + ad.getId());
                        System.out.println("비밀번호 : " + ad.getPwd());

                        if (ad.getPwd() == null) {
                            Toast.makeText(getApplicationContext(), "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                        }

                        if (ad.getId() != null && ad.getId().substring(0, 1).equals("p")) {
                            // 페이지 이동
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
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
            System.out.println("[Map > UU] : + "+map[0]);
            System.out.println("[Map > UU] : + "+map[0].get("uuid"));

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
        try {
            fp_id = fp.getId();
            fp_pwd = fp.getPwd();
            fp_uu = fp.getUuid();
            if(fp_pwd.length() == 60 && fp_uu.length() == 19) {
                if(fp_pwd.length() == 60) {
                    if (fp_uu.length() == 19) {
                        Unique = UUID.getDeviceId(null);
                        if (fp_uu.equals(Unique)) {
                            if (biocheck != null) {
                                Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignInActivity.this, MainActivity3.class);
                                intent.putExtra("id", fp_id);
                                startActivity(intent);
                            } else if (biocheck == null) {
                                System.out.println("기다려");
                            }
                        }
                    }
                }
                }else{
                    Toast.makeText(getApplicationContext(), "등록된 지문이 없습니다. \n 로그인 후 지문을 등록해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e){}
            }
        }
    }
