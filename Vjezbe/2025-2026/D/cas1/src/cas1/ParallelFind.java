package cas1;

public class ParallelFind implements Runnable {
	
	int x, l, r;
	int[] arr;
	int rezultat;
	
	public ParallelFind(int x, int l, int r, int[] arr) {
		this.x = x;
		this.l = l;
		this.r = r;
		this.arr = arr;
	}
	
	@Override
	public void run() {
		rezultat = -1;
		for(int i=l;i<Math.min(r, arr.length);i++) {
			System.out.println(Thread.currentThread().getName() + " checks index " + i);
			if(x == arr[i]) {
				rezultat = i;
				break;
			}
		}
	}
	
	public static int search(int x, int[] A, int numThreads) {
		int blockSize = Math.ceilDiv(A.length, numThreads);
		
		ParallelFind[] runnables = new ParallelFind[numThreads];
		Thread[] threads = new Thread[numThreads];
		
		for(int i=0;i<numThreads;i++) {
			int l = i * blockSize;
			int r = (i+1) * blockSize;
			
			runnables[i] = new ParallelFind(x, l, r, A);
			threads[i] = new Thread(runnables[i], "Thread" + i);
//			threads[i].setName("Thread" + i);
			threads[i].start();
		}
		
		for(int i=0;i<numThreads;i++)
			try{
				threads[i].join();
			} catch (InterruptedException e) {}
		
		for(int i=0;i<numThreads;i++)
			if(runnables[i].rezultat != -1)
				return runnables[i].rezultat;
		
		return -1;				
	}

}
