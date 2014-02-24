package nio.multithreads.common.selector;

 

 
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import nio.multithreads.common.dto.Component;
import nio.multithreads.common.dto.SocketChannelState;
 

public class TimeoutThread implements Runnable {
	private Component component = null;
	private long MAX_ACTIVETIME_DIFF = 60 * 1000;//如果1分钟通道没更新则超时
	 public TimeoutThread( Component component ) {
		this.component = component ;
	}
	 
	public void run() {
		 ConcurrentHashMap<Object,SocketChannelState> stateMaps = this.component.getChannelStates();
		while(true){
			Collection<SocketChannelState> states = stateMaps.values();
			for(SocketChannelState state: states){
				long activeTime = state.getActiveTime();
				long activeTimeDiff = System.currentTimeMillis() - activeTime;
				if(MAX_ACTIVETIME_DIFF < activeTimeDiff){//超过MAX_ACTIVETIME_DIFF时间了 
					System.out.println(">>>>>>>>>timeout thread closeing one channel");
					this.component.closeChannel(state.getChannel());
					
				}
			}
			
			try {
				Thread.sleep(2000);//2s查询一次
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		}
		
	}

}
