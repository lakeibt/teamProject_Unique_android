package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.activity.SignInActivity;

public class lodingclass extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lodingclass);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}