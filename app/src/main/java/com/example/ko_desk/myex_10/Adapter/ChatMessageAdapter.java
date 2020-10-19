package com.example.ko_desk.myex_10.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.ko_desk.myex_10.ChatMessage;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.widget.FontActivity;


import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter<AttendVO> extends ArrayAdapter {

    List msgs = new ArrayList();
    private TextView chatText;
    private CardView backcolor;
    private TableLayout tableLayout;
    List<List<AttendVO>> dtos = new ArrayList<>();
    private List chatMessageList = new ArrayList();
    private LinearLayout singleMessageContainer;


    public ChatMessageAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }


    //@Override
    public void add(ChatMessage object) {

        msgs.add(object);
        super.add(object);

    }

    public void add(ChatMessage object, List<AttendVO> dtos){
        msgs.add(object);
        this.dtos.add(dtos);
        super.add(object);
    }


    @Override
    public int getCount() {
        return msgs.size();
    }


    @Override
    public ChatMessage getItem(int index) {
        return (ChatMessage) msgs.get(index);
    }


    @SuppressLint("WrongConstant")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            // inflator를 생성하여, chatting_message.xml을 읽어서 View객체로 생성한다.
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.chatting_message, parent, false);

        }


        // Array List에 들어 있는 채팅 문자열을 읽어

        /*ChatMessage msg = (ChatMessage) msgs.get(position);



        // Inflater를 이용해서 생성한 View에, ChatMessage를 삽입한다.

        TextView msgText = (TextView) row.findViewById(R.id.chatmessage);

        msgText.setText(msg.getMessage());

        msgText.setTextColor(Color.parseColor("#000000"));

        // 메세지를 번갈아 가면서 좌측,우측으로 출력

        LinearLayout chatMessageContainer = (LinearLayout)row.findViewById(R.id.chatmessage_container);

        ChatMessage chatMessageObj = getItem(position);
        TextView chatText = row.findViewById(R.id.chatmessage);
        //boolean message_left = chatMessageObj.isLeft();
        chatText.setText(chatMessageObj.message);
        //chatText.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_a : R.drawable.bubble_b);
        chatMessageContainer.setGravity(chatMessageObj.isLeft() ? Gravity.LEFT : Gravity.RIGHT);
        *//*int align;

        if(message_left) {

            align = Gravity.LEFT;

            chatMessageObj.setLeft(false);

        }else{

            align = Gravity.RIGHT;

            chatMessageObj.setLeft(true);

        }

        chatMessageContainer.setGravity(align);*//*

        return row;*/

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.chatting_message, parent, false);
        }
        singleMessageContainer = row.findViewById(R.id.chatmessage_container);
        ChatMessage chatMessageObj = getItem(position);
        chatText = (TextView) row.findViewById(R.id.chatmessage);
        chatText.setText(chatMessageObj.message);
        chatText.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);
        /*
        tableLayout = row.findViewById(R.id.tableLayout);
        if(dtos.size()>0) {
            TableRow tRow[] = new TableRow[dtos.size() + 1];
            TextView text[][] = new TextView[dtos.size() + 1][4];

            tRow[0] = new TableRow(this.getContext());
            for (int i = 0; i < 3; i++) {
                text[0][i] = new TextView(this.getContext());
                text[0][i].setGravity(Gravity.CENTER);    // 폰트정렬
                if (i == 0) {
                    text[0][i].setText("강의명");
                } else if (i == 1) {
                    text[0][i].setText("일자");
                } else if (i == 2) {
                    text[0][i].setText("출결여부");
                }
                tRow[0].addView(text[0][i]);
            }
            tableLayout.addView(tRow[0]);

            for (int i = 1; i < dtos.size() + 1; i++) {

                tRow[i] = new TableRow(this.getContext());
                for (int j = 0; j < 3; j++) {
                    text[i][j] = new TextView(this.getContext());
                    if (j == 0) {
                        text[i][j].setText(dtos.get(position).get(i).getLec_name());
                    } else if (j == 1) {
                        text[i][j].setText(dtos.get(position).get(i).getLec_dt());
                    } else if (j == 2) {
                        if (dtos.get(position).get(i).getAttend_fl() == 0) {
                            text[i][j].setText("결석");
                        } else if (dtos.get(position).get(i).getAttend_fl() == 1) {
                            text[i][j].setText("출석");
                        } else if (dtos.get(position).get(i).getAttend_fl() == 2) {
                            text[i][j].setText("지각");
                        }

                    }
                    tRow[i].addView(text[i][j]);
                }
                tableLayout.addView(tRow[i]);
            }
        }

        tableLayout.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);
*/
        singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);
//        FontActivity.setGlobalFont(getContext(),row);

        return row;


    }
}