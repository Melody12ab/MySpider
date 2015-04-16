package test.others;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import junit.framework.TestCase;

import com.mbm.elint.entity.util.KeywordPojo;
import com.mbm.util.IOUtil;
import com.mbm.util.StringOperatorUtil;
import com.mbm.util.URLReader;
import com.vaolan.parser.VaoLanHtmlParser;
import com.weibo.common.utils.AnsjPaser;

public class RandomTest extends TestCase {

	public void testLinkString() {
		List<String> list = new ArrayList<String>();
		list.add("123");
		list.add("123");
		list.add("123");
		list.add("123");
		String str = "";
		for (String temp : list) {
			str += (temp + " ");
		}
		System.out.println(str);
	}

	public void testLinkedListAdd() {
		LinkedList<String> linkedList = new LinkedList<String>();
		linkedList.add("one");
		linkedList.add("two");

		System.out.println("First-------------" + linkedList.pollLast());
	}

	public void testSimpleDateFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
		System.out.println(sdf.format(new Date()));
	}

	public void testBigHashMap() {
		Map<String, String> map1 = new HashMap<String, String>();
		long size = 5000000;
		for (int i = 0; i < size; i++) {
			map1.put("key1234567890-" + i, "value-" + i);
		}
		System.out.println("放进去完毕");
		long nowTime = System.currentTimeMillis();
		System.out.println("map1--value-----" + map1.get("key1234567890-9999"));
		long endTime = System.currentTimeMillis();

		System.out.println("string key 查询共用时---------" + (endTime - nowTime));

		Map<Integer, String> map2 = new HashMap<Integer, String>();
		for (int i = 0; i < size; i++) {
			map2.put(i, "value-" + i);
		}
		System.out.println("放进去完毕");
		nowTime = System.currentTimeMillis();
		System.out.println("map1--value-----" + map2.get(9999));
		endTime = System.currentTimeMillis();

		System.out.println("string key 查询共用时---------" + (endTime - nowTime));
	}

	public void testReturn() {
		for (int i = 1; i <= 10; i++) {
			System.out.println("i----------" + i);
			return;
		}
		System.out.println("1111");
	}

	public void testTimer() {
		TimerTask tt = new TimerTask() {
			int count = 1;

			@Override
			public void run() {
				System.out.println("执行了一次----" + count++);
				// try {
				// Thread.sleep(5000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
			}
		};
		Timer timer = new Timer();
		timer.schedule(tt, 5000, 500);
	}

	public void testWhileReturn() {
		int i = 0;
		while (i < 10) {
			System.out.println(i);
			if (i == 1) {
				return;
			}
			i++;
		}
	}

	public void testChineseCharAt() {
		String str = "我是谁1a";
		for (int i = 0; i < str.length(); i++) {
			System.out.println(str.charAt(i) + "-------" + (int) str.charAt(i));
		}
	}

	public void testHashSetContainsAndRemove() {
		ArrayList<KeywordPojo> list = new ArrayList<KeywordPojo>();
		list.add(new KeywordPojo("one", 0));
		if (list.remove(new KeywordPojo("one", 0))) {
			System.out.println("删除one成功!");
		} else {
			System.out.println("删除one失败!");
		}

		HashSet<KeywordPojo> hash = new HashSet<KeywordPojo>();
		hash.add(new KeywordPojo("one", 0));

		if (hash.contains(new KeywordPojo("one", 0))) {
			System.out.println("删除one成功!");
		} else {
			System.out.println("删除one失败!");
		}
	}

	public static int catchExceptionReturn() {
		try {
			int i = 3 / 0;
		} catch (Exception e) {
			System.out.println("异常出现了!");
			return 2;
		} finally {
			System.out.println("执行到finally了");
		}
		System.out.println("执行到最后了!");
		return 3;
	}

	public static void downloadHttp() {
		URLReader
				.download(
						"http://login.sina.com.cn/cgi/pin.php?r=3783409&s=0&p=xd-d76a83fa50e81d292f282ceb745e395baa66",
						"aa/aa.png");
	}

	public static void getRightChar() throws Exception {
		String str = URLDecoder
				.decode(
						"%CA%E4%C8%EB%B5%C4%D1%E9%D6%A4%C2%EB%B2%BB%D5%FD%C8%B7",
						"GBK");
		System.out.println(str);
	}

	public static void addPrefixToString() throws Exception {
		String str = IOUtil.readFile("d://test.txt");
		StringReader sr = new StringReader(str);
		BufferedReader br = new BufferedReader(sr);

		String temp_line = null;
		StringBuilder sb = new StringBuilder();
		while (((temp_line = br.readLine()) != null)) {
			if (!StringOperatorUtil.isBlank(temp_line)) {
				// System.out.println("http://www.weibo.com/"+temp_line);
				sb.append("http://www.weibo.com/" + temp_line + "\n");
			}
		}

		IOUtil.writeFile("d://result.txt", sb.toString());
		br.close();
	}

	public static void main(String[] args) throws Exception {
//		System.out
//				.println("5394206608#1254535910#1213239282#1573256107#2842095250#1230663070#1704381654#1497390470#1762700155#1676082433#1767819164#1681241312#1738933752#1706987705#1197199510#1679704482#1463564877#1662766362#1192148495#1195201334#1927633355#1562373795#1774289524#5248773753#1256857734#1496851811#1882386593#1769684987#1152440821#1496855785#1733275311#5234797239#1158088282#1740756372#1563926367#1760710005#1402602034#2512591982#1229144165#1676714563#2214257545#1649769582#1644225642#1572307980#1222062284#2185375831#1197197733#1643055531#1644258785#1694427250#5187664653#1673464921#1768706212#2173291530#1498931013#1401103625#1650250313#1197153650#1074305942#1642099365#1758161805#1646439112#1746274673#1677675054#1645101450#1280048863#1135520725#1663363857#2481632295#1213873321#1655665171#1223762662#1309359643#1917369903#1295614221#1405294382#1227086635#1233614375#1807802183#2556059657#1642294753#1673965152#1231462557#1271958883#1685277980#1250741203#2581620413#1653927424#1655532463#1649195040#2824142045#1217647421#1245243201#1184540153#1408752422#3100185195#2100634243"
//						.split("#").length);
		
		Class c=Class.forName("");
		c.newInstance();
		
	}
}
