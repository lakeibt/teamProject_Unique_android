package com.example.ko_desk.myex_10.vo;

import java.io.Serializable;
import java.util.List;

public class JsonVO implements Serializable {
    private Object vo;
    private String keyword;     //서비스를 구분하기위한 키워드
    private List<AttendVO> attend;  //출결정보
    private List<ScoreVO> score;   //성적정보
    private StudentVO myInfo;   //내 정보
    private List<Stu_Reg_Lec_VO> timetable;     //시간표
    private List<List<Stu_Reg_Lec_VO>> totaltimetable;  //전체시간표

    public List<List<Stu_Reg_Lec_VO>> getTotaltimetable() {
        return totaltimetable;
    }

    public void setTotaltimetable(List<List<Stu_Reg_Lec_VO>> totaltimetable) {
        this.totaltimetable = totaltimetable;
    }

    public List<Stu_Reg_Lec_VO> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<Stu_Reg_Lec_VO> timetable) {
        this.timetable = timetable;
    }

    public StudentVO getMyInfo() {
        return myInfo;
    }

    public void setMyInfo(StudentVO myInfo) {
        this.myInfo = myInfo;
    }

    public List<ScoreVO> getScore() {
        return score;
    }

    public void setScore(List<ScoreVO> score) {
        this.score = score;
    }

    public List<AttendVO> getAttend() {
        return attend;
    }

    public void setAttend(List<AttendVO> attend) {
        this.attend = attend;
    }

    public Object getVo() {
        return vo;
    }

    public void setVo(Object vo) {
        this.vo = vo;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
