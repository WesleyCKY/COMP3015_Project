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

public class Studio {
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
	ArrayList<Socket> clist = new ArrayList<Socket>();
	boolean established;
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	String studioName;
	public Studio(String studioName, DataInputStream in, DataOutputStream out, Socket clientSocket) throws IOException {
		this.studioName = studioName;
		this.in = in;
		this.out = out;

		srvSocket = new ServerSocket(tcpport);

		while (true) {
			// main thread for controlling connection

			if (established) {
				System.out.println("Making thread...");
				synchronized (clist) {
					clist.add(clientSocket);
					System.out.printf("Total %d clients are connected!", clist.size());
				}

				Thread t = new Thread(() -> { // establish a new thread
					System.out.println("Calling tcpConnect()...");
					// tcpConnect(clientSocket);
					synchronized (clist) {
						// System.out.println("Remove already!!!" + clientSocket.getPort());
						// list.remove(clientSocket);
					}
				});
				t.start();
			}
		}

		// establish a UDP connection
		// udpConnect();
		// socket.close();

		// accept connection

	}

	public void receiveSocket() {
	}

	public void tcpConnect(Socket clientSocket) throws IOException {
		System.out.println("In tcpConnect()...");
		// srvSocket = new ServerSocket(tcpport);

		// clientSocket = srvSocket.accept();
		System.out.println("Waiting...");
		// establish a TCP connection

		System.out.printf("TCP server is listening at port %d...", tcpport);

		System.out.println("Accepted!");

		send(clientSocket); // call send default sketch

		receive(clientSocket);
	}

	public void receive(Socket clientSocket) throws IOException { // send color pixels
		try {

			while (true) {
				boolean b = in.readBoolean();
				if (!b) {
					int len = in.readInt();
					in.read(buffer, 0, len);

					for (Socket s : clist) {
						if (s != clientSocket) { // not send differential update to clientSocket itself
							DataOutputStream sout = new DataOutputStream(s.getOutputStream());

							String str = new String(buffer, 0, len);
							sout.writeBoolean(b);
							System.out.println("The data is text" + b);
							sout.writeInt(str.length());
							System.out.println("The data length is " + str.length());
							sout.write(str.getBytes(), 0, str.length());
							System.out.println("The content of the text: " + str);
						}
					}
				} else {
					int pix = in.readInt(); // receive a pix
					System.out.println("In SimpleServer receive(), Pixel: " + pix);

					int col = in.readInt();
					System.out.println("In SimpleServer receive(), Col: " + col);

					int row = in.readInt();
					System.out.println("In SimpleServer receive(), Row: " + row);

					data[row][col] = pix;

					System.out.println("clientSocketList length: " + clist.size());

					for (Socket s : clist) {
						if (s != clientSocket) { // not send differential update to clientSocket itself

							// send differential update
							DataOutputStream sout = new DataOutputStream(s.getOutputStream());
							System.out.println(s.getPort());
							sout.writeBoolean(b);
							System.out.println("The data is pixel: " + b);
							sout.writeInt(pix); // client side need multiple thread to perform keep standby receiving
												// and
												// sending
							System.out.println("Sent pixel successfully, Pixel: " + pix);
							sout.writeInt(col);
							System.out.println("Sent col successfully, col: " + col);
							sout.writeInt(row);
							System.out.println("Sent row successfully row: " + row);

						}
					}
				}
			}
		} catch (IOException e) {
		}
		try {
			clientSocket.close();
		} catch (IOException e) {
		}

	}

	public void send(Socket clientSocket) throws IOException {
		DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

		for (int row = 0; row < 50; row++) {
			for (int col = 0; col < 50; col++) {
				if (data[row][col] != 0) {
					out.writeBoolean(true);
					out.writeInt(data[row][col]);
					out.writeInt(col);
					out.writeInt(row);
					System.out.println("Successfully sent pixel!!!");
				}

			}
		}
	}
	public String getName() {
		return studioName;
	}

}
