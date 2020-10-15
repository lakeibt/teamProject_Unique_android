package com.example.ko_desk.myex_10.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.Adapter.RecyclerAdapter_proclasschecknext;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.ShopVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Shopdt extends Activity {

    private View parent_view;
    private Button btn_clear, btn_back;
    String num, id, junname, salename2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopnext);
        parent_view = findViewById(android.R.id.content);
        Intent intent = getIntent();
        num = intent.getStringExtra("num"); //req.getParameter("id")'
        id = intent.getStringExtra("id");
        junname = intent.getStringExtra("name");
        Log.d("받은 넘버 값???",num);
        Log.d("받은 아디 값???",id);
        Log.d("받은 네임 값???",junname);
        Shopdt.InnerTask task = new Shopdt.InnerTask();
        Map<String, String> map = new HashMap<>();
        map.put("num", num);
        task.execute(map);


        btn_clear = findViewById(R.id.btn_clear);
        btn_back = findViewById(R.id.btn_back);


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Shopdt.this).setMessage("삭제 하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Shopdt.DeleteTask task = new Shopdt.DeleteTask();
                                Map<String, String> map = new HashMap<>();
                                map.put("num", num);
                                task.execute(map);

                                Toast.makeText(getApplicationContext(), "삭제 처리 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), MainActivity2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("id", id);
                                intent.putExtra("name", junname);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), Shop.class);
                intent.putExtra("id", id);
                intent.putExtra("name", junname);
                startActivity(intent);
            }
        });


    }

    private class InnerTask extends AsyncTask<Map, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/shoplistnext"); //@RequestMapping url
            http.addOrReplace("num", num);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(String o) {
            Log.d("JSON_RESULT", o);
            Gson gson = new Gson();
            try {
                JSONArray job = new JSONArray(o);
                for(int i=0; i < job.length(); i++) {
                    JSONObject jsonobject = job.getJSONObject(i);
                    String title = jsonobject.getString("title");
                    String price = jsonobject.getString("price");
                    String day = jsonobject.getString("day");
                    String text = jsonobject.getString("text");
                    salename2 = jsonobject.getString("name");

                    TextView title1 = (TextView) findViewById(R.id.title);
                    TextView price1 = (TextView) findViewById(R.id.price);
                    TextView salename1 = (TextView) findViewById(R.id.salename);
                    TextView day1 = (TextView) findViewById(R.id.day);
                    TextView textarea1 = (TextView) findViewById(R.id.textarea);
                    title1.setText(title);
                    price1.setText(price+" 원");
                    day1.setText("글쓴 시간 : "+day);
                    textarea1.setText(text);
                    salename1.setText(salename2);
                }

                if(!junname.equals(salename2)) {
                    btn_clear.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class DeleteTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/shoplistclear"); //@RequestMapping url
            http.addOrReplace("num", num);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("JSON_RESULT", (String)o);

        }
    }
}