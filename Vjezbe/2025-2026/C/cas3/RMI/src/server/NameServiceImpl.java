package server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NameServiceImpl extends UnicastRemoteObject implements NameService {

	
	static final int max_size = 1000;
	private String[] names = new String[max_size];
	private String[] hosts = new String[max_size]; 
	private int[] ports = new int[max_size];
	private int table_size = 0;
	
	public NameServiceImpl() throws RemoteException {	
	}
	
	@Override
	public int search(String name) throws RemoteException {
		for(int i=0;i<table_size;i++)
			if(name.compareTo(names[i]) == 0) 
				return i;
		
		return -1;
	}

	@Override
	public int insert(String name, String host, int port) throws RemoteException {
		int oldIndex = search(name);
		if(oldIndex != -1)
			return 0; 
		
		names[table_size] = name;
		hosts[table_size] = host;
		ports[table_size] = port;
		table_size ++;
		return 1;
	}

	@Override
	public int get_port(int index) throws RemoteException {
		if(index < 0 || index >= table_size)
			throw new IndexOutOfBoundsException();
		
		return ports[index];
	}

	@Override
	public String get_host_name(int index) throws RemoteException {
		if(index < 0 || index >= table_size)
			throw new IndexOutOfBoundsException();
		
		return hosts[index];
	}
	
	public static void main(String[] args) throws RemoteException {
		java.rmi.registry.LocateRegistry.createRegistry(1099);
		try {
			NameServiceImpl obj = new NameServiceImpl();
			Naming.rebind("name_service_1", obj);
			System.out.println("Name service is bound in registry");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
