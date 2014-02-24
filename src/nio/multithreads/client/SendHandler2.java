package nio.multithreads.client;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import nio.multithreads.common.dto.Component;
import nio.multithreads.common.dto.SocketChannelData;

public class SendHandler2 implements Runnable {
	private Component component = null;
	public static  List<SocketChannelData> inputs = new LinkedList<SocketChannelData>();
	
	public SendHandler2( Component component ) {
		this.component = component ;
	}
	
	int index = 0;
	public void run() {
		  try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		 while(true) {
			 byte inBuffer[] = new byte[100];
			 SocketChannelData data = null;
	            /*synchronized(inpfuts) {
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
	            }*/
			 /*int size = 0 ;
			 try {
				 size = System.in.read(inBuffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			byte[] buffer = new byte[size];
			System.arraycopy(inBuffer, 0, buffer, 0, size);*/
			 
			 data = new SocketChannelData();
			 data.setChannel((SocketChannel) this.component.getChannel());
			 data.setBuffer(Integer.toString(index).getBytes());
				handleData(data);
			index++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        }
	}
	private void handleData(SocketChannelData data) {
		 
		component.addOutput(data.getChannel(), data.getBuffer());
		this.component.updateActiveTime(data.getChannel());
		 
	}

}
