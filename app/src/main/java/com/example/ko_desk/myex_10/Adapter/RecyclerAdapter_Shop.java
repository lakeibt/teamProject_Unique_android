package com.example.ko_desk.myex_10.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.activity.Shopdt;
import com.example.ko_desk.myex_10.vo.TableinfoVO;

import java.util.ArrayList;

public class RecyclerAdapter_Shop extends RecyclerView.Adapter<RecyclerAdapter_Shop.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<TableinfoVO> listData = new ArrayList<>();
    String imageUrl;
    private ImageView image;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
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

    public void addItem(TableinfoVO data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView person, price, title, num, id, myname;
        private CardView card;
        private NestedScrollView nested_scroll_view;
        private String going, name1, id1, pushname;
        LinearLayout item;




        ItemViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.num);
            title = itemView.findViewById(R.id.title);
            card = itemView.findViewById(R.id.card);
            person = itemView.findViewById(R.id.person);
            item = itemView.findViewById(R.id.item);
            id = itemView.findViewById(R.id.myid);
            myname = itemView.findViewById(R.id.myname);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView num = itemView.findViewById(R.id.num);
                    TextView name = itemView.findViewById(R.id.person);
                    TextView id = itemView.findViewById(R.id.myid);
                    going = num.getText().toString();
                    name1 = name.getText().toString();

                    id1 = id.getText().toString();
                    Log.e("넘버 값", going);
                    Intent intent = new Intent(view.getContext() , Shopdt.class);
                    Log.e("이름 값", pushname);
                    Log.e("아이디 값", id1);
                    intent.putExtra("num",going);
                    intent.putExtra("name",pushname);
                    intent.putExtra("id",id1);
                    view.getContext().startActivity(intent);
                }
            });
            nested_scroll_view = itemView.findViewById(R.id.nested_scroll_view);
        }

        void onBind(TableinfoVO data) {
            Log.e("넘버값 설정", String.valueOf(data.getNum()));
            num.setText(String.valueOf(data.getNum()));
            title.setText(data.getTitle());
            person.setText(data.getName());
            id.setText(data.getId());
            myname.setText(data.getMyname());

            pushname = data.getMyname();
        }
    }
}
