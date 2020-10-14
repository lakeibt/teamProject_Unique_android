package com.example.ko_desk.myex_10.activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.vo.FingerPrintVO;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class AuthLoginCheck extends AppCompatActivity {

    public void check() {
        String Unique;
        Unique = UUID.getDeviceId(null);
        Intent intent = getIntent();

        Map<String, String> map = new HashMap<>();
        checkTask check = new checkTask();
        map.put("uuid", Unique);
        System.out.println(map.get("uuid"));
        check.execute(map);
    };

    //키 확인 task
    public class checkTask extends AsyncTask<Map, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map... map) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/android_bio_signCheck"); //스프링 url
            http.addAllParameters(map[0]);
            System.out.println("[Map > UU] : + "+map[0]);
            System.out.println("[Map > UU] : + "+map[0].get("uuid"));

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            System.out.println("---" + body);

            return body;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            Log.d("JSON_RESULT", jsonData);
            Gson gson = new Gson();
            FingerPrintVO fp = gson.fromJson(jsonData, FingerPrintVO.class);
            System.out.println("SSSSSS : " + jsonData);
            String fp_id = fp.getId();
            String fp_pwd = fp.getPwd();
            String fp_uu = fp.getUuid();
            if(fp_pwd.length() == 60 && fp_uu.length() == 19) {
                System.out.println("=========================================================================");
                System.out.println("AC아이디 : " + fp_id);
                System.out.println("AC비밀번호 : " + fp_pwd);
                System.out.println("AC키값 : " + fp_uu);
                System.out.println("=========================================================================");
                System.out.println("=========================================================================");
                System.out.println("AC아이디 : " + fp_id.length());
                System.out.println("AC비밀번호 : " + fp_pwd.length());
                System.out.println("AC키값 : " + fp_uu.length());
                System.out.println("=========================================================================");

            }
        }
    }
}
