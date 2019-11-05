
// Description: Broadcasts a request with username to the network using UDP
//
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class SimpleClient {

	private String serverIP;
	private int serverPort;

	public SimpleClient(String name) throws IOException {
		String msg = "Name: " + name;
		String content; // read server content
		InetAddress serverAddr = InetAddress.getByName("158.182.8.255");

		DatagramSocket receivedSocket = new DatagramSocket(4321); // socket used for receving
		DatagramSocket sentSocket = new DatagramSocket(1234);
		DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
		DatagramPacket sentPacket = new DatagramPacket(msg.getBytes(), msg.length(), serverAddr, 1234);

		// Send a request to server using UDP
		sentSocket.send(sentPacket);
		System.out.println("Sent!!!");

		System.out.println("Listening...");
		receivedSocket.receive(receivedPacket); // Receive server's response
		System.out.println(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));
		System.out.println("Received");

		// Receive back a packet from the server, which contains serverIP and serverPort
		String receivedMsg = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
		serverIP = receivedMsg.substring(0, receivedMsg.indexOf("\n"));
		serverPort = Integer.parseInt(receivedMsg.substring(receivedMsg.indexOf(":") + 2));

		System.out.println("IP:" + serverIP);
		System.out.println("Port:" + serverPort);

		System.out.println("yeah");

		receivedSocket.close();
		sentSocket.close();

		establish(); // establish TCP connection
	}

	public void establish() throws UnknownHostException, IOException {
		System.out.println("establishing...");
		// Setup TCP Connection with server

		// System.out.println(serverPort);
		// System.out.println(serverIP);

		Socket tcpSocket = new Socket(serverIP, serverPort);
		// System.out.println("tcp Connect!");

		// Receive the data sent by server
		DataInputStream in = new DataInputStream(tcpSocket.getInputStream());
		// System.out.println("Set up inputStream!");

		// Send data to server
		DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream());
		// System.out.println("Set up outputStream!");

		// int[][] data; // drawing data
		int data;
		int len;
		int totalSize = 0;
		byte[] buffer = new byte[1024];
		// int[] arr;

		totalSize = in.readInt();
		
		// Read data
		while (totalSize > 0) {
			System.out.println("Reading data...");
			len = in.readInt();
			totalSize--;
			System.out.println(totalSize);
		}
		
	}
}
