package com.weibo.common.utils;

/**
 * Copyright (c) 2005-2010 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: Page.java 1183 2010-08-28 08:05:49Z calvinxiu $
 */
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dw.party.mbmsupport.dto.Result;
import com.google.common.collect.Lists;
/**
 * 返回搜索對象的封狀類，包含了result,用于返回搜索之后结果状态,为的是出现异常时给予返回通用的搜索结果
 * @param <T> Page
 * @author zel
 */
public class ResultPage<T> extends Result implements Serializable{
	//--排序方式--//
	/**
	 * --排序方式--
	 */
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	//-- 页参数 --//
	/**
	 * -- 页参数 --
	 */
	protected int pageNo = 1;
	protected int pageSize = 15;
	protected String orderBy = null;
	protected String order = null;
	protected boolean autoCount = true;

	//-- 用来存放Page中的结果集合 --//
	/**
	 * -- 用来存放Page中的结果集合 --
	 */
	//Lists.newArrayList();	
	protected List<T> result =Lists.newArrayList(); 
	protected long totalCount = -1;

	//-- 构造方法--//
	/**
	 * 空构造方法
	 */
	public ResultPage() {
		this.pageSize=50;
	}
    /**
     * 初始化每页要存放的条目数
     * @param pageSize
     */
	public ResultPage(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 得到页号
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置页号
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * 得到重新设置了页号的page对象
	 */
	public ResultPage<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	/**
	 * 得到每页当中的result中有多少条结果
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置一页中预设的条目数
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 得到设置了每页中所要存放的条目数的page对象
	 */
	public ResultPage<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/**
	 * 得到当前页的第一条记录
	 */
	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}

	/**
	 * 得到排序字段
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 得到设置了排序字段的page对象 
	 */
	public ResultPage<T> orderBy(final String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}

	/**
	 * 得到排序方式，是升序还是降序
	 */
	public String getOrder() {
		return order;
	}

	/**
	 *
	 * 设置排序方式
	 * @param order
	 */
	public void setOrder(final String order) {
		String lowcaseOrder = StringUtils.lowerCase(order);

		String[] orders = StringUtils.split(lowcaseOrder, ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
				throw new IllegalArgumentException("不合理的排序方式，" + orderStr + "即不是升序也不是降序");
			}
		}
		this.order = lowcaseOrder;
	}

	/**
	 * 算是设置排序方式
	 */
	public ResultPage<T> order(final String theOrder) {
		setOrder(theOrder);
		return this;
	}

	/**
	 * 按某个字段排序是否已设置
	 */
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}

	/**
	 * 记数是否设置
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 设置是否自动记数
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	/**
	 * 设置是否记数，并返回当前对象
	 */
	public ResultPage<T> autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	/**
	 * 得到page中的结果集
	 */
	public List<T> getResult() {
		return result;
	}
	/**
	 * 设置结果集
	 */
	public void setResult(final List<T> result) {
		this.result =result;
	}
	
//	/**
//	 * 设置结果集
//	 */
//	public void setResult(final List<T> result) {
//		this.result = (ArrayList<T>)result;
//	}

	/**
	 * 得到某总结果集的总条数，可用于分页
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置某结果集的总条目数
	 */
	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 得到总页数
	 */
	public long getTotalPages() {
		if (totalCount < 0) {
			return -1;
		}

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是不是还有下一页 
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 得到下一页的页号,没有下一页的话，返回当前页的页号
	 */
	public int getNextPage() {
		if (isHasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 判断是否有上一页
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 得到上一页的页号，若没有上一页，返回当前页。
	 */
	public int getPrePage() {
		if (isHasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}
}
