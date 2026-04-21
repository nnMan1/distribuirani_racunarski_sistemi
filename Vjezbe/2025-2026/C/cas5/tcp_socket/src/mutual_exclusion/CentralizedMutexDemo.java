package mutual_exclusion;

import java.io.IOException;

public class CentralizedMutexDemo {

	static final int COORDINATOR_PORT = 6000;
	static final int NUM_NODES = 3;
	static final int ITERATIONS_PER_NODE = 3;

	public static void main(String[] args) {
		Thread coordinatorThread = new Thread(() -> {
			MutualExclusionCoordinator.main(new String[]{"" + COORDINATOR_PORT});
		});
		coordinatorThread.setDaemon(true);
		coordinatorThread.start();

		// Sacekaj da se koordinator pokrene
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			return;
		}

		System.out.println("=== Centralizovani Mutual Exclusion Demo ===");
		System.out.println("Broj cvorova: " + NUM_NODES + ", iteracija po cvoru: " + ITERATIONS_PER_NODE);
		System.out.println();

		Thread[] nodeThreads = new Thread[NUM_NODES];
		for (int i = 0; i < NUM_NODES; i++) {
			final int nodeId = i;
			nodeThreads[i] = new Thread(() -> {
				try {
					MutualExclusionNode node = new MutualExclusionNode(
						nodeId, "localhost", COORDINATOR_PORT, ITERATIONS_PER_NODE);
					node.start();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			});
			nodeThreads[i].start();
		}

		for (Thread t : nodeThreads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				return;
			}
		}

		System.out.println("\n=== Svi cvorovi zavrsili ===");
	}
}
