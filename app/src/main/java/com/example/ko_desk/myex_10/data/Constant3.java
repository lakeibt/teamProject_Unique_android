package com.example.ko_desk.myex_10.data;

import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.fragment.ChatBot;
import com.example.ko_desk.myex_10.fragment.Commute;
import com.example.ko_desk.myex_10.fragment.CommuteS;
import com.example.ko_desk.myex_10.fragment.MainHome3;
import com.example.ko_desk.myex_10.fragment.Myinfo3;
import com.example.ko_desk.myex_10.fragment.MyinfoMod3;
import com.example.ko_desk.myex_10.fragment.Parking;
import com.example.ko_desk.myex_10.fragment.Salary;
import com.example.ko_desk.myex_10.navigationdrawer.NavMenuModel;

import java.util.ArrayList;

/**
 * Created by miki on 7/7/17.
 */

public class Constant3 {

    public static ArrayList<NavMenuModel> getMenuNavigasi(){
        ArrayList<NavMenuModel> menu = new ArrayList<>();

        menu.add(new NavMenuModel("메인", R.drawable.home, MainHome3.newInstance("메인")));

        menu.add(new NavMenuModel("마이페이지", R.drawable.my,
                new ArrayList<NavMenuModel.SubMenuModel>() {{
                    add(new NavMenuModel.SubMenuModel("정보 조회", Myinfo3.newInstance("조회")));
                    add(new NavMenuModel.SubMenuModel("정보 수정", MyinfoMod3.newInstance("수정")));
        }}));

        menu.add(new NavMenuModel("급여조회", R.drawable.money, Salary.newInstance("급여조회")));

        menu.add(new NavMenuModel("출퇴근 관리", R.drawable.rotating,
                new ArrayList<NavMenuModel.SubMenuModel>() {{
                    add(new NavMenuModel.SubMenuModel("출퇴근 NFC", Commute.newInstance("NFC를 태깅해 주세요")));
                    add(new NavMenuModel.SubMenuModel("출퇴근 조회", CommuteS.newInstance("출퇴근 조회")));
                }}));

        menu.add(new NavMenuModel("기능구현", R.drawable.parking,
                new ArrayList<NavMenuModel.SubMenuModel>() {{
                    add(new NavMenuModel.SubMenuModel("주차", Parking.newInstance("주차")));
                    add(new NavMenuModel.SubMenuModel("TESTLINE3", MyinfoMod3.newInstance("TESTLINE3")));
                }}));

        menu.add(new NavMenuModel("챗봇", R.drawable.chat, ChatBot.newInstance("챗봇")));

        return menu;
    }
}
