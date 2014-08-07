package com.hrs.mediarequesttool.pojos;

public class Error {

	private String error_system;
	private int error_level;
	private int error_type;
	private String error_code;
	private String error_detail;
	private String error_message;
	private String error_date;

	public Error() {
		setError_system("媒体連携 アカウント管理ツール");
	}

	public String getError_system() {
		return error_system;
	}

	public void setError_system(String error_system) {
		this.error_system = error_system;
	}

	public int getError_level() {
		return error_level;
	}

	public void setError_level(int error_level) {
		this.error_level = error_level;
	}

	public int getError_type() {
		return error_type;
	}

	public void setError_type(int error_type) {
		this.error_type = error_type;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_detail() {
		return error_detail;
	}

	public void setError_detail(String error_detail) {
		this.error_detail = error_detail;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	
	public String getError_date() {
		return error_date;
	}
	
	public void setError_date(String error_date) {
		this.error_date = error_date;
	}
}
