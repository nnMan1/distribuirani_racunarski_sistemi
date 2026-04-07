package p2p_communication;

import java.io.IOException;

public class ComputationNodeV1 {

	Connector connector;

	public ComputationNodeV1(int id, int N, String nameServerAddress, int nameServerPort) throws IOException {
		connector = new Connector(id, N, nameServerAddress, nameServerPort);
	}

	public void start() throws IOException {
		connector.connect();

		int id = connector.id;
		int N = connector.N;

		for(int i = 0; i < N; i++) {
			if(i != id) {
				connector.sendMessage(i, "Hello from node " + id);
			}
		}

		for(int i = 0; i < N; i++) {
			if(i != id) {
				String msg = connector.receiveMessage(i);
				System.out.println("Node " + id + " received: " + msg);
			}
		}

		connector.close();
	}

	public static void main(String[] args) {
		if(args.length < 4) {
			System.out.println("Usage: ComputationNodeV1 <id> <N> <nameServerAddress> <nameServerPort>");
			return;
		}

		int id = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		String nameServerAddress = args[2];
		int nameServerPort = Integer.parseInt(args[3]);

		try {
			ComputationNodeV1 node = new ComputationNodeV1(id, N, nameServerAddress, nameServerPort);
			node.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
