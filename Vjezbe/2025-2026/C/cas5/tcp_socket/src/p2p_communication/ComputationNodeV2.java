package p2p_communication;

import java.io.IOException;
import java.util.Arrays;

public class ComputationNodeV2 {

	Connector connector;
	VectorClock clock;

	public ComputationNodeV2(int id, int N, String nameServerAddress, int nameServerPort) throws IOException {
		connector = new Connector(id, N, nameServerAddress, nameServerPort);
		clock = new VectorClock(id, N);
	}

	void sendWithClock(int destId, String message) throws IOException {
		int[] ts = clock.sendTick();
		String fullMessage = VectorClock.toString(ts) + " " + message;
		System.out.println("Node " + connector.id + " [VC=" + Arrays.toString(ts) + "] SEND -> Node " + destId + ": " + message);
		connector.sendMessage(destId, fullMessage);
	}

	String receiveWithClock(int srcId) throws IOException {
		String fullMessage = connector.receiveMessage(srcId);
		int spaceIdx = fullMessage.indexOf(' ');
		String vcStr = fullMessage.substring(0, spaceIdx);
		String message = fullMessage.substring(spaceIdx + 1);
		int[] receivedTs = VectorClock.fromString(vcStr);
		int[] newTs = clock.receiveUpdate(receivedTs);
		System.out.println("Node " + connector.id + " [VC=" + Arrays.toString(newTs) + "] RECV <- Node " + srcId
				+ ": " + message + " (msg_vc=" + Arrays.toString(receivedTs) + ")");
		return message;
	}

	public void start() throws IOException {
		connector.connect();

		int id = connector.id;
		int N = connector.N;

		int[] ts = clock.tick();
		System.out.println("Node " + id + " [VC=" + Arrays.toString(ts) + "] lokalni dogadjaj: inicijalizacija");

		for (int i = 0; i < N; i++) {
			if (i < id) {
				receiveWithClock(i);
				sendWithClock(i, "Hello from node " + id);
			} else if (i > id) {
				sendWithClock(i, "Hello from node " + id);
				receiveWithClock(i);
			}
		}

		ts = clock.tick();
		System.out.println("Node " + id + " [VC=" + Arrays.toString(ts) + "] lokalni dogadjaj: zavrseno");

		connector.close();
	}

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Usage: ComputationNodeV2 <id> <N> <nameServerAddress> <nameServerPort>");
			return;
		}

		int id = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		String nameServerAddress = args[2];
		int nameServerPort = Integer.parseInt(args[3]);

		try {
			ComputationNodeV2 node = new ComputationNodeV2(id, N, nameServerAddress, nameServerPort);
			node.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
