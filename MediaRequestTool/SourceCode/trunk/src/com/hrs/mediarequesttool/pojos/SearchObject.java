package com.hrs.mediarequesttool.pojos;

import java.io.Serializable;

public class SearchObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3393176181077014119L;
	private int page;
	private String direction;
	private String status;
	private String searchText;
	private String sort;
	private int relationRequestId;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getRelationRequestId() {
		return relationRequestId;
	}

	public void setRelationRequestId(int relationRequestId) {
		this.relationRequestId = relationRequestId;
	}
}
