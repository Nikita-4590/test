package com.hrs.mediarequesttool.pojos;

public class Account {

	public final static int TABAITAI = 0;
	public final static int UKERUKUN = 1;
	public final static int FAC = 2;
	private int id;
	private String login_id;
	private String admin_id;
	private String created_at;
	private int type;
	private String media_name;
	private String media_id;
	private String shop_name;
	private String url;
	private int company_id;
	private boolean status;
	private String crawl_date;
	private String sync_entry_date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}

	public String getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMedia_name() {
		return media_name;
	}

	public void setMedia_name(String media_name) {
		this.media_name = media_name;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getCrawl_date() {
		return crawl_date;
	}

	public void setCrawl_date(String crawl_date) {
		this.crawl_date = crawl_date;
	}

	public String getSync_entry_date() {
		return sync_entry_date;
	}

	public void setSync_entry_date(String sync_entry_date) {
		this.sync_entry_date = sync_entry_date;
	}
}
