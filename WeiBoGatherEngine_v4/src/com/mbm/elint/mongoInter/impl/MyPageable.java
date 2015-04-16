package com.mbm.elint.mongoInter.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class MyPageable implements Pageable {
	private int offset;
	private int pageNumber;
	private int pageSize;
	private Sort sort;

	public MyPageable(int offset, int pageNumber, int pageSize, Sort sort) {
		this.offset = offset;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.sort = sort;
	}

	public int getOffset() {
		return offset;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Sort getSort() {
		return sort;
	}

}
