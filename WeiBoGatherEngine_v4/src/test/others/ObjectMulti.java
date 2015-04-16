package test.others;

public class ObjectMulti {
	public static void testMultiObject(Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			System.out.println(objs[i]);
		}
	}

	public static void main(String[] args) {
       testMultiObject(new Object(),new Object());
	}
}
