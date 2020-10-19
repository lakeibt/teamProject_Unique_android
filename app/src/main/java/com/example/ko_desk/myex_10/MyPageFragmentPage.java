package com.example.ko_desk.myex_10;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ko_desk.myex_10.vo.JsonVO;
import com.example.ko_desk.myex_10.widget.FontActivity;
import com.google.gson.Gson;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyPageFragmentPage extends Fragment {
    private TextView weather;
    private TextView realtime;
    private TextView score;
    private TextView attendance;
    private TextView timetable;
    private Intent intent;

    private String weatherToday = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=날씨"; //파싱할 홈페이지의 URL주소
    private String realTime = "https://datalab.naver.com/keyword/realtimeList.naver?where=main"; //파싱할 홈페이지의 URL주소
    private String searchKeyword = "";
    private String htmlContentInStringFormat="";
    public MyPageFragmentPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        intent = getActivity().getIntent();
        View view = inflater.inflate(R.layout.fragment_mypage_page, container, false);

        weather = view.findViewById(R.id.weather);
        realtime = view.findViewById(R.id.realtime);
        score = view.findViewById(R.id.score);
        attendance = view.findViewById(R.id.attendance);
        timetable = view.findViewById(R.id.timetable);

        //날씨를 클릭한 경우
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchKeyword = "날씨";
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();

            }
        });

        //실시간 검색어를 클릭한 경우
        realtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchKeyword = "실시간";
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();

            }
        });
        //성적을 클릭한 경우
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = intent.getExtras().getString("id");

                MyPageFragmentPage.InnerTask task = new MyPageFragmentPage.InnerTask();
                Map<String, Object> map = new HashMap<>();

                //아이디와 키워드를 저장해서 서버로 보낸다
                map.put("id",id);
                map.put("keyword","score");

                try {
                    //서버에서 온 결과
                    Object result = task.execute(map).get();
                    Log.d("result",result.toString());

                    //화면전환을 위한 부분
                    Intent nextActivity = new Intent(getActivity(),ScoreActivity.class);
                    Gson gson = new Gson();
                    JsonVO json = gson.fromJson((String) result, JsonVO.class);

                    //화면전환시 값을 들고 간다
                    nextActivity.putExtra("json",json);
                    startActivity(nextActivity);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        //출결정보를 클릭한 경우
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = intent.getExtras().getString("id");

                MyPageFragmentPage.InnerTask task = new MyPageFragmentPage.InnerTask();
                Map<String, Object> map = new HashMap<>();

                //아이디와 키워드를 저장해서 서버로 보낸다
                map.put("id",id);
                map.put("keyword","attendance");

                try {
                    //서버에서 온 결과
                    Object result = task.execute(map).get();
                    Log.d("result",result.toString());

                    //화면전환을 위한 부분
                    Intent nextActivity = new Intent(getActivity(),AttendanceActivity.class);
                    Gson gson = new Gson();
                    JsonVO json = gson.fromJson((String) result, JsonVO.class);

                    //화면전환시 값을 들고 간다
                    nextActivity.putExtra("json",json);
                    startActivity(nextActivity);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        //시간표를 클릭한 경우
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = intent.getExtras().getString("id");

                MyPageFragmentPage.InnerTask task = new MyPageFragmentPage.InnerTask();
                Map<String, Object> map = new HashMap<>();

                //아이디와 키워드를 저장해서 서버로 보낸다
                map.put("id",id);
                map.put("keyword","timetable");


                try {
                    //서버에서 온 결과
                    Object result = task.execute(map).get();
                    Log.d("result",result.toString());

                    //화면전환을 위한 부분
                    Intent nextActivity = new Intent(getActivity(),TimetableActivity.class);
                    Gson gson = new Gson();
                    JsonVO json = gson.fromJson((String) result, JsonVO.class);

                    //화면전환시 값을 들고 간다
                    nextActivity.putExtra("json",json);
                    startActivity(nextActivity);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });



//        FontActivity.setGlobalFont(getContext(),view);

        return view;
    }


    //(2-1) newInstance() 추가
    public static MyPageFragmentPage newInstance() {
        Bundle args = new Bundle();

        MyPageFragmentPage fragment = new MyPageFragmentPage();
        fragment.setArguments(args);

        return fragment;
    }
    //각 Activity 마다 Task 작성
    public class InnerTask extends AsyncTask {

        //doInBackground 실행되기 이전에 동작
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //작업을 쓰레드로 처리
        @Override
        protected Object doInBackground(Object[] objects) {
            //HTTP 요청 준비
            HttpClient.Builder http = null;
            if(((Map<String, Object>)objects[0]).get("keyword").equals("score")){   //성적 정보
                http = new HttpClient.Builder("POST", Web.servletURL + "androidScore"); //스프링 url
            }else if(((Map<String, Object>)objects[0]).get("keyword").equals("attendance")){   //출석 정보
                http = new HttpClient.Builder("POST", Web.servletURL + "androidAttendance"); //스프링 url
            }else if(((Map<String, Object>)objects[0]).get("keyword").equals("myInfo")){   //학사 정보
                http = new HttpClient.Builder("POST", Web.servletURL + "androidMyInfo"); //스프링 url
            }else if(((Map<String, Object>)objects[0]).get("keyword").equals("timetable")){   //시간표 정보
                http = new HttpClient.Builder("POST", Web.servletURL + "androidTotalTimetable"); //스프링 url
            }
            http.addOrReplace("keyword",((Map<String, Object>)objects[0]).get("keyword").toString());
            http.addOrReplace("id",((Map<String, Object>)objects[0]).get("id").toString());


            //HTTP 요청 전송
            HttpClient post = http.create();
            post.request();

            String body = post.getBody(); //Web의 Controller에서 리턴한 값
            return body;
        }

        //doInBackground 종료되면 동작
        @Override
        protected void onPostExecute(Object o) {

        }
    }
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlContentInStringFormat = "";
                if(searchKeyword.equals("날씨")){ //키워드가 날씨라면

                    //날씨정보를 가진 URL로 이동
                    Document doc = Jsoup.connect(weatherToday).get();


                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(지역부분)
                    Elements titles= doc.select("span.btn_select em");

                    for(Element e: titles){
                        System.out.println("title: " + e.text());
                        htmlContentInStringFormat += e.text().trim() + "\n ";
                    }

                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(온도부분)
                    Elements temper = doc.select("p.info_temperature span.todaytemp");

                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(날씨상태부분)
                    Elements cast = doc.select("ul.info_list p.cast_txt");

                    //풍속
                    Elements wind = doc.select("div.info_list.wind._tabContent dd.weather_item._dotWrapper span");

                    //습도
                    Elements humidity = doc.select("div.info_list.humidity._tabContent dd.weather_item._dotWrapper span");
                    System.out.println("-------------------------------------------------------------");
                    htmlContentInStringFormat += temper.get(0).text().trim() + "℃\n";
                    htmlContentInStringFormat += cast.get(0).text().trim().substring(0,cast.get(0).text().trim().indexOf(",")) + "\n";
                    htmlContentInStringFormat += wind.get(0).text().trim() + "m/s\n";
                    htmlContentInStringFormat += humidity.get(0).text().trim() + "%";

                }else if(searchKeyword.equals("실시간")){  //키워드가 실시간이라면

                    //실시간검색어를 가진 URL로 이동
                    Document doc = Jsoup.connect(realTime).get();

                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(현재시간)
                    Elements titles = doc.select(".keyword_rank.select_date strong.rank_title.v2");

                    for (Element e : titles) {
                        System.out.println("title: " + e.text());
                        htmlContentInStringFormat += e.text().trim() + "\n";
                    }
                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(실시간 검색어)
                    Elements keyword = doc.select(".keyword_rank.select_date span.title");

                    for(int i=0;i<10;i++) {
                        htmlContentInStringFormat += (i+1) + " : " + keyword.get(i).text().trim() + "\n";
                    }
                }




            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(searchKeyword.equals("날씨")){
                Intent nextActivity = new Intent(getActivity(),WeatherActivity.class);

                nextActivity.putExtra("weather",htmlContentInStringFormat);
                startActivity(nextActivity);
            }else if(searchKeyword.equals("실시간")){
                Intent nextActivity = new Intent(getActivity(),RealtimeActivity.class);

                nextActivity.putExtra("realtime",htmlContentInStringFormat);
                startActivity(nextActivity);
            }
        }
    }
}
