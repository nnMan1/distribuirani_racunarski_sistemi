package slanje_slike;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.TreeMap;

public class Server {

	public static void main(String[] args) {
		
		ByteArrayOutputStream imageStream = new ByteArrayOutputStream();

		TreeMap<Integer, byte[]> imageMap = new TreeMap<Integer, byte[]>();
		
		try(DatagramSocket socket = new DatagramSocket(5000);
			FileOutputStream fos = new FileOutputStream("primljena_slika.png");) {
			
			socket.setReceiveBufferSize(1024*1024);
			
			System.out.println("Server je pokrenut i ceka poruku ...");
			
			while(true) {
				byte[] baferZaPrijem = new byte[8192];
				DatagramPacket dolazniPaket = new DatagramPacket(
						baferZaPrijem, 
						baferZaPrijem.length);
				socket.receive(dolazniPaket);
				
				if(dolazniPaket.getLength() == 4)
					break;
				
				ByteBuffer buffer = ByteBuffer.wrap(dolazniPaket.getData());
				int packetId = buffer.getInt();
				byte[] imageData = Arrays.copyOfRange(dolazniPaket.getData(), 4, dolazniPaket.getLength());
								
				imageMap.put(packetId, imageData);
//				imageStream.write(imageData);
			}
			
//			fos.write(imageStream.toByteArray());	
			
			for(byte[] fragment: imageMap.values()) { //niz vrijednosti sortiran po kljucevima
				fos.write(fragment);
			}
			
		} catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
				
	}

}
