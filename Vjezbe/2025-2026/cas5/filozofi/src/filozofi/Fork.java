package filozofi;

public class Fork {
	
	private int id;
	private boolean isUsed;
	
	public Fork(int id) {
		this.id = id;
		this.isUsed = false;
	}
	
	public int getId() {
		return this.id;
	}
	
	public synchronized void acquire() throws InterruptedException {
		while(isUsed)
			wait();
		
		isUsed=true;
	}
	
	public synchronized void release() {
		isUsed=false;
		notify();
	}
}
