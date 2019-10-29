
import java.net.*;
import java.io.*;
import java.util.Scanner;

//client
public class KidPaint {
    //	initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private DataInputStream out = null;

    //	constructor to put ip address and port number
    public KidPaint(String address, int port) {
//		establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected !");
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }
        String line = "";

        while (!line.equals("exit")) {

            Scanner in = new Scanner(System.in);
            line = in.nextLine();


            try {
                input.close();
                out.close();
                socket.close();
            } catch (IOException z) {
                System.out.println(z);
            }
        }
    }

    public static void main(String[] args) {
        KidPaint kidpaint = new KidPaint("127.0.0.1", 5000);
        InputName name = new InputName();
        name.setVisible(true);
    }
}
