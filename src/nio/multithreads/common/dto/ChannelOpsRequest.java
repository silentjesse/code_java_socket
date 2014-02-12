package nio.multithreads.common.dto;

import java.nio.channels.SelectableChannel;

 

/***********************************************************************
* 文 件 名 : ChannelOpsRequest.java 
* <br/>工程: socket  
* <br/>创 建 人： 洪建忠  
* <br/>日   期： 2014-2-9 下午09:45:10 
* <br/>描   述： 类名ChannelOpsRequest指的是通道对selector的状态及其他操作请求
* <br/>福建邮科电信业务部厦门研发中心                                                                                                                                                              
* <br/>http://www.fsti.com                                              
* <br/>CopyRright (c) 2011-2011   <br/><br/>
**********************************************************************/
public class ChannelOpsRequest {
 
    private SelectableChannel    channel;
    private EnumRequestType              type;
    private int              interestSet;

    
     
    
    public SelectableChannel getChannel() {
		return channel;
	}

	public EnumRequestType getType() {
		return type;
	}

	public int getInterestSet() {
		return interestSet;
	}

	/**
     * @param channel 通道 
     * @param type 请求类型
     * @param interestSet  通道的interestSet
     */
    public ChannelOpsRequest(SelectableChannel channel, EnumRequestType type, int interestSet){
        this.channel = channel;
        this.type = type;
        this.interestSet = interestSet;
    }
    
    public enum EnumRequestType{
    	/**
    	 *请求通道注册到selector
    	 */
    	CHANNEL_REGISTER("请求通道注册到selector"),
    	
    	/**
    	 *请求改变interestset到selector,例如写,读
    	 */
    	CHANNEL_INTERESTSET("请求改变interestset到selector,例如写,读"),
    	
    	/**
    	 * 请求通道解除注册到selector
    	 */
    	CHANNEL_unREGISTER("请求通道解除与selector的注册关系");
    	
    	private String comment;//注释
    							//增加该字段主要强制以后的开发者在新增加新成员时,
    							//添加注释
    	private EnumRequestType(String comment){
    		this.comment = comment;
    	}
		public String getComment() {
			return comment;
		}
    	
    	
    }
}
