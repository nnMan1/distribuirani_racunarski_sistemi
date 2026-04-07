package tcp_comunication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.jar.Attributes.Name;

public class NameServer {

	NameTable table;
	
	public NameServer() {
		table = new NameTable();
	}
	
	void handleClient(Socket client) throws IOException {
		
		try(
			Socket s = client;
			BufferedReader reader =  new BufferedReader(
										new InputStreamReader(client.getInputStream()));
			BufferedWriter out =  new BufferedWriter(
										new OutputStreamWriter(client.getOutputStream()));
				) {
		
			String line = reader.readLine();
			StringTokenizer tokenizer = new StringTokenizer(line);
			
			String tag = tokenizer.nextToken();
			System.out.println(tag);
					
			if(tag.equals("search")) {
				int index = table.search(tokenizer.nextToken());
				if(index == -1)	
					out.write(-1 + " " + "nullhost");
				else
					out.write(table.getPort(index) + " " + table.getHostName(index));
			} else if(tag.equals("insert")) {
				String name = tokenizer.nextToken();
				String hostName = tokenizer.nextToken();
				int port = Integer.parseInt(tokenizer.nextToken());
				int retValue = table.insert(name, hostName, port);
				out.write(""+retValue);
			} 
			out.newLine();
			out.flush();
		} catch (Exception e) {
			System.err.println("Greška prilikom obrade klijenta: " + e.getMessage());
		}
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NameServer ns = new NameServer();
		
		try(ServerSocket socket = new ServerSocket(5000)) {
			while(true) {
				Socket connection = socket.accept();
				ns.handleClient(connection);
			}			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
