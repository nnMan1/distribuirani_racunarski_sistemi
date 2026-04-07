package tcp_comunication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NameServerClient {
	
	public String host;
	public int port;
	
	public NameServerClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public int insertName(String name, String hname, int portNum) throws IOException {
        // Try-with-resources osigurava da se SVE zatvori (socket, reader, writer)
        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            out.write("insert " + name + " " + hname + " " + portNum + "\n");
            out.flush();

            String response = in.readLine();
            return (response != null) ? Integer.parseInt(response) : -1;
        }
    }

    public String search(String name) throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            out.write("search " + name + "\n");
            out.flush();

            return in.readLine();
        }
    }
	
	public static void main(String[] args) {
		
		NameServerClient client = new NameServerClient("localhost", 5000);

		try {
			client.insertName("google.com", "172.217.19.110", 443);
			client.insertName("facebook.com", "31.13.84.36", 443);

			String address = client.search("google.com");
			System.out.println(address);

			address = client.search("facebook.com");
			System.out.println(address);
			

			address = client.search("ucg.ac.me");
			System.out.println(address);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	

}
