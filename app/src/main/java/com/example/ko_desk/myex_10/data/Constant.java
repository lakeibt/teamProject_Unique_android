package com.example.ko_desk.myex_10.data;

import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.fragment.ChatBot;
import com.example.ko_desk.myex_10.fragment.GradesCheck;
import com.example.ko_desk.myex_10.fragment.MainHome;
import com.example.ko_desk.myex_10.fragment.Message;
import com.example.ko_desk.myex_10.fragment.SearchClass;
import com.example.ko_desk.myex_10.fragment.Myinfo;
import com.example.ko_desk.myex_10.fragment.MyinfoMod;
import com.example.ko_desk.myex_10.navigationdrawer.NavMenuModel;

import java.util.ArrayList;

/**
 * 수정날짜  2020/09/16 마지막 수정자 최병현
 */

public class Constant {

    public static ArrayList<NavMenuModel> getMenuNavigasi(){
        ArrayList<NavMenuModel> menu = new ArrayList<>();

        menu.add(new NavMenuModel("메인", R.drawable.home, MainHome.newInstance("메인")));

        menu.add(new NavMenuModel("내 정보", R.drawable.my,
                new ArrayList<NavMenuModel.SubMenuModel>() {{
                    add(new NavMenuModel.SubMenuModel("내정보 보기", Myinfo.newInstance("내정보 보기")));
                    add(new NavMenuModel.SubMenuModel("내정보 수정", MyinfoMod.newInstance("내정보 수정")));
        }}));

        menu.add(new NavMenuModel("수강과목조회", R.drawable.pan, SearchClass.newInstance("수강과목조회")));
        menu.add(new NavMenuModel("성적 조회", R.drawable.check, GradesCheck.newInstance("성적조회")));
        menu.add(new NavMenuModel("쪽지", R.drawable.message, Message.newInstance("메시지")));
        menu.add(new NavMenuModel("챗봇", R.drawable.chat, ChatBot.newInstance("챗봇")));
        menu.add(new NavMenuModel("TESTLINE", R.drawable.test,
                new ArrayList<NavMenuModel.SubMenuModel>() {{
                    add(new NavMenuModel.SubMenuModel("TESTLINE1", Myinfo.newInstance("테스트라인1")));
                    add(new NavMenuModel.SubMenuModel("TESTLINE2", MyinfoMod.newInstance("테스트라인2")));
                }}));

        return menu;
    }
}
