package com.mbm.LJSearch.pojo;

import java.io.UnsupportedEncodingException;

public class LJFieldValue {
	private byte[][] byteArray = null;
	
	private int fieldSize = 0 ;
	
	public LJFieldValue(int fieldSize) {
		this.fieldSize = fieldSize ;
		this.byteArray = new byte[fieldSize][]  ;
	}
	
	public void setPamerValues(int i , byte[] bytes){
		byteArray[i] = bytes ;
	}
	
	public String getField(int i){
		try {
			return new String(byteArray[i],"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null ;
	}

	public int getFieldSize() {
		return fieldSize;
	}

	public void setFieldSize(int fieldSize) {
		this.fieldSize = fieldSize;
	}
}
