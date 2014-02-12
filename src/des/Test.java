package des;

public class Test {

	/**  
	 *  TODO 
	 * @author JZ.Hunt 
	 * @date 2012-11-23 下午5:46:24
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {
		String str = "abc";
		char[] charArray = str.toCharArray();
		for(int c: charArray){ 
			System.out.println(c);
		}
		
		System.out.println(97 == 'a');
		System.out.println((int)'a');
		System.out.println((char)97);
	 

	}

}
