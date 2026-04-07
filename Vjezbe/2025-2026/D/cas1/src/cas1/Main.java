package cas1;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner in = new Scanner(System.in);
//		
//		MojThread t1 = new MojThread();
//		t1.start();
//		
//		int x = in.nextInt();
//		System.out.println(x);

//		Automobil a1 = new Automobil("Audi", "a4", "PG-AT 208");
//		TeretniAutomobil t1 = new TeretniAutomobil("TAM", "150", "PB-AF 111", 10);
//		
//		TeretniAutomobil t2 = new TeretniAutomobil("Mercedes", "25", "PG-AT 225", 20);
//		
//		a1.ubrzaj(5);
////		System.out.println(a1.getBrzina());
////		
//		t1.ubrzaj(2);
////		System.out.println(t1.brzina);
////		System.out.println(t1.getNosivost());
//		System.out.println(a1);
//		System.out.println(t1);
//		System.out.println(t2);
//		
//		Thread thr1 = new Thread(t1);
//		thr1.setDaemon(true);
//		thr1.start();
//		
//		int ubrzanje1 = in.nextInt();
//		t1.ubrzaj(ubrzanje1);
//		
//		int ubrzanje2 = in.nextInt();
//		t1.ubrzaj(ubrzanje2);
//		
//		int ubrzanje3 = in.nextInt();
//		t1.ubrzaj(ubrzanje3);
		
//		Runnable r1 = new MojRunnable();
//		Thread t2 = new Thread(r1);
//		t2.start();
//		
//		int x = in.nextInt();
//		System.out.println(x);
//		
		
		int[] arr = {5, 8, 9, 6, 4, 7, 3, 9, 4, 1, 8, 6, 12};
		int idx = ParallelFind.search(9, arr, 5);
		System.out.println(idx);
		
	}

}
