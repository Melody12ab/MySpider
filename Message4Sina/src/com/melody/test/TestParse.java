package com.melody.test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sun.security.provider.MD5;

public class TestParse {
	public static void main(String[] args) throws Exception {
		File input = new File("demo.html");
		Document doc = null;
		Elements elements=null;
		StringBuilder sb=new StringBuilder();
		try {
			doc = Jsoup.parse(input, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		elements=doc.select("div.date >em");
		System.out.println(elements.get(0).text());
//		Iterator<Element> it=elements.iterator();
//		fs7dt6kn0o7da662q76jh3is96
//		System.out.println(sb.toString());
		
	}
	
}
