package cas1;

public class Automobil {
	int brzina;
	String marka;
	String model;
	String brTablica;
	
	public Automobil(String marka, String model, String brTablica) {
		this.brzina = 0;
		this.marka = marka;
		this.model = model;
		this.brTablica = brTablica;
	}
	
	public void ubrzaj(int delta) {
		brzina += delta;
	}
	
	public int getBrzina() {
		return brzina;
	}
	
	public String getMarka() {
		return marka;
	}
	
	public String getModel() {
		return model;
	}
	
	@Override
	public String toString() {
		return "Automobil("+marka+", " + model + ", " + brTablica + ", " + brzina + "km/h)";
	}
	
}
