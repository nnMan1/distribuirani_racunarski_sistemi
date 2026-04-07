package slanje_slike;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Client {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub

		int SERVER_PORT = 5000;
		InetAddress serverAddress = InetAddress.getByName("localhost");
		int PACKET_SIZE = 4096;
		
		try(
				InputStream imageStream = new FileInputStream("slika.png");
				DatagramSocket socket = new DatagramSocket();
			) {
			byte[] imageBytes = imageStream.readAllBytes();

			int i = 0;
			int offset = 0; //dokle smo stigli sa slanjem slike
			while(offset < imageBytes.length) {
				
				int lenght = Math.min(PACKET_SIZE, imageBytes.length - offset);
//				byte[] messageBytes = Arrays.copyOfRange(imageBytes, offset, offset + lenght);
				
				ByteBuffer buffer = ByteBuffer.allocate(lenght + 4);
				buffer.putInt(i);
				buffer.put(imageBytes, offset, lenght);				
				
//				DatagramPacket packet = new DatagramPacket(messageBytes, lenght, serverAddress, SERVER_PORT);
				DatagramPacket packet = new DatagramPacket(buffer.array(), lenght + 4, serverAddress, SERVER_PORT);

				socket.send(packet);
				
				i++;
				offset += lenght;
			}
			
			ByteBuffer buffer = ByteBuffer.allocate(4);
			buffer.putInt(i);
			DatagramPacket packet = new DatagramPacket(buffer.array(), 4, serverAddress, SERVER_PORT);
			socket.send(packet);
			
			
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

}
