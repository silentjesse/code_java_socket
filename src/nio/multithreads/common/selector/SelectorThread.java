package nio.multithreads.common.selector;

import java.io.IOException;
 
import java.nio.ByteBuffer;
 
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
 
import java.util.List;
 

import nio.multithreads.common.dto.ChannelOpsRequest;
import nio.multithreads.common.dto.Component;
import nio.multithreads.common.dto.ChannelOpsRequest.EnumRequestType;
 

public class SelectorThread implements Runnable {
	 
 
	
	// The selector we'll be monitoring
    private Selector            selector;
	
    
    // The buffer into which we'll read data when it's available
    private ByteBuffer          readBuffer     = ByteBuffer.allocate(8192);
    
    private Component component = null;
	
	public SelectorThread( Component component ) {
		this.component  = component;  
		selector = this.component .getSelector();
	}
	
	public void run() {
		 
		 SelectionKey key = null;
		 List<ChannelOpsRequest> opsRequests = this.component .getChannelOpsReqs();
        while (true) {
            try {
                // Process any pending changes 
                synchronized (opsRequests) {
                    Iterator<ChannelOpsRequest> changes = opsRequests.iterator();
                    while (changes.hasNext()) {
                        ChannelOpsRequest req = changes.next();
                        switch (req.getType()) {
                            case CHANNEL_INTERESTSET:
                            	System.out.println("**********改变通道兴趣" + req.getChannel());
                                key = req.getChannel().keyFor(this.selector);
                                if(key == null){
                                	System.out.println("通道不在注册列表中===>清除该通道信息");
                                	//this.component.closeChannel((SocketChannel) req.getChannel());
                                	req.getChannel().close();
	   	                        	 this.component.removeChannelState((SocketChannel)req.getChannel());//从状态列表中去掉。
	   	                        	 this.component.getOuts().remove((SocketChannel)req.getChannel());//将要发送出去的消息去掉
	   	                        	
                                }else{
                                	 key.interestOps(req.getInterestSet());
                                }
                                break;
                            case CHANNEL_REGISTER:
                            	System.out.println("**********注册通道" + req.getChannel());
                            	key = req.getChannel().register(this.selector, req.getInterestSet()); 
                            	break;
                            case CHANNEL_unREGISTER:
                            	System.out.println("**********开始注销通道" + req.getChannel());
                            	key = req.getChannel().keyFor(this.selector);
                            	if(key != null){key.cancel();} 
	                        	 req.getChannel().close();
	                        	 this.component.removeChannelState((SocketChannel)req.getChannel());//从状态列表中去掉。
	                        	 this.component.getOuts().remove((SocketChannel)req.getChannel());//将要发送出去的消息去掉
	                        	
                            	break;
                            default:
                            	break;
                        }
                    }
                    opsRequests.clear();
                }

                // Wait for an event one of the registered channels
                this.selector.select();
                // Iterate over the set of keys for which events are available
                Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    // Check what event is available and deal with it
                    if (key.isConnectable()) {
                    	System.out.println("--->connectable");
                        this.finishConnection(key);
                    }
                    else if (key.isAcceptable()) {
                    	System.out.println("--->acceptable");
                        this.accept(key);
                    } else if (key.isReadable()) {
                    	System.out.println("--->readable");
                        this.read(key);
                    } else if (key.isWritable()) {
                    	
                        this.write(key);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	private void accept(SelectionKey key) throws IOException {
        // For an accept to be pending the channel must be a server socket channel.
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        // Accept the connection and make it non-blocking
        SocketChannel socketChannel = serverSocketChannel.accept();
       // Socket socket = socketChannel.socket();
        socketChannel.configureBlocking(false);

        // Register the new SocketChannel with our Selector, indicating
        // we'd like to be notified when there's data waiting to be read
        this.component.requestChannelOps(socketChannel, ChannelOpsRequest.EnumRequestType.CHANNEL_REGISTER, SelectionKey.OP_READ);
          
        
        //供给超时线程检查是否超时
        this.component.addNewChannelState(socketChannel);
        
    }

	private void finishConnection(SelectionKey key) throws IOException {
	    SocketChannel socketChannel = (SocketChannel) key.channel();
	  
	    // Finish the connection. If the connection operation failed
	    // this will raise an IOException.
	    try {
	      socketChannel.finishConnect();
	    } catch (IOException e) {
	      // Cancel the channel's registration with our selector
	      //this.component.requestChannelOps(socketChannel, ChannelOpsRequest.EnumRequestType.CHANNEL_unREGISTER, 0);
	    	this.component.closeChannel(socketChannel);
	      this.component.addInput(socketChannel, null, 0);
	      return;
	    }
	    
	    //供给超时线程检查是否超时
        this.component.addNewChannelState(socketChannel);
        
        
	    // Register an interest in writing on this channel
        this.component.requestChannelOps(socketChannel, ChannelOpsRequest.EnumRequestType.CHANNEL_REGISTER, SelectionKey.OP_READ);
        
	  }

	private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        
        // Clear out our read buffer so it's ready for new data
        this.readBuffer.clear();

        // Attempt to read off the channel
        int numRead;
        try {
            numRead = socketChannel.read(this.readBuffer);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
         
        	this.component.closeChannel(socketChannel);
        	this.component.addInput(socketChannel, null, 0);
            return;
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
        	 
        	this.component.closeChannel(socketChannel);
        	this.component.addInput(socketChannel, null, 0);
            return;
        }
        
        // Hand the data off to our worker thread
        //this.worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
        this.component.addInput(socketChannel, this.readBuffer.array(), numRead);
        
        
    }

    private void write(SelectionKey key) throws IOException {
    	 
    	System.out.println("--->writable");
        SocketChannel socketChannel = (SocketChannel) key.channel();
        List<byte[]> bufferData = this.component.getOuts().get(socketChannel);
        boolean noData = true;
        synchronized (bufferData) {
            List<byte[]> queue = bufferData; 
            // Write until there's not more data ...
            while (!queue.isEmpty()) {
                ByteBuffer buf = ByteBuffer.wrap( queue.get(0));
               // System.out.println("--->writable content:1"   );
               // System.out.println(socketChannel.isConnected() + " " + socketChannel.isOpen() + " " + socketChannel.);
                socketChannel.write(buf);
                 
                //System.out.println("--->writable content :2"   );
                if (buf.remaining() > 0) {
                    // ... or the socket's buffer fills up
                    break;
                }
                queue.remove(0);
               // System.out.println("--->writable content size: " + queue.size()   );
               
            }

            if (!queue.isEmpty()) {
                // We wrote away all data, so we're no longer interested
                // in writing on this socket. Switch back to waiting for
                // data.
            	noData = false; 
            }
        }
        
        if(noData){
        	this.component.requestChannelOps(socketChannel, EnumRequestType.CHANNEL_INTERESTSET, SelectionKey.OP_READ);
        }
    }

	

}
