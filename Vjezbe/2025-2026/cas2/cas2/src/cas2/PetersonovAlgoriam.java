package cas2;

public class PetersonovAlgoriam implements MojLock {

	volatile boolean[] wantCS = {false, false};
	volatile int turn = 0;
	
	@Override
	public void requestCS(int pid) {
		int j = 1 - pid; // id drugog threada
		wantCS[pid] = true; //ja bih usao u KS
		turn = j; //ako drugi thread zeli da udje pusicu njega prvog
		while(wantCS[j] && turn == j);
	}

	@Override
	public void releaseCS(int pid) {
		wantCS[pid] = false;		
	}

}
