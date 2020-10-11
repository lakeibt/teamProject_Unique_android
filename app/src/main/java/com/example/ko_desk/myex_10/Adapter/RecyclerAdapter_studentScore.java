package com.example.ko_desk.myex_10.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ko_desk.myex_10.R;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerAdapter_studentScore extends RecyclerView.Adapter<RecyclerAdapter_studentScore.ItemViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<Map<String, String>> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_student_score, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        Log.d("viewSize:", String.valueOf(listData.size()));
        return listData.size();
    }

    public void addItem(Map<String, String> data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private TextView textView5;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.stucourse_coursename);
            textView2 = itemView.findViewById(R.id.stucourse_professor);
            textView3 = itemView.findViewById(R.id.stucourse_day);
            textView4 = itemView.findViewById(R.id.stucourse_time);
            textView5 = itemView.findViewById(R.id.stucourse_score);
        }

        void onBind(Map<String, String> data) {
            int time = Integer.parseInt(data.get("time"));
            int grade = Integer.parseInt(data.get("grade"));
            int firstTime = time;
            int endTime = grade + time - 1;
            String courseTime = firstTime + "-" + endTime;

            textView1.setText(data.get("course"));
            textView2.setText(data.get("professor"));
            textView3.setText(data.get("day"));
            textView4.setText(courseTime);

            if(data.get("score").equals("AA") || data.get("score").equals("BB") || data.get("score").equals("CC") || data.get("score").equals("DD"))
                textView5.setText(data.get("score").substring(0, 1) + "+");
            else textView5.setText(data.get("score"));
        }
    }
}
