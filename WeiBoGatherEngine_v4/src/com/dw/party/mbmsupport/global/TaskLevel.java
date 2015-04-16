package com.dw.party.mbmsupport.global;

/**
 * 区别任务的优先级，分为A、B、C、D、E 从A到E周期时间依次递增,时间为分钟 A=10,B=30,C=120,D=240,E=1440
 * 
 * @author zel
 * 
 */
public class TaskLevel {
	/**
	 * A级任务,循环周期为10分钟
	 */
	public static final int A = 1;
	/**
	 * B级任务,循环周期为30分钟
	 */
	public static final int B = 2;
	/**
	 * C级任务,循环周期为120分钟
	 */
	public static final int C = 3;
	/**
	 * D级任务,循环周期为240分钟
	 */
	public static final int D = 4;
	/**
	 * E级任务,循环周期为1440分钟
	 */
	public static final int E = 5;

	public static final int Default = C;
}
