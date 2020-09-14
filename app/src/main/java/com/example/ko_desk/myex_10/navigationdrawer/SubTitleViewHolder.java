package com.example.ko_desk.myex_10.navigationdrawer;

import android.view.View;
import android.widget.TextView;

import com.example.ko_desk.myex_10.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by miki on 7/7/17.
 */

public class SubTitleViewHolder extends ChildViewHolder {
    private TextView subTitleTextView;

    public SubTitleViewHolder(View itemView) {
        super(itemView);
        subTitleTextView = (TextView) itemView.findViewById(R.id.main_nav_submenu_item_title);
    }

    public void setSubTitletName(String name) {
        subTitleTextView.setText(name);
    }
}
