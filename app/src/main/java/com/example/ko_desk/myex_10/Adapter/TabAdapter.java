package com.example.ko_desk.myex_10.Adapter;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ko_desk.myex_10.AraFragmentPage;
import com.example.ko_desk.myex_10.MyPageFragmentPage;


public class TabAdapter extends FragmentPagerAdapter {

    //생성자
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    //getItem() 오버라이드 : 생성했던 Fragment(추가했던 newInstance()메소드)들을 가져온다
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0://첫 번째 탭
                return AraFragmentPage.newInstance();
            case 1://두 번째 탭
                return MyPageFragmentPage.newInstance();
            default:
                return null;
        }

    }


    //getCount() 오버라이드 : Fragment의 페이지 개수 리턴
    private static int PAGE_NUMBER = 1;

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }



    //getPageTitle() 오버라이드 : 탭에 이름 부여
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "ARA";
            case 1:
                return "MENU";
            default:
                return null;
        }

    }
}
