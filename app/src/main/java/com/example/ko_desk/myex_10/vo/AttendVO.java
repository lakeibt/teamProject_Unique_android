package com.example.ko_desk.myex_10.vo;

import java.io.Serializable;

// 출결
public class AttendVO implements Serializable {

	private int attend_no; // 출석번호
	private String st_no; // 학번
	private int lec_no; // 강의번호
	private int attend_fl; // 출결여부
	private String lec_dt; // 강의날짜



	private String lec_name;	//강의명

	public String getLec_name() {
		return lec_name;
	}

	public void setLec_name(String lec_name) {
		this.lec_name = lec_name;
	}
	public int getAttend_no() {
		return attend_no;
	}

	public void setAttend_no(int attend_no) {
		this.attend_no = attend_no;
	}

	public int getLec_no() {
		return lec_no;
	}

	public void setLec_no(int lec_no) {
		this.lec_no = lec_no;
	}

	public int getAttend_fl() {
		return attend_fl;
	}

	public void setAttend_fl(int attend_fl) {
		this.attend_fl = attend_fl;
	}

	public String getSt_no() {
		return st_no;
	}

	public void setSt_no(String st_no) {
		this.st_no = st_no;
	}

	public String getLec_dt() {
		return lec_dt;
	}

	public void setLec_dt(String lec_dt) {
		this.lec_dt = lec_dt;
	}
}