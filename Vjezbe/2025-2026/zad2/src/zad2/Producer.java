package zad2;

public class Producer extends Thread {
	private Buffer buffer;
	
	public Producer(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		int i = 0;
		try {
			while(true) {
				int item = i++;
				buffer.produce(item);
				Thread.sleep(100);
			}
		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}
	}
}
