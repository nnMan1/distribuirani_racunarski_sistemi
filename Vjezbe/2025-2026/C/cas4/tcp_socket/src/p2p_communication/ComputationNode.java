package p2p_communication;

import java.io.IOException;
import java.util.StringTokenizer;

public class ComputationNode {

	Connector connector;
	LamportClock clock;

	public ComputationNode(int id, int N, String nameServerAddress, int nameServerPort) throws IOException {
		connector = new Connector(id, N, nameServerAddress, nameServerPort);
		clock = new LamportClock();
	}

	// Salje poruku sa Lamport timestamp-om: "<timestamp> <message>"
	void sendWithClock(int destId, String message) throws IOException {
		int ts = clock.sendTick();
		String fullMessage = ts + " " + message;
		System.out.println("Node " + connector.id + " [LC=" + ts + "] SEND -> Node " + destId + ": " + message);
		connector.sendMessage(destId, fullMessage);
	}

	// Prima poruku i azurira Lamport sat
	String receiveWithClock(int srcId) throws IOException {
		String fullMessage = connector.receiveMessage(srcId);
		StringTokenizer st = new StringTokenizer(fullMessage, " ", false);
		int receivedTs = Integer.parseInt(st.nextToken());
		// Ostatak poruke nakon timestamp-a
		String message = fullMessage.substring(fullMessage.indexOf(' ') + 1);
		int newTs = clock.receiveUpdate(receivedTs);
		System.out.println("Node " + connector.id + " [LC=" + newTs + "] RECV <- Node " + srcId
				+ ": " + message + " (msg_ts=" + receivedTs + ")");
		return message;
	}

	public void start() throws IOException {
		connector.connect();

		int id = connector.id;
		int N = connector.N;

		int ts = clock.tick(); //lokalni dogadjaj
		System.out.println("Node " + id + " [LC=" + ts + "] lokalni dogadjaj: inicijalizacija");

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
		System.out.println("Node " + id + " [LC=" + ts + "] lokalni dogadjaj: zavrseno");

		connector.close();
	}

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Usage: ComputationNode <id> <N> <nameServerAddress> <nameServerPort>");
			return;
		}

		int id = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		String nameServerAddress = args[2];
		int nameServerPort = Integer.parseInt(args[3]);

		try {
			ComputationNode node = new ComputationNode(id, N, nameServerAddress, nameServerPort);
			node.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
