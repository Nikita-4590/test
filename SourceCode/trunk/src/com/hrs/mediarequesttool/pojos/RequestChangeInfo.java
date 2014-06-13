package com.hrs.mediarequesttool.pojos;

public class RequestChangeInfo {
	private int relation_request_id;
	private String status;
	private String status_description;
	private String renkei_date;
	private int director_id;
	private String director_name;
	
	public int getRelation_request_id() {
		return relation_request_id;
	}
	public void setRelation_request_id(int relation_request_id) {
		this.relation_request_id = relation_request_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus_description() {
		return status_description;
	}
	public void setStatus_description(String status_description) {
		this.status_description = status_description;
	}
	public String getRenkei_date() {
		return renkei_date;
	}
	public void setRenkei_date(String renkei_date) {
		this.renkei_date = renkei_date;
	}
	public int getDirector_id() {
		return director_id;
	}
	public void setDirector_id(int director_id) {
		this.director_id = director_id;
	}
	public String getDirector_name() {
		return director_name;
	}
	public void setDirector_name(String director_name) {
		this.director_name = director_name;
	}
	
}
