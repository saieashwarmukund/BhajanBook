package org.bhajanbook.service;

public class BhajanTitleVO {
	int id;
	String bhajanTitle;
	String shruti;
	
	public BhajanTitleVO() {
		initialize();
	}
	
	public BhajanTitleVO(int id, String hdr) {
		this.id = id;
		this.bhajanTitle = hdr;
	}
	
	public String getBhajanTitle() {
		return bhajanTitle;
	}
	
	public int getId() {
		return id;
	}
	public String getShruti() {
		return shruti;
	}
	private void initialize() {
		this.id = 0;
		this.bhajanTitle = "";
	}
	public void setBhajanTitle(String hdr) {
		this.bhajanTitle = hdr;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setShruti(String shruti) {
		this.shruti = shruti;
	}
	@Override
	public String toString() {
		return new StringBuffer("Id : ").append(this.id)
				.append(" Hdr : ").append(this.bhajanTitle).toString();
	}



}
