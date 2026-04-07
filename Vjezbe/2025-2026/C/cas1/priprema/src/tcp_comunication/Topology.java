package tcp_comunication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Topology {
	public static LinkedList<Integer> readNeighbours(int myId, int N) {
		
		LinkedList<Integer> neighbours = new LinkedList<Integer>();
		
		try(BufferedReader dIn = new BufferedReader(
								 new FileReader("topoloy"+myId+".txt"))) {
			StringTokenizer st = new StringTokenizer(dIn.readLine());
			while(st.hasMoreTokens()) {
				int neighbour = Integer.parseInt(st.nextToken());
				neighbours.add(neighbour);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return neighbours;
		
	}
}
