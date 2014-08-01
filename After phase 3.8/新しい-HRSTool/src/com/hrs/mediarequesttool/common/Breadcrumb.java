package com.hrs.mediarequesttool.common;

public class Breadcrumb {
	private String text;
	
	private String url;
	
	public Breadcrumb(String text) {
		this.text = text;
		this.url = null;
	}
	
	public Breadcrumb(String text, String url) {
		this.text = text;
		this.url = url;
	}
	
	public boolean hasLink() {
		return this.url != null;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getUrl() {
		return this.url;
	}
}
