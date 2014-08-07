package com.hrs.mediarequesttool.common;

import java.util.List;

public class PagingResult<T> {
	private int currentPage;

	private int totalPage;

	private List<T> list;

	private boolean exceed;

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setPage(int currentPage, int totalRecord, int pageSize) {
		this.currentPage = currentPage;
		this.totalPage = totalRecord / pageSize
				+ (totalRecord % pageSize == 0 ? 0 : 1);

		this.exceed = this.currentPage >= this.totalPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public boolean isExceed() {
		return exceed;
	}
}
