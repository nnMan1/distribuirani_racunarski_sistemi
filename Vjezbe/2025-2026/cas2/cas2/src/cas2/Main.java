package cas2;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		MojLock lock = new PetersonovAlgoriam();
		
		MojRunnable task1 = new MojRunnable(0, lock);
		MojRunnable task2 = new MojRunnable(1, lock);
		
		Thread t1 = new Thread(task1, "0");
		Thread t2 = new Thread(task1, "1");
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
	}

}
