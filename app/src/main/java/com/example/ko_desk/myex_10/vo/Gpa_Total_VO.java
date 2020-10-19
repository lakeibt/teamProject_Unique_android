package com.example.ko_desk.myex_10.vo;

import java.io.Serializable;

// 성적
public class Gpa_Total_VO implements Serializable {

	private String st_no; // 학번
	private String gpa_semester; // 학기
	private float gpa_total; // 평점
	private int rnum;

	public String getGpa_semester() {
		return gpa_semester;
	}

	public void setGpa_semester(String gpa_semester) {
		this.gpa_semester = gpa_semester;
	}

	public String getSt_no() {
		return st_no;
	}

	public void setSt_no(String st_no) {
		this.st_no = st_no;
	}

	public float getGpa_total() {
		return gpa_total;
	}

	public void setGpa_total(float gpa_total) {
		this.gpa_total = gpa_total;
	}

	public int getRnum() {
		return rnum;
	}

	public void setRnum(int rnum) {
		this.rnum = rnum;
	}
}