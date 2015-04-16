package com.weibo.utils.sina;

public class SinaVersionChangeUtil {
    public String getMediaUrl(String sendUrl){
    	return sendUrl.replaceFirst("//.*?weibo","//media.weibo");
    }
    
    public String getEnterAndProUrl(String sendUrl){
    	return sendUrl.replaceFirst("//.*?weibo","//e.weibo");
    }
    
    public String getPersonUrl(String sendUrl){
    	return sendUrl.replaceFirst("//.*?weibo","//www.weibo");
    }
    
	//    
	// public static void main(String[] args) {
	// String url1="http://www.weibo.com/123";
	// String url2="http://weibo.com/123";
	// String url3="http://e.weibo.com/123";
	// String url4="http://media.weibo.com/123";
	//        
	// System.out.println(getMediaUrl(url1));
	// System.out.println(getMediaUrl(url2));
	// System.out.println(getMediaUrl(url3));
	// System.out.println(getMediaUrl(url4));
	//        
	// System.out.println(getEnterAndProUrl(url1));
	// System.out.println(getEnterAndProUrl(url2));
	// System.out.println(getEnterAndProUrl(url3));
	// System.out.println(getEnterAndProUrl(url4));
	// }
}
