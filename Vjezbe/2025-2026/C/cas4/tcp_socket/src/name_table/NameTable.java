package name_table;

public class NameTable {
	final int max_size = 1000;
	private String[] names = new String[max_size]; //id cvora
	private String[] hosts = new String[max_size]; //ip adresa
	private int[] ports = new int[max_size];	//port
	private int table_size = 0;					//br cvorova koji se do sad registrovao
	
	int insert(String name, String host, int port) {
		int oldIndex = search(name);
		if(oldIndex != -1)
			return 0; 
		
		names[table_size] = name;
		hosts[table_size] = host;
		ports[table_size] = port;
		table_size ++;
		return 1;
	}

	int search(String name) {
		for(int i=0;i<table_size;i++)
			if(name.compareTo(names[i]) == 0) 
				return i;
		
		return -1;
	}
	
	int getPort(int idx) {
		if(idx < 0 || idx >= table_size)
			throw new IndexOutOfBoundsException();
		
		return ports[idx];
	}
	
	String getHostName(int idx) {
		if(idx < 0 || idx >= table_size)
			throw new IndexOutOfBoundsException();
		
		return hosts[idx];
	}
	
}
