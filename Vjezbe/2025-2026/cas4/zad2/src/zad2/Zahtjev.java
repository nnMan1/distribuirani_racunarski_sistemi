package zad2;

import java.util.concurrent.Callable;

public class Zahtjev implements Callable<Integer> {

	int id;
	
	public Zahtjev(int id) {
		this.id = id;
	}
	
	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Thread " + Thread.currentThread().getId() + " je obradio  zahtjev " + id );
		return 5;
	}

}
