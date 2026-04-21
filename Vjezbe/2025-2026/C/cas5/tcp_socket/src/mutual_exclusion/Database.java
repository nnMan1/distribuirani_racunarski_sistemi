package mutual_exclusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Database {
	private static class Ticket {
		String brLeta;
		String brSjedista;
		String JMBG;
		
		public Ticket(String brLeta, String brSjedista, String JMBG) {
			this.brLeta = brLeta;
			this.brSjedista = brSjedista;
			this.JMBG = JMBG;
		}
	};
	
	List<Ticket> tickets = new LinkedList<>();
	ServerSocket listener;
	
	public Database(int port) throws IOException {
		this.listener = new ServerSocket(port);
	}
	
	
	boolean isAvailable(String brLeta, String brSjedista) {
		return tickets.stream()
				.filter(t -> t.brLeta == brLeta && t.brSjedista == brSjedista)
				.toList()
				.size() > 0;
	}

	void book(String brojLeta, String brojSjedišta, String JMBG) {
		tickets.add(new Ticket(brojLeta, brojSjedišta, JMBG));
	}
	
	void handleRequests(Socket socket) throws IOException {
		BufferedReader reader = new BufferedReader(
				 new InputStreamReader(socket.getInputStream()));
		BufferedWriter writer = new BufferedWriter(
			 				 new OutputStreamWriter(socket.getOutputStream()));
		
		String line = reader.readLine();
		StringTokenizer tokenizer = new StringTokenizer(line);
		
		String command = tokenizer.nextToken();
		
		if(command == "isAvaliable") {
			
			writer.write("true");
			writer.flush();
		} else if(command == "book") {
			
		}
		
	}
	
	void run() {
		
	}
}
