package com.hrs.mediarequesttool.common;

import com.hrs.mediarequesttool.pojos.User;

public class Role {
	private static  String[] media_checker = {"ASSIGNED","CONFIRMING"};
	private static  String[] leader = {"ASSIGNED","CONFIRMING","FINISHED"};
	private static  String[] member = {"ASSIGNED","CONFIRMING","FINISHED","OK"};
	
	
	public String[] generateSQL(Object object) {
		User user = (User) object;
		String[] sql = null;
		switch(user.getUser_role()) {
			case 0 :
				sql = leader;
				break;
			case 1 :
				sql = media_checker;
				break;
			case 2 :
				sql = member;
				break;
			default :
				sql = member;
		}		
		return sql;
	}
	public String createSQL(String[] roleLst) {
		String sql = "";
		int count = 0;
		for(String i : roleLst) {
			if(count != 0) {
				sql += " or ";
			}
			sql += "status_type = '" + i + "'";
			count ++;
		}
		return sql;
	}
}
