package cas2;

public class MojRunnable1 implements Runnable {

	int x = 0;
	
	@Override
	public void run() {
		x = x + 1;		
	}

}
