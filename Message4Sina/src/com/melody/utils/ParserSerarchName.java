package com.melody.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class ParserSerarchName {
	public static LinkedList<String> ParseName() {
		LinkedList<String> search_name = new LinkedList<String>();
		BufferedReader br = null;
		String result=null;
		try {
			br = new BufferedReader(new InputStreamReader(
					ParserSerarchName.class.getClassLoader()
							.getResourceAsStream("search_name.txt")));
			while((result=br.readLine())!=null){
				search_name.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return search_name;
	}
	public static void main(String[] args) {
		LinkedList<String> name=ParseName();
		Iterator<String> it=name.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
}
