package http.format.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Chat {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Socket socket = new Socket("127.0.0.1", 8888);
		OutputStream os = socket.getOutputStream();	
		String format =new String("/dateformat/yyyy-MM-dd");
		byte[]bytes =format.getBytes();
		os.write(bytes);
		
	} 
}
