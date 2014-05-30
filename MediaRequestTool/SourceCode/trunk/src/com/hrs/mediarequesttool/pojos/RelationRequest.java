package com.hrs.mediarequesttool.pojos;

public class RelationRequest {
	private int relation_request_id;
	
	private int company_auto_id;
	private int media_id;
	private String requester_name;
	
	private String requester_mail;
	private String requester_phone;
	private String url;
	
	private String company_id;
	
	private String login_id_1;
	private String login_id_2;
	private String login_password_1;
	private String login_password_2;
		
	
	private String crawl_date;
	private String other_comment;
	private String status;
	private String assign_user_name;
	
	private int update_user_id;
	private String created_at;
	private String updated_at;
	
	private String company_name;
	private String media_name;
	private String status_description;
	
	/**
	 * @return the requester_name
	 */
	public String getRequester_name() {
		return requester_name;
	}
	/**
	 * @param requester_name the requester_name to set
	 */
	public void setRequester_name(String requester_name) {
		this.requester_name = requester_name;
	}
	/**
	 * @return the media_id
	 */
	
	/**
	 * @return the requester_mail
	 */
	public String getRequester_mail() {
		return requester_mail;
	}
	/**
	 * @param requester_mail the requester_mail to set
	 */
	public void setRequester_mail(String requester_mail) {
		this.requester_mail = requester_mail;
	}
	/**
	 * @return the requester_phone
	 */
	public String getRequester_phone() {
		return requester_phone;
	}
	/**
	 * @param requester_phone the requester_phone to set
	 */
	public void setRequester_phone(String requester_phone) {
		this.requester_phone = requester_phone;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the login_id_1
	 */
	public String getLogin_id_1() {
		return login_id_1;
	}
	/**
	 * @param login_id_1 the login_id_1 to set
	 */
	public void setLogin_id_1(String login_id_1) {
		this.login_id_1 = login_id_1;
	}
	/**
	 * @return the login_id_2
	 */
	public String getLogin_id_2() {
		return login_id_2;
	}
	/**
	 * @param login_id_2 the login_id_2 to set
	 */
	public void setLogin_id_2(String login_id_2) {
		this.login_id_2 = login_id_2;
	}
	/**
	 * @return the login_password_1
	 */
	public String getLogin_password_1() {
		return login_password_1;
	}
	/**
	 * @param login_password_1 the login_password_1 to set
	 */
	public void setLogin_password_1(String login_password_1) {
		this.login_password_1 = login_password_1;
	}
	/**
	 * @return the login_password_2
	 */
	public String getLogin_password_2() {
		return login_password_2;
	}
	/**
	 * @param login_password_2 the login_password_2 to set
	 */
	public void setLogin_password_2(String login_password_2) {
		this.login_password_2 = login_password_2;
	}
	/**
	 * @return the crawl_date
	 */
	public String getCrawl_date() {
		return crawl_date;
	}
	/**
	 * @param crawl_date the crawl_date to set
	 */
	public void setCrawl_date(String crawl_date) {
		this.crawl_date = crawl_date;
	}
	/**
	 * @return the other_comment
	 */
	public String getOther_comment() {
		return other_comment;
	}
	/**
	 * @param other_comment the other_comment to set
	 */
	public void setOther_comment(String other_comment) {
		this.other_comment = other_comment;
	}
	/**
	 * @return the status
	 */
	
	/**
	 * @return the assign_user_name
	 */
	public String getAssign_user_name() {
		return assign_user_name;
	}
	/**
	 * @param assign_user_name the assign_user_name to set
	 */
	public void setAssign_user_name(String assign_user_name) {
		this.assign_user_name = assign_user_name;
	}
	/**
	 * @return the update_user_id
	 */
	public int getUpdate_user_id() {
		return update_user_id;
	}
	/**
	 * @param update_user_id the update_user_id to set
	 */
	public void setUpdate_user_id(int update_user_id) {
		this.update_user_id = update_user_id;
	}
	/**
	 * @return the created_at
	 */
	public String getCreated_at() {
		return created_at;
	}
	/**
	 * @param created_at the created_at to set
	 */
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	/**
	 * @return the updated_at
	 */
	public String getUpdated_at() {
		return updated_at;
	}
	/**
	 * @param updated_at the updated_at to set
	 */
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	/**
	 * @return the company_name
	 */
	public String getCompany_name() {
		return company_name;
	}
	/**
	 * @param company_name the company_name to set
	 */
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	/**
	 * @return the media_name
	 */
	public String getMedia_name() {
		return media_name;
	}
	/**
	 * @param media_name the media_name to set
	 */
	public void setMedia_name(String media_name) {
		this.media_name = media_name;
	}	
	/**
	 * @return the media_id
	 */
	public int getMedia_id() {
		return media_id;
	}
	public int getCompany_auto_id() {
		return company_auto_id;
	}
	public void setCompany_auto_id(int company_auto_id) {
		this.company_auto_id = company_auto_id;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	/**
	 * @param media_id the media_id to set
	 */
	public void setMedia_id(int media_id) {
		this.media_id = media_id;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the relation_request_id
	 */
	public int getRelation_request_id() {
		return relation_request_id;
	}
	/**
	 * @param relation_request_id the relation_request_id to set
	 */
	public void setRelation_request_id(int relation_request_id) {
		this.relation_request_id = relation_request_id;
	}
	/**
	 * @return the company_company_id
	 */
	
	/**
	 * @return the status_description
	 */
	public String getStatus_description() {
		return status_description;
	}
	/**
	 * @param status_description the status_description to set
	 */
	public void setStatus_description(String status_description) {
		this.status_description = status_description;
	}
}
