package com.mbm.LJSearch.pojo;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @项目名称：SocketDemo
 * @类名称：LJSearchResult
 * @类描述：检索结果累封装了所有结果的参数
 * @创建人：Ansj
 * @创建时间：2010-12-9 下午02:00:32
 * @修改备注：
 * @version
 * 
 */
public class LJSearchResult {

	// 命中的总数
	private int totalCount;
	// 起始位置
	private int start;
	// 当前返回的文档数
	private int size;
	// 列总数
	private int fieldSize;
	// 数组参数的长度
	private int[] pamersSize;
	// socket输入六
	DataInputStream dis = null;
	// 元素集合
	LJFieldValue[] array = null;

	// 包含的Object元素
	// private List objs = null;

	// 封装构造方法
	public LJSearchResult(DataInputStream dis) {
		this.dis = dis;
	};

	public void readSocket()
			throws IOException, InstantiationException, IllegalAccessException {

		// 空读一下
		dis.readInt();

		// 当前返回的总条数
		totalCount = dis.readInt();
		System.out.println("当前返回的总条数:" + totalCount);
		// 当前起始位置
		start = dis.readInt();
		System.out.println("当前起始位置:" + start);
		// 当前返回的条数
		size = dis.readInt();
		System.out.println("当前返回的条数:" + size);
		// 参数的个数
		fieldSize = dis.readInt();
		System.out.println("参数的个数:" + fieldSize);

		// 结果的长度
		int valueSize = dis.readInt();
		System.out.println("结果的长度:" + valueSize);

		// 每个参数内存区域的长度设置
		pamersSize = new int[fieldSize * size];
		// 单条记录总参数的长度
		// 每条参数的长度
		for (int i = 0; i < pamersSize.length; i++) {
			pamersSize[i] = dis.readInt();
		}

		array = new LJFieldValue[size];

		LJFieldInterFace lf = null;

		int num1 = 0;

		int num2 = 0;

		LJFieldValue ljFieldValue = null;

		byte[] bytes = null;

		for (int i = 0; i < pamersSize.length; i++) {
			num1 = i / fieldSize - 1;
			num2 = i % fieldSize;
			if (num2 == 0) {
				if (ljFieldValue != null) {
					array[num1] = ljFieldValue  ;
					ljFieldValue = null ;
				}

				ljFieldValue = new LJFieldValue(fieldSize);
			}
			bytes = new byte[pamersSize[i]];

			dis.readFully(bytes);

			ljFieldValue.setPamerValues(num2, bytes);
		}
		if (ljFieldValue != null) {
			array[num1+1] = ljFieldValue;
		}
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFieldSize() {
		return fieldSize;
	}

	public void setFieldSize(int fieldSize) {
		this.fieldSize = fieldSize;
	}

	public int[] getPamersSize() {
		return pamersSize;
	}

	public void setPamersSize(int[] pamersSize) {
		this.pamersSize = pamersSize;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}

	public LJFieldValue[] getArray() {
		return array;
	}

}
