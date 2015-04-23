package com.melody.impl;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.melody.iface.Parser;

public class parse2ResultStr implements Parser {

	Document doc = null;
	Elements elements = null;

	@Override
	public String parser(String source) {
		StringBuilder sb = new StringBuilder();
		doc = Jsoup.parse(source);
		elements = doc.select("div.person_detail");
		Iterator<Element> it = elements.iterator();
		while (it.hasNext()) {
			Element element = it.next();
			sb.append(element.select(".person_name>a").text() + "\t");
			sb.append(element.select(".person_addr >.m_icon").attr("title")
					+ "\t");
			sb.append(element.select(".person_addr >span").get(1).text() + "\t");
			sb.append(element.select(".person_addr >a").text() + "\t");
			sb.append(element.select(".person_card").text() + "\t");
			sb.append(element.select(".person_info>p").text() + "\t");
			sb.append(element.select(".person_label").text() + "\n");
		}
		return sb.toString();
	}
	
}
