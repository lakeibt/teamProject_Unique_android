package com.example.ko_desk.myex_10.data;

import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.fragment.MainHome;
import com.example.ko_desk.myex_10.fragment.SearchClass;
import com.example.ko_desk.myex_10.fragment.Myinfo;
import com.example.ko_desk.myex_10.fragment.MyinfoMod;
import com.example.ko_desk.myex_10.navigationdrawer.NavMenuModel;

import java.util.ArrayList;

/**
 * Created by miki on 7/7/17.
 */

public class Constant {

    public static ArrayList<NavMenuModel> getMenuNavigasi(){
        ArrayList<NavMenuModel> menu = new ArrayList<>();

        menu.add(new NavMenuModel("메인", R.drawable.ic_beranda, MainHome.newInstance("kuy")));

        menu.add(new NavMenuModel("내 정보", R.drawable.ic_teman,
                new ArrayList<NavMenuModel.SubMenuModel>() {{
                    add(new NavMenuModel.SubMenuModel("내정보 보기", Myinfo.newInstance("unch")));
                    add(new NavMenuModel.SubMenuModel("내정보 수정", MyinfoMod.newInstance("utututu")));
        }}));

        menu.add(new NavMenuModel("수강과목조회", R.drawable.ic_notifikasi, SearchClass.newInstance("cuy")));
        menu.add(new NavMenuModel("성적 조회", R.drawable.ic_notifikasi, SearchClass.newInstance("cuy")));
        menu.add(new NavMenuModel("쪽지", R.drawable.ic_notifikasi, SearchClass.newInstance("cuy")));
        menu.add(new NavMenuModel("쳇봇", R.drawable.ic_notifikasi, SearchClass.newInstance("cuy")));
        menu.add(new NavMenuModel("TESTLINE", R.drawable.ic_teman,
                new ArrayList<NavMenuModel.SubMenuModel>() {{
                    add(new NavMenuModel.SubMenuModel("TESTLINE", Myinfo.newInstance("unch")));
                    add(new NavMenuModel.SubMenuModel("TESTLINE", MyinfoMod.newInstance("utututu")));
                }}));

        return menu;
    }
}
