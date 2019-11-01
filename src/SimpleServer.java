import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SimpleServer {
	public SimpleServer() throws IOException {
		DatagramSocket socket = new DatagramSocket(1234);
		DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
		String msg; // msg that will be returned to the client
		
		while (true) {
			
			System.out.println("Listening...");

			socket.receive(receivedPacket); // Keep listening the incoming packets
			String content = new String(receivedPacket.getData(), 0, receivedPacket.getLength()); // Once received, get the content of the packet

			if (content.contains("Name:")) { // Check if it's the right packet
				System.out.println(receivedPacket.getAddress()); // If yes, get the sender's address
			
				InetAddress localAddress = InetAddress.getLocalHost(); // Get Host Address
				//System.out.println(a);
				msg = localAddress.getHostAddress() + "\nPort Number: 1234";

				DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), receivedPacket.getAddress(), 1234);
				socket.send(packet);
				
				int[][] data = new int[50][50];	
			}
		}
	}

}
