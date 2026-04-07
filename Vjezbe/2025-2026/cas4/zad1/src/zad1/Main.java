package zad1;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Racun r1 = new SinhronizovaniRacun(0);
		StedniRacun r2 = new StedniRacun(0);
		
		RunnableIzmijeniRacun izm1 = new RunnableIzmijeniRacun(r1);
		RunnableIzmijeniRacun izm2 = new RunnableIzmijeniRacun(r1);
		
		Thread t1 = new Thread(izm1);
		Thread t2 = new Thread(izm2);
		
		long start = System.currentTimeMillis();
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
			System.out.println(r1.citajStanje());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Proteklo vrijeme: " + (end - start));
	
	}

}
