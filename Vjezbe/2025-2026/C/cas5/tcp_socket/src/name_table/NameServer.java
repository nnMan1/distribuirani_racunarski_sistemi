package name_table;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class NameServer {
	
	NameTable table;
	
	public NameServer() {
		table = new NameTable();
	}
	
	synchronized void handleClient(Socket socket) {
		try(BufferedReader reader = new BufferedReader(
				 new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(
				 				 new OutputStreamWriter(socket.getOutputStream()))){
			
			String line = reader.readLine();
			StringTokenizer st = new StringTokenizer(line);
			
			String tag = st.nextToken();
			
			if(tag.equals("search")) {
				int idx = table.search(st.nextToken());
				if(idx != -1) 
					writer.write(table.getPort(idx) + " " + table.getHostName(idx));
				else
					writer.write("-1 nullhost");
				
				writer.write("\n");
				writer.flush();				
			} else if(tag.equals("insert")) {
				String name = st.nextToken();
				String hostName = st.nextToken();
				int port = Integer.parseInt(st.nextToken());
				
				int ret = table.insert(name, hostName, port);
				writer.write("" + ret + "\n");
				writer.flush();
			} 			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		int NAME_SERVER_PORT = 5000;
		if(args.length >= 1) 
			NAME_SERVER_PORT = Integer.parseInt(args[0]);
		
		NameServer ns = new NameServer();
		
		try(ServerSocket server = new ServerSocket(NAME_SERVER_PORT)) {
			while(true) {
				Socket socket = server.accept();
				 ns.handleClient(socket);
				 socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
