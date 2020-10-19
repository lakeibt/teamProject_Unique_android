package com.example.ko_desk.myex_10;

import android.content.Intent;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.ko_desk.myex_10.Adapter.TimetableViewAdapter;
import com.example.ko_desk.myex_10.vo.JsonVO;
import com.example.ko_desk.myex_10.vo.Stu_Reg_Lec_VO;
import com.example.ko_desk.myex_10.widget.FontActivity;

import java.util.ArrayList;
import java.util.List;

public class TimetableActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        FontActivity.setGlobalFont(this,getWindow().getDecorView());

        linearLayout = findViewById(R.id.timetable_linearLayout);
        listView = findViewById(R.id.timetable_listView);

        Intent intent = getIntent();
        JsonVO json = (JsonVO) intent.getSerializableExtra("json");
        List<List<Stu_Reg_Lec_VO>> totaltimetable = json.getTotaltimetable();

        //서버에서 받아온 성적정보를 리스틉뷰에 뿌려준다
        String[][] timetable = new String[5][15];
        ArrayList<TimetableItems> data = new ArrayList<>();

        if (totaltimetable.size() > 0) {
            for (int i = 0; i < 5; i++) {

                for (Stu_Reg_Lec_VO vo : totaltimetable.get(i)) {
                    int startTime = vo.getLec_dt() - 8;
                    int endTime = vo.getLec_dt() + vo.getLec_point() - 8;

                    for (int j = startTime; j < endTime; j++) {
                        timetable[i][j] = vo.getLec_name();
                    }
                }
            }
            ArrayList<String> top = new ArrayList<>();
            top.add("시간");
            top.add("월요일");
            top.add("화요일");
            top.add("수요일");
            top.add("목요일");
            top.add("금요일");
            data.add(new TimetableItems(top));
            //동적으로 TableRow와 TextView를 생성해서 TableLayout에 붙인다
            for(int j=0;j<14;j++){  //시간
                ArrayList<String> table = new ArrayList<>();
                table.add((j+1)+"교시("+(j+9)+")");
                for(int i=0 ;i < 5; i++){   //요일

                    if(timetable[i][j] == null){
                        timetable[i][j] = "";
                    }
                    table.add(timetable[i][j]);
                }
                data.add(new TimetableItems(table));
            }


        }

        TimetableViewAdapter adapter = new TimetableViewAdapter(this,R.layout.timetable_view,data);
        FontActivity.setGlobalFont(this,getWindow().getDecorView());
        listView.setAdapter(adapter);
    }


}
