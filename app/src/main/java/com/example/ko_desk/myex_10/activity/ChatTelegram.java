package com.example.ko_desk.myex_10.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ko_desk.myex_10.Adapter.AdapterChatTelegram;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.vo.Message;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ChatTelegram extends AppCompatActivity {

    TextToSpeech tts;
    private ImageView btn_send;
    private ImageView btn_mic;
    private EditText et_content;
    private AdapterChatTelegram adapter;
    private RecyclerView recycler_view;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ActionBar actionBar;
    String id;
    View text;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_telegram);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 5);
            toast("순순히 권한을 넘기지 않으면, 음성 인식 기능을 사용할 수 없다!");
        }
        iniComponent();
    }

    public void iniComponent() {
        recycler_view = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);

        adapter = new AdapterChatTelegram(this);
        recycler_view.setAdapter(adapter);

        adapter.insertItem(new Message(adapter.getItemCount(), "[uni] 질문을 말씀해주세요.", false, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));
        //adapter.insertItem(new Message(adapter.getItemCount(), "Hello!", true, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));

        et_content = findViewById(R.id.text_content);

        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputVoice(et_content);
            }
        });

        btn_mic = findViewById(R.id.btn_mic);

        btn_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat();
            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
            }
        });

        et_content.addTextChangedListener(contentWatcher);

        (findViewById(R.id.lyt_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void inputVoice(final TextView txt){
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            final SpeechRecognizer stt = SpeechRecognizer.createSpeechRecognizer(this);
            stt.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    toast("음성 입력 시작...");
                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                }

                @Override
                public void onEndOfSpeech() {
                    toast("음성 입력 종료");
                    stt.destroy();
                }

                @Override
                public void onError(int error) {
                    toast("오류 발생 : " + error);
                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result = (ArrayList<String>) results.get(SpeechRecognizer.RESULTS_RECOGNITION);

                    String strMsg = result.get(0);

                    /*String text = ;
                    replyAnswer(strMsg, text);*/
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.insertItem(new Message(adapter.getItemCount(), strMsg, false, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));
                            recycler_view.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }, 1000);
                    stt.destroy();
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
            stt.startListening(intent);
        } catch (Exception e) {
            toast(e.toString());
        }
    }



    private void sendChat() {
        final String msg = et_content.getText().toString();
        if (msg.isEmpty()) return;
        adapter.insertItem(new Message(adapter.getItemCount(), msg, true, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));
        et_content.setText("");
        recycler_view.scrollToPosition(adapter.getItemCount() - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.insertItem(new Message(adapter.getItemCount(), msg, false, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));
                recycler_view.scrollToPosition(adapter.getItemCount() - 1);
            }
        }, 1000);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setImageResource(R.drawable.ic_mic);
            } else {
                btn_send.setImageResource(R.drawable.ic_send);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_telegram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null != data){
                    List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strMsg = result.get(0);
                    adapter.insertItem(new Message(strMsg));
                }
                break;
            }
        }
    }

    private void replyAnswer(String input, TextView txt){
        try{
            if(input.equals("안녕")){
                toast("안녕하세요");
                txt.append("[짭스티] 누구세요?\n");
                tts.speak("누구세요?", TextToSpeech.QUEUE_FLUSH, null);
            }
            else if(input.equals("너는 누구니")){
                toast("짜가다");
                txt.append("[짭스티] 나는 짭스티라고 해.\n");
                tts.speak("나는 짭스티라고 해.", TextToSpeech.QUEUE_FLUSH, null);
            }
            else if(input.equals("종료")){
                toast("안녕히가세요.");
                finish();
            }
            else {
                toast("뭐라는거야");
                txt.append("[짭스티] 뭐라는거야?\n");
                tts.speak("뭐라는거야?", TextToSpeech.QUEUE_FLUSH, null);
            }
        } catch (Exception e) {
            toast(e.toString());
        }
    }
    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}