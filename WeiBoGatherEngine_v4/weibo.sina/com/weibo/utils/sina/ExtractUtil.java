package com.weibo.utils.sina;

import com.weibo.common.utils.AnsjPaser;

public class ExtractUtil {
	private String preRegex="@";
	private String txtRegex=".+?";
	private String endRegex="<";
	private AnsjPaser extAtParser=new AnsjPaser(preRegex,endRegex,txtRegex);
	
	public String getAtString(String content) {
		extAtParser.reset(content);
		String temp_at = "";
		while (extAtParser.hasNext()) {
			temp_at += extAtParser.getNextTxt();
			temp_at+="#";
		}
		return temp_at;
	}
	
	public static void main(String[] args) {
		ExtractUtil extractUtil=new ExtractUtil();
		String content="@凤凰网<";
		System.out.println(extractUtil.getAtString(content));
	}
}
