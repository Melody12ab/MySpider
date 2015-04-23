package test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;


public class TestBase64 {
	public static void main(String[] args) throws UnsupportedEncodingException {
		byte [] str="你好".getBytes();
		for(byte i=0;i<str.length;i++){
			System.out.println(Integer.toBinaryString(str[i]));
		}
		String encode=Base64.getEncoder().encodeToString(str);
		System.out.println(encode);
		byte [] decode=Base64.getDecoder().decode(encode);
		for(byte i=0;i<decode.length;i++){
			System.out.println(Integer.toBinaryString(decode[i]));
		}
		System.out.println(new String(decode));
		
		String url="QUFmdHA6Ly95OnlAZHguZGwxMjM0LmNvbTo4MDA2L1u159OwzOzMw3d3dy5keTIwMTguY29tXdDHvMq0qdS9QkTW0NOiy6vX1i5ybXZiWlo=";
		System.out.println(new String(Base64.getDecoder().decode(url),"GBK"));
	}
}
