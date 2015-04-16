package test.others;

/**
 * 线程打断测试
 * 
 * @author zel
 * 
 */
public class ThreadInteruptTest {

	public static void main(String[] args) throws Exception {
		OneRunnable oneRunnable = new OneRunnable();
		Thread tt = new Thread(oneRunnable);

		tt.start();

		Thread.sleep(1000);

		tt.interrupt();

		System.out.println("执行了inter");
		System.out.println("主线程再睡!");
		Thread.sleep(10000);
	}
}

class OneRunnable implements Runnable {

	public void run() {
		try {
			Thread.sleep(50000);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("被打断了!");
		}
	}

}
