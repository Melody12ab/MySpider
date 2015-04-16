package com.mbm.util;

public class StringTransform {
	public static String getExtByPath(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName
				.length());
	}

	public static String getFileNameByPath(String fileName) {
		return fileName.substring(fileName.lastIndexOf("\\") + 1, fileName
				.lastIndexOf("."));
	}

	public static byte[] getByteArray(String source, String charset) {
		try {
			if (source == null) {
				return null;
			}
			return source.getBytes(charset);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("字符串转换转换成指定charset对应的byteArray时出现错误!");
		}
		return null;
	}

	public static String getA200String(String a200) {
		System.out.println("a200-----------------------" + a200);
		if (a200 == null || a200.trim().length() < 1) {
			return null;
		}
		switch (a200.toCharArray()[0]) {
		case 'R':
			return "未实施";
		case 'X':
			return "现行";
		case '*':
			return "部分作废";
		case 'W':
			return "作废";
		case '#':
			return "调整";
		case 'N':
			return "现行";
		default:
			return null;
		}
	}

	public static void main(String args[]) {
		String str = "webapps\\los_BackgroundManage\\UserFiles\\File\\upload\\65835720060509164227.pdf";

		System.out.println(getExtByPath(str));
		System.out.println(getFileNameByPath(str));
	}

}
