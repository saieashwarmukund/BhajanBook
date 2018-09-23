

package org.bhajanbook.service;

import java.util.ArrayList;
import java.util.List;

public class UserRoleVO extends BaseVO {
	String userId;

	String firstName;

	String lastName;

	List<String> roleList;
	
	List<PlaylistVO> userPlaylistList;

	public UserRoleVO() {
		initialize();
	}
	public UserRoleVO(String userId, String first, String last) {
		super();
		this.firstName = first;
		this.lastName = last;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public String getUserId() {
		return userId;
	}

	public List<PlaylistVO> getUserPlaylistList() {
		return userPlaylistList;
	}
	private void initialize() {
		this.userId = "";
		this.firstName = "";
		this.lastName = "";
		this.roleList = new ArrayList<String>();
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserPlaylistList(List<PlaylistVO> userPlaylistList) {
		this.userPlaylistList = userPlaylistList;
	}
}
