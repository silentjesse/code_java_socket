package nio.multithreads.common.dto;

import java.io.IOException;
 
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownServiceException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nio.multithreads.common.dto.ChannelOpsRequest.EnumRequestType;

public class Component {
	
	 public enum EnumChannelType{
	    	SERVER(),//服务端类型
	    	CLIENT();//客户端类型
	    	private EnumChannelType( ){
	    		 
	    	}
	    }
 
	private Selector    selector = null;
	private SelectableChannel channel = null;
	
	public Selector getSelector() {
		
		return selector;
	}
	
	public Component( ) throws IOException{
		this.selector = SelectorProvider.provider().openSelector();
	}
	
	/**  
	 *  在component被new出来后，接下来就是要调用该方法 
	 * @author JZ.Hunt 
	 * @date 2014-2-11 下午03:27:08
	 * @param channelType
	 * @param hostAddress
	 * @param port
	 * @throws IOException
	 * @return void
	 */
	public void createChannel(EnumChannelType channelType,  InetAddress hostAddress, int port) throws IOException {
		
		this.selector = SelectorProvider.provider().openSelector();
		switch(channelType){
			case CLIENT: 
		        // Create a non-blocking socket channel
		        SocketChannel socketChannel = SocketChannel.open();
		        socketChannel.configureBlocking(false);

		        // Kick off connection establishment
		        socketChannel.connect(new InetSocketAddress(hostAddress, port));
				 
				this.channel = socketChannel;
				requestChannelOps(socketChannel, EnumRequestType.CHANNEL_REGISTER, SelectionKey.OP_CONNECT);
				break;
			case SERVER:
				hostAddress = null;
				// Create a new non-blocking server socket channel
				ServerSocketChannel serverChannel = ServerSocketChannel.open();
		        serverChannel.configureBlocking(false);
				 InetSocketAddress isa = new InetSocketAddress(hostAddress,  port );
			     serverChannel.socket().bind(isa);
			     this.channel = serverChannel;
			     requestChannelOps(serverChannel, EnumRequestType.CHANNEL_REGISTER, SelectionKey.OP_ACCEPT);
				break;
			default:
				throw new UnknownServiceException("channelType is unknown");
			
		}
		this.selector.wakeup();
	}
	




	private LinkedList<ChannelOpsRequest> channelOpsReqs = new LinkedList<ChannelOpsRequest>();
	
	 /**  
	 *  通道selector状态请求
	 * @author JZ.Hunt 
	 * @date 2014-2-10 下午04:52:41
	 * @param channel
	 * @param requestType
	 * @param interestSet
	 * @return void
	 */
	public void requestChannelOps(SelectableChannel channel,
									EnumRequestType requestType, int interestSet) {
    	ChannelOpsRequest request = new ChannelOpsRequest(channel, 
    			requestType, 
    			interestSet);
	     
	     synchronized (this.channelOpsReqs) {
	     	  this.channelOpsReqs.add(request);
		 }
			
	}
	
	//数据由 selector线程提供
	//供给其他线程更新通道心跳以及超时线程检测心跳
	//1、其他线程更新心跳
	//2、超时线程通过关注该map来除去一些死连接
	private ConcurrentHashMap<Object, SocketChannelState> channelStates = new ConcurrentHashMap<Object, SocketChannelState>();
	
	/**  
	 *  新增key通道状态关系，供给超时线程检查是否超时 
	 * @author JZ.Hunt 
	 * @date 2014-2-10 下午04:52:14
	 * @param channel
	 * @return void
	 */
	public void addNewChannelState(SocketChannel channel){
		 SocketChannelState channelModel = new SocketChannelState();
	     channelModel.setChannel(channel);
	     channelModel.updateActiveTime();
	     this.channelStates.put(channel, channelModel);
	}
	
	public void removeChannelState(SocketChannel channel){
		 this.channelStates.remove(channel);
	}
	
	
	/**  
	 *  更新通道心跳 
	 * @author JZ.Hunt 
	 * @date 2014-2-11 下午05:22:34
	 * @param channel
	 * @return void
	 */
	public void updateActiveTime(SocketChannel channel){
		SocketChannelState state = this.channelStates.get(channel);
		if(state != null){
			state.updateActiveTime();
		}
		
	}
	
	
	//数据由selector线程提供
	//供给receive handlers线程  
	private   List<SocketChannelData>  inputs = new LinkedList<SocketChannelData>();//输入队列,从socket接收到的信息都保存到该列表中
	
	//数据由send handlers提供线程提供
	//供给selector线程使用
	private ConcurrentHashMap<SocketChannel, List<byte[]>> outs = new ConcurrentHashMap<SocketChannel, List<byte[]>>();//输出队列

	
	
	
	public LinkedList<ChannelOpsRequest> getChannelOpsReqs() {
		return channelOpsReqs;
	}

	 

	 

	public List<SocketChannelData> getInputs() {
		return inputs;
	}

	public Map<SocketChannel, List<byte[]>> getOuts() {
		return outs;
	}

	 
 

	/**  
	 *  若array为null,则表示该通道被关闭了 
	 * @author JZ.Hunt 
	 * @date 2014-2-11 下午06:27:25
	 * @param socketChannel
	 * @param array
	 * @param numRead
	 * @return void
	 */
	public void addInput(SocketChannel socketChannel, byte[] array, int numRead) {
		
		SocketChannelData channelModel = new SocketChannelData();
	     channelModel.setChannel(socketChannel);
	     if(array == null){//表示通道关闭掉
	    	 channelModel.setBuffer(null);
		 }else{
			 byte[] buffer =  new byte[numRead];
		     System.arraycopy(array, 0, buffer, 0, buffer.length); 
		     channelModel.setBuffer(buffer);
		 }
	     
	     synchronized (this.inputs) {
			this.inputs.add(channelModel);
			this.inputs.notifyAll();
		}
	    
	}
	
	
	public void addOutput(SocketChannel socketChannel, byte[] buffer){
		
		List<byte[]>  queue =  this.outs.get(socketChannel);
		if(queue == null){
			queue = new LinkedList<byte[]>();
		}
		synchronized (queue) {
			 queue.add(buffer);
		}
		
		this.outs.put(socketChannel, queue);
		System.out.println("===========>output queue size =" + queue.size());
		this.requestChannelOps(socketChannel, EnumRequestType.CHANNEL_INTERESTSET, SelectionKey.OP_WRITE);
		this.selector.wakeup();
		
	}

	 

	public ConcurrentHashMap<Object, SocketChannelState> getChannelStates() {
		return channelStates;
	}

	/**  
	 *  关闭通道 
	 * @author JZ.Hunt 
	 * @date 2014-2-11 下午02:00:24
	 * @param channel
	 * @return void
	 */
	public void closeChannel(SocketChannel channel) {
		this.requestChannelOps(channel, EnumRequestType.CHANNEL_unREGISTER, 0);  
		//System.out.println("====>closeing one channel :" + channel);
		this.selector.wakeup();
	}

	/** 
	 *  若component为服务端组件则返回ServerSocketChannel类型的通道<br>
	 *  若component为客户端组件则返回SocketChannel类型通道
	 * @author JZ.Hunt 
	 * @date 2014-2-11 下午03:13:50
	 * @return
	 * @return SelectableChannel
	 */
	public SelectableChannel getChannel() {
		return channel;
	}

	
	
	public static Boolean exit = false;

	 
	
	public void exitSystem(){
		System.exit(0);
	}
	
	
	
}
