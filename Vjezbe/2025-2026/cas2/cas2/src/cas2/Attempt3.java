package cas2;

public class Attempt3 implements MojLock {

	volatile int turn = 0;
	
	@Override
	public void requestCS(int pid) {
		while(turn != pid);
	}

	@Override
	public void releaseCS(int pid) {
		turn = 1 - turn;
	}
}
