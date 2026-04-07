package p2p_communication;

import java.io.IOException;

public class ComputationNode {
	
	Connector connector;

	public ComputationNode(int id, int N, String nameServerAddress, int nameServerPort) throws IOException {
		connector = new Connector(id, N, nameServerAddress, nameServerPort);
	}

	public void start(boolean deadlockDemo) throws IOException {
		connector.connect();

		int id = connector.id;
		int N = connector.N;

//		if(deadlockDemo) {
//			/*
//			 * Ako se TCP buffer napuni, slanje poruke ce blokirati dok se buffer ne isprazni.
//			 * Ako svi cvorovi pokusaju poslati velike poruke prije nego sto procitaju poruke od drugih, 
//			 * moze doci do situacije gdje svi writeri cekaju da se buffer isprazni.
//			 */
//			connector.setBufferSizes(1024, 1024);
//
//			StringBuilder sb = new StringBuilder();
//			for(int j = 0; j < 500000; j++) sb.append('X');
//			String largeMessage = sb.toString();
//
//			System.out.println("Node " + id + ": sending " + largeMessage.length() + " bytes to all peers BEFORE reading...");
//
//			for(int i = 0; i < N; i++) {
//				if(i != id) {
//					System.out.println("Node " + id + ": sending to node " + i + "...");
//					connector.sendMessage(i, largeMessage);
//					System.out.println("Node " + id + ": send to node " + i + " completed.");
//				}
//			}
//
//			for(int i = 0; i < N; i++) {
//				if(i != id) {
//					String msg = connector.receiveMessage(i);
//					System.out.println("Node " + id + " received " + msg.length() + " bytes from node " + i);
//				}
//			}
//		} else {
			// Normalna komunikacija bez deadlocka radi za male poruke
//			for(int i = 0; i < N; i++) {
//				if(i != id) {
//					connector.sendMessage(i, "Hello from node " + id);
//				}
//			 }
//
//			 for(int i = 0; i < N; i++) {
//			 	if(i != id) {
//					String msg = connector.receiveMessage(i);
//					System.out.println("Node " + id + " received: " + msg);
//			 	}
//			 }

			// Sigurno slanje i primanje poruka bez deadlocka (send-receive pattern)
			// Ako A < B => A salje poruku B-u, a B prima poruku od A
			// onda se osigurava da se nece dogoditi situacija gdje svi cekaju na slanje.
			for(int i = 0; i < N; i++) {
				if(i < id) {
					String msg = connector.receiveMessage(i);
					System.out.println("Node " + id + " received: " + msg);
					connector.sendMessage(i, "Hello from node " + id);
				} else if(i > id) {
					connector.sendMessage(i, "Hello from node " + id);
					String msg = connector.receiveMessage(i);
					System.out.println("Node " + id + " received: " + msg);
				}
			}
//		}


		connector.close();
	}

	public static void main(String[] args) {
		if(args.length < 4) {
			System.out.println("Usage: ComputationNode <id> <N> <nameServerAddress> <nameServerPort> [deadlock]");
			return;
		}

		int id = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		String nameServerAddress = args[2];
		int nameServerPort = Integer.parseInt(args[3]);
		boolean deadlockDemo = args.length >= 5 && args[4].equals("deadlock");

		try {
			ComputationNode node = new ComputationNode(id, N, nameServerAddress, nameServerPort);
			node.start(deadlockDemo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
