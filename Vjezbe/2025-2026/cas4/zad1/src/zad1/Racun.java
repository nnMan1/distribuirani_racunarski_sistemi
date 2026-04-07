package zad1;

public class Racun {
	static int nextId=0;
	
	int id;
	int stanje;
	
	public Racun(int stanje) {
		this.stanje = stanje;
		this.id = ++nextId;
	}
	
	public int citajStanje() {
		return this.stanje;
	}
	
	public void izmijeniStanje(int vrijednost) throws Exception {
		this.stanje += vrijednost;
	}
}
