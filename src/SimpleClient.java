// Description: Broadcasts a request with username to the network using UDP
//
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class SimpleClient {
	public SimpleClient (String name) throws IOException {
	String msg = "Name: "+name;
	String content; // read server content
	
	DatagramSocket receivedSocket = new DatagramSocket(4321); // socket used for receving
	DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
	
	while(true) {
		System.out.println("Listening...");
		
		receivedSocket.receive(receivedPacket); // Receive server's response
		System.out.println("Received");
	}
}
}
