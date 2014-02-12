package nio;

import java.nio.CharBuffer;

public class ex1 {

	/**  
	 *  TODO 
	 * @author JZ.Hunt 
	 * @date 2012-8-3 下午02:26:18
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {
		String var = "12345678";
		CharBuffer buff = CharBuffer.wrap(var);
		 
		for (int i=0, n=buff.length(); i<n; i++) {
		 if(i == 5){
			 // System.out.println("----------------------------");
			  buff.flip();
			 // System.out.println("position=" + buff.position() 
				//	  + " limit=" +  buff.limit() + " capacity=" + buff.capacity() + " mark=" + buff.mark());
			  System.out.println("----------------------------");
			  
		  }
		  System.out.println("position=" + buff.position() 
				  + " limit=" +  buff.limit() + " capacity=" + buff.capacity() + " mark=" + buff.mark());
		 
		  
		  
		  System.out.println(buff.get());
		  //System.out.println("length=" + buff.length() + " i=" + i + " n=" + n);
		  
		}
		System.out.println("position=" + buff.position() 
				  + " limit=" +  buff.limit() + " capacity=" + buff.capacity() + " mark=" + buff.mark());
		
		System.out.println(buff.get());
		System.out.println("position=" + buff.position() 
				  + " limit=" +  buff.limit() + " capacity=" + buff.capacity() + " mark=" + buff.mark());
		System.out.println(buff.get());
		System.out.println("position=" + buff.position() 
				  + " limit=" +  buff.limit() + " capacity=" + buff.capacity() + " mark=" + buff.mark());
		
		
		//System.out.println(buff.get());
		//System.out.println("position=" + buff.position() 
		//		  + " limit=" +  buff.limit() + " capacity=" + buff.capacity() + " mark=" + buff.mark());
		
		//System.out.println(buff.get());
		//System.out.println("position=" + buff.position() 
		//		  + " limit=" +  buff.limit() + " capacity=" + buff.capacity() + " mark=" + buff.mark());
		
		
		
	}
	

}
