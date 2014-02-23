package protosocket.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ReadThread implements Runnable{
	Socket client = null;
	public ReadThread(Socket client){
		this.client = client;
	}
	int index = 0;
	public void run() {
		try {
			Thread.sleep(10000);
			System.out.println("==========>read thread is wakeing up");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedInputStream input = null;
		try {
			input = new BufferedInputStream(this.client.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
			synchronized (WriteThread.msgQueue) {
				  int size = 1;
					//因为不知道消息不知道有多长
					//而且在正文中也有可能出现结束的那种符号oxFFFFFFFF,所以只能一点一点的解析
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					//取得开始标识符
					byte[] bs = new byte[10]; //开始标识符 
					String html = "";
					//System.out.println("exit".indexOf("exit"));
					try {
						while((size = input.read(bs)) != -1){
							baos.write(bs, 0, size);
							html = baos.toString("utf8");
							System.out.println("============>" + html);
							//System.out.println("html.indexOf('exit')=" + html.indexOf("exit"));
							if(html.indexOf("exit") != -1){
								break;
							}
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println("out:" + html);
				
				
				
			 
				
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
	}

}
