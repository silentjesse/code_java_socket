package protosocket.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Startup {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket( "localhost", 9090 );
		WriteThread writeThread = new WriteThread(client.getOutputStream());
		new Thread(writeThread).start();
		
		ScannerThread scanThread = new ScannerThread(client); 
		new Thread(scanThread).start();
		
		ReadThread readThread = new ReadThread(client); 
		new Thread(readThread).start();
		
	}

}
