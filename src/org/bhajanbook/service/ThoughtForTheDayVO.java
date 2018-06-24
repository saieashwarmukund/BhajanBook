package org.bhajanbook.service;

public class ThoughtForTheDayVO {
	private String thought;
	
	public ThoughtForTheDayVO() {
		this.thought = "";
	}
	
	public String getThought() {
		return this.thought;
	}
	
	public void setThought(String thought) {
		this.thought = thought;
	}
	
	@Override
	public String toString() {
		return new StringBuffer("Thought : ").append(this.thought).toString();
	}	
}
