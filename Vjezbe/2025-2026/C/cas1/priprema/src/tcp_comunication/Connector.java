package tcp_comunication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class Connector implements Closeable{
	int myId;
	int numProc;
	String nameServerAddress;
	int nameServerPort;
	
	NameServerClient nameServerClient;
	
	ServerSocket listener; //dolazne konekcije
	Socket [] link;		   //odlazne konekcije. Za svaki drugi cvor po jedan socket
		
	public Connector(int myId, int numProc, String nameServerAddress, int nameServerPort) throws IOException {
		this.myId = myId;
		this.numProc = numProc;
		listener = new ServerSocket();
		this.nameServerAddress = nameServerAddress;
		this.nameServerPort = nameServerPort;
	}
	
	
	public void connect(BufferedReader[] dataIn, BufferedWriter[] dataOut) throws IOException, InterruptedException {
		nameServerClient = new NameServerClient(nameServerAddress, nameServerPort);
		nameServerClient.insertName("node"+myId, listener.getInetAddress().getHostAddress(), listener.getLocalPort());		
	
		for(int i=0;i<myId;i++) {
			Socket s = listener.accept();
			BufferedReader dIn = new BufferedReader(
								 new InputStreamReader(s.getInputStream()));
			
			BufferedWriter dOut = new BufferedWriter(
									new OutputStreamWriter(s.getOutputStream()));
			
			String line = dIn.readLine();
			StringTokenizer st = new StringTokenizer(line);
			
			int hisId = Integer.parseInt(st.nextToken());
			String tag = st.nextToken();
			
			if(tag.equals("hello")) {
				link[hisId] = s;
				dataIn[hisId] = dIn;
				dataOut[hisId] = dOut;
			}			
		}
		
		for(int i=myId+1;i<numProc;i++) {
			String client = nameServerClient.search("node"+i);
			while(client.equals("-1")) {
				Thread.sleep(100);
				client = nameServerClient.search("node"+i);
			}
			
			StringTokenizer st = new StringTokenizer(client);
			String clientName = st.nextToken();
			int clientPort = Integer.parseInt(st.nextToken());
			
			Socket s = new Socket(clientName, clientPort);
			BufferedReader dIn = new BufferedReader(
								 new InputStreamReader(s.getInputStream()));
			
			BufferedWriter dOut = new BufferedWriter(
									new OutputStreamWriter(s.getOutputStream()));
			
			dOut.write(myId + "\n");
			dOut.write("hello");
			dOut.flush();
			
			link[i] = s;
			dataIn[i] = dIn;
			dataOut[i] = dOut;
		}
	}


	@Override
	public void close() throws IOException {
		listener.close();
		for(Socket l:link)
			l.close();
	}
	
}
