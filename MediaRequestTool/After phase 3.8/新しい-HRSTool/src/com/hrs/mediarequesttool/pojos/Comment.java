package com.hrs.mediarequesttool.pojos;

import java.util.List;

import com.hrs.mediarequesttool.pojos.CommentProperty;

public class Comment {
	private int request_comment_id;
	private int request_id;
	private int user_id;
	private String comment_reason;
	private String old_value;
	private String new_value;
	private String created_at;
	private String user_login_id;
	private String user_name;
	
	private List<CommentProperty> properties;
	
	public int getRequest_comment_id() {
		return request_comment_id;
	}
	
	public void setRequest_comment_id(int request_comment_id) {
		this.request_comment_id = request_comment_id;
	}
	
	public int getRequest_id() {
		return request_id;
	}
	
	public void setRequest_id(int request_id) {
		this.request_id = request_id;
	}
	
	public int getUser_id() {
		return user_id;
	}
	
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public String getComment_reason() {
		return comment_reason;
	}
	
	public void setComment_reason(String comment_reason) {
		this.comment_reason = comment_reason;
	}
	
	public String getOld_value() {
		return old_value;
	}
	
	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}
	
	public String getNew_value() {
		return new_value;
	}
	
	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}

	public List<CommentProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<CommentProperty> properties) {
		this.properties = properties;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUser_login_id() {
		return user_login_id;
	}

	public void setUser_login_id(String user_login_id) {
		this.user_login_id = user_login_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
}
