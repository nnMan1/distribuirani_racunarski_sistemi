package tcp_comunication;

public class NameTable {
	final int maxSize = 1000;
	private String[] names = new String[maxSize];
	private String[] hosts = new String[maxSize];
	private int[] ports = new int[maxSize];
	private int dirsize = 0;
	
	synchronized int search(String s) {
		for(int i=0;i<maxSize;i++)
			if(s.equals(names[i]))
				return i;
		
		return -1;
	}
	
	synchronized int insert(String s, String hostName, int portNumber) {
		int oldIndex = search(s);
		if(oldIndex != -1) 
			return 0;
		
		names[dirsize] = s;
		hosts[dirsize] = hostName;
		ports[dirsize] = portNumber;
		dirsize ++;
		return 1;		
	}
	
	int getPort(int index) {
		return ports[index];
	}
	
	String getHostName(int index) {
		return hosts[index];
	}
	
}
