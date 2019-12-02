
// Description: Broadcasts a request with username to the network using UDP
//
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class SimpleClient extends JFrame {

	private String serverIP;
	private int serverPort;
	private Socket tcpSocket; // tcp socket used for data transmission between users and server
	private static DataOutputStream out; // OutputStream used for sending data
	private static DataInputStream in; // InputStream used for receiving the data sent by server
	private int[][] downloadedSketchData;
	private String name;
	private UI ui = UI.getInstance();
	private static JTextField textField = new JTextField();
	private byte[] buffer = new byte[1024];

	public SimpleClient() throws IOException {
		inputName(); // Input name window
	}

	public void udpConnection(String name) throws UnknownHostException, IOException {
		String msg = "Name: " + name;
		String content; // read server content
		InetAddress broadcastAddr = InetAddress.getByName("255.255.255.255");

		// DatagramSocket receivedSocket = new DatagramSocket(4321); // socket used for
		// receving
		// DatagramSocket sentSocket = new DatagramSocket(9876);
		DatagramSocket socket = new DatagramSocket(0);
		DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
		DatagramPacket sentPacket = new DatagramPacket(msg.getBytes(), msg.length(), broadcastAddr, 1234);

		// Send a request to server using UDP
		// sentSocket.send(sentPacket);
		socket.send(sentPacket);

//		System.out.println("Sent!!!");

//		System.out.println("Listening...");
		// receivedSocket.receive(receivedPacket); // Receive server's response
		socket.receive(receivedPacket);

//		System.out.println(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));
//		System.out.println("Received");

		// Receive back a packet from the server, which contains serverIP and serverPort
		String receivedMsg = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
		serverIP = receivedMsg.substring(0, receivedMsg.indexOf(","));
		serverPort = Integer.parseInt(receivedMsg.substring(receivedMsg.indexOf(",") + 2));

//		System.out.println("IP:" + serverIP);
//		System.out.println("Port:" + serverPort);

//		System.out.println("yeah");

		// receivedSocket.close();
		// sentSocket.close();
		socket.close();

		establishTcp(); // establish TCP connection
		// download(); // download sketch data

		Thread receiveDataThread = new Thread(() -> {
			receiveData();
		});

		receiveDataThread.start();

//		Thread receiveMsgThread = new Thread(() -> {
//			System.out.println("In receive msg thread");
//			receiveMsg();
//		});
//
////		receiveMsgThread.start();

	}

	public void establishTcp() throws UnknownHostException, IOException {
//		System.out.println("establishing tcp connection...");
		// Setup TCP Connection with server

		// System.out.println(serverPort);
		// System.out.println(serverIP);

		tcpSocket = new Socket(serverIP, serverPort);
		// System.out.println("tcp Connect!");

		// Receive the data sent by server
		in = new DataInputStream(tcpSocket.getInputStream());
		// System.out.println("Set up inputStream!");

		// Send data to server
		out = new DataOutputStream(tcpSocket.getOutputStream());


	}

	public static void send(int pixel, int col, int row) throws IOException {
		// System.out.println("In send()...");
		out.writeBoolean(true); // true if send pixel
		// System.out.println(true);
		out.writeInt(pixel); // send pixel
		// System.out.println("Sent Pixel!");
		out.writeInt(col); // send pixel_col
		// System.out.println("Sent Pixel Col!");
		out.writeInt(row); // send pixel_row
		// System.out.println("Sent Pixel Row!");
	}

	public static void send(LinkedList<Point> list, int pixel) throws IOException {
		byte buffer[] = new byte[1024];
		int i = 0;
		for (Point p : list) {
			out.writeBoolean(true);
			out.writeInt(pixel); // color
			out.writeInt((int) p.getX());
			out.writeInt((int) p.getY());
//			System.out.println("getX(): " + (int) p.getX() + ", getY(): " + (int) p.getY());
			i++;
		}
//		System.out.println("Total: " + i);
	}

	public static void sendMsg(String msg) {
		try {
			msg = textField.getText() + ": "+ msg;
//			System.out.println("Message length is " + msg.length());
			out.writeBoolean(false); // false if send msg
			out.writeInt(msg.length());
			out.write(msg.getBytes(), 0, msg.length());
//			System.out.println(false);
//			System.out.println("Sent msg already!!!!!");
		} catch (IOException e) {
			// textArea.append("Unable to send message to the server!\n");
			e.printStackTrace();
		}
	}

	public void receiveData() {
		try {
//			System.out.println("In receiveDataThread...");
			while (true) {

				int pixel;
				int col;
				int row;

				if (in.readBoolean()) { // if pixel, return true

					pixel = in.readInt();
					col = in.readInt();
					row = in.readInt();

//					System.out.println("In SimpleClient receiveData(), Pixel: " + pixel);
//					System.out.println("In SimpleClient receiveData(), Col: " + col);
//					System.out.println("In SimpleClient receiveData(), Row: " + row);

					// textArea.append(new String(buffer, 0, len) + "\n");
					ui.selectColor(pixel);
//					System.out.println("!!!!!!!Pixel: " + pixel);
					ui.paintPixel(col, row);
					
				} else { // if msg, return false
					int len = in.readInt();
					in.read(buffer, 0, len);

//					System.out.println(new String(buffer, 0, len) + "\n");

					ui.onTextInputted(new String(buffer, 0, len));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveMsg() {
		try {
			byte[] buffer = new byte[1024];

			while (true) {
				if (!in.readBoolean()) {
					int len = in.readInt();
					in.read(buffer, 0, len);

//					System.out.println(new String(buffer, 0, len) + "\n");

					ui.onTextInputted(new String(buffer, 0, len) + "\n");

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void inputName() {
		this.setSize(new Dimension(320, 240));

		Container container = this.getContentPane(); // Create Container to store text field
		container.setLayout(new GridLayout(3, 0)); // Set layout using FlowLayout

		JLabel label = new JLabel("Please enter your name: ");

		JButton submit = new JButton("Submit");

		container.add(label);
		container.add(textField);
		container.add(submit);

		// Once submit name, activate the UI window
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Hi!" + textField.getText());
				try {
					udpConnection(textField.getText()); // Create new client once input the user name

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// ui = UI.getInstance(); // get the instance of UI
				// ui.setData(new int[50][50], 20); // set the data array and block size.
				// comment this statement to use the default data array and block size.

				ui.setVisible(true);
				setVisible(false); // If submitted, close input name window
			}
		});
	}
}
