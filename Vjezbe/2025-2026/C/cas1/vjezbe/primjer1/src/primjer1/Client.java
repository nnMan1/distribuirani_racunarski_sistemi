package primjer1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
			
		InetAddress IPAdresa = InetAddress.getByName("localhost");
		try(DatagramSocket socket = new DatagramSocket();) {
			byte[] podaciZaSlanje = "Zdravo sa klijenta".getBytes("UTF-8");
			
			DatagramPacket paketZaSlanje = new DatagramPacket(
					podaciZaSlanje, 
					podaciZaSlanje.length,
					IPAdresa,
					5000
					);
			
			socket.send(paketZaSlanje);
			
			byte[] baferZaPrijem = new byte[8192];
			DatagramPacket odgovorPaket = new DatagramPacket(baferZaPrijem, baferZaPrijem.length);
			socket.receive(odgovorPaket);
			
			String poruka = new String(odgovorPaket.getData(), 0, odgovorPaket.getLength());
			System.out.println(poruka);
			
		} catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		System.out.println(IPAdresa.getHostAddress());
			
	}

}
