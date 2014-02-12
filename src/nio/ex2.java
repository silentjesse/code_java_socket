package nio;

import java.nio.CharBuffer;

import sun.nio.ch.DirectBuffer;

public class ex2 {

	/**  
	 *  TODO 
	 * @author JZ.Hunt 
	 * @date 2012-8-3 下午04:13:40
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CharBuffer buff = CharBuffer.allocate(20);
		buff.put('a');
		System.out.println(buff.position());
		//buff.flip();
		buff.rewind();
		System.out.println(buff.get());
		//DirectBuffer
	}

}
