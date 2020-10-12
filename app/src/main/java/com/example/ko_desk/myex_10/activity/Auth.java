package com.example.ko_desk.myex_10.activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.example.ko_desk.myex_10.R;
import java.util.concurrent.Executor;

public class Auth extends AppCompatActivity {

    String id;
    String pwd;
    String uuid;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);//화면

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        pwd = intent.getStringExtra("pwd");
        uuid = intent.getStringExtra("uuid");
        System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII" + id);
        System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII" + pwd);
        System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu" + uuid);

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
                .setTitle("본인 인증")
                .setSubtitle("지문으로 인증해 주세요.")
                .setNegativeButtonText("취소")
                .setConfirmationRequired(false)
                .setDeviceCredentialAllowed(false)
                .build();

        //  사용자가 다른 인증을 이용하길 원할 때 추가하기
        Button biometricLoginButton = findViewById(R.id.btn_fpSignin);
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }
}