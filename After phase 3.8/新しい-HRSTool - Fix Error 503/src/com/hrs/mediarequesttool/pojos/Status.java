package com.hrs.mediarequesttool.pojos;

public class Status {
	
	private String status_type;
	private String description;
	private String created_dt;
	private String updated_dt;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the created_dt
	 */
	public String getCreated_dt() {
		return created_dt;
	}
	/**
	 * @param created_dt the created_dt to set
	 */
	public void setCreated_dt(String created_dt) {
		this.created_dt = created_dt;
	}
	/**
	 * @return the status_type
	 */
	public String getStatus_type() {
		return status_type;
	}
	/**
	 * @param status_type the status_type to set
	 */
	public void setStatus_type(String status_type) {
		this.status_type = status_type;
	}
	/**
	 * @return the updated_dt
	 */
	public String getUpdated_dt() {
		return updated_dt;
	}
	/**
	 * @param updated_dt the updated_dt to set
	 */
	public void setUpdated_dt(String updated_dt) {
		this.updated_dt = updated_dt;
	}
}	
