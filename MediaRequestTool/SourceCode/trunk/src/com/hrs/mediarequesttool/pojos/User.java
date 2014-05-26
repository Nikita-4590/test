package com.hrs.mediarequesttool.pojos;

import java.io.Serializable;

public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3792988180704397311L;

	private int id;
	
	private String user_id;
	
	private String user_name;
	
	private String user_password;
	
	private String created_at;
	
	private String updated_at;
	
	private boolean enabled_flag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public boolean isEnabled_flag() {
		return enabled_flag;
	}

	public void setEnabled_flag(boolean enabled_flag) {
		this.enabled_flag = enabled_flag;
	}
	
}
