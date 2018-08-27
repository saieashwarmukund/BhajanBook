package org.bhajanbook.service;

public class BaseVO {
	int status;

	String mesg;

	public BaseVO() {
		initialize();
	}

	public String getMesg() {
		return mesg;
	}

	public int getStatus() {
		return status;
	}
	private void initialize() {
		this.status = 0;
		this.mesg = "";
	}
	
	public void setMesg(String mesg) {
		this.mesg = mesg;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}



}
