package test.other;

public class RandomTest {
	public static void print(String mess) {
		System.out.println(mess);
	}

	public static void main(String[] args) throws Exception {
		print("start");
		Thread t = Thread.currentThread();
	
		t.interrupt();
		
		synchronized (t) {
			t.wait(1000l);
		}
//		t.sleep(5000);
		

		print("stop");
	}
}
