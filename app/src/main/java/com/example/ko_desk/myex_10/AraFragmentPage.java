package com.example.ko_desk.myex_10;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.ko_desk.myex_10.Adapter.ChatMessageAdapter;
import com.example.ko_desk.myex_10.vo.AttendVO;
import com.example.ko_desk.myex_10.vo.Gpa_Total_VO;
import com.example.ko_desk.myex_10.vo.JsonVO;
import com.example.ko_desk.myex_10.vo.ScoreVO;
import com.example.ko_desk.myex_10.vo.Stu_Reg_Lec_VO;
import com.example.ko_desk.myex_10.vo.StudentVO;
import com.example.ko_desk.myex_10.widget.FontActivity;
import com.google.gson.Gson;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class AraFragmentPage extends Fragment {
    //음성인식 화면
    ChatMessageAdapter chatMessageAdapter;
    TextToSpeech tts;   //Text To Speech
    private String weatherToday = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=날씨"; //파싱할 홈페이지의 URL주소
    private String realTime = "https://datalab.naver.com/keyword/realtimeList.naver?where=main"; //파싱할 홈페이지의 URL주소
    private String corona = "https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=코로나";
    private String searchKeyword = "";  //jsoup에 보낼 키워드
    private String htmlContentInStringFormat="";    //웹크롤링 후 결과를 저장할 문자열
    private ImageButton btnSpeak;   //음성인식 버튼
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private Intent intent;

    public AraFragmentPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        intent = getActivity().getIntent();



        View view = inflater.inflate(R.layout.fragment_ara_page, container, false);

        btnSpeak = view.findViewById(R.id.btnSpeak);
        //음성인식 버튼 클릭시
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(view);
            }
        });
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.KOREAN); //한국어로 설정
            }
        });
        /*View araMenu = LayoutInflater.from(this).inflate(R.layout.ara, null);
        root.addView(araMenu);

        new AraAnimation.AraBuilder(araMenu, araMenu.findViewById(R.id.ara_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();*/
        chatMessageAdapter = new ChatMessageAdapter(getContext(), R.layout.chatting_message);

        //채팅부분
        final ListView listView = (ListView) view.findViewById(R.id.listView1);

        listView.setAdapter(chatMessageAdapter);

        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); //메세지가 추가될 때 자동 스크롤


        //메세지가 추가될 때
        chatMessageAdapter.registerDataSetObserver(new DataSetObserver() {

            @Override

            public void onChanged() {

                super.onChanged();

                listView.setSelection(chatMessageAdapter.getCount() - 1);

            }

        });
        //처음 화면에 안녕하세요를 뿌려준다.
        chatMessageAdapter.add(new ChatMessage(true,"안녕하세요"));
        tts.speak("안녕하세요.", TextToSpeech.QUEUE_FLUSH,null);
        //FontActivity.setGlobalFont(getContext(),view);

        return view;

    }


    //(2-1) newInstance() 추가
    public static AraFragmentPage newInstance() {
        Bundle args = new Bundle();

        AraFragmentPage fragment = new AraFragmentPage();
        fragment.setArguments(args);

        return fragment;
    }

    //음성인식 버튼 눌렀을 때
    public void send(View view){
        try{
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getContext().getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
            final SpeechRecognizer stt = SpeechRecognizer.createSpeechRecognizer(getContext());
            stt.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {
                    toast("음성 입력 시작..");
                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {
                    toast("음성 입력 종료..");
                }

                @Override
                public void onError(int i) {
                    toast("오류발생 : " + i);
                    stt.destroy();
                }

                @Override
                public void onResults(Bundle bundle) {
                    //음성인식 결과 얻어옴
                    List<String> result = (ArrayList<String>) bundle.get(SpeechRecognizer.RESULTS_RECOGNITION);

                    //음성인식 결과 중 정확도가 가장 높은 문자열을 뿌려준다.
                    String strMsg = result.get(0);

                    //결과를 채팅에 뿌려준다
                    chatMessageAdapter.add(new ChatMessage(strMsg));

                    //ARA가 답변하는 부분
                    replyAnswer(strMsg, chatMessageAdapter);
                    stt.destroy();
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
            stt.startListening(intent);
        }catch(Exception e){
            toast(e.toString());
        }



    }
    //Toast메세지 메소드로 쉽게 구현
    private void toast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //ARA 답변부분
    //input : 내가 말한 내용
    //chatMessageAdapter : 채팅창에 붙이기 위한 어댑터
    private void replyAnswer(String input, ChatMessageAdapter chatMessageAdapter){
        try {
            InnerTask task = new InnerTask();
            Map<String, Object> map = new HashMap<>();

            String strMsg = ""; //채팅창에 출력되는 문자열

            //로그인 화면에서 받아온 id를 map에 저장
            String id = intent.getExtras().getString("id");
            String name = intent.getExtras().getString("name");
            map.put("id", id);
            if(input.contains("시작") || input.contains("1팀") || input.contains("발표") || input.contains("1조")){
                strMsg = "[유일봇] : 발표 시작합니다.";
                chatMessageAdapter.add(new ChatMessage(true,strMsg));
                tts.speak("UNIQUE팀 발표 시작하겠습니다. 잘부탁드립니다!", TextToSpeech.QUEUE_FLUSH,null);
            }else if(input.contains("발표자") || input.contains("황동국") || input.contains("황 동국") || input.contains("황동 국")){
                strMsg = "[유일봇] : KOSMO 65기 발표자 황동국입니다.";

                chatMessageAdapter.add(new ChatMessage(true,strMsg));
                //tts.speak("안녕하세요 저는 유일봇입니다.", TextToSpeech.QUEUE_FLUSH,null);

            }else if(input.contains("끝") || input.contains("종료") || input.contains("마지막")){
                strMsg = "[유일봇] : UNIQUE팀 발표를 마치겠습니다. 감사합니다";
                chatMessageAdapter.add(new ChatMessage(true,strMsg));
                tts.speak("UNIQUE팀 발표를 마치겠습니다. 감사합니다.", TextToSpeech.QUEUE_FLUSH,null);

            }else if(input.contains("안녕") || input.contains("반가워") || input.contains("반갑")){   //안녕이라는 문자가 포함된 경우
                strMsg = "[유일봇] : 안녕하세요.";
                chatMessageAdapter.add(new ChatMessage(true,strMsg));
                tts.speak("안녕하세요.", TextToSpeech.QUEUE_FLUSH,null);

                //내 정보를 얻어오기 위한 부분
                /*
                * input.contains(" 내 정보") 를 사용한 이유는 교내 정보 같은 단어들과 구분하기 위함
                * input.startsWith("내 정보") 를 사용한 이유는 음성의 첫부분에 "내 정보"가 들어갈 경우 input.contains(" 내 정보")에 해당하지 않아서
                * */
            }else if(input.contains("학사") || input.contains(" 내 정보") || input.contains(" 내정보") || input.startsWith("내 정보") || input.startsWith("내정보")){
                strMsg = "[유일봇] : 내 정보 출력";
                chatMessageAdapter.add(new ChatMessage(true,strMsg));

                //서버에 보낼 서비스를 구분하기위한 키워드
                map.put("keyword","myInfo");
                task.execute(map);
                tts.speak("내 정보입니다.", TextToSpeech.QUEUE_FLUSH,null);


            }else if(input.contains("성적") || input.contains("점수") || input.contains("학점")){ //성적정보를 위한 부분
                strMsg = "[유일봇] : 성적정보 출력";
                chatMessageAdapter.add(new ChatMessage(true,strMsg));

                //서버에 보낼 서비스를 구분하기위한 키워드
                map.put("keyword","score");
                task.execute(map);

//            }else if(input.contains("시간표") || input.contains("수업")){    //시간표를 위한 부분
//                strMsg = "[석무] : 시간표 출력";
//                chatMessageAdapter.add(new ChatMessage(true,strMsg));
//
//                //입력된 문자열에 요일이 포함되지만 요일로 시작하면 안되고, 요일 앞에 공백이 있으면 안됨
//                if(input.contains("요일") && !input.startsWith("요일") && !input.contains(" 요일")){
//
//                    //해당 요일을 받아옴
//                    String day = input.substring(input.indexOf("요일")-1,input.indexOf("요일"));
//                    switch (day){
//                        case "월":
//                            map.put("day",0);
//                            break;
//                        case "화":
//                            map.put("day",1);
//                            break;
//                        case "수":
//                            map.put("day",2);
//                            break;
//                        case "목":
//                            map.put("day",3);
//                            break;
//                        case "금":
//                            map.put("day",4);
//                            break;
//                        default:
//                            map.put("day",5);
//                            break;
//                    }
//
//                }else if(input.contains("모레")){ //모레라는 단어가 포함된 경우 내일모레의 수업을 알려줌
//                    Calendar cal = Calendar.getInstance();
//
//                    //현재 날짜 가져옴
//                    int today = cal.get(Calendar.DAY_OF_WEEK);
//
//                    //7이 넘어가는 경우(토요일 -> 일요일)를 위한 코드
//                    today = (today+2) % 7;
//                    switch (today){
//                        case 2:
//                            map.put("day",0);
//                            break;
//                        case 3:
//                            map.put("day",1);
//                            break;
//                        case 4:
//                            map.put("day",2);
//                            break;
//                        case 5:
//                            map.put("day",3);
//                            break;
//                        case 6:
//                            map.put("day",4);
//                            break;
//                        default:
//                            map.put("day",5);
//                            break;
//                    }
//
//                }else if(input.contains("내일")){ //내일이라는 단어가 포함된 경우 내일의 수업을 알려줌
//                    Calendar cal = Calendar.getInstance();
//
//                    //현재 날짜 가져옴
//                    int today = cal.get(Calendar.DAY_OF_WEEK);
//
//                    //7이 넘어가는 경우(토요일 -> 일요일)를 위한 코드
//                    today = (today+1) % 7;
//                    switch (today){
//                        case 2:
//                            map.put("day",0);
//                            break;
//                        case 3:
//                            map.put("day",1);
//                            break;
//                        case 4:
//                            map.put("day",2);
//                            break;
//                        case 5:
//                            map.put("day",3);
//                            break;
//                        case 6:
//                            map.put("day",4);
//                            break;
//                        default:
//                            map.put("day",5);
//                            break;
//                    }
//
//                }else{  //그렇지 않은 경우 오늘 수업을 알려준다
//                    Calendar cal = Calendar.getInstance();
//
//                    //현재 날짜 가져옴
//                    int today = cal.get(Calendar.DAY_OF_WEEK);
//
//                    switch (today){
//                        case 2:
//                            map.put("day",0);
//                            break;
//                        case 3:
//                            map.put("day",1);
//                            break;
//                        case 4:
//                            map.put("day",2);
//                            break;
//                        case 5:
//                            map.put("day",3);
//                            break;
//                        case 6:
//                            map.put("day",4);
//                            break;
//                        default:
//                            map.put("day",5);
//                            break;
//                    }
//                }
//                //서버에 보낼 서비스를 구분하기위한 키워드
//                map.put("keyword","timetable");
//                task.execute(map);
//

            }else if(input.contains("날씨")) {    //날씨정보를 위한 부분
                strMsg = "[유일봇] : 날씨 정보 출력";
                if(input.contains("모레")){
                    searchKeyword = "모레날씨";
                }else if(input.contains("내일")){
                    searchKeyword = "내일날씨";
                }else if(input.contains("오늘") || input.contains("현재") || input.contains("지금")){
                    searchKeyword = "날씨";
                }else{
                    searchKeyword = "날씨";
                }
                chatMessageAdapter.add(new ChatMessage(true,strMsg));

                //날씨정보를 얻기위한 웹크롤링 오픈소스 jsoup 사용
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();

            }else if((input.contains("실시간") && input.contains("검색")) || input.contains("실시간검색") || input.contains("실시간 검색")) {
                strMsg = "[유일봇] : 실시간 검색어 출력";
                searchKeyword = "실시간";
                chatMessageAdapter.add(new ChatMessage(true, strMsg));

                //실시간 검색어 정보를 얻기위한 웹크롤링 오픈소스 jsoup 사용
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }else if(input.contains("코로나")||input.contains("코로나 확진자")||input.contains("코로나 실시간")||input.contains("일일 코로나 확진자")){
                strMsg = "[유일봇] : 일일 코로나 확진자 수 출력";
                searchKeyword= "코로나";
                chatMessageAdapter.add(new ChatMessage(true,strMsg));
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }else if(input.contains("게임")||input.contains("재미있는 게임")||input.contains("어몽어스")||input.contains("우주인")){
                strMsg = "[유일봇] : 어몽어스를 다운받으러 갑니다.";
                chatMessageAdapter.add(new ChatMessage(true,strMsg));
                tts.speak("어몽어스를 다운받으러 갑니다.", TextToSpeech.QUEUE_FLUSH,null);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.innersloth.spacemafia")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.innersloth.spacemafia")));
                }
                
            } else{
                Random random = new Random();
                int i = random.nextInt(2);

                strMsg = (i==1?"[유일봇] : 다시 말씀해주세요.":"[유일봇] : 한번 더 말씀해주세요.");
                chatMessageAdapter.add(new ChatMessage(true,strMsg));
                tts.speak(i==1?"다시 말씀해주세요.":"한번 더 말씀해주세요.", TextToSpeech.QUEUE_FLUSH,null);
            }
        }catch(Exception e){
            toast(e.toString());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    List<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strMsg = result.get(0);
                    chatMessageAdapter.add(new ChatMessage(strMsg));
                }
                break;
            }

        }
    }

    //웹 크롤링을 위한 jsoup 클래스 정의
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
                    Document doc = (Document) Jsoup.connect(weatherToday).get();


                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(지역부분)

                    Elements titles= doc.select("span.btn_select em");
                    String speak = "";
                    for(Element e: titles){
                        System.out.println("title: " + e.text());
                        htmlContentInStringFormat += e.text().trim() + "의 현재날씨는 \n";
                    }

                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(온도부분)
                    titles= doc.select("p.info_temperature span.todaytemp");
                    htmlContentInStringFormat += titles.get(0).text().trim() + "도, ";

                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(날씨상태부분)
                    titles = doc.select("ul.info_list p.cast_txt");
                    htmlContentInStringFormat += titles.get(0).text().trim().substring(0,titles.get(0).text().trim().indexOf(",")) + "입니다\n";

                    tts.speak( htmlContentInStringFormat, TextToSpeech.QUEUE_FLUSH,null);
                }else if(searchKeyword.equals("내일날씨")){ //키워드가 내일날씨라면

                    //날씨정보를 가진 URL로 이동
                    Document doc = Jsoup.connect(weatherToday).get();


                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(지역부분)
                    Elements titles= doc.select("span.btn_select em");

                    for(Element e: titles){
                        System.out.println("title: " + e.text());
                        htmlContentInStringFormat += e.text().trim() + "의 내일날씨는 \n";
                    }

                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(온도부분)
                    Elements temper = doc.select("p.info_temperature span.todaytemp");

                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(날씨상태부분)
                    Elements cast = doc.select("ul.info_list p.cast_txt");

                    htmlContentInStringFormat += "오전 : " + temper.get(1).text().trim() + "도, " + cast.get(1).text().trim()+"\n";
                    htmlContentInStringFormat += "오후 : " + temper.get(2).text().trim() + "도, " + cast.get(2).text().trim();
                    tts.speak( htmlContentInStringFormat, TextToSpeech.QUEUE_FLUSH,null);
                }else if(searchKeyword.equals("모레날씨")) {    //키워드가 모레날씨라면
                    //날씨정보를 가진 URL로 이동
                    Document doc = Jsoup.connect(weatherToday).get();


                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(지역부분)
                    Elements titles = doc.select("span.btn_select em");

                    for (Element e : titles) {
                        System.out.println("title: " + e.text());
                        htmlContentInStringFormat += e.text().trim() + "의 모레날씨는 \n";
                    }

                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(온도부분)
                    Elements temper = doc.select("p.info_temperature span.todaytemp");

                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(날씨상태부분)
                    Elements cast = doc.select("ul.info_list p.cast_txt");

                    htmlContentInStringFormat += "오전 : " + temper.get(3).text().trim() + "도, " + cast.get(3).text().trim() + "\n";
                    htmlContentInStringFormat += "오후 : " + temper.get(4).text().trim() + "도, " + cast.get(4).text().trim();
                    tts.speak( htmlContentInStringFormat, TextToSpeech.QUEUE_FLUSH,null);
                }else if(searchKeyword.equals("실시간")){  //키워드가 실시간인경우
                    //실시간검색어 정보를 가진 URL로 이동
                    Document doc = Jsoup.connect(realTime).get();
                    //https://datalab.naver.com/keyword/realtimeList.naver?where=mai
                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(현재 시간)
                    Elements titles = doc.select(".keyword_rank.select_date strong.rank_title.v2");

                    for (Element e : titles) {
                        System.out.println("title: " + e.text());
                        htmlContentInStringFormat += e.text().trim() + "\n";
                    }
                    //연결한 URL에서 해당 태그를 가진 값을 가져옴(실시간 검색어)
                    Elements keyword = doc.select("span.item_title_wrap span.item_title");
                    String speak = "";
                    for(int i=0;i<10;i++) {
                        htmlContentInStringFormat += (i+1) + " : " + keyword.get(i).text().trim() + "\n";
                        speak += (i+1) +",     "+ keyword.get(i).text().trim() + ", ";
                    }
                    tts.speak("실시간 검색어는 " + speak , TextToSpeech.QUEUE_FLUSH,null);
                }else if(searchKeyword.equals("코로나")){
                    Document doc = Jsoup.connect(corona).get();

                    //Elements titles1 = doc.select("li.info_02 span.info_title");
                    Elements titles1 = doc.select("li.info_02 em.info_num");
                    Elements titles2 = doc.select("li.info_03 em.info_num");
                    System.out.println(titles1);
                    //System.out.println("titles2");
                    //System.out.println(titles2);
                    htmlContentInStringFormat += "국내 유입 : "  + titles1.text() +"명" + "\n";
                    htmlContentInStringFormat += "해외 유입 : "  + titles2.text() +"명" ;
                    tts.speak(htmlContentInStringFormat, TextToSpeech.QUEUE_FLUSH, null);
                }




            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            chatMessageAdapter.add(new ChatMessage(true,htmlContentInStringFormat));
            /*tts.speak(htmlContentInStringFormat, TextToSpeech.QUEUE_FLUSH,null);*/
        }
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
                http = new HttpClient.Builder("POST", Web.servletURL + "android/chatScore"); //스프링 url
            }else if(((Map<String, Object>)objects[0]).get("keyword").equals("attendance")){   //출석 정보
                http = new HttpClient.Builder("POST", Web.servletURL + "androidAttendance"); //스프링 url
            }else if(((Map<String, Object>)objects[0]).get("keyword").equals("myInfo")){   //학사 정보
                http = new HttpClient.Builder("POST", Web.servletURL + "android/androidMyInfo"); //스프링 url
            }else if(((Map<String, Object>)objects[0]).get("keyword").equals("timetable")){   //시간표 정보
                http = new HttpClient.Builder("POST", Web.servletURL + "androidTimetable"); //스프링 url
                http.addOrReplace("day",((Map<String, Object>)objects[0]).get("day").toString());
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
            Log.d("JSON_RESULT", (String) o);

            //JSON으로 받은 데이터를 VO Obejct로 바꿔준다.
            try{
                Gson gson = new Gson();

                JsonVO json = gson.fromJson((String) o, JsonVO.class);
                String keyword = json.getKeyword();
                String msg = "";
                if (keyword.equals("attendance")) { //출결정보

                    //출결정보를 담는다
                    List<AttendVO> list = json.getAttend();

                    // 결과를 뿌려줌
                    if(list!=null){
                        if(list.size()>0) {
                            msg += "강의명        일자        출결여부\n";
                            msg += "--------------------\n";
                            for (int i = 0; i < list.size(); i++) {
                                msg += list.get(i).getLec_name() + "        " + list.get(i).getLec_dt() + "        ";
                                if (list.get(i).getAttend_fl() == 0) {
                                    msg += "결석\n";
                                } else if (list.get(i).getAttend_fl() == 1) {
                                    msg += "출석\n";
                                } else if (list.get(i).getAttend_fl() == 2) {
                                    msg += "지각\n";
                                }
                            }
                            tts.speak("출결 정보입니다.", TextToSpeech.QUEUE_FLUSH,null);
                        }else{
                            tts.speak("출결 정보가 없습니다", TextToSpeech.QUEUE_FLUSH,null);
                            msg = "출결 정보가 없습니다.";
                        }
                    }else{
                        tts.speak("출결 정보가 없습니다", TextToSpeech.QUEUE_FLUSH,null);
                        msg = "출결 정보가 없습니다.";
                    }

                    chatMessageAdapter.add(new ChatMessage(true,msg));

                } else if (keyword.equals("score")) {   //성적정보
                    //성적정보를 담는다
                    List<ScoreVO> list = json.getScore();

                    // 결과가 있다면 뿌려준다
                    if(list!=null){
                        if(list.size()>0) {

                            //msg += "과목명                 학점                성적\n";
                            //msg += "———————————————————————————\n";
                            String speak = "";
                            for (int i = 0; i < list.size(); i++) {
                                msg += "과목명 : " + list.get(i).getCo_name() + "\n";
                                msg += "학  점 : " + list.get(i).getGrades_code() + ",      성  적 : " + list.get(i).getGrades_score()+ "\n";
                                //msg += "성  적 : " + list.get(i).getGrades_score() + "\n";

                                //msg += list.get(i).getCo_name() + "                " + list.get(i).getGrades_code() + "           "+ list.get(i).getGrades_score()+"\n";
                                //speak += list.get(i).getCo_name() +", "+ list.get(i).getGrades_code()+", "+ list.get(i).getGrades_score();
                            }
                            tts.speak("성적 정보는 " + speak + "입니다.", TextToSpeech.QUEUE_FLUSH,null);
                        }else{
                            tts.speak("성적이 없습니다.", TextToSpeech.QUEUE_FLUSH,null);
                            msg = "성적이 없습니다.";
                        }
                    }else{
                        tts.speak("성적이 없습니다.", TextToSpeech.QUEUE_FLUSH,null);
                        msg = "성적이 없습니다.";
                    }

                    chatMessageAdapter.add(new ChatMessage(true,msg));

                } else if (keyword.equals("myInfo")) {  //내 정보

                    //내 정보를 담는다
                    StudentVO vo = json.getMyInfo();

                     //결과가 있다면
                    if(vo != null) {
                        msg += "학번 : " + vo.getId() + "\n";
                        msg += "이름 : " + vo.getName() + "\n";
                        msg += "전공 : " + vo.getM_code() + "\n";
                        msg += "핸드폰 : " + vo.getTel() + "\n";
                        msg += "생년월일 : " + vo.getJumin1() + "\n";
                        msg += "주소 : " + vo.getAddress() + "\n";
                        msg += "이메일 : " + vo.getEmail();
                    }
                    chatMessageAdapter.add(new ChatMessage(true,msg));

                } else if (keyword.equals("timetable")) {   //시간표정보

                    //시간표 정보를 담는다
                    List<Stu_Reg_Lec_VO> lectureList = json.getTimetable();


                    String[] timetable = new String[14];
                    //결과가 있는경우
                    if(lectureList!=null){
                        if(lectureList.size()>0) {
                            for (Stu_Reg_Lec_VO vo : lectureList) {
                                int startTime = vo.getLec_dt() - 8;
                                int endTime = vo.getLec_dt() + vo.getLec_point() - 8;

                                for (int i = startTime; i < endTime; i++) {
                                    timetable[i] = vo.getLec_name();
                                }
                            }
                            //교시를 표현하기 위함
                            int time = 9;
                            for(int i=0; i<timetable.length;i++){
                                msg += i+1+"교시("+(time+i)+"시) : ";
                                if(timetable[i]==null){
                                    msg += "\n";
                                }else{
                                    msg += timetable[i] + "\n";
                                }
                            }
                            tts.speak("시간표 정보입니다. ", TextToSpeech.QUEUE_FLUSH,null);
                        }else{
                            tts.speak("수업이 없습니다.", TextToSpeech.QUEUE_FLUSH,null);
                            msg = "수업이 없습니다.";
                        }
                    }else{
                        tts.speak("수업이 없습니다.", TextToSpeech.QUEUE_FLUSH,null);
                        msg = "수업이 없습니다.";
                    }

                    chatMessageAdapter.add(new ChatMessage(true,msg));

                }
            }catch(Exception e){

            }
        }
    }


}
