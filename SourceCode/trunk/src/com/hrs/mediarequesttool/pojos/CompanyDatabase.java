package com.hrs.mediarequesttool.pojos;

public class CompanyDatabase {
	private int company_id;
	private String db_address;
	private int db_port;
	private String db_name;
	private String db_user_id;
	private String db_password;

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public String getDb_address() {
		return db_address;
	}

	public void setDb_address(String db_address) {
		this.db_address = db_address;
	}

	public int getDb_port() {
		return db_port;
	}

	public void setDb_port(int db_port) {
		this.db_port = db_port;
	}

	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}

	public String getDb_user_id() {
		return db_user_id;
	}

	public void setDb_user_id(String db_user_id) {
		this.db_user_id = db_user_id;
	}

	public String getDb_password() {
		return db_password;
	}

	public void setDb_password(String db_password) {
		this.db_password = db_password;
	}

}
