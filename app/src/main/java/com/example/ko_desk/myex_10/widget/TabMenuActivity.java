package com.example.ko_desk.myex_10.widget;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.ko_desk.myex_10.Adapter.TabAdapter;
import com.example.ko_desk.myex_10.R;

public class TabMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabmenu);
        //마이크 권한물어봄
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},5);
        }

        //(5-1) 아답터 객체생성
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());

        //(5-2) 리소스 연결
        ViewPager viewPage = (ViewPager)findViewById(R.id.view_pager);
        viewPage.setAdapter(adapter);

        /*TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPage);*/
    }

}
