package p2p_communication;

import java.util.Arrays;

public class VectorClock {

	private int[] clock;
	private int nodeId;

	public VectorClock(int nodeId, int N) {
		this.nodeId = nodeId;
		this.clock = new int[N];
		this.clock[nodeId]++;
	}

	public synchronized int[] tick() {
		clock[nodeId]++;
		return clock.clone();
	}

	public synchronized int[] sendTick() {
		clock[nodeId]++;
		return clock.clone();
	}

	public synchronized int[] receiveUpdate(int[] receivedTimestamp) {
		for (int i = 0; i < clock.length; i++) {
			clock[i] = Math.max(clock[i], receivedTimestamp[i]);
		}
		clock[nodeId]++;
		return clock.clone();
	}

	public synchronized int[] getValue() {
		return clock.clone();
	}

	public static String toString(int[] vc) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < vc.length; i++) {
			if (i > 0) sb.append(',');
			sb.append(vc[i]);
		}
		return sb.toString();
	}

	public static int[] fromString(String s) {
		String[] parts = s.split(",");
		int[] vc = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			vc[i] = Integer.parseInt(parts[i]);
		}
		return vc;
	}
}
