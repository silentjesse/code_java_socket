package nio.multithreads.common.dto;

import java.nio.channels.SocketChannel;

public class SocketChannelData {
	private SocketChannel channel;
	 
	
	private byte[] buffer;
	
	public SocketChannel getChannel() {
		return channel;
	}
	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}
	 
	
	/**  
	 *  当取得的buffer为null时表示通知receive handler该通道已被关闭 
	 * @author JZ.Hunt 
	 * @date 2014-2-10 下午05:28:29
	 * @return
	 * @return byte[]
	 */
	public byte[] getBuffer() {
		return buffer;
	}
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	
	
	
}
