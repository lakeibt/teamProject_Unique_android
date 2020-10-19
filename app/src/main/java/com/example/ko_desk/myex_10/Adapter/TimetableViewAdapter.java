package com.example.ko_desk.myex_10.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ko_desk.myex_10.R;

import com.example.ko_desk.myex_10.TimetableItems;
import com.example.ko_desk.myex_10.widget.FontActivity;

import java.util.ArrayList;

public class TimetableViewAdapter extends BaseAdapter {
    private ArrayList<TimetableItems> data;
    private LayoutInflater inflater;
    private int layout;
    public TimetableViewAdapter(Context context, int layout, ArrayList<TimetableItems> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public ArrayList<String> getItem(int position){return data.get(position).getArrayList();}
    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        TimetableItems listviewitem = data.get(position);
        TextView[] textViewArr = new TextView[6];
        textViewArr[0] = convertView.findViewById(R.id.timetableItem1);
        textViewArr[1] = convertView.findViewById(R.id.timetableItem2);
        textViewArr[2] = convertView.findViewById(R.id.timetableItem3);
        textViewArr[3] = convertView.findViewById(R.id.timetableItem4);
        textViewArr[4] = convertView.findViewById(R.id.timetableItem5);
        textViewArr[5] = convertView.findViewById(R.id.timetableItem6);
        for(int i=0; i< 6; i++){
            textViewArr[i].setText(listviewitem.getArrayList().get(i));
        }

        //FontActivity.setGlobalFont(parent.getContext(),convertView);

        return convertView;
    }
}
