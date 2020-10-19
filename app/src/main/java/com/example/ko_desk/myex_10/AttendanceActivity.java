package com.example.ko_desk.myex_10;

import android.content.Intent;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_attendance);

        //폰트적용
        //FontActivity.setGlobalFont(this,getWindow().getDecorView());

        //linearLayout = findViewById(R.id.attendance_linearLayout);
        //listView = findViewById(R.id.attendance_listView);

//        Intent intent = getIntent();
//        List<AttendVO> attendVOList;
//        JsonVO json = (JsonVO) intent.getSerializableExtra("json");
//
//        //서버에서 받아온 출결정보
//        attendVOList = json.getAttend();
//        ArrayList<AttendanceItems> data = new ArrayList<>();
//
//        if(attendVOList.size() > 0){
//            for(AttendVO vo :attendVOList){
//                String str = vo.getLec_name() + "\t" + vo.getLec_dt() + "\t";
//                if(vo.getAttend_fl() == 0){
//                    str += "결석(Ⅹ)";
//                }else if(vo.getAttend_fl() == 1){
//                    str += "출석(○)";
//                }else if(vo.getAttend_fl() == 2){
//                    str += "지각(△)";
//                }
//
//                data.add(new AttendanceItems(str));
//            }
//        }else{
//            data.add(new AttendanceItems("출결정보가 없습니다"));
//        }
//
//
//        AttendanceViewAdapter adapter = new AttendanceViewAdapter(this,R.layout.attendance_view,data);
//        //리스트뷰에 담는다
//        listView.setAdapter(adapter);
    }
}
