package test;

import java.util.ArrayList;

//错误示范
public class yuesefu {
	static int[] num;

	public void shu(int n, int a, int x) {
		num = new int[n];
		for (int i = 0; i < n; i++) {
			num[i] = 1;
		}
		for (int i = a - 1; i < n * n; i++) {
			if ((i - a + 1) % x == 0) {
				int tmp = i % n;
				System.out.println("去掉第"+(tmp+1)+"个人");
				num[tmp] = 0;
			}
			if (hasdone(n)) {
				return;
			}
		}
	}

	public boolean hasdone(int n) {
		int sum = 0;
		for (int i = 0; i < n; i++) {
			sum += num[i];
		}
		return sum == 0 ? true : false;
	}
	
	public static void Josephus(int number,int start,int distance){
		ArrayList<String> list=new ArrayList<String>(number);
		for(int i=0;i<number;i++){
			list.add((char)('A'+i)+"");
		}
		System.out.println("约瑟夫环("+number+","+start+","+distance+"), ");
		System.out.println(list.toString());
		int i=start;
		while(list.size()>1){
			i=(i+distance-1)%list.size();
			System.out.println("删除"+list.remove(i).toString()+",");
			System.out.println(list.toString());
		}
	}
	
	
	public static void main(String[] args) {
//		new yuesefu().shu(8, 5, 3);
		yuesefu.Josephus(8, 5, 3);
	}
}
