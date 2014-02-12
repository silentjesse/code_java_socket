package nio.multithreads.common.dto;

import java.nio.channels.SocketChannel;

public class SocketChannelState {
	private SocketChannel channel;
	private long activeTime = System.currentTimeMillis();
 
	
	 
	
	public SocketChannel getChannel() {
		return channel;
	}
	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}
	public long getActiveTime() {
		return activeTime;
	}
	
	public void updateActiveTime(){
		activeTime = System.currentTimeMillis();
	}
	
 
	
	
}
