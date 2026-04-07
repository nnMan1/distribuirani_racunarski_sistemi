package p2p_communication;

import java.io.IOException;

public class ComputationNodeV2 {

	Connector connector;

	public ComputationNodeV2(int id, int N, String nameServerAddress, int nameServerPort) throws IOException {
		connector = new Connector(id, N, nameServerAddress, nameServerPort);
	}

	public void start() throws IOException {
		connector.connect();

		int id = connector.id;
		int N = connector.N;

		connector.setBufferSizes(1024, 1024);

		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < 500000; j++) sb.append('X');
		String largeMessage = sb.toString();

		System.out.println("Node " + id + ": sending " + largeMessage.length() + " bytes to all peers BEFORE reading...");

		for(int i = 0; i < N; i++) {
			if(i != id) {
				System.out.println("Node " + id + ": sending to node " + i + "...");
				connector.sendMessage(i, largeMessage);
				System.out.println("Node " + id + ": send to node " + i + " completed.");
			}
		}

		for(int i = 0; i < N; i++) {
			if(i != id) {
				String msg = connector.receiveMessage(i);
				System.out.println("Node " + id + " received " + msg.length() + " bytes from node " + i);
			}
		}

		connector.close();
	}

	public static void main(String[] args) {
		if(args.length < 4) {
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
