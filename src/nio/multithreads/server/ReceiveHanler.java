package nio.multithreads.server;


 
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import nio.multithreads.common.dto.Component;
import nio.multithreads.common.dto.SocketChannelData;
import nio.multithreads.common.dto.SocketChannelState;
 
public class ReceiveHanler implements Runnable {
	private Component component = null;
	
	
	//数据由receive handlers线程
	//receivehandlers通过先进来的信息判断通道是归属于某个特定的用户,然后再用户的唯一标识id和channel放置于map中
	//之后如果有信息再进来通过该通道就可以直接确定该信息是属于该特定用户
	//然后发送线程那边也可以直接通过特定用户的唯一标识id来确认该将消息送给哪上目的通道 
	private static ConcurrentHashMap<SocketChannel, Object> channelIds = new ConcurrentHashMap<SocketChannel, Object>();
	private static ConcurrentHashMap<Object, SocketChannel> idChannels = new ConcurrentHashMap<Object, SocketChannel>();
	
	
	
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
		Object id = channelIds.get(data.getChannel());
		byte[] buffer = data.getBuffer();
		System.out.println("there is " + SendHandler.inputs.size() + " msg in input pool");
		if(id == null){
			System.out.println("==========================>unknown id, let's parse this msg");  
			//为socket连接配置唯一身份
			//通过解析消息，然后分配唯一身份(通过ip在数据库里面匹配，或者通过用户传入的数据匹配)
			//如果分配成功则同时也更新通道心跳,如果一直都不成功则等待下一条消息的来临。
			//如果这个解析身份的过程超过了TimeoutThread所设置的时间，则TimeoutThread会将该通道关闭。
			//在这里我们假设已经成功解析身份了
			id = new String("user_" + channelIds.size()); 
			channelIds.put(channel, id);
			idChannels.put(id, channel);
			 this.component.updateActiveTime(channel);
			 data.setBuffer(("your id is "+ id).getBytes());
			 synchronized (SendHandler.inputs) {
				 	
					SendHandler.inputs.add(data);
					SendHandler.inputs.notifyAll();
				}
			return;
		}
		
		 
		
		
		if(buffer == null){//如果为null,则表示selectorThread通道receive handler线程该通道被关闭了
			System.out.println("==========================>channel " + id + " has been closed by client....");
			return;
		}else{
			this.component.updateActiveTime(channel);
			System.out.println("==========================>receive msg from " + id + ":" + new String(buffer));
			synchronized (SendHandler.inputs) {
				SendHandler.inputs.add(data);
				SendHandler.inputs.notifyAll();
			}
		}
		
		
	}
	 
	public static ConcurrentHashMap<Object, SocketChannel> getIdChannels() {
		return idChannels;
	}
	public static ConcurrentHashMap<SocketChannel, Object> getChannelIds() {
		return channelIds;
	}
	
	
	
	
}
