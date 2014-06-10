package com.hrs.mediarequesttool.common;

import com.hrs.mediarequesttool.pojos.User;

public class Role {
	
	private final static String[] MEDIA_CHECKER = {"NEW", "CONFIRMING", "NG", "OK"};
	private final static String[] LEADER = {"NEW", "CONFIRMING", "NG", "OK", "PROCESSING"};
	private final static String[] DIRECTOR = {"PROCESSING", "FINISHED"};
	
	private final static String DIRECTOR_STATUS_PRIORITY = "director";
	private final static String LEADER_STATUS_PRIORITY = "leader";
	private final static String MEDIA_CHECKER_STATUS_PRIORITY = "media_checker";
	
	private String[] roles;
	private String priority;	

	public Role(Object object) {
		if (object instanceof User) {
			User user = (User) object;
			switch (user.getUser_role()) {
			case 0:
				setRoles(MEDIA_CHECKER);
				setPriority(MEDIA_CHECKER_STATUS_PRIORITY);
				break;
			case 1:
				setRoles(LEADER);
				setPriority(LEADER_STATUS_PRIORITY);
				break;
			case 2:
			default:
				setRoles(DIRECTOR);
				setPriority(DIRECTOR_STATUS_PRIORITY);
				break;
			}
		} else {
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
}
