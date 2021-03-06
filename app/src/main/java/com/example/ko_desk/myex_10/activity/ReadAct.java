package com.example.ko_desk.myex_10.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.NdefMessageParser;
import com.example.ko_desk.myex_10.ParsedRecord;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.TextRecord;
import com.example.ko_desk.myex_10.Web;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadAct extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_MULTI = 0;
    TextView readResult;

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_URI = 2;
    private String PhoneNum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);

        ArrayList<String> permissions = new ArrayList<String>();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.READ_SMS);
        }

        if(permissions.size() > 0) {
            String[] reqPermissionArray = new String[permissions.size()];
            reqPermissionArray = permissions.toArray(reqPermissionArray);
            ActivityCompat.requestPermissions(this, reqPermissionArray, MY_PERMISSIONS_REQUEST_MULTI);
        }

        readResult = (TextView) findViewById(R.id.readResult);

        // NFC 관련 객체 생성
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent targetIntent = new Intent(this, ReadAct.class);
        //Activity stack 에 동일한Activity가 연속적으로 쌓이면 Activity를 재사용 하는 Flag 입니다
        //[A] [B] [B] -> [A] [B]
        //태그를 계속 인식할 때 새로운 Activity를 만들지 않고 현재 Activity에서 Intent를 받기 위해서
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, targetIntent, 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        mFilters = new IntentFilter[]{ndef,};

        mTechLists = new String[][]{new String[]{NfcF.class.getName()}};

        Intent passedIntent = getIntent();
        if (passedIntent != null) {
            String action = passedIntent.getAction();
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
                processTag(passedIntent);
            }
        }

    }

    /***************************************************************
     * 여기서부턴 NFC 관련 메소드
     * NFC 단말에 태그가 인식되면, Intent를 통해서 Activity로 전달됩니다.
     * Activity가 이 Intent를 받기 위해서는 NfcAdapter 클래스의
     * enableForegroundDispatch(..) 를 이용합니다.
     **************************************************************/
    public void onResume() {
        super.onResume();

        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                    mTechLists);
        }
    }

    public void onPause() {
        super.onPause();

        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

    /****************************************************************
     *  NFC 태그 스캔시 자동 호출되는 메소드
     *  스캔된 태그의 모든 정보는 Intent에 저장되어 있음
     ****************************************************************/
    public void onNewIntent(Intent passedIntent) {
        // 전달받은 Intent에서 NFC 태그의 고유 ID를 얻음
        Tag tag = passedIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            byte[] tagId = tag.getId();
            // TextView에 NFC 태그의 고유 ID를 덧붙임
            readResult.append("\n태그 ID : " + toHexString(tagId) + "\n");

            TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String PhoneNum = telManager.getLine1Number();
            if(PhoneNum.startsWith("+82")){
                PhoneNum = PhoneNum.replace("+82", "0");
                String renewer = "(\\d{3})(\\d{3,4})(\\d{4})";
                PhoneNum2 = PhoneNum.replaceAll(renewer, "$1-$2-$3");
                readResult.append("폰 번호 : " + PhoneNum2 + "\n");
            }

            ReadAct.InnerTask task = new ReadAct.InnerTask();
            Map<String, String> map = new HashMap<>();
            map.put("phonenum", PhoneNum2);
            map.put("tagid", toHexString(tagId));
            task.execute(map);

        }
        // 전달받은 Intent에서 NFC 태그에 등록한 비즈니스 정보(사원코드 등)를 얻음
        if (passedIntent != null) {
            processTag(passedIntent); // processTag 메소드 호출
        }
    }

    /*******************************************
     *  바이너리 형식의 NFC 태그 ID를 문자열 형식으로
     *  바꿔서 리턴하는 메소드
     *******************************************/
    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
                    CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }

    /************************************************************
     * 전달받은 Intent에서 NFC 태그에 등록한 비즈니스 정보(사원코드 등)
     * 를 얻는 메소드
     * @param passedIntent - NFC태그의 등록된 정보를 가지고있는 Intent
     **************************************************************/
    private void processTag(Intent passedIntent) {
        Parcelable[] rawMsgs = passedIntent
                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs == null) {
            return;
        }

        Toast.makeText(getApplicationContext(), "스캔 성공!",Toast.LENGTH_SHORT).show();

        Log.i("info", "rawMsgs.length:"+rawMsgs.length);
        // 참고! rawMsgs.length : 스캔한 태그 개수
        NdefMessage[] msgs;
        if (rawMsgs != null) {
            msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
                showTag(msgs[i]);
            }
        }
    }

    /***************************************************************
     * NdefMessage에서 NdefRecode들을 추출후 NdefRecode내용중 텍스값만 추출
     * 예-사원코드(A0001)
     * @param mMessage
     * @return
     **************************************************************/
    private int showTag(NdefMessage mMessage) {
        List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            ParsedRecord record = records.get(i);

            int recordType = record.getType();
            String recordStr = ""; // NFC 태그로부터 읽어들인 텍스트 값
            if (recordType == ParsedRecord.TYPE_TEXT) {
                recordStr = "태그 값 : " + ((TextRecord) record).getText();
            }

            // 읽어들인 텍스트 값을 TextView에 덧붙임
            readResult.append(recordStr + "\n");

            /**************************************
             * 인터넷 연결후
             * 서버(http://172.16.4.1:8000/www_android/nfc/product.jsp)에
             * tag의 비즈니스 데이타 text(사원코드)값 code 파라메터에 담아서
             * 전송
             *************************************/
            Log.i("info", recordStr);

            //String uriString ="http://172.16.4.1:8000/www_android/nfc/product.jsp?code="+((TextRecord) record).getText();
            //Uri uri = Uri.parse(uriString);
            //Intent it  = new Intent(Intent.ACTION_VIEW,uri);

            //startActivity(it);

            //finish();
        }


        return size;
    }

    //각 Activity 마다 Task 작성
    public class InnerTask extends AsyncTask<Map, Integer, String> {

        //doInBackground 실행되기 이전에 동작
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //작업을 쓰레드로 처리
        @Override
        protected String doInBackground(Map... maps) {
            //HTTP 요청 준비
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/androidNfcTag"); //스프링 url
            //파라미터 전송
            http.addAllParameters(maps[0]);

            //HTTP 요청 전송
            HttpClient post = http.create();
            post.request(); // jsp의 submit 역할

            String body = post.getBody(); //Web의 Controller에서 리턴한 값
            System.out.println("---" + body);
            return body;
        }

        //doInBackground 종료되면 동작

        /**
         * @param s : doInBackground에서 리턴한 body. JSON 데이터
         */
        @Override
        protected void onPostExecute(String s) {
            Log.d("JSON_RESULT", s);
            //JSON으로 받은 데이터를 VO Obejct로 바꿔준다.
            System.out.println("-----" + s);


            if (s.length() > 0) {
                Gson gson = new Gson();
                System.out.println("SSSSSS : "+s);
                Toast.makeText(getApplicationContext(), "출틔근 태깅 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReadAct.this, lodingclass.class);
                startActivity(intent);

            }else {
                Toast.makeText(getApplicationContext(), "출틔근 태깅 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

}