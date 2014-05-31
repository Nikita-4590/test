package com.hrs.mediarequesttool.common;

import com.hrs.mediarequesttool.pojos.User;

public class Role {
	private final static String[] MEDIA_CHECKER = { "ASSIGNED", "CONFIRMING" };
	private final static String[] LEADER = { "NEW", "ASSIGNED", "CONFIRMING",
			"OK", "NG", "FINISHED", "DELETED" };
	private final static String[] MEMBER = { "ASSIGNED", "CONFIRMING",
			"FINISHED", "OK" };
	private final static String MEMBER_STATUS_PRIORITY = "created_at";
	private final static String LEADER_STATUS_PRIORITY = "created_at";
	private final static String MEDIA_CHECKER_STATUS_PRIORITY = "created_at";

	public String getSortPriority(Object object) {
		return null;
	}

	private String[] roles;
	private String priority;

	public String[] getRoles() {
		return roles;
	}

	public Role(Object object) {
		if (object instanceof User) {
			User user = (User) object;
			switch (user.getUser_role()) {
			case 0:
				setRoles(LEADER);
				setPriority(LEADER_STATUS_PRIORITY);
				break;
			case 1:
				setRoles(MEDIA_CHECKER);
				setPriority(MEDIA_CHECKER_STATUS_PRIORITY);
				break;
			case 2:
			default:
				setRoles(MEMBER);
				setPriority(MEMBER_STATUS_PRIORITY);
				break;
			}
		} else {
			setRoles(MEMBER);
			setPriority(MEMBER_STATUS_PRIORITY);
		}
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
