package com.mbm.LJSearch.pojo;

public class PageSplitPojo {
	private String name;

	private Boolean hasHref;

	private int value;

	public PageSplitPojo(String name, int value , boolean hasHref) {
		this.name = name;
		this.hasHref = hasHref;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getHasHref() {
		return hasHref;
	}

	public void setHasHref(Boolean hasHref) {
		this.hasHref = hasHref;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
