package com.hrs.mediarequesttool.common.pojo;

public abstract class Account {
	protected final String convert(String input) {
		return input == null ? null : input.replaceAll("－", "ー");
	}

	protected String media_name;
	protected String company_id;
	protected String media_id;
	
	public String getMedia_name() {
		return media_name;
	}

	public void setMedia_name(String media_name) {
		this.media_name = media_name;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
}
