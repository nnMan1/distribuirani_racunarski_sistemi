package p2p_communication;

import java.io.IOException;

public class ComputationNodeV3 {

	Connector connector;

	public ComputationNodeV3(int id, int N, String nameServerAddress, int nameServerPort) throws IOException {
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

		System.out.println("Node " + id + ": sending " + largeMessage.length() + " bytes using send-receive pattern...");

		// Send-receive pattern: za svaki par (A, B) gdje A < B:
		//   A salje pa prima, B prima pa salje
		for(int i = 0; i < N; i++) {
			if(i < id) {
				String msg = connector.receiveMessage(i);
				System.out.println("Node " + id + " received " + msg.length() + " bytes from node " + i);
				connector.sendMessage(i, largeMessage);
				System.out.println("Node " + id + ": sent to node " + i);
			} else if(i > id) {
				connector.sendMessage(i, largeMessage);
				System.out.println("Node " + id + ": sent to node " + i);
				String msg = connector.receiveMessage(i);
				System.out.println("Node " + id + " received " + msg.length() + " bytes from node " + i);
			}
		}

		connector.close();
	}

	public static void main(String[] args) {
		if(args.length < 4) {
			System.out.println("Usage: ComputationNodeV3 <id> <N> <nameServerAddress> <nameServerPort>");
			return;
		}

		int id = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		String nameServerAddress = args[2];
		int nameServerPort = Integer.parseInt(args[3]);

		try {
			ComputationNodeV3 node = new ComputationNodeV3(id, N, nameServerAddress, nameServerPort);
			node.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
