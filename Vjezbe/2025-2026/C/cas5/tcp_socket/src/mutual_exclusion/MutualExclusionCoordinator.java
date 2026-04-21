package mutual_exclusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class MutualExclusionCoordinator {

	private boolean csOccupied = false;
	private final Queue<BufferedWriter> waitQueue = new LinkedList<>();

	public synchronized void requestCS(int nodeId, BufferedWriter writer) throws IOException {
		if (!csOccupied) {
			csOccupied = true;
			System.out.println("Coordinator: GRANT -> Node " + nodeId + " (CS slobodan)");
			writer.write("grant\n");
			writer.flush();
		} else {
			System.out.println("Coordinator: Node " + nodeId + " stavljen u red cekanja (velicina reda: " + (waitQueue.size() + 1) + ")");
			waitQueue.add(writer);
		}
	}

	public synchronized void releaseCS(int nodeId) throws IOException {
		if (!waitQueue.isEmpty()) {
			BufferedWriter nextWriter = waitQueue.poll();
			System.out.println("Coordinator: RELEASE od Node " + nodeId + ", GRANT -> sljedeci iz reda (preostalo u redu: " + waitQueue.size() + ")");
			nextWriter.write("grant\n");
			nextWriter.flush();
		} else {
			csOccupied = false;
			System.out.println("Coordinator: RELEASE od Node " + nodeId + ", CS sada slobodan");
		}
	}

	void handleClient(Socket socket) {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
			 BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream()))) {

			String line;
			while ((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				String tag = st.nextToken();
				int nodeId = Integer.parseInt(st.nextToken());

				if (tag.equals("request")) {
					System.out.println("Coordinator: REQUEST od Node " + nodeId);
					requestCS(nodeId, writer);
				} else if (tag.equals("release")) {
					releaseCS(nodeId);
				}
			}
		} catch (IOException e) {
			// klijent se diskonektovao
		}
	}

	public static void main(String[] args) {
		int port = 6000;
		if (args.length > 0) port = Integer.parseInt(args[0]);

		MutualExclusionCoordinator coordinator = new MutualExclusionCoordinator();

		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Coordinator slusam na portu " + port);
			while (true) {
				Socket socket = server.accept();
				new Thread(() -> coordinator.handleClient(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
