import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

//public class request_Connect {  //client side request
//	public request_Connect() throws IOException {
//		String request = "Request";
//		String reply = "Reply received";
//
//		DatagramSocket socket = new DatagramSocket(12345);
//		DatagramPacket packet = new DatagramPacket(request.getBytes(), request.length(),
//				InetAddress.getByName("255.255.255.255"), 12345); 
//		socket.send(packet); //broadcasting "Request" message
//
//		DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
//		while (true) {
//			System.out.println("Listening...");
//
//			socket.receive(receivedPacket);
//			String content = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
//
//			if (content.equals(reply))
//				System.out.println(receivedPacket.getAddress());
//			else if (content.equals(request)) {
//				packet = new DatagramPacket(reply.getBytes(), reply.length(), receivedPacket.getAddress(),
//						receivedPacket.getPort());
//				socket.send(packet);
//			}
//		}
//	}
//}
public class simple_client {
	public simple_client(String server, int port) throws IOException {
		Socket socket = new Socket(server, port);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		// thread
		Thread t = new Thread(() -> {
			byte[] buffer = new byte[1024];
			try {
				while (true) {
					int len = in.readInt();
					in.read(buffer, 0, len);
					System.out.println(new String(buffer, 0, len));
				}
			} catch (IOException ex) {
				System.err.println("Connection dropped!");
				System.exit(-1);
			}
		});
		// start the threat
		t.start();
		
		while(true) {
			
		}
	}
}