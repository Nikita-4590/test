package com.hrs.mediarequesttool.common;

import com.hrs.mediarequesttool.pojos.User;

public class Role {
	private final static String[] MEDIA_CHECKER = { "ASSIGNED", "CONFIRMING" };
	private final static String[] LEADER = {"NEW", "ASSIGNED", "CONFIRMING", "OK", "NG", "FINISHED", "DELETED"};
	private final static String[] MEMBER = { "ASSIGNED", "CONFIRMING", "FINISHED",
			"OK" };

	public String[] generateSQL(Object object) {
		if (object instanceof User) {
			User user = (User) object;
			String[] sql = null;
			switch (user.getUser_role()) {
			case 0:
				sql = LEADER;
				break;
			case 1:
				sql = MEDIA_CHECKER;
				break;
			case 2:
				sql = MEMBER;
				break;
			default:
				sql = MEMBER;
				break;
			}
			return sql;
		} else {
			// ...
			return MEMBER;
		}
	}
	
}
