import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class SimpleServer {
	int tcpport; // server port
	int udpport = 1234; // 
	private Socket clientSocket;
	byte[] buffer = new byte[1024];
//	DataInputStream in;
//	DataOutputStream out;
	int[][] data = new int[50][50];
	String msg; // msg that will be returned to the client
	DatagramSocket socket;
//	DatagramPacket receivedPacket;
	ServerSocket srvSocket;
	ArrayList<Socket> list  = new ArrayList<Socket>();

	public static void main(String[] args) throws UnknownHostException {
		SimpleServer s;
		boolean established;

		try {
			s = new SimpleServer();
			while (true) {
				established = s.udpConnect(); // main thread for controlling connection
				s.clientSocket = s.srvSocket.accept();
				System.out.println("established");
				if (established) {
					System.out.println("Making thread...");
					synchronized (s.list) {
						s.list.add(s.clientSocket);
						System.out.printf("Total %d clients are connected!", s.list.size());
					}
					Thread t = new Thread(() -> { // establish a new thread
						try {
							System.out.println("Calling tcpConnect()...");
							s.tcpConnect(s.clientSocket);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						synchronized (s.list) {
							s.list.remove(s.clientSocket);
						}
					});
					t.start();
					// s.tcpport++;
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public SimpleServer() throws IOException {
		// default sketch data
		System.out.println("The default data is:");
		for (int i = 0; i <= 49; i++) {
			for (int j = 0; j <= 49; j++) {
				data[i][j] = -543230;
			}
		}
		srvSocket = new ServerSocket(tcpport);
		// establish a UDP connection
//		udpConnect();
//		socket.close();

		// accept connection

	}

	public void tcpConnect(Socket clientSocket) throws IOException {
		System.out.println("In tcpConnect()...");
//		srvSocket = new ServerSocket(tcpport);

//		clientSocket = srvSocket.accept();
		System.out.println("Waiting...");
		// establish a TCP connection
		
		

		System.out.printf("TCP server is listening at port %d...", tcpport);

		System.out.println("Accepted!");

		send(); // call send default sketch
		receive(); // call receive update method
	}

	public boolean udpConnect() throws IOException {
		socket = new DatagramSocket(udpport);
		DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);

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

				srvSocket = new ServerSocket(0); // establish server socket by allocate a random port

				// receive random port
				tcpport = srvSocket.getLocalPort();
//				tcpport = 8888;
				System.out.println(tcpport);
				msg = localAddress.getHostAddress() + ", " + tcpport; // get port
				// socket = new DatagramSocket(4321);
				DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), receivedPacket.getAddress(),
						receivedPacket.getPort());
				socket.send(packet);
				System.out.println("Sent !!!");

			}
			socket.close();
//			srvSocket.close();
			return (true);
		}
	}

	public void send() throws IOException {
		// get the data cols and rows of default sketch
		DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
		out.writeInt(50);
		out.writeInt(50);

		System.out.println("The file size is sent!!");
		// send out the default sketch data
		for (int[] i : data) {
			for (int p : i) {
				out.writeInt(p);
				System.out.print(p);
			}
		}
		System.out.println("The default sketch is sent!");
	}

	public void receive() throws IOException { // send color pixels

		DataInputStream in = new DataInputStream(clientSocket.getInputStream());

		System.out.println("I am port: "+tcpport);
		System.out.println("I am port: "+clientSocket.getPort());

		while (true) {
			int pix = in.readInt(); // receive a pix
			System.out.println(pix);
			int col = in.readInt();
			System.out.println(col);
			int row = in.readInt();
			System.out.println(row);
			data[row][col] = pix;
		}
	}

	// private void server(Socket clientSocket) throws IOException {
	// // display the client socket and port number
	// System.out.printf("Established a connection to host %s:%d\n\n",
	// clientSocket.getInetAddress(),
	// clientSocket.getPort());
	// // set up data input and output stream
	//
	// System.out.println("Established the in/output stream");
	//
	// int data[][] = { { 0, 1, 2 }, { 3, 4, 5 } };
	// int fileSize = 0;
	// for (int i[] : data) {
	// fileSize += i.length;
	// }
	// out.writeInt(fileSize);
	// System.out.println("The file size is sent!!");
	//
	// for (int[] i : data) {
	// for (int p : i) {
	// out.writeInt(p);
	// System.out.println(p);
	// }
	// }
	// }

}
