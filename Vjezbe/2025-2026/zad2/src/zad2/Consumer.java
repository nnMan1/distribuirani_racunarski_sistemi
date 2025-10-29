package zad2;

public class Consumer extends Thread {
	int id;
	private Buffer buffer;
	
	public Consumer(int id, Buffer buffer) {
		this.id = id;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		
		try {
			while(true) {
				int item = buffer.consume();
				System.out.println("Consumer " + id + " consumed " + item);
				Thread.sleep(1000);
			}
		} catch(Exception e) {
			System.out.println(e);
			Thread.currentThread().interrupt();
		}
	}
}
