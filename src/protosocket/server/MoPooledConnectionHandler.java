package protosocket.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
 
import java.io.IOException;
 
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

 

/*import utils.dcase.data.DataConvert;
import utils.dcase.exception.ExceptionTool;
import utils.dcase.internet.http.HttpUtils;*/

 
 
 

public class MoPooledConnectionHandler implements Runnable {
	 
    //connection是当前正在处理的 Socket 
	protected Socket client;
	//静态 LinkedList 保存需被处理的连接,工作队列
    protected static List<Socket> pool = new LinkedList<Socket>();
    static int error = 0;
    private String ENCODING = "";
   // Logger logger = Logger.getLogger(MoPooledConnectionHandler.class);
    public MoPooledConnectionHandler(final String encoding ) {
    	ENCODING = encoding;
    }
    /**
     * 将攫取连接的流，使用它们，并在任务完成之后清除它们
     * @throws IOException 
     */
    public void  handleConnection() throws IOException {
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		//ByteArrayOutputStream baos = null;
		
		try {
			input = new BufferedInputStream(client.getInputStream());
			output = new BufferedOutputStream(client.getOutputStream());
			System.out.println("server port :" + client.getRemoteSocketAddress());
			
			parseAndResponse(input, output);
			
			
			//printAllBytes(input, output);
			
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			System.out.println();
			System.out.println("close connection");
			if(input != null){
				input.close();
			}
			
			if(output != null){
				output.close();
			}
			client.close();
		}
	}
    
  
    
  
    
    
	private void parseAndResponse(BufferedInputStream input,
			BufferedOutputStream output) throws NumberFormatException, UnsupportedEncodingException, IOException,Exception {
		
		  int size = 1;
		//因为不知道消息不知道有多长
		//而且在正文中也有可能出现结束的那种符号oxFFFFFFFF,所以只能一点一点的解析
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//取得开始标识符
		byte[] bs = new byte[10]; //开始标识符 
		String html = "";
		System.out.println("exit".indexOf("exit"));
		while((size = input.read(bs)) != -1){
			baos.write(bs, 0, size);
			html = baos.toString("utf8");
			System.out.println("============>" + html);
			//System.out.println("html.indexOf('exit')=" + html.indexOf("exit"));
			if(html.indexOf("exit") != -1){
				break;
			}
			output.write(("out:" + html).getBytes(ENCODING)); 
			output.flush();
		}
		
		System.out.println("out:" + html);
		//output.write(("out:" + html).getBytes(ENCODING)); 
		/*String body = HttpUtils.getBody(new DataInputStream(input), ENCODING, null);
		//HttpUtils.parseInputBody(client, ENCODING, null);
		System.out.println(body);
		output.write(body.getBytes(ENCODING));*/
		
		
		
	}
	 
    
	/**
	 * 将把传入请求添加到池中，并告诉其它正在等待的对象该池已经有一些内容
	 * @param requestToHandle
	 */	
	public static void processRequest(Socket requestToHandle) {
		
    	//确保没有别人能跟我们同时修改连接池
        synchronized(pool) {
        	//把传入的Socket添加到LinkedList的尾端
            pool.add(pool.size(), requestToHandle);
            //通知其它正在等待该池的Thread，池现在已经可用
            pool.notifyAll();
        }
    }
    public void run() {
        while(true) {
            synchronized(pool) {
                while(pool.isEmpty()) {
                    try {
                    	//对池上的wait()的调用释放锁，而wait()接着就在自己返回之前再次攫取该锁
                        pool.wait();//在连接池上等待，并且池中一有连接就处理它
                    }
                    catch(InterruptedException e) {
                    	System.out.println(e.getMessage());
                    }
                }
                //恰巧碰上非空池的处理程序将跳出while(pool.isEmpty())循环并攫取池中的第一个连接
                client= pool.remove(0); 
            }
            
          
            try {
				handleConnection();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
        }
    }
    
}
/**
  /// <summary>
        /// 旧定位数据
        /// </summary>
        public static byte[] OldGPSData = new byte[]{
            0xFF, 0xFF, 0xFF, 0xFF, 0x02, 0x28, 0x00, 0x0B, 0x31, 0x38, 0x39, 0x35, 0x32, 0x30,
            0x30, 0x37, 0x34, 0x38, 0x32, 0xFC, 0xD3, 0x79, 0x48, 0x67, 0x8D, 0x9A, 0x12, 0x00,
            0x00, 0x56, 0x3F, 0x09, 0x15, 0x0E, 0x1F, 0x29, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0xFF, 0xFF, 0xFF, 0xFF};
*/