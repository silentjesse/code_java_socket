package protosocket.client;

import java.io.IOException;
import java.net.Socket;

public class ScannerThread implements Runnable{
	Socket client = null;
	public ScannerThread(Socket client){
		this.client = client;
	}
	int index = 0;
	public void run() {
		while(true){
			synchronized (WriteThread.msgQueue) {
				try {
					
					WriteThread.msgQueue.add(Integer.toString(index));
					index ++;
					System.out.println("------------>" + index);
					WriteThread.msgQueue.add(Integer.toString(index));
					index ++;
					System.out.println("------------>" + index);
					
					WriteThread.msgQueue.notifyAll();
					
					//client.getInputStream().read();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		if(index > 1000){
			break;
		}
			
		}
	}

}
