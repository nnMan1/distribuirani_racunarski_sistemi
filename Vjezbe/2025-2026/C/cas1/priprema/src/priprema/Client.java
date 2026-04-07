package priprema;
import java.io.IOException;
import java.net.*;

public class Client {

	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket();
        InetAddress IPAdresa = InetAddress.getByName("localhost");
        
        byte[] podaciZaSlanje = "Zdravo sa klijenta!".getBytes();
        
        DatagramPacket paketZaSlanje = new DatagramPacket(podaciZaSlanje, podaciZaSlanje.length, IPAdresa, 9876);
        socket.send(paketZaSlanje);
        
        byte[] baferZaPrijem = new byte[8192];
        DatagramPacket dolazniPaket = new DatagramPacket(baferZaPrijem, baferZaPrijem.length);
        
        socket.receive(dolazniPaket);
        
        String odgovor = new String(dolazniPaket.getData(), 0, dolazniPaket.getLength());
        System.out.println("Server kaže: " + odgovor);
        
        socket.close();
	}

}
