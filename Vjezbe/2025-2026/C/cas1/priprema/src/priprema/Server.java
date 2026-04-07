package priprema;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.DatagramPacket;

public class Server {

	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(9876);
        byte[] baferZaPrijem = new byte[8192];
        
        System.out.println("Server je pokrenut i čeka poruku...");
        
        while(true) {
            DatagramPacket dolazniPaket = new DatagramPacket(baferZaPrijem, baferZaPrijem.length);
            socket.receive(dolazniPaket);
            
            String poruka = new String(dolazniPaket.getData(), 0, dolazniPaket.getLength());
            System.out.println("Primljeno: " + poruka);
            
            InetAddress adresaPosiljaoca = dolazniPaket.getAddress();
            int portPosiljaoca = dolazniPaket.getPort();
            byte[] odgovorBajtovi = "Poruka primljena!".getBytes();
            
            DatagramPacket odgovorPaket = new DatagramPacket(odgovorBajtovi, odgovorBajtovi.length, adresaPosiljaoca, portPosiljaoca);
            socket.send(odgovorPaket);
        }
    }

}
