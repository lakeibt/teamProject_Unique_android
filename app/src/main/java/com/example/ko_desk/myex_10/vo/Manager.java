package com.example.ko_desk.myex_10.vo;

import java.sql.Timestamp;

public class Manager {
    private String id;
    private String pwd;
    private String photo;
    private String name;
    private String eng_name;
    private int jumin1;
    private int jumin2;
    private int gender;
    private int frgn;
    private String nation;
    private String tel;
    private String email;
    private String address;
    private String depart;
    private String rank;
    private Timestamp r_code;
    private int entrancedate;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEng_name() {
        return eng_name;
    }
    public void setEng_name(String eng_name) {
        this.eng_name = eng_name;
    }
    public int getJumin1() {
        return jumin1;
    }
    public void setJumin1(int jumin1) {
        this.jumin1 = jumin1;
    }
    public int getJumin2() {
        return jumin2;
    }
    public void setJumin2(int jumin2) {
        this.jumin2 = jumin2;
    }
    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public int getFrgn() {
        return frgn;
    }
    public void setFrgn(int frgn) {
        this.frgn = frgn;
    }
    public String getNation() {
        return nation;
    }
    public void setNation(String nation) {
        this.nation = nation;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getDepart() {
        return depart;
    }
    public void setDepart(String depart) {
        this.depart = depart;
    }
    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public Timestamp getR_code() {
        return r_code;
    }
    public void setR_code(Timestamp r_code) {
        this.r_code = r_code;
    }
    public int getEntrancedate() {
        return entrancedate;
    }
    public void setEntrancedate(int entrancedate) {
        this.entrancedate = entrancedate;
    }
}
