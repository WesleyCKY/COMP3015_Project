import java.net.*;
import java.io.*;
import java.util.Scanner;

//client
public class KidPaint {

    public static void main(String[] args) throws UnknownHostException {
        try {
			SimpleClient name = new SimpleClient();
			name.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
}
