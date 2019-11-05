// Description: Broadcasts a request with username to the network using UDP
//
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class SimpleClient {
	public SimpleClient (String name) throws IOException {
	String msg = "Name: "+name;
	String content; // read server content
	InetAddress serverAddr= InetAddress.getByName("158.182.8.255");
	
	DatagramSocket receivedSocket = new DatagramSocket(4321); // socket used for receving
	DatagramSocket sentSocket = new DatagramSocket(1234);
	DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
	DatagramPacket sentPacket = new DatagramPacket(msg.getBytes(), msg.length(), serverAddr, 1234);
	
	sentSocket.send(sentPacket);
	System.out.println("Sent!!!");
	
	while(true) {
		System.out.println("Listening...");
		receivedSocket.receive(receivedPacket); // Receive server's response
		System.out.println(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));
		System.out.println("Received");
		
		String receivedMsg = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
		String serverIP = receivedMsg.substring(0, receivedMsg.indexOf("\n"));
		String serverPort = receivedMsg.substring(receivedMsg.indexOf(":")+2);
		
		System.out.println("IP:" + serverIP);
		System.out.println("Port:" + serverPort);
		
		break;
	}
	System.out.println("yeah");
}
}
