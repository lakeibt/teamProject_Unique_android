package com.example.ko_desk.myex_10;

import android.content.Intent;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.ko_desk.myex_10.Adapter.ScoreViewAdapter;
import com.example.ko_desk.myex_10.vo.Gpa_Total_VO;
import com.example.ko_desk.myex_10.vo.JsonVO;
import com.example.ko_desk.myex_10.widget.FontActivity;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        FontActivity.setGlobalFont(this,getWindow().getDecorView());

        linearLayout = findViewById(R.id.score_linearLayout);
        listView = findViewById(R.id.score_listView);

        Intent intent = getIntent();
        List<Gpa_Total_VO> score;
        JsonVO json = (JsonVO) intent.getSerializableExtra("json");

        //서버에서 받아온 성적정보를 리스틉뷰에 뿌려준다
        //score = json.getScore();
        ArrayList<ScoreItems> data = new ArrayList<>();
//
//        if(score.size() > 0){
//            for(Gpa_Total_VO vo :score){
//                data.add(new ScoreItems("학기 :" + vo.getGpa_semester() + "\n평점 : " + vo.getGpa_total()));
//            }
//        }else{
//            data.add(new ScoreItems("성적이 없습니다"));
//        }


        ScoreViewAdapter adapter = new ScoreViewAdapter(this,R.layout.score_view,data);
        listView.setAdapter(adapter);
    }
}
