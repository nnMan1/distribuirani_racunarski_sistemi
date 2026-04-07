package cas2;

public class Attempt1 implements MojLock {

	volatile boolean openDoor = true;
		
	@Override
	public void requestCS(int pid) {
		while(!openDoor);
		openDoor = false;
	}

	@Override
	public void releaseCS(int pid) {
		openDoor = true;
	}

}
