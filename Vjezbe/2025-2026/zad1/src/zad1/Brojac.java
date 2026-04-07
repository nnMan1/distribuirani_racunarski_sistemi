package zad1;

public class Brojac {
	int brojac = 0;
	
	public synchronized void increment() {
		this.brojac++;
	}
	
	public synchronized void increment1() {
		this.brojac++;
	}
}
