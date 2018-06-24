package org.bhajanbook.service;

public class BhajanTitleVO {
	int id;
	String bhajanTitle;
	
	public BhajanTitleVO() {
		initialize();
	}
	
	private void initialize() {
		this.id = 0;
		this.bhajanTitle = "";
	}
	public BhajanTitleVO(int id, String hdr) {
		this.id = id;
		this.bhajanTitle = hdr;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBhajanTitle() {
		return bhajanTitle;
	}
	public void setBhajanTitle(String hdr) {
		this.bhajanTitle = hdr;
	}
	@Override
	public String toString() {
		return new StringBuffer("Id : ").append(this.id)
				.append(" Hdr : ").append(this.bhajanTitle).toString();
	}



}
