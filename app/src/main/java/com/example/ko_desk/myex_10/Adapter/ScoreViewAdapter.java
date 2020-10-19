package com.example.ko_desk.myex_10.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ko_desk.myex_10.ScoreItems;
import com.example.ko_desk.myex_10.widget.FontActivity;
import com.example.ko_desk.myex_10.R;

import java.util.ArrayList;

public class ScoreViewAdapter extends BaseAdapter {
    private ArrayList<ScoreItems> data;
    private LayoutInflater inflater;
    private int layout;
    public ScoreViewAdapter(Context context, int layout, ArrayList<ScoreItems> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getStr();}
    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        ScoreItems listviewitem=data.get(position);
        TextView name=(TextView)convertView.findViewById(R.id.scoreItem);
        name.setText(listviewitem.getStr());

        //FontActivity.setGlobalFont(parent.getContext(),convertView);

        return convertView;
    }
}
