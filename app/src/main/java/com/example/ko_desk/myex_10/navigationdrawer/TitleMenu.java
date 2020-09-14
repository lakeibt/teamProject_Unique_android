package com.example.ko_desk.myex_10.navigationdrawer;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by miki on 7/7/17.
 */

public class TitleMenu extends ExpandableGroup<SubTitle> {
    private int imageResource = 0;

    public TitleMenu(String title, List<SubTitle> items, int imageResource) {
        super(title, items);
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }
}
