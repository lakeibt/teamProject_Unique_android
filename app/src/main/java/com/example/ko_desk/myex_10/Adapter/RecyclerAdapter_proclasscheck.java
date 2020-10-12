package com.example.ko_desk.myex_10.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.activity.Tools;
import com.example.ko_desk.myex_10.activity.ViewAnimation;
import com.example.ko_desk.myex_10.vo.InClassHowVO;

import java.util.ArrayList;

public class RecyclerAdapter_proclasscheck extends RecyclerView.Adapter<RecyclerAdapter_proclasscheck.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<InClassHowVO> listData = new ArrayList<>();
    private RecyclerView recyclerView;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_proclasscheckdt, parent, false);
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
        return listData.size();
    }

    public void addItem(InClassHowVO data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private View lyt_expand_text;
        private ImageButton bt_toggle_text;
        private NestedScrollView nested_scroll_view;
        private String going;

        ItemViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            bt_toggle_text = itemView.findViewById(R.id.bt_toggle_text);
            lyt_expand_text = itemView.findViewById(R.id.lyt_expand_text);
            lyt_expand_text.setVisibility(View.GONE);

            bt_toggle_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleSectionText(bt_toggle_text);
                    TextView push = itemView.findViewById(R.id.title);
                    going = push.getText().toString();
                    Log.e("타이틀 값", going);
                }
            });
            nested_scroll_view = itemView.findViewById(R.id.nested_scroll_view);
        }

        void onBind(InClassHowVO data) {
            title.setText(data.getCo_name());

        }

        private void toggleSectionText(View view) {
            boolean show = toggleArrow(view);
            if (show) {
                ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                    @Override
                    public void onFinish() {
                        Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                    }
                });
            } else {
                ViewAnimation.collapse(lyt_expand_text);
            }
        }

        public boolean toggleArrow(View view) {
            if (view.getRotation() == 0) {
                view.animate().setDuration(200).rotation(180);
                return true;
            } else {
                view.animate().setDuration(200).rotation(0);
                return false;
            }
        }
    }
}
