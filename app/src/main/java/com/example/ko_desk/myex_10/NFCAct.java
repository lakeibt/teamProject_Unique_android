package com.example.ko_desk.myex_10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;


public class NFCAct extends Activity {

    private static final String ARG_PARAM = "";

    Button r, w;

    public static NFCAct newInstance() {
        NFCAct fragment = new NFCAct();
        return fragment;
    }

    public static NFCAct newInstance(String param) {
        NFCAct fragment = new NFCAct();
        Bundle args = new Bundle();
        return fragment;
    }

    String paramText;


    public View onre(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nfc, container, false);;
        TextView paramView = (TextView) view.findViewById(R.id.param);
        paramView.setText(paramText);

        return view;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc);

        r = (Button) findViewById(R.id.readActivityButton);
        w = (Button) findViewById(R.id.writeActivityButton);

        r.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(), ReadAct.class);
                startActivity(intent);
            }
        });

        w.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), WriteAct.class);
                startActivity(intent);
            }
        });

    }

}