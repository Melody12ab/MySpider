package com.dw.party.mbmsupport.dto;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.stereotype.Component;

/**
 * 此类为特词提取结果的pojo类，word为提取出来的特征词，count是指该词出现的频率
 * 
 * @author zel
 */
@Component
public class FeatureWord implements Comparable<FeatureWord>, Serializable {
	public FeatureWord() {

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FeatureWord) {
			// System.out.println("sdfsd");
			return ((FeatureWord) obj).getWord().equals(this.word);
		}
		return false;
	}

	public FeatureWord(String word, int count) {
		super();
		this.word = word;
		this.count = count;
	}
    /**
     * 特征词的string值
     */
	private String word;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
    /**
     * 返回的特征词的词频
     */
	private int count;

	public int compareTo(FeatureWord word) {
		if (this.count > word.count) {
			return -1;
		} else if (this.count < word.count) {
			return 1;
		} else {
			return 1;
		}
		// 此代表逆序
		// return this.count - word.count ;
		// return word.count - this.count;
	}

	public String toString() {
		return this.word + "-------" + this.count;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// FeatureWord word0 = new FeatureWord("two", 22);
		// FeatureWord word1 = new FeatureWord("one", 2);
		// FeatureWord word2 = new FeatureWord("three", 18);
		//			
		// FeatureWord word3 = new FeatureWord("two", 22);
		//	
		// List list=new ArrayList();
		//	        
		// list.add(word0);
		// list.add(word1);
		// list.add(word2);
		//	 
		// ListIterator<FeatureWord> i = list.listIterator();
		// System.out.println(i.next().toString());
		//	        
		// System.out.println(list.indexOf(word3));
		//	        
		// System.out.println("排序前---------------"+list);
		//	 
		// Collections.sort(list);
		//	 
		// System.out.println("排序后---------------"+list);

		// int x = -3;
		// float y = 10.0f;
		// System.out.println(y%x);
		//		 
		// String[] s = new String[10];
		// System.out.println(s[9]);
		// System.out.println(s.length);
		// finally语句
		// try {
		// throw new java.lang.Exception("抛出异常");
		// } catch (Exception e) {
		// // TODO: handle exception
		// System.out.println("catch 输出");
		// return ;
		// }
		// finally{
		// System.out.println("finally 输出");
		// }
		// 循环标记
		// char a ;
		// outer:
		// for (int i = 0; i < 5; i++) {
		// System.out.println("外层"+i+"层循环");
		// for (int j = 0; j < 5; j++) {
		// a = (char)System.in.read();
		// System.out.println("内层"+j+"层循环");
		// if (a=='b') {
		// break outer;
		// }
		// if (a=='c') {
		//						
		// continue outer;
		// }
		// }
		// }
	}
}
