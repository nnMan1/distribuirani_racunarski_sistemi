package cas2;
import java.util.*;

public class MojRunnable implements Runnable {

	int myId;
	MojLock lock;
	Random r = new Random();
	
	public MojRunnable(int id, MojLock lock) {
		this.myId = id;
		this.lock = lock;
	}
	
	synchronized void criticalSection() {
		System.out.println(Thread.currentThread().getName() + " is in critical section ****");
		try {
			Thread.sleep(r.nextInt(1000));
		}  catch (Exception e) {}
		System.out.println(Thread.currentThread().getName() + " finished critical section ****");
	}
	
	void nonCriticalSection() {
		System.out.println(Thread.currentThread().getName() + " is not in CS");
		try {
			Thread.sleep(r.nextInt(1000));
		}  catch (Exception e) {}	}
	
	
	@Override
	public void run() {
		while(true) {
//			lock.requestCS(myId);
			criticalSection();
//			lock.releaseCS(myId);
			
			nonCriticalSection();
		}
	}

}
