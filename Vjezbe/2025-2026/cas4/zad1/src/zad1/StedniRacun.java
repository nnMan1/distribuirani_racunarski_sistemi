package zad1;

public class StedniRacun extends Racun {
	
	int periodStednje; //koliko mjeseci ne moze da se podigne ustedjevina

	public StedniRacun(int stanje) {
		super(stanje);
	}
	

	public synchronized void izmijeniStanje(int vrijednost) throws Exception {
//		super.izmijeniStanje(vrijednost);
		if(this.periodStednje == 0 || vrijednost > 0)
			this.stanje += vrijednost;
		else
			throw new Exception("Jos nije prosao period stednje");
		
	}
	
	
}
