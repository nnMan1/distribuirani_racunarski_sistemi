package name_table;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NameClient {
	
	String host;
	int port;
	
	public NameClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public int insert(String name, String host, int port) throws IOException {
		try (Socket socket = new Socket(this.host, this.port);
				 BufferedReader reader = new BufferedReader(
						 				 new InputStreamReader(socket.getInputStream()));
				 BufferedWriter writer = new BufferedWriter(
						 				 new OutputStreamWriter(socket.getOutputStream()))) {
				
				writer.write("insert " + name+ " " + host + " " + port + "\n");
				writer.flush();
				
				String response = reader.readLine();
				return response != null ? Integer.parseInt(response) : -1;
			}
	}
	
	public String search(String name) throws IOException {
		try (Socket socket = new Socket(this.host, this.port);
				 BufferedReader reader = new BufferedReader(
						 				 new InputStreamReader(socket.getInputStream()));
				 BufferedWriter writer = new BufferedWriter(
						 				 new OutputStreamWriter(socket.getOutputStream()))) {
				
				writer.write("search " + name +"\n");
				writer.flush();
				
				String response = reader.readLine();
				return response;
			} 
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NameClient client = new NameClient("localhost", 5000);
		
		try  {
			client.insert("google.com", "8.8.8.8", 443);
			client.insert("cvor_1", "localhost", 9000);

			String address = client.search("cvor_1");
			System.out.println(address);
		} catch (Exception e) {
		}
	}

}
