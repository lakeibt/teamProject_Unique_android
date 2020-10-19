package com.example.ko_desk.myex_10.vo;

import java.io.Serializable;

public class ScoreVO implements Serializable {

    private String id;  //아이디
    private String co_name;   //과목명
    private String p_name;  //과목 교수

    private int co_year;  //년도
    private int co_semester;  //학기
    private String grades_code;   // 학점
    private int grades_score;   // 성적

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCo_name() {
        return co_name;
    }

    public void setCo_name(String co_name) {
        this.co_name = co_name;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public int getCo_year() {
        return co_year;
    }

    public void setCo_year(int co_year) {
        this.co_year = co_year;
    }

    public int getCo_semester() {
        return co_semester;
    }

    public void setCo_semester(int co_semester) {
        this.co_semester = co_semester;
    }

    public String getGrades_code() {
        return grades_code;
    }

    public void setGrades_code(String grades_code) {
        this.grades_code = grades_code;
    }

    public int getGrades_score() {
        return grades_score;
    }

    public void setGrades_score(int grades_score) {
        this.grades_score = grades_score;
    }
}
