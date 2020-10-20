package com.example.ko_desk.myex_10.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.unity.UnityPlayerActivity;

public class Arclass extends AppCompatActivity {

    View girl, pic;
    String id, name, pushclassname, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unity);

        pic = findViewById(R.id.pic);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Arclass.this, UnityPlayerActivity.class);
                startActivity(intent);
            }
        });

        girl = findViewById(R.id.girl);
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName compName = new ComponentName("com.DefaultCompany.AR_Makeup","com.DefaultCompany.AR_Makeup.Beauty");
                //패키지명은 연락처  액티비티명은 최근기록 입력
                Intent intent23 = new Intent(Intent.ACTION_MAIN);
                intent23.addCategory(Intent.CATEGORY_LAUNCHER);
                intent23.setComponent(compName);
                startActivity(intent23);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
