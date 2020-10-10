package com.example.ko_desk.myex_10.vo;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by psn on 2018-02-07.
 */
/* 스프링 컨트롤러와 JSON 연동을 위해 Map<키,값>으로 받기위한 클래스 */
public class Data {
    private String data1;
    private String data2;
    private String data3;
    private String data4;
    private String data5;
    private String data6;
    private String data7;
    private String data8;
    private String data9;
    private String data10;
    private String data11;
    private String data12;
    private String data13;
    private String data14;
    private String data15;

    @SerializedName("member")   /*import후 오른쪽 위 sync now를 클릭해야 동기화됨*/
    private Map<String, String> member = new HashMap<String, String>();

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data13) { this.data3 = data3; }

    public String getData4() { return data4; }

    public void setData4(String data4) { this.data4 = data4; }

    public String getData5() { return data5; }

    public void setData5(String data5) { this.data5 = data5; }

    public String getData6() { return data6; }

    public void setData6(String data6) { this.data6 = data6; }

    public String getData7() { return data7; }

    public void setData7(String data7) { this.data7 = data7; }

    public String getData8() { return data8; }

    public void setData8(String data8) { this.data8 = data8; }

    public String getData9() { return data9; }

    public void setData9(String data9) { this.data9 = data9; }

    public String getData10() { return data10; }

    public void setData10(String data10) { this.data10 = data10; }

    public String getData11() { return data11; }

    public void setData11(String data11) { this.data11 = data11; }

    public String getData12() { return data12; }

    public void setData12(String data12) { this.data12 = data12; }

    public String getData13() { return data13; }

    public void setData13(String data13) { this.data13 = data13; }

    public String getData14() { return data14; }

    public void setData14(String data14) { this.data14 = data14; }

    public String getData15() { return data15; }

    public void setData15(String data15) { this.data15 = data15; }

    public Map<String, String> getMember() { return member; }

    public void setMember(Map<String, String> member) { this.member = member; }

}
