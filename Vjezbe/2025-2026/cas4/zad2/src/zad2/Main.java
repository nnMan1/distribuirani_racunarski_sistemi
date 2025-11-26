package zad2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService pool = Executors.newFixedThreadPool(3);
		
		Zahtjev[] zahtjevi = new Zahtjev[20];
		Future[] futures = new Future[20];
		
		for(int i=0;i<20;i++) {
			zahtjevi[i] = new Zahtjev(i);
			futures[i] = pool.submit(zahtjevi[i]);
		}
		
		for(int i=0;i<20;i++) {
			try {
				System.out.println(i + " " + futures[i].get());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		pool.shutdown();
	}

}
