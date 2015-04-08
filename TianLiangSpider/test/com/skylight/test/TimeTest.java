package com.skylight.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTest {
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(1428496242337L);
		System.out.println(sdf.format(date));
	}
}
