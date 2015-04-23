package com.melody.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class TestBilibili {
	public static void main(String[] args) throws IOException {
		Response flvresponse=Jsoup.connect("http://cn-hnsy1-dx.acgvideo.com/vg2/d/a2/348"
				+ "5951-1.flv?expires=1429791300&ssig=Sl65mOqvF-anrKteiHA79g&o=3662410450&rate=0").ignoreContentType(true).execute();
		FileOutputStream fos=new FileOutputStream(new File("a.flv"));
		fos.write(flvresponse.bodyAsBytes());
		fos.close();
	}
}
