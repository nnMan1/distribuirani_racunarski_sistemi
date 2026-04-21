package mutual_exclusion;

import name_table.NameServer;

public class LamportMutexDemo {

    static final int NUM_NODES           = 3;
    static final int ITERATIONS_PER_NODE = 3;
    static final int NAME_SERVER_PORT    = 13005;
    static final String NAME_SERVER_HOST = "localhost";

    public static void main(String[] args) throws InterruptedException {
        Thread nameServerThread = new Thread(()->
            NameServer.main(new String[]{"" + NAME_SERVER_PORT})
        );
        nameServerThread.setDaemon(true);
        nameServerThread.start();

        try { Thread.sleep(300); } catch (InterruptedException e) { return; }

        System.out.println("=== Lamportov Mutual Exclusion Demo ===");
        System.out.println("Broj cvorova: " + NUM_NODES +
                           ", iteracija po cvoru: " + ITERATIONS_PER_NODE);
        System.out.println();

        Thread[] threads = new Thread[NUM_NODES];

        for (int i = 0; i < NUM_NODES; i++) {
            final int nodeId = i;
            threads[i] = new Thread(() -> {
                try {
                    LamportMutexNode node = new LamportMutexNode(
                            nodeId, NUM_NODES, ITERATIONS_PER_NODE,
                            NAME_SERVER_HOST, NAME_SERVER_PORT);
                    node.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Node-" + nodeId);
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println();
        System.out.println("=== Demo zavrsen ===");
    }
}
