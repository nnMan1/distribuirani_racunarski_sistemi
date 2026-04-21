package mutual_exclusion;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
import p2p_communication.Connector;
import p2p_communication.LamportClock;

enum MessageType {
	REQUEST,
	REPLY,
	RELEASE
}

public class LamportMutexNode {

    private final int id;
    private final int numNodes;
    private final int iterations;

    private final Connector connector;
    private final LamportClock clock;

    private final PriorityQueue<int[]> requestQueue = new PriorityQueue<>(
            Comparator.comparingInt((int[] a) -> a[0]).thenComparingInt(a -> a[1]));

    private int replyCount = 0;

    private int myRequestTs = -1;

    private final Object lock = new Object();

    public LamportMutexNode(int id, int numNodes, int iterations, 
                           String nameServerAddress, int nameServerPort) throws IOException {
        this.id = id;
        this.numNodes = numNodes;
        this.iterations = iterations;
        this.connector = new Connector(id, numNodes, nameServerAddress, nameServerPort);
        this.clock = new LamportClock();
    }

    private void sendMessage(int targetId, MessageType type, int ts) throws IOException {
        String message = type + " " + ts + " " + id;
        connector.sendMessage(targetId, message);
    }

    private void broadcast(MessageType type, int ts) throws IOException {
        for (int i = 0; i < numNodes; i++) {
            if (i != id) 
            	sendMessage(i, type, ts);
        }
    }
    
    private void requestCS() throws IOException {
        synchronized (lock) {
            int ts = clock.tick();
            myRequestTs = ts;
            replyCount = 0;
            requestQueue.add(new int[]{ts, id});
        }
        int ts;
        synchronized (lock) { ts = myRequestTs; }
        System.out.printf("[Node %d | ts=%d] Saljem REQUEST svima%n", id, ts);
        broadcast(MessageType.REQUEST, ts);
    }

    private void waitForCS() throws InterruptedException {
        synchronized (lock) {
            while (!canEnterCS()) {
                lock.wait();
            }
        }
    }

    private boolean canEnterCS() {
        if (requestQueue.isEmpty()) return false;
        int[] head = requestQueue.peek();
        if (head[1] != id) return false;
        return replyCount >= numNodes - 1;
    }

    private void releaseCS() throws IOException {
        int ts;
        synchronized (lock) {
            requestQueue.removeIf(r -> r[1] == id && r[0] == myRequestTs);
            ts = clock.tick();
        }
        System.out.printf("[Node %d | ts=%d] Saljem RELEASE svima%n", id, ts);
        broadcast(MessageType.RELEASE, ts);
    }

    private void handleRequest(int senderTs, int senderId) throws IOException {
        clock.receiveUpdate(senderTs);
        synchronized (lock) {
            requestQueue.add(new int[]{senderTs, senderId});
        }
        int replyTs = clock.tick();
        System.out.printf("[Node %d | ts=%d] Primio REQUEST od Node %d (ts=%d), saljem REPLY%n",
                id, replyTs, senderId, senderTs);
        sendMessage(senderId, MessageType.REPLY, replyTs);
    }

    private void handleReply(int senderTs, int senderId) {
        clock.receiveUpdate(senderTs);
        synchronized (lock) {
            replyCount++;
            System.out.printf("[Node %d] Primio REPLY od Node %d (replyCount=%d/%d)%n",
                    id, senderId, replyCount, numNodes - 1);
            lock.notifyAll();
        }
    }

    private void handleRelease(int senderTs, int senderId) {
        clock.receiveUpdate(senderTs);
        synchronized (lock) {
            requestQueue.removeIf(r -> r[1] == senderId);
            System.out.printf("[Node %d] Primio RELEASE od Node %d, brisem iz reda%n",
                    id, senderId);
            lock.notifyAll();
        }
    }

    private void startMessageListener() {
        Thread listener = new Thread(() -> {
            try {
                for (int srcId = 0; srcId < numNodes; srcId++) {
                    if (srcId == id) continue;
                    final int finalSrcId = srcId;
                        new Thread(() -> {
                        try {
                            while (true) {
                                String message = connector.receiveMessage(finalSrcId);
                                if (message == null) break;
                                
                                StringTokenizer st = new StringTokenizer(message);
                                MessageType type = MessageType.valueOf(st.nextToken());
                                int ts = Integer.parseInt(st.nextToken());
                                int senderId = Integer.parseInt(st.nextToken());
                                
                                switch (type) {
                                    case MessageType.REQUEST -> handleRequest(ts, senderId);
                                    case MessageType.REPLY   -> handleReply(ts, senderId);
                                    case MessageType.RELEASE -> handleRelease(ts, senderId);
                                    default      -> System.err.println("Node " + id + ": nepoznata poruka: " + message);
                                }
                            }
                        } catch (IOException e) {
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        listener.setDaemon(true);
        listener.start();
    }

    public void run() throws IOException, InterruptedException {
        connector.connect();
        System.out.println("Node " + id + ": spojen sa svim čvorovima.");

        startMessageListener();
        Thread.sleep(200);

        for (int i = 1; i <= iterations; i++) {
            long wait = (long) (Math.random() * 1000) + 200;
            System.out.printf("[Node %d] Radim van KS (%dms)...%n", id, wait);
            Thread.sleep(wait);

            requestCS();
            waitForCS();

            long csTime = (long) (Math.random() * 1000) + 300;
            System.out.printf(">>> [Node %d | iter=%d] ULAZIM u kriticnu sekciju (%dms)%n", id, i, csTime);
            Thread.sleep(csTime);
            System.out.printf("<<< [Node %d | iter=%d] IZLAZIM iz kriticne sekcije%n", id, i);

            releaseCS();
        }

        System.out.println("[Node " + id + "] Zavrsio sve iteracije.");
        connector.close();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 5) {
            System.out.println("Upotreba: LamportMutexNode <id> <numNodes> <iterations> <nameServerAddress> <nameServerPort>");
            return;
        }
        int id                  = Integer.parseInt(args[0]);
        int numNodes            = Integer.parseInt(args[1]);
        int iterations          = Integer.parseInt(args[2]);
        String nameServerAddr   = args[3];
        int nameServerPort      = Integer.parseInt(args[4]);

        LamportMutexNode node = new LamportMutexNode(id, numNodes, iterations, nameServerAddr, nameServerPort);
        node.run();
    }
}
