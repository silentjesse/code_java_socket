package nio.multithreads.server;

import java.io.IOException;
 
  
 
import nio.multithreads.common.dto.Component;
import nio.multithreads.common.dto.Component.EnumChannelType;
import nio.multithreads.common.selector.SelectorThread;
import nio.multithreads.common.selector.TimeoutThread;
import nio.multithreads.server.ReceiveHanler;
import nio.multithreads.server.SendHandler;

 

public class Startup {
	public static void main(String[] args) throws IOException { 
		Component component = new Component();
		component.createChannel(EnumChannelType.SERVER, null, 9090);
		
		ReceiveHanler receiveHanler = new ReceiveHanler(component);
		SendHandler sendHandler = new SendHandler(component);
		SelectorThread selectorThread = new SelectorThread(component);
		
		new Thread(receiveHanler).start();
		new Thread(sendHandler).start();
		new Thread(selectorThread).start(); 
		new Thread(new TimeoutThread(component)).start();
	}
	
 
}
