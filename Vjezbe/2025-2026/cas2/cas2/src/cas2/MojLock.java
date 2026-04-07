package cas2;

public interface MojLock {
	public void requestCS(int pid);
	public void releaseCS(int pid);
}
