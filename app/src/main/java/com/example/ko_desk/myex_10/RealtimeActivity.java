package com.example.ko_desk.myex_10;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.ko_desk.myex_10.Adapter.RealtimeViewAdapter;
import com.example.ko_desk.myex_10.widget.FontActivity;

import java.util.ArrayList;

public class RealtimeActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);
        FontActivity.setGlobalFont(this,getWindow().getDecorView());

        linearLayout = findViewById(R.id.realtime_linearLayout);
        listView = findViewById(R.id.realtimeListView);

        Intent intent = getIntent();
        String realtime = intent.getExtras().getString("realtime");
        Log.d("realtime",realtime);

        //실시간 검색어 결과를 개행문자를 기준으로 자르고 리스트에 뿌려줌
        String[] realtimeList = realtime.split("\n");
        ArrayList<RealtimeItems> data = new ArrayList<>();
        for(String str : realtimeList){
            data.add(new RealtimeItems(str));
        }

        RealtimeViewAdapter adapter = new RealtimeViewAdapter(this,R.layout.realtime_view,data);
        listView.setAdapter(adapter);
    }
}
