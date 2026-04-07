package zad1;

public class RunnableIzmijeniRacun implements Runnable {

	Racun r;
	
	public RunnableIzmijeniRacun(Racun r)  {
		this.r = r;
	}
	
	@Override
	public void run() {
		for(int i=0;i<100000;i++)
//			synchronized(this.r) {
				try {
					this.r.izmijeniStanje(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
//			}
	}

}
