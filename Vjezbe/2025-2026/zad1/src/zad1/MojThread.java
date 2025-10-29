package zad1;

public class MojThread extends Thread {

	int id;
	Brojac brojac;
	
	public MojThread(int id, Brojac brojac) {
		this.id = id;
		this.brojac = brojac;
	}
	
	public void run() {
		for(int i=0;i<100000000;i++)
			if(id == 1)
				brojac.increment1();
			else 
				brojac.increment();
	}
	
}
