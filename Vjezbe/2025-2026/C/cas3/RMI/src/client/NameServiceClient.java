package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import server.NameService;

public class NameServiceClient {

	public static void main(String[] args) {
		
		try {
			NameService name_service = (NameService) Naming.lookup("rmi://127.0.0.1:1099/name_service_1");
			name_service.insert("google.com", "8.8.8.8", 443);
			name_service.insert("facebook.com", "84.93.128.64", 443);
			
			String name = "facebook.com";
			int idx = name_service.search(name);
			String host_name = name_service.get_host_name(idx);
			System.out.println("Host for " + name + " is " + name_service.get_host_name(idx));		
			System.out.println("Port for " + name + " is " + name_service.get_port(idx));		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
