package com.weibo.utils.qq;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mbm.util.MD5;
@Component
@Scope(value = "prototype")
public class UidGeneratorUtil {
	public  char first_not_0_num;
	public  Object syn_key = new Object();

	public String md5(String domain) {
		synchronized (syn_key) {
			domain = MD5.MD5(domain);
			domain = domain.replaceAll("[A-Z]", getFirstNotZero(domain
					.replaceAll("[A-Z]", getFirstNumber(domain))));
			if (domain.charAt(0) == '0') {
				domain = domain.replaceFirst("0", getFirstNotZero(domain));
			}
			return domain.substring(0, 18);
		}
	}

	public String getFirstNotZero(String key) {
		int i = 0;
		while ((first_not_0_num = key.charAt(i)) == '0') {
			i++;
		}
		return first_not_0_num + "";
	}

	public String getFirstNumber(String key) {
		int i = 0;
		first_not_0_num = key.charAt(i);
		while (first_not_0_num < '0' || first_not_0_num > '9') {
			i++;
			first_not_0_num = key.charAt(i);
		}
		return first_not_0_num + "";
	}

	public static void main(String[] args) {
		// String domain = "erliang20088";
		// for (int i = 0; i < 10; i++) {
		// domain += i;
		// System.out.println("md5--" + md5(domain));
		// // System.out.println("md5 length--" + md5(domain).length());
		// domain = "erliang20088";
		// }
	}
}
