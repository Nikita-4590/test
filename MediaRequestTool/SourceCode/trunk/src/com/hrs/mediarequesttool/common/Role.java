package com.hrs.mediarequesttool.common;

import com.hrs.mediarequesttool.pojos.User;

public class Role {
	
	private final static String[] MEDIA_CHECKER = {"NEW", "CONFIRMING", "NG", "OK","PROCESSING"};
//	private final static String[] LEADER = {"NEW", "CONFIRMING", "NG", "OK", "PROCESSING"};
	private final static String[] DIRECTOR = {"PROCESSING", "FINISHED"};
	
	private final static String DIRECTOR_STATUS_PRIORITY = "director";
//	private final static String LEADER_STATUS_PRIORITY = "leader";
	private final static String MEDIA_CHECKER_STATUS_PRIORITY = "media_checker";
	
	private final static String[] REQUEST_STATUS_UNREAD = {"DELETED","DESTROYED"};
	private final static String HIGHT_LIGHT_IF_DIRECTOR = "PROCESSING";
//	private final static String HIGHT_LIGHT_LEADER = "OK";
	private final static String HIGHT_LIGHT_IF_MEDIA_CHECKER = "NEW";
	private String[] roles;
	private String priority;	
	private String [] unReadStatus;
	private String hightLight;
	public Role(Object object) {
		setUnReadStatus(REQUEST_STATUS_UNREAD);
		if (object instanceof User) {
			User user = (User) object;
			switch (user.getUser_role()) {
			case 0:
				setHightLight(HIGHT_LIGHT_IF_MEDIA_CHECKER);
				setRoles(MEDIA_CHECKER);
				setPriority(MEDIA_CHECKER_STATUS_PRIORITY);
				break;
			case 1:
			default:
				setHightLight(HIGHT_LIGHT_IF_DIRECTOR);
				setRoles(DIRECTOR);
				setPriority(DIRECTOR_STATUS_PRIORITY);
				break;
			}
		} else {
			setHightLight(HIGHT_LIGHT_IF_DIRECTOR);
			setRoles(DIRECTOR);
			setPriority(DIRECTOR_STATUS_PRIORITY);
		}
	}
	
	public String[] getRoles() {
		return roles;
	}
	public void setRoles(String[] roles) {
		this.roles = roles;
	}
	
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String[] getUnReadStatus() {
		return unReadStatus;
	}

	public void setUnReadStatus(String[] unReadStatus) {
		this.unReadStatus = unReadStatus;
	}

	public String getHightLight() {
		return hightLight;
	}

	public void setHightLight(String hightLight) {
		this.hightLight = hightLight;
	}
	
}
