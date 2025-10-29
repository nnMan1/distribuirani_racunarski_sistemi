package zad2;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Buffer buffer = new Buffer(10);
		
		Thread producer = new Producer(buffer);
		Thread consumer1 = new Consumer(0, buffer);
		Thread consumer2 = new Consumer(1, buffer);
		Thread consumer3 = new Consumer(2, buffer);
		Thread consumer4 = new Consumer(3, buffer);
		Thread consumer5 = new Consumer(4, buffer);
		
		producer.start();
		consumer1.start();
		consumer2.start();
		consumer3.start();
		consumer4.start();
		consumer5.start();
	}

}
