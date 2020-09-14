package com.example.ko_desk.myex_10.data;

import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.fragment.MainHome3;
import com.example.ko_desk.myex_10.fragment.Myinfo3;
import com.example.ko_desk.myex_10.fragment.MyinfoMod3;
import com.example.ko_desk.myex_10.fragment.SearchClass3;
import com.example.ko_desk.myex_10.navigationdrawer.NavMenuModel;

import java.util.ArrayList;

/**
 * Created by miki on 7/7/17.
 */

public class Constant3 {

    public static ArrayList<NavMenuModel> getMenuNavigasi(){
        ArrayList<NavMenuModel> menu = new ArrayList<>();

        menu.add(new NavMenuModel("관리자 메인", R.drawable.ic_beranda, MainHome3.newInstance("kuy")));

        menu.add(new NavMenuModel("관리자1", R.drawable.ic_teman,
                new ArrayList<NavMenuModel.SubMenuModel>() {{
                    add(new NavMenuModel.SubMenuModel("관리자1-1", Myinfo3.newInstance("unch")));
                    add(new NavMenuModel.SubMenuModel("관리자1-2", MyinfoMod3.newInstance("utututu")));
        }}));

        menu.add(new NavMenuModel("관리자2", R.drawable.ic_notifikasi, SearchClass3.newInstance("cuy")));
        menu.add(new NavMenuModel("관리자3", R.drawable.ic_notifikasi, SearchClass3.newInstance("cuy")));
        menu.add(new NavMenuModel("관리자4", R.drawable.ic_notifikasi, SearchClass3.newInstance("cuy")));
        menu.add(new NavMenuModel("관리자5", R.drawable.ic_notifikasi, SearchClass3.newInstance("cuy")));
        menu.add(new NavMenuModel("TESTLINE1", R.drawable.ic_teman,
                new ArrayList<NavMenuModel.SubMenuModel>() {{
                    add(new NavMenuModel.SubMenuModel("TESTLINE2", Myinfo3.newInstance("unch")));
                    add(new NavMenuModel.SubMenuModel("TESTLINE3", MyinfoMod3.newInstance("utututu")));
                }}));

        return menu;
    }
}
