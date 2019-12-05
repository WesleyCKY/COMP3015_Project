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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleServer {
	int tcpport = 0; // server port
	int udpport = 1234; //
	// private Socket clientSocket;
	byte[] buffer = new byte[1024];
	// DataInputStream in;
	// DataOutputStream out;
	int[][] data = new int[50][50];
	String msg; // msg that will be returned to the client
	DatagramSocket socket;
	// DatagramPacket receivedPacket;
	ServerSocket srvSocket;
	ArrayList<Studio> studiolist = new ArrayList<Studio>();

	boolean established;
	String option;
	String studioName;
	DataInputStream in;
	DataOutputStream out;
	byte[] optionBuffer = new byte[1024];

	public static void main(String[] args) throws IOException {
		// SimpleServer s;
		// boolean established;

		try {

			SimpleServer server = new SimpleServer();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			// System.out.println("e1 Exception");
			e1.printStackTrace();
		}
	}

	public SimpleServer() throws IOException {

		srvSocket = new ServerSocket(tcpport);

		while (true) {
			established = udpConnect(); // main thread for controlling connection
			System.out.println("UDP established");

			if (established) {
				Socket clientSocket = srvSocket.accept();
				System.out.println("TCP established");
				System.out.print(clientSocket);
				in = new DataInputStream(clientSocket.getInputStream());
				out = new DataOutputStream(clientSocket.getOutputStream());
				System.out.println("sending studio list...");

				// send the list of studio and ask for user option
				try {
					out.writeInt(studiolist.size());
					System.out.println("Studio list size sent!");
					// sending names of
					for (Studio studio : studiolist) {
						System.out.println("the length of name is :"+studio.getName().length());
						out.writeInt(studio.getName().length());
						out.write(studio.getName().getBytes());
						System.out.println("The list of studio: "); 
						System.out.println(studio.getName());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Thread t = new Thread(() -> { // establish a new studio thread
					// cases
					try {
						receiveOption();
						if (option.contains("create")) {
							studioName = option.substring(7); // get the Studio title
							System.out.println("The name of the studio: " + studioName);
							Studio studio = new Studio(studioName, in, out, clientSocket); // handle clients in each
							System.out.println("New Studio created!");
							synchronized (studiolist) {
								studiolist.add(studio);
								System.out.printf("Total %d studios are created!", studiolist.size());
								for (Studio s : studiolist) {
									System.out.println("Elemnts in stuio list: ");
									System.out.println(s.getName());
								}
							}
							studio.handleClient();
						} else if (option.contains("select")) {
							studioName = option.substring(7); // get the Studio title
							System.out.println("The selected studio: " + studioName);
							for(Studio studio: studiolist) {
								if(studio.getName().equals(studioName)) {
									System.out.println("The Studio name found!");
									studio.handleClient();
								}
								
							}
							

						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						// System.out.println("e1 Exception");
						e1.printStackTrace();
					}
					synchronized (studiolist) {
						// System.out.println("Remove already!!!" + clientSocket.getPort());
						// slist.remove(clientSocket);
					}
				});
				t.start();
			}

		}
	}

	// establish a UDP connection
	// udpConnect();
	// socket.close();

	// accept connection

	public boolean udpConnect() throws IOException {
		socket = new DatagramSocket(udpport);
		DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);

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
			// tcpport = 8888;
			System.out.println(tcpport);
			msg = localAddress.getHostAddress() + ", " + tcpport; // get port
			// socket = new DatagramSocket(4321);
			DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), receivedPacket.getAddress(),
					receivedPacket.getPort());
			socket.send(packet);
			System.out.println("Sent !!!");
		}
		socket.close();
		// srvSocket.close();
		return (true);

	}

	public void receiveOption(){ // capture the option from user
		try {
			int size = 0;
			size = in.read(optionBuffer);
			option = new String(optionBuffer, 0, size);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
