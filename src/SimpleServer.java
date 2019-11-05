import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
	public SimpleServer() throws IOException {
		int svrport = 1234;
		DatagramSocket socket = new DatagramSocket(svrport);
		DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
		String msg; // msg that will be returned to the client

		while (true) {

			System.out.println("Listening...");

			socket.receive(receivedPacket); // Keep listening the incoming packets
			String content = new String(receivedPacket.getData(), 0, receivedPacket.getLength()); // Once received, get
																									// the content of
																									// the packet

			if (content.contains("Name:")) { // Check if it's the right packet
				System.out.println(receivedPacket.getAddress()); // If yes, get the sender's address

				InetAddress localAddress = InetAddress.getLocalHost(); // Get Host Address
				// System.out.println(a);
				msg = localAddress.getHostAddress() + "\nPort Number: 1234";
				socket = new DatagramSocket(4321);
				DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), receivedPacket.getAddress(),
						4321);
				socket.send(packet);

			}
			break;

		}
		socket.close();
		// establish a TCP connection
		ServerSocket srvSocket = new ServerSocket(svrport);

		System.out.printf("TCP server is listening at port %d...", svrport);
		Socket clientSocket = srvSocket.accept();
		System.out.println("Accepted!");
		server(clientSocket);

	}

	private void server(Socket clientSocket) throws IOException {
		// buffer
		byte[] buffer = new byte[1024];
		// display the client socket and port number
		System.out.printf("Established a connection to host %s:%d\n\n", clientSocket.getInetAddress(),
				clientSocket.getPort());
		// set up data input and output stream
		DataInputStream in = new DataInputStream(clientSocket.getInputStream());
		DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

		System.out.println("Established the in/output stream");

		// int len = in.readInt();
		// in.read(buffer, 0, len);
		// System.out.println(buffer.toString());
		String str = "The message sent by TCP is:";

		// System.out.println("message is prepared!");
		// send out the message by TCP
		// out.writeInt(str.length());
		// System.out.println("message size is sent!");
		// out.write(str.getBytes(), 0, str.length());
		// System.out.println("message conrtent is sent!");
		int data[][] = { { 0, 1, 2 }, { 3, 4, 5 } };
		int fileSize=0;
		for (int i[]: data) {
			fileSize+=i.length;
		}
		out.writeInt(fileSize);
		System.out.println("The file size is sent!!");
		
		for (int[] i : data) {
			for (int p : i) {
				out.writeInt(p);
				System.out.println(p);
			}
		}
		clientSocket.close();
	}

}
