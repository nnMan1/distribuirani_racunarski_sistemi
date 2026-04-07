package zad2;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
	private Queue<Integer> buffer = new LinkedList<>();
	private int capacity;
	
	public Buffer(int capacity) {
		this.capacity = capacity;
	}
	
	public synchronized void produce(int item) throws InterruptedException {
		
		while(buffer.size() == capacity) 
			wait(); //buffer je pun, cekaj dok ne dobijes obavjestenje da je neko uzeo nesto iz buffera
		
		this.buffer.add(item);
		System.out.println("Produced " + item);
		notifyAll(); //buffer vise nije prazan, obavijesti threadeove da mogu da nastave sa konzumiranjem
	}
	
	public synchronized int consume() throws InterruptedException {
		while(buffer.isEmpty())
			wait(); //buffer je prazan, cekaj dok producer ne doda nesto u buffer
		
		int item = buffer.poll();
		notifyAll();
		return item;
	}
	
}
