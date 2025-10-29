package cas2;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		MojRunnable1 task = new MojRunnable1();
		
		Thread t1 = new Thread(task);
		Thread t2 = new Thread(task);
		
		t1.run();
		t2.run();
		
		t1.join();
		t2.join();
		
		System.out.println(task.x);
	}

}
