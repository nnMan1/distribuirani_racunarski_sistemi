package slanje_slike;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.*;
import java.nio.ByteBuffer;

public class Client {
	
	//klijent salje sliku serveru i server je cuva

	public static void main(String[] args) throws Exception {
		
		int port = 9876;		
		InetAddress serverAddress = InetAddress.getByName("localhost");
		
		DatagramSocket socket = new DatagramSocket(); //kod klijenta ne stavljamo InetAddress i port
		InputStream imageStream = new FileInputStream("slika.png");
		byte[] imageBytes = imageStream.readAllBytes();
		
		int fragmentSize = 4096;
		int offset = 0;
		
		int i = 0;
		
		while(offset < imageBytes.length) {
			int length = Math.min(fragmentSize, imageBytes.length - offset);

			ByteBuffer bb = ByteBuffer.allocate(4 + length); // 4 bajta za int + podaci
			bb.putInt(i); // Ubacujemo ID na početak
			bb.put(imageBytes, offset, length); // Dodajemo komad slike
			
			DatagramPacket packet = new DatagramPacket(bb.array(), 4 + length, serverAddress, port);
			socket.send(packet);
			offset += length;
			
//			Thread.sleep(1); // Pauza od samo 1 milisekunde pravi ogromnu razliku
			
			System.out.println("Fragment " + i++ + " sent.");
		}		
		
		
		socket.send(new DatagramPacket(new byte[0], 0, serverAddress, port));
		System.out.println("Slika je poslata!");
		
		byte[] buffer = new byte[8192];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);	
		socket.receive(packet);
		
        String message = new String(packet.getData(), 0, packet.getLength());		
		System.out.println("Server kaze: " + message);
		
        socket.close();
	}

}
