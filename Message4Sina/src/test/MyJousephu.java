package test;

//简单的约瑟夫环问题
public class MyJousephu {

	public static void main(String[] args) {

		boolean[] arr = new boolean[8];// 一共8个人
		for (int i = 0; i < arr.length; i++) {
			arr[i] = true;
		}
		int leftCount = arr.length;
		int countNum = 0;
		int index = 5 - 1;// 从index开始数
		while (leftCount > 0) {//剩下的人数
			if (arr[index] == true) {
				countNum++;
				if (countNum == 3) {// 数到3的时候退出
					System.out.println("拿出第" + (index + 1) + "人");
					countNum = 0;
					arr[index] = false;
					leftCount--;
				}
			}
			index++;
			if (index == arr.length) {
				index = 0;
			}
		}
		// for (int i = 0; i < arr.length; i++) {
		// if (arr[i] == true) {
		// System.out.print(i);
		// }
		// }

	}

}