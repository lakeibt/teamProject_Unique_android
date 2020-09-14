package com.example.ko_desk.myex_10.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ko_desk.myex_10.R;

public class MyinfoMod extends Fragment {

    private static final String ARG_PARAM = "";

    public static MyinfoMod newInstance() {
        MyinfoMod fragment = new MyinfoMod();
        return fragment;
    }

    public static MyinfoMod newInstance(String param) {
        MyinfoMod fragment = new MyinfoMod();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    String paramText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramText = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myinfo_mod, container, false);;
        TextView paramView = (TextView) view.findViewById(R.id.param);
        paramView.setText(paramText);

        return view;
    }
}
