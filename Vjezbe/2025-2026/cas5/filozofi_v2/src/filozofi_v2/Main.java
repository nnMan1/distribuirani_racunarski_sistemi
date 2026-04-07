package filozofi_v2;

public class Main {

	public static void main(String[] args) {
		int n = 10;
		
		Fork[] forks = new Fork[n];
		for(int i=0;i<n;i++)
			forks[i] = new Fork(i);
		
		Philosopher[] philosophers = new Philosopher[n];
		for(int i=0;i<n;i++) 
			philosophers[i] = new Philosopher(i, forks[i], forks[(i+1)%n]);
		
		for(int i=0;i<n;i++) {
			Thread t = new Thread(philosophers[i]);
			t.start();
		}
			

	}

}
