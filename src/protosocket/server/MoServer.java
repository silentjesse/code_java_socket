package protosocket.server;

import java.io.*;


import java.net.*;

//import org.apache.log4j.Logger;

 
public class MoServer {
	//protected transient Logger logger = Logger.getLogger(MoServer.class);
   //服务器能同时处理的活动客户机连接的最大数目
    protected int maxConnections;
   //进入的连接的侦听端口
    protected int listenPort;
    //将接受客户机连接请求的 ServerSocket 
    protected ServerSocket serverSocket; 
    public MoServer(int aListenPort, int maxConnections) { 
    	System.out.println("MT][start listening port:" + aListenPort);
        listenPort= aListenPort;
        this.maxConnections = maxConnections;
       
    }
    public void acceptConnections() {
        try {
            ServerSocket server = new ServerSocket(listenPort);
            Socket client = null;
            while(true) {
            	client = server.accept();
            	System.out.println("-------------------");
            	System.out.println("get a connection");
            	client.setSoTimeout(30*1000); 
                handleConnection(client);
            }
        }catch(BindException e) {
            System.out.println("");
        }
        catch(IOException e) {
            System.out.println(""+listenPort);
        }
    }
    
    /**
     * 委派PooledConnectionHandler处理连接
     */
    protected void handleConnection(Socket connectionToHandle) {
        MoPooledConnectionHandler.processRequest(connectionToHandle);
    }
    
    /**
     * 创建maxConnections个PooledConnectionHandler并在新Thread中激活它们。
     * PooledConnectionHandler将等着处理进入的连接，每个都在它自己的Thread中进行。
     * @param trackService 
     */
    public void setUpHandlers(String encoding ) {
        for(int i=0; i<maxConnections; i++) {
            MoPooledConnectionHandler currentHandler = new MoPooledConnectionHandler(encoding);
            new Thread(currentHandler, "Handler " + i).start();
        }
    }
   
  
}