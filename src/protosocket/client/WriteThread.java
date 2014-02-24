package protosocket.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WriteThread  implements Runnable{
	public static boolean send = false;
	
	public static List<String> msgQueue = new ArrayList<String>();
	
	OutputStream client = null;
	
	public WriteThread(OutputStream client){
		this.client = client;
	}
	
	public void run() {
		while(true){
			synchronized (msgQueue) {
				
				if(msgQueue.size() != 0){ 
					try {
						
						
						String msg = msgQueue.remove(0); 
						if(msg.equals("655")){
							System.out.println();
						}
						client.write(msg.getBytes());
						System.out.println("xxxxxxxxxx:"+msg);
						client. flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					try {
						msgQueue.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}
	}

}
