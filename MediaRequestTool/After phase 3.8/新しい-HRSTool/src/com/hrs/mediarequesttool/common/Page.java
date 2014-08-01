package com.hrs.mediarequesttool.common;

public class Page {
	private final int offset;
	private final int limit;

	public Page(int page) {
		limit = PropertiesLoader.instance.getPageSize();

		offset = limit * page;
	}
	
	public Page(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}
}
