package com.example.ko_desk.myex_10;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.ko_desk.myex_10.widget.FontActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView city;
    TextView now;
    TextView temperature;
    TextView wind;
    TextView humidity;
    TextView state;
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        FontActivity.setGlobalFont(this,getWindow().getDecorView());
        Date today = new Date();
//
//        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
//        SimpleDateFormat time = new SimpleDateFormat("a hh:mm:ss");
//
//        linearLayout = findViewById(R.id.weather_linearLayout);
//
//        //city = findViewById(R.id.city);
//        temperature = findViewById(R.id.temperature);
//        state = findViewById(R.id.state);
//        wind = findViewById(R.id.wind);
//        humidity = findViewById(R.id.humidity);
//
//        now = findViewById(R.id.date);
//        icon = findViewById(R.id.icon);
//
//        Intent intent = getIntent();
//        String weather = intent.getExtras().getString("weather");
//
//        //날씨결과를 담는다
//        String[] weatherList = weather.split("\n");
//        ArrayList<WeatherItems> data = new ArrayList<>();
//
//        if(weatherList.length > 4){
//            city.setText(weatherList[0]);   //지역
//            temperature.setText(weatherList[1]); //온도
//            state.setText(weatherList[2]);  //날씨 상태
//            wind.setText(weatherList[3]);   //풍속
//            humidity.setText(weatherList[4]);   //습도
//        }
//
//        now.setText(date.format(today) + "\n" + time.format(today));
//


    }
}
