package p2p_communication;

public class LamportClock {

	private int clock;

	public LamportClock() {
		this.clock = 0;
	}

	public synchronized int tick() {
		return ++clock;
	}

	public synchronized int sendTick() {
		return ++clock;
	}

	public synchronized int receiveUpdate(int receivedTimestamp) {
		clock = Math.max(clock, receivedTimestamp) + 1;
		return clock;
	}

	public synchronized int getValue() {
		return clock;
	}
}
