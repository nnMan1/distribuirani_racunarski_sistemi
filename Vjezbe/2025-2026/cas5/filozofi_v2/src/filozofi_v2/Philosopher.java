package filozofi_v2;


import java.util.Random;

public class Philosopher implements Runnable {
	private int id;
	private Fork leftFork;
	private Fork rightFork;
	private Random rand = new Random();
	
	public Philosopher(int id, Fork leftFork, Fork rightFork) {
		this.id = id;
		this.leftFork = leftFork;
		this.rightFork = rightFork;
	}
	
	public void think() throws InterruptedException {
		
		System.out.println("Filozod " + id + " pocinje da misli");

		int time = rand.nextInt(1000) + 500;
		Thread.sleep(time);
		
		System.out.println("Filozod " + id + " je zavrsio sa razmisnjanjem");

		
	}
	
	public void eat() throws InterruptedException {
		System.out.println("Filozod " + id + " zeli da jede");
		
		if(this.id == 0) {
			this.pickUpFork(rightFork);
			this.pickUpFork(leftFork);
		} else {
			this.pickUpFork(leftFork);
			this.pickUpFork(rightFork);
		}
		
		System.out.println("Filozof " + id + " je poceo da jede");
		
		int time = rand.nextInt(1000) + 500;
		Thread.sleep(time);
		
		System.out.println("Filozod " + id + " je zavrsio sa jelom");
		releaseFork(leftFork);
		releaseFork(rightFork);	
	}

	@Override
	public void run() {

		while(true) {
			try {
				eat();
				think();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	public void pickUpFork(Fork fork) throws InterruptedException {
		fork.getLock().lock();
	}
	
	public void releaseFork(Fork fork) {
		fork.getLock().unlock();
	}
}