package com.dw.party.mbmsupport.dto;

/**
 * 搜索操作时候的排序对象封装
 * 
 * @author zel
 */
public class SupportSort implements java.io.Serializable {
	// 序列化标志
	private static long serialVersionUID = 39292093920320L;
	/**
	 * 排序字段名
	 */
	private String sortArgName;

	public SupportSort(String sortArgName, String sortType) {
		this.sortArgName = sortArgName;
		this.sortType = sortType;
	}

	public SupportSort() {

	}

	/**
	 * 获得排序的字段名，如grabTime,publishTime
	 * 
	 * @return
	 */
	public String getSortArgName() {
		return sortArgName;
	}

	/**
	 * 设置排序的字段名，如grabTime,publishTime
	 * 
	 * @return
	 */
	public void setSortArgName(String sortArgName) {
		this.sortArgName = sortArgName;
	}

	/**
	 * 获得排序的顺序，如asc,desc
	 * 
	 * @return
	 */
	public String getSortType() {
		return sortType;
	}

	/**
	 * 设置排序的顺序，如asc,desc
	 */
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	/**
	 * 排序方式
	 */
	private String sortType;

}
