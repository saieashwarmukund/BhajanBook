package org.bhajanbook.service;

public class PlaylistVO {
	String playlistKey;
	String playlistName;
	boolean owned;
	
	public PlaylistVO() {
		initialize();
	}

	public PlaylistVO(String id, String name) {
		this.playlistKey = id;
		this.playlistName = name;
	}

	public String getPlaylistKey() {
		return playlistKey;
	}

	public String getPlaylistName() {
		return playlistName;
	}

	private void initialize() {
		this.playlistKey = "";
		this.playlistName = "";
	}

	public boolean isOwned() {
		return owned;
	}
	
	
	public void setPlaylistKey(String playlistKey) {
		this.playlistKey = playlistKey;
	}
	
	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}
	public void setOwned(boolean owned) {
		this.owned = owned;
	}

}
