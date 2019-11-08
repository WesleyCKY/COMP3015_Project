
// Description: Broadcasts a request with username to the network using UDP
//
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class SimpleClient  extends JFrame{

	private String serverIP;
	private int serverPort;
	private Socket tcpSocket; // tcp socket used for data transmission between users and server
	private static DataOutputStream out; // OutputStream used for sending data
	private static DataInputStream in; // InputStream used for receiving the data sent by server
	private int[][] downloadedSketchData;
	private String name;
	private UI ui = UI.getInstance();	
	

	public SimpleClient() throws IOException {
		inputName(); // Input name window
	}
	
	public void udpConnection(String name) throws UnknownHostException, IOException {
		String msg = "Name: " + name;
		String content; // read server content
		InetAddress broadcastAddr = InetAddress.getByName("255.255.255.255");

		DatagramSocket receivedSocket = new DatagramSocket(4321); // socket used for receving
		DatagramSocket sentSocket = new DatagramSocket(1234);
		DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
		DatagramPacket sentPacket = new DatagramPacket(msg.getBytes(), msg.length(), broadcastAddr, 1234);

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
		download(); // download sketch data
	}

	public void establish() throws UnknownHostException, IOException {
		System.out.println("establishing...");
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
	
	public void download() throws IOException {
		int arrRow=0;
		int arrCol=0;
		int pixel;
		
		arrRow = in.readInt(); // read arr_row
		arrCol = in.readInt(); // read arr_col
		System.out.println(arrRow);
		System.out.println(arrCol);
		downloadedSketchData = new int[arrRow][arrCol]; // initialise new downloaded array with size
		
		arrRow-=1;
		arrCol-=1;
		
		while(true) {
			pixel = in.readInt(); // read pixel
//			System.out.println(pixel);
			for (int i=0; i<=arrRow; i++) {
				for (int j=0; j<=arrCol; j++) {
					downloadedSketchData[i][j] = pixel;
//					System.out.println(downloadedSketchData[i][j]+"Row: "+i+" Col: "+j);
				}
			}
			break;
		}
		
//		for (int[] i: downloadedSketchData) {
//			for (int a: i) {
//				System.out.println(a);
//			}
//		}
		
//		ui.setData(downloadedSketchData, arrRow+1);
		ui.setData(downloadedSketchData, 20);
		
	}
	
	public static void send(int pixel, int col, int row) throws IOException {
		out.writeInt(pixel); // send pixel
		out.writeInt(col); // send pixel_col
		out.writeInt(row); // send pixel_row
	}
	
	public void inputName() {
		this.setSize(new Dimension(320, 240));

		Container container = this.getContentPane(); // Create Container to store text field
		container.setLayout(new GridLayout(3, 0)); // Set layout using FlowLayout

		JLabel label = new JLabel("Please enter your name: ");
		JTextField textField = new JTextField();
		JButton submit = new JButton("Submit");

		container.add(label);
		container.add(textField);
		container.add(submit);

		// Once submit name, activate the UI window
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hi!"+textField.getText());
				try {
					udpConnection(textField.getText()); // Create new client once input the user name
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
//				ui = UI.getInstance();			// get the instance of UI
//				ui.setData(new int[50][50], 20);	// set the data array and block size. comment this statement to use the default data array and block size.
				
				ui.setVisible(true);
				setVisible(false); // If submitted, close input name window
			}	
		});
	}
}
