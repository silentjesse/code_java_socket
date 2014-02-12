package nio.multithreads.client;


 
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import nio.multithreads.common.dto.Component;
import nio.multithreads.common.dto.SocketChannelData;
 
 
public class ReceiveHanler implements Runnable {
	private Component component = null;
	
	
	 
	
	
	
	public ReceiveHanler( Component component ) {
		this.component = component ;
	}
	public void run() {
		
		List<SocketChannelData>  inputs = this.component.getInputs();
		 while(true) {
			 
			 SocketChannelData data = null;
	            synchronized(inputs) {
	                while(inputs.isEmpty()) {
	                	
	                    try {
	                    	//对池上的wait()的调用释放锁，而wait()接着就在自己返回之前再次攫取该锁
	                    	inputs.wait();//在连接池上等待，并且池中一有连接就处理它
	                    }
	                    catch(InterruptedException e) {
	                    	System.out.println(e.getMessage());
	                    }
	                }
	                //恰巧碰上非空池的处理程序将跳出while(pool.isEmpty())循环并攫取池中的第一个连接
	                data = inputs.remove(0); 
	            }
	            
	             	
					handleData(data);
				 
	        }
	}
	
	
	private void handleData(SocketChannelData data) {
		
		SocketChannel channel = data.getChannel();
		 
		byte[] buffer = data.getBuffer();
		 
		
		if(buffer == null){//如果为null,则表示selectorThread通道receive handler线程该通道被关闭了
			System.out.println("==========================>channel has been closed by server....");
			this.component.exitSystem();
			return;
		}else{
			this.component.updateActiveTime(channel);
			System.out.println("==========================>receive rsp :" + new String(buffer)); 
		}
		
		
	}
	  
	
}
