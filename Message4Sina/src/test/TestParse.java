package test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestParse {
	public static void main(String[] args) throws Exception {
		File input = new File("demo01.html");
		Document doc = null;
		Elements elements=null;
		StringBuilder sb=new StringBuilder();
		try {
			doc = Jsoup.parse(input, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		elements=doc.select("div.person_detail");
		Iterator<Element> it=elements.iterator();
		while(it.hasNext()){
			Element element=it.next();
			sb.append(element.select(".person_name>a").text()+"\t");
			sb.append(element.select(".person_addr >.m_icon").attr("title")+"\t");
			sb.append(element.select(".person_addr >span").get(1).text()+"\t");
			sb.append(element.select(".person_addr >a").text()+"\t");
			sb.append(element.select(".person_card").text()+"\t");
			sb.append(element.select(".person_info>p").text()+"\t");
			sb.append(element.select(".person_label").text()+"\n");
		}
		System.out.println(sb.toString());
	}
	
}
