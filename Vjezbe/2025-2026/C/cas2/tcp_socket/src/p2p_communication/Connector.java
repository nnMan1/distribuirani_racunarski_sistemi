package p2p_communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import name_table.NameClient;

public class Connector implements Closeable {
	int N; //broj cvorova u sistemu
	int id; //id trenuntog procesa
	NameClient nameClient;
	
	ServerSocket listener; //slusamo poruke koje nam dolaze
	Socket[] links; //odlazne konekcije prema ostalim cvorovima
	BufferedReader[] readers;
	BufferedWriter[] writers;
	
	public Connector(int id, int N, String nameServerAddress, int nameServerPort) throws IOException {
		this.id = id;
		this.N = N;
		this.nameClient = new NameClient(nameServerAddress, nameServerPort);
		this.listener = new ServerSocket(0); // bind to any available port
		
		links = new Socket[N];
		readers = new BufferedReader[N];
		writers = new BufferedWriter[N];
	}
	
	void connect() throws IOException {
		// Register this node with the name server using localhost
		nameClient.insert("" + id, listener.getInetAddress().getHostAddress(), listener.getLocalPort());
	
		// Accept connections from nodes with lower id
		for(int i = 0; i < id; i++) {
			Socket socket = listener.accept();
			BufferedReader reader = new BufferedReader(
					 new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(
				 				 new OutputStreamWriter(socket.getOutputStream()));
			
			String line = reader.readLine();
			StringTokenizer st = new StringTokenizer(line);
			int hisId = Integer.parseInt(st.nextToken());
			String tag = st.nextToken();
			
			if(tag.equals("hello")) {
				links[hisId] = socket;
				readers[hisId] = reader;
				writers[hisId] = writer;
			}
		}
		
		// Connect to nodes with higher id
		for(int i = id + 1; i < N; i++) {
			String client = nameClient.search("" + i);
			while(client == null || client.equals("-1 nullhost")) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new IOException("Interrupted while waiting for node " + i);
				}
				client = nameClient.search("" + i);
			}
			
			StringTokenizer st = new StringTokenizer(client);
			int port = Integer.parseInt(st.nextToken());
			String host = st.nextToken();
			
			Socket socket = new Socket(host, port);
			BufferedReader reader = new BufferedReader(
					 new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(
				 				 new OutputStreamWriter(socket.getOutputStream()));
			
			writer.write("" + id + " hello\n");
			writer.flush();
			
			links[i] = socket;
			readers[i] = reader;
			writers[i] = writer;
		}
		
		System.out.println("Node " + id + " connected to all peers.");
	}
	
	public void setBufferSizes(int sendSize, int receiveSize) throws IOException {
		for(int i = 0; i < N; i++) {
			if(links[i] != null) {
				links[i].setSendBufferSize(sendSize);
				links[i].setReceiveBufferSize(receiveSize);
				System.out.println("Node " + id + " -> Node " + i 
					+ " sendBuf=" + links[i].getSendBufferSize() 
					+ " recvBuf=" + links[i].getReceiveBufferSize());
			}
		}
	}
	
	public void sendMessage(int destId, String message) throws IOException {
		writers[destId].write(message + "\n");
		writers[destId].flush();
	}
	
	public String receiveMessage(int srcId) throws IOException {
		return readers[srcId].readLine();
	}
	
	@Override
	public void close() throws IOException {
		for(int i = 0; i < N; i++) {
			if(links[i] != null) {
				links[i].close();
				//socket.close
				//automatski zatvara reader i writer
			}
		}
		listener.close();
	}
}
