package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NameService extends Remote {
	public int search(String name) throws RemoteException;
	public int insert(String name, String host, int port) throws RemoteException;
	public int get_port(int index) throws RemoteException; 
	public String get_host_name(int index) throws RemoteException;
}
