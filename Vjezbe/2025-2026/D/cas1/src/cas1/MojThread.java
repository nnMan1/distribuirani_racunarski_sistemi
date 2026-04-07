package cas1;

public class MojThread extends Thread {
	
	@Override
	public void run() {
		
		System.out.println("Thread je poceo sa izvrsavanjem");
		try {
			Thread.sleep(10 * 1000);
		} catch(InterruptedException e) {}
		
		System.out.println("Ovo se izvrsava u threadu");
	}

}
