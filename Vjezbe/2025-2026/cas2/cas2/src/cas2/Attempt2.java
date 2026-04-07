package cas2;

public class Attempt2 implements MojLock {

	boolean[] wantCS = {false, false};
	
	@Override
	public void requestCS(int pid) {
		wantCS[pid] = true;
		while(wantCS[1-pid] != false);
	}

	@Override
	public void releaseCS(int pid) {
		wantCS[pid] = false;		
	}

}
