package primjer1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

	public static void main(String[] args) throws UnknownHostException {
		
		InetAddress IPAdresa = InetAddress.getByName("localhost");
		try(DatagramSocket socket = new DatagramSocket(5000);) {
			
			System.out.println("Server je pokrenut i ceka poruku ...");
			
			while(true) {
				byte[] baferZaPrijem = new byte[8192];
				DatagramPacket dolazniPaket = new DatagramPacket(
						baferZaPrijem, 
						baferZaPrijem.length);
				socket.receive(dolazniPaket);
				
				String poruka = new String(dolazniPaket.getData(), 0, dolazniPaket.getLength());
				System.out.println(poruka);
				
				int port = dolazniPaket.getPort();
				InetAddress adresa = dolazniPaket.getAddress();
				
				byte[] odgovor = "Poruka primljena.".getBytes();
				
				DatagramPacket odgovorPaket = new DatagramPacket(odgovor, odgovor.length, adresa, port);
				socket.send(odgovorPaket);
			}
			
		} catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		System.out.println(IPAdresa.getHostAddress());
			
	}

}
