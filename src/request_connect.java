import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class request_connect {
	public request_connect() throws IOException {
		String msg = "request";
		
		DatagramSocket socket = new DatagramSocket(12345);
		DatagramPacket packet = new DatagramPacket(msg.getBytes(),msg.length(),InetAddress.getByName("255.255.255.255"), 5555);

		socket.send(packet); // sending broadcasting message out to request connection
	}
}
