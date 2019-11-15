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
	int tcpport=0; // server port
	int udpport = 1234; // 
	// private Socket clientSocket;
	byte[] buffer = new byte[1024];
//	DataInputStream in;
//	DataOutputStream out;
	int[][] data = new int[50][50];
	String msg; // msg that will be returned to the client
	DatagramSocket socket;
//	DatagramPacket receivedPacket;
	ServerSocket srvSocket;
	ArrayList<Socket> list  = new ArrayList<Socket>();
	boolean established;

	public static void main(String[] args) throws IOException {
		// SimpleServer s;
		// boolean established;

		try {
		// 	s = new SimpleServer();
		// 	while (true) {
		// 		established = s.udpConnect(); // main thread for controlling connection
		// 		s.clientSocket = s.srvSocket.accept();
		// 		System.out.println("established");
		// 		if (established) {
		// 			System.out.println("Making thread...");
		// 			synchronized (s.list) {
		// 				s.list.add(s.clientSocket);
		// 				System.out.printf("Total %d clients are connected!", s.list.size());
		// 			}
		// 			Thread t = new Thread(() -> { // establish a new thread
		// 				try {
		// 					System.out.println("Calling tcpConnect()...");
		// 					s.tcpConnect(s.clientSocket);

		// 				} catch (IOException e) {
		// 					// TODO Auto-generated catch block
		// 					// System.out.println("e Exception");
		// 					e.printStackTrace();
		// 				}
		// 				synchronized (s.list) {
		// 					System.out.println("Remove already!!!"+s.clientSocket.getPort());
		// 					s.list.remove(s.clientSocket);
		// 				}
		// 			});
		// 			t.start();
		// 		}
		// 	}

			SimpleServer server = new SimpleServer();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			// System.out.println("e1 Exception");
			e1.printStackTrace();
		}
	}

	public SimpleServer() throws IOException {
		// default sketch data
//		System.out.println("The default data is:");
//		for (int i = 0; i <= 49; i++) {
//			for (int j = 0; j <= 49; j++) {
//				data[i][j] = -543230;
//			}
//		}
		
		srvSocket = new ServerSocket(tcpport);

		while (true) {
				established = udpConnect(); // main thread for controlling connection
				Socket clientSocket = srvSocket.accept();
				System.out.println("established");
				
				if (established) {
					System.out.println("Making thread...");
					synchronized (list) {
						list.add(clientSocket);
						System.out.printf("Total %d clients are connected!", list.size());
					}

					Thread t = new Thread(() -> { // establish a new thread
						try {
							System.out.println("Calling tcpConnect()...");
							tcpConnect(clientSocket);

						} catch (IOException e) {
							// TODO Auto-generated catch block
							// System.out.println("e Exception");
							e.printStackTrace();
						}
						synchronized (list) {
							System.out.println("Remove already!!!"+clientSocket.getPort());
							list.remove(clientSocket);
						}
					});
					t.start();
				}
			}

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

		send(clientSocket); // call send default sketch
		receive(clientSocket); // call receive update method
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

/*
	public void send(Socket clientSocket) throws IOException {
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
*/
	public void send(Socket clientSocket) throws IOException{
		DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
		
//		for (int[] i : data) {
//			for (int p : i) {
//				out.writeInt(p);
//				System.out.print(p);
//			}
//		}
		
		for (int row=0; row<50; row++) {
			for (int col=0; col<50; col++) {
//				System.out.println("Pixel == "+data[row][col]);
				if (data[row][col] != 0) {		
					out.writeInt(data[row][col]);
					out.writeInt(col);
					out.writeInt(row);
					System.out.println("Successfully sent pixel!!!");
				}
					
			}
		}
	}
	
	public void receive(Socket clientSocket) throws IOException { // send color pixels

		DataInputStream in = new DataInputStream(clientSocket.getInputStream());

		System.out.println("Server port: "+tcpport);
		System.out.println("clientSocket port: "+clientSocket.getPort());

		while (true) {
			int pix = in.readInt(); // receive a pix
			System.out.println("In SimpleServer receive(), Pixel: "+pix);
			int col = in.readInt();
			System.out.println("In SimpleServer receive(), Col: "+col);
			int row = in.readInt();
			System.out.println("In SimpleServer receive(), Row: "+row);
			data[row][col] = pix;

			System.out.println("clientSocketList length: "+list.size());

			for (Socket s: list){
				if(s != clientSocket) { // not send differential update to clientSocket itself

					// send differential update
					DataOutputStream sout = new DataOutputStream(s.getOutputStream());
					System.out.println(s.getPort());
					sout.writeInt(pix); // client side need multiple thread to perform keep standby receiving and sending
					System.out.println("Sent pixel successfully, Pixel: "+pix);
					sout.writeInt(col);
					System.out.println("Sent col successfully, col: "+col);
					sout.writeInt(row);
					System.out.println("Sent row successfully row: "+row);

				}
			}


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
