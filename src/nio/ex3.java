package nio;

import java.io.IOException;
 
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;

public class ex3 {

	/**  
	 *  TODO 
	 * @author JZ.Hunt 
	 * @date 2012-8-3 下午05:54:37
	 * @param args
	 * @return void
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String host = "127.0.0.1";
	    SocketChannel channel = null; 
	    int sum = 0;
	    try {
	
	      // Setup
	      InetSocketAddress socketAddress = 
	        new InetSocketAddress(host, 8888);
	      Charset charset = 
	        Charset.forName("utf-8");
	      CharsetDecoder decoder = 
	        charset.newDecoder();
	      CharsetEncoder encoder = 
	        charset.newEncoder();
	
	      // Allocate buffers
	      ByteBuffer buffer = 
	        ByteBuffer.allocateDirect(1024);
	      CharBuffer charBuffer = 
	        CharBuffer.allocate(1024);
	
	      // Connect
	      channel = SocketChannel.open();
	      channel.configureBlocking(false);
	      channel.connect(socketAddress);
	      //System.out.println(channel.isConnectionPending());
	       
	    	// System.out.println( channel.finishConnect());
	      
	     
	     //System.out.println(channel.isConnectionPending());
	      System.out.println(channel.isConnectionPending());
	      System.out.println(channel.isConnected());
	      // Open Selector
	      Selector selector = Selector.open();
	      //System.out.println(channel.isConnectionPending());
	      // Register interest in when connection
	      channel.register(selector, 
	        SelectionKey.OP_CONNECT | 
	          SelectionKey.OP_READ);
	
	      // Wait for something of interest to happen
	      
	      while ((sum = selector.select()) > 0) {
	        // Get set of ready objects
	        Set readyKeys = selector.selectedKeys();
	        Iterator readyItor = readyKeys.iterator();
	        System.out.println(readyKeys.size());
	        // Walk through set
	        while (readyItor.hasNext()) {
	        	
	          // Get key from set
	          SelectionKey key = 
	            (SelectionKey)readyItor.next();
	         
	          // Remove current entry
	          readyItor.remove();
	
	          // Get channel
	          SocketChannel keyChannel = 
	            (SocketChannel)key.channel();
	          	
	          if (key.isConnectable()) {
	            if (keyChannel.isConnectionPending()) {
	            	try{
	            		 keyChannel.finishConnect(); 
	            		 System.out.println("complete creating connection with server");
	            	}catch (Exception e) {
						System.out.println("找不到服务器主机..");
						 break;
					}
	             
	            }
	
	            // Send request
	            String request = 
	                 "GET / \r\n\r\n";
	            keyChannel.write(encoder.encode(
	              CharBuffer.wrap(request)));
	            
	          } else if (key.isReadable()) {
	        	  System.out.println("isReadable....");
	            // Read what's ready in response
	            keyChannel.read(buffer);
	            buffer.flip();
	
	            // Decode buffer
	            decoder.decode(buffer, 
	              charBuffer, false);
	
	            // Display
	            charBuffer.flip();
	            System.out.print(charBuffer);
	
	            // Clear for next pass
	            buffer.clear();
	            charBuffer.clear();
	
	          } else {
	            System.err.println("Ooops");
	          }
	        }
	      }
	    } catch (UnknownHostException e) {
	      System.err.println(e);
	    } catch (IOException e) {
	      System.err.println(e);
	    } finally {
	    	System.out.println(sum);
	      /*if (channel != null) {
	        try {
	          channel.close();
	        } catch (IOException ignored) {
	        }
	      }*/
	    }
	    System.out.println(".........");
	}

}
