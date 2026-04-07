package slanje_slike;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.TreeMap;

public class Server {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		DatagramSocket socket = new DatagramSocket(9876);
		socket.setReceiveBufferSize(1024 * 1024); 
		//ako je buffer mali moguce je da discarduje neke pakete
        byte[] bafer = new byte[8192]; 
        
        // Koristimo ByteArrayOutputStream za dinamičko prikupljanje bajtova
        TreeMap<Integer, byte[]> mapaSlika = new TreeMap<>();
        ByteArrayOutputStream slikaStream = new ByteArrayOutputStream();
        
        System.out.println("Server spreman za prijem slike...");

        int i = 0;
        
        while (true) {
            DatagramPacket dolazniPaket = new DatagramPacket(bafer, bafer.length);
            socket.receive(dolazniPaket); // Čeka paket

            // Provera da li je stigao prazan paket (oznaka za kraj)
            if (dolazniPaket.getLength() == 0) {
            	
            	byte[] poruka = "Dobio sam posljednji paket.".getBytes();
            	
            	InetAddress clientAddr = dolazniPaket.getAddress();
            	int clientPort = dolazniPaket.getPort();
            	System.out.println(clientPort + " " + clientAddr.getHostAddress());
            	DatagramPacket packet = new DatagramPacket(poruka, poruka.length, clientAddr, clientPort);
            	socket.send(packet);
            	
                System.out.println("Svi fragmenti primljeni. Snimanje fajla...");
                break; 
            }
            
            int primljeniID = ByteBuffer.wrap(dolazniPaket.getData()).getInt();
            
            // Upisujemo primljene bajtove u naš stream
//            slikaStream.write(dolazniPaket.getData(), 0, dolazniPaket.getLength());
            byte[] podaci = new byte[dolazniPaket.getLength() - 4];
            System.arraycopy(dolazniPaket.getData(), 4, podaci, 0, podaci.length);
            mapaSlika.put(primljeniID, podaci);
            System.out.println("Primljen fragment veličine: " + dolazniPaket.getLength() + " bajtova.");
        
            System.out.println("Recieved fragment + " + primljeniID);
        }

        // 3. Snimanje prikupljenih bajtova u finalni fajl
//        byte[] finalnaSlika = slikaStream.toByteArray();
//        System.out.println(finalnaSlika.length);
        try (FileOutputStream fos = new FileOutputStream("primljena_slika.jpg")) {
//            fos.write(finalnaSlika);
            for(byte[] fragment: mapaSlika.values()) {
            	fos.write(fragment);
            }
        }
        

        System.out.println("Slika uspešno sačuvana kao 'primljena_slika.jpg'");
        socket.close();
	}

}
