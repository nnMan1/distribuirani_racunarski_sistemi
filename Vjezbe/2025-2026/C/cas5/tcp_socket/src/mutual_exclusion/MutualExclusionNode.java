package mutual_exclusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MutualExclusionNode {

	int id;
	String coordinatorHost;
	int coordinatorPort;
	int iterations;

	public MutualExclusionNode(int id, String coordinatorHost, int coordinatorPort, int iterations) {
		this.id = id;
		this.coordinatorHost = coordinatorHost;
		this.coordinatorPort = coordinatorPort;
		this.iterations = iterations;
	}

	public void start() throws IOException, InterruptedException {
		try (Socket socket = new Socket(coordinatorHost, coordinatorPort);
			 BufferedReader reader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
			 BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream()))) {

			for (int i = 0; i < iterations; i++) {
				long waitTime = (long) (Math.random() * 1000);
				System.out.println("Node " + id + ": radim van KS (" + waitTime + "ms)...");
				Thread.sleep(waitTime);

				System.out.println("Node " + id + ": saljem REQUEST koordinatoru...");
				writer.write("request " + id + "\n");
				writer.flush();

				String response = reader.readLine();
				if (response != null && response.equals("grant")) {
					//bankovna transkackija
					System.out.println(">>> Node " + id + ": ULAZIM u kriticnu sekciju (iteracija " + (i + 1) + ")");
					Thread.sleep((long) (Math.random() * 1500) + 500);
					System.out.println("<<< Node " + id + ": IZLAZIM iz kriticne sekcije");

					writer.write("release " + id + "\n");
					writer.flush();
				}
			}
		}
		System.out.println("Node " + id + ": zavrsio.");
	}

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: MutualExclusionNode <id> <coordinatorHost> <coordinatorPort> [iterations]");
			return;
		}

		int id = Integer.parseInt(args[0]);
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		int iterations = args.length > 3 ? Integer.parseInt(args[3]) : 3;

		try {
			MutualExclusionNode node = new MutualExclusionNode(id, host, port, iterations);
			node.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
