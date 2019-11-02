// Description: Broadcasts a request with username to the network using UDP
//
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class SimpleClient {
	public SimpleClient (String name) throws IOException {
	String msg = "Name: "+name;
	String content; // read server content
	InetAddress serverIP; // server IP
	
	DatagramSocket socket = new DatagramSocket(5555);
	DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName("158.182.201.255"), 5555);
	socket.send(packet);
	
	DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
	
	while(true) {
		System.out.println("Listening...");
		
		socket.receive(receivedPacket); // Receive server's response
		serverIP = receivedPacket.getAddress();
		content = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
		if (content != null) break; // if content has content, stop listening
	}
	
	String serverPort = content.substring(content.indexOf(":")+2);
	int port = Integer.parseInt(serverPort);
	
	Socket tcpSocket = new Socket(serverIP, port);
	
	DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream()); // Establish TCP connection with server
	byte[] buffer = new byte[1024];
	
	}
}
