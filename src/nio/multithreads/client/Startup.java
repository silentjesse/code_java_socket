package nio.multithreads.client;

import java.io.IOException;
import java.net.InetAddress;
 

 
import nio.multithreads.common.dto.Component;
import nio.multithreads.common.dto.Component.EnumChannelType;
import nio.multithreads.common.selector.SelectorThread;
 

 

public class Startup {
	public static void main(String[] args) throws IOException { 
		Component component = new Component();
		component.createChannel(EnumChannelType.CLIENT, InetAddress.getByName("localhost"), 9090);
		
		Thread receiveHanler = new Thread(new ReceiveHanler(component)) ;
		Thread sendHandler =new Thread( new SendHandler(component));
		Thread selectorThread = new Thread(new SelectorThread(component)) ;
		  
		receiveHanler.start();
		sendHandler.start();
		selectorThread.start();
		
		 
	}
	
 
}
