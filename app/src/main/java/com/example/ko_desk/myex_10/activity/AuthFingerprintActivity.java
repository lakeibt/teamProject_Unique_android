package com.example.ko_desk.myex_10.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class AuthFingerprintActivity extends AppCompatActivity {

    String id;
    String Unique;
    String fp_id;
    String fp_uu;
    String uuid;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        uuid = intent.getStringExtra("uuid");
        System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII" + id);
        System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu" + uuid);

        checkInsert(id, uuid);

    }

    protected void checkInsert(String id, String uuid) {

        setContentView(R.layout.fingerprint);

        if (uuid != null) {
            new AlertDialog.Builder(AuthFingerprintActivity.this)
                    .setMessage("지문이 이미 등록되어있습니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which){
                            Intent intent = new Intent(AuthFingerprintActivity.this, MainActivity3.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    })
                    .show();

        } else {
            executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(this,
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
                    Toast.makeText(getApplicationContext(),
                            R.string.auth_success_message, Toast.LENGTH_SHORT).show();
                }

                @Override   //fail
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), R.string.auth_fail_message,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });

            //netive 지문 팝업 내용
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("지문을 확인")
                    .setSubtitle("등록된 지문과 일치한지 확인")
                    .setNegativeButtonText("취소")
                    .setConfirmationRequired(false)
                    .setDeviceCredentialAllowed(false)
                    .build();

            //  사용자가 다른 인증을 이용하길 원할 때 추가하기
            Button biometricLoginButton = findViewById(R.id.fingerAddButton);
            biometricLoginButton.setOnClickListener(view -> {
                biometricPrompt.authenticate(promptInfo);
            });

            Map<String, String> map = new HashMap<>();
            insertTask check = new insertTask();
            map.put("id", id);
            map.put("uuid", Unique);
            check.execute(map);
        }
    }
    private class insertTask extends AsyncTask<Map, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... map) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/android_bio_add"); //스프링 url
            http.addAllParameters(map[0]);

            System.out.println("[Map > ID] : + " + map[0].get("id"));
            System.out.println("[Map > UU] : + " + map[0].get("uuid"));

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            System.out.println("---" + body);
            return body;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            Log.d("JSON_RESULT", jsonData);
            System.out.println("-----" + jsonData);

            try {
                JSONObject fp = new JSONObject(jsonData);
                fp_id = fp.getString("id");
                fp_uu = fp.getString("uuid");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonData.length() > 0) {
                Gson gson = new Gson();
                System.out.println("SSSSSS : " + jsonData);
                Toast.makeText(getApplicationContext(), "값 읽기", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "값 읽기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

