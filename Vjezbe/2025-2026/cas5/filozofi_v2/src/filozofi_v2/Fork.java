package filozofi_v2;

import java.util.concurrent.locks.ReentrantLock;

public class Fork {
	
	private int id;
	private ReentrantLock lock = new ReentrantLock();
	
	public Fork(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public ReentrantLock getLock() {
		return this.lock;
	}
}
