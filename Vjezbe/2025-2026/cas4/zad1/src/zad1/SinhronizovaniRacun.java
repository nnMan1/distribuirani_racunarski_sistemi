package zad1;

public class SinhronizovaniRacun extends Racun {

	public SinhronizovaniRacun(int stanje) {
		super(stanje);
		// TODO Auto-generated constructor stub
	}
	
	public synchronized int citajStanje() {
		return super.citajStanje();
	}
	
	public synchronized void izmijeniStanje(int vrijednost) throws Exception {
		super.izmijeniStanje(vrijednost);
	}

}
