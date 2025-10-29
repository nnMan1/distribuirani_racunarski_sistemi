package zad1;
import java.util.*;

public class Main {
	
	public static int kolicnik(int x, int y) throws Exception {
		if(y == 0)
			throw new Exception("Doslo je do greske. Nije moguce dijeliti sa 0");
			
		return x / y;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner in = new Scanner(System.in);
		
		int x = in.nextInt();
		int y = in.nextInt();
		
		try {
			int z = kolicnik(x, y);				
			System.out.println("x/y = " + z);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		Brojac brojac = new Brojac();
		
		MojThread t1 = new MojThread(1, brojac);
		MojThread t2 = new MojThread(2, brojac);
		MojThread t3 = new MojThread(3, brojac);
		
		t1.start();
		t2.start();
		t3.start();
		
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch(Exception e) {
			System.out.println(e);
		}
		
		System.out.println(brojac.brojac);
	}

}
