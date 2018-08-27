package org.bhajanbook.service;

import java.util.ArrayList;
import java.util.List;

public class ThoughtForTheDayVO {
	private String thought;
	private List<PlaylistVO> userPlaylistList = new ArrayList<PlaylistVO>();
	
	public ThoughtForTheDayVO() {
		this.thought = "";
	}

	public String getThought() {
		return this.thought;
	}

	public List<PlaylistVO> getUserPlaylistList() {
		return userPlaylistList;
	}
	
	public void setThought(String thought) {
		this.thought = thought;
	}
	
	public void setUserPlaylistList(List<PlaylistVO> userPlaylistList) {
		this.userPlaylistList = userPlaylistList;
	}
	
	@Override
	public String toString() {
		return new StringBuffer("Thought : ").append(this.thought).toString();
	}	
}
