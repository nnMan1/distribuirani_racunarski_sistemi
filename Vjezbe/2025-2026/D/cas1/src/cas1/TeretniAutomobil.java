package cas1;

public class TeretniAutomobil extends Automobil implements Runnable {
	
	int nosivost;
	
	public TeretniAutomobil(String marka, String model, String brTablica, int nosivost) {
		super(marka, model, brTablica);
		this.nosivost = nosivost;
	}
	
	public int getNosivost() {
		return nosivost;
	}
	
	@Override
	public String toString() {
		return "TeretniAutomobil("+marka+", " + model + ", " + brTablica + ", " + nosivost + "t, " + brzina + "km/h)";
	}

	@Override
	public void run() {
		while(true) {
			System.out.println(getBrzina());
			try{
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		
	}
	
	
	
}
