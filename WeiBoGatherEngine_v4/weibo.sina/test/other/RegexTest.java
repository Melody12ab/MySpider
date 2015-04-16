package test.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import com.mbm.util.IOUtil;
import com.vaolan.utils.StaticValue;
import com.weibo.common.utils.AnsjPaser;

public class RegexTest {
	public static void testExtractHTML() throws Exception {
		File f = new File("D:/test.txt");
		FileReader fr = new FileReader(f);

		BufferedReader br = new BufferedReader(fr);

		String temp1 = null;
		String str = null;
		while ((temp1 = br.readLine()) != null) {
			str += temp1;
		}

		String beginRegex = "(uid=){1}";
		String textRegex = "(\\d){1,20}";
		String endRegex = "(&fnick)\\S*sex";

		AnsjPaser ansj = new AnsjPaser(beginRegex, endRegex, textRegex, str,
				"zel");

		String attentionList = ansj.getTextList();
		String[] list = attentionList.split("#");
		System.out.println("attention list size-------" + list.length);

		for (String temp : list) {
			System.out.println("---------" + temp);
		}
	}

	public static void testExtractOid() throws Exception {
		File f = new File("D:/test.txt");
		FileReader fr = new FileReader(f);

		BufferedReader br = new BufferedReader(fr);

		String temp1 = null;
		String str = null;
		while ((temp1 = br.readLine()) != null) {
			str += temp1;
		}
		String beginRegex = "\\$oid(\\s){0,15}:(\\s){0,15}\"";
		String textRegex = "(\\d){1,20}";
		String endRegex = "\"";

		AnsjPaser ansj = new AnsjPaser(beginRegex, textRegex, endRegex, str,
				"zel");

		System.out.println(ansj.getText());
	}

	public static void testExtDocUrl() throws Exception {
		File f = new File("D:/test.txt");
		FileReader fr = new FileReader(f);

		BufferedReader br = new BufferedReader(fr);

		String temp1 = null;
		String str = null;
		while ((temp1 = br.readLine()) != null) {
			str += temp1;
		}
		String beginRegex = "href=\"/";
		// String textRegex="[\\s\\S]*";
		String textRegex = ".*?";

		String endRegex = "\"(\\s){0,5}class=\"date\"";

		// AnsjPaser ansj=new
		// AnsjPaser(beginRegex,textRegex,endRegex,str,"zel");
		AnsjPaser ansj = new AnsjPaser(beginRegex, endRegex, str, textRegex);

		System.out.println(ansj.getText());
	}

	public static void main(String[] args) throws Exception {
		String str = IOUtil.readFile("d://test.txt");
		// System.out.println(str);
		// String content = JSONObject.fromObject(str).get("data").toString();
		// System.out.println(str);
		// 姓名抽取
		String beginRegex = "data-id=\"";
		String endRegex = "\"";
		AnsjPaser ansjId = new AnsjPaser(beginRegex, endRegex);
		;

		ansjId.reset(str);
		int i = 0;
		while (ansjId.hasNext()) {
			i++;
			// System.out.println(i + "---" + ansjId.getNextText());
			System.out.println(ansjId.getNextText());
		}
	}

}
