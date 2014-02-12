package test;


import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;



 
/**
 * 加密/解码工具类
 * 
 */
public class DesTool {
	private static Inner inner = Inner.getInstance();
	private static Logger logger=Logger.getLogger("Deshandler");
	/**
	 * 3DES解密字节数组
	 * @param b 待加密的字节数组
	 * @return 3DES加密后的节节数组
	 */
	public synchronized static byte[] decrypt(byte[] b) {
		try {
			return inner.decrypt(b);
		} catch (Exception ex) {
			logger.error("3DES解密出错,", ex);
			return null;
		}
	}

	/**
	 * 解密字符串
	 * @param strIn 需解密的字符串
	 * @return 解密后的字符串,异常返回空
	 */
	public synchronized static String decrypt(String strIn){
		try {
			return new String(decrypt(HexUtils.fromHexString(strIn)));
		} catch (Exception ex) {
			logger.error("3DES解密出错,", ex);
			return null;
		}
	}
	
	/**
	 * 加密字节数组
	 * @param arrB 需加密的字节数组
	 * @return 加密后的字节数组
	 */
	public synchronized static byte[] encrypt(byte[] arrB){
		try {
			return inner.decrypt(arrB);
		} catch (Exception ex) {
			logger.error("3DES加密出错,", ex);
			return null;
		}
	}
	/**
	 * 加密字符串
	 * @param str 待加密的字符串
	 * @return 3DES加密后的字符串
	 */
	public synchronized static String encrypt(String str) {
		try {
			return inner.encrypt(str);
		} catch (Exception ex) {
			logger.error("3DES加密出错,", ex);
			return null;
		}
	}
	
	/**
	 * 获取当前key值
	 */
	public static String getKeyStr() {
		return inner.getKeyStr();
	}
	/**
	 * 动态修改key值
	 * @param keyStr
	 */
	public synchronized static void setKeyStr(String keyStr) {
		inner.setKeyStr(keyStr);
	}
	/**
	 * 获取当前加密向量
	 * @return
	 */
	public static String getDesStr() {
		return inner.getDesStr();
	}

	/**
	 * 动态修改加密向量
	 * @param desStr
	 */
	public static void setDesStr(String desStr) {
		inner.setDesStr(desStr);
	}

	/**
	 * 获取当前加密方式
	 * @return
	 */
	public static String getAlgorithm() {
		return inner.getAlgorithm();
	}
	/**
	 * 动态修改加密方式/填充方式
	 * @param algorithm
	 */
	public synchronized static void setAlgorithm(String algorithm) {
		inner.setAlgorithm(algorithm);
	}

	/**
	 * 从指定字符串生成密钥
	 * @param arrBTmp 构成该字符串的字节数组
	 * @return 生成的密钥
	 */
	public static byte[] HexStringToByteArray(String s) {
		byte[] buf = new byte[s.length() / 2];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte) (chr2hex(s.substring(i * 2, i * 2 + 1)) * 0x10 + chr2hex(s
					.substring(i * 2 + 1, i * 2 + 2)));
		}
		return buf;
	}

	private static byte chr2hex(String chr) {
		if (chr.equals("0")) {
			return 0x00;
		} else if (chr.equals("1")) {
			return 0x01;
		} else if (chr.equals("2")) {
			return 0x02;
		} else if (chr.equals("3")) {
			return 0x03;
		} else if (chr.equals("4")) {
			return 0x04;
		} else if (chr.equals("5")) {
			return 0x05;
		} else if (chr.equals("6")) {
			return 0x06;
		} else if (chr.equals("7")) {
			return 0x07;
		} else if (chr.equals("8")) {
			return 0x08;
		} else if (chr.equals("9")) {
			return 0x09;
		} else if (chr.equals("A")) {
			return 0x0a;
		} else if (chr.equals("B")) {
			return 0x0b;
		} else if (chr.equals("C")) {
			return 0x0c;
		} else if (chr.equals("D")) {
			return 0x0d;
		} else if (chr.equals("E")) {
			return 0x0e;
		} else if (chr.equals("F")) {
			return 0x0f;
		}
		return 0x00;
	}

	public static void main(String args[]) throws Exception {
		System.out.println(encrypt("police20120202093352policetelecomnavigator"));
	}
}





/**
 * 内部类,作为工具类的单例,以便通过上层进行key,加密向量的动态修改
 * 
 * @author lydawen 2009-12-3
 * 
 */
final class Inner {
	private String keyStr = "fgdjtortiryfpdwqmhxybmwl";
	private String desStr = "12345678";
	private String algorithm = "DESede/CBC/PKCS7Padding";

	private static Inner instance;
	private Key key = getKey(keyStr);

	private byte[] desInitValue = HexUtils.strToByte(desStr);// 默认向量
	private IvParameterSpec ivspec = new IvParameterSpec(desInitValue);

	private Cipher encryptCipher = null;

	private Cipher decryptCipher = null;

	/**
	 * 屏蔽构造方法
	 */
	private Inner() {

	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	static Inner getInstance() {
		if (instance == null) {
			synchronized(Inner.class){
				if(instance == null){
						Security.addProvider(new com.sun.crypto.provider.SunJCE());
						Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());//添加PKCS7Padding支持
						instance = new Inner();
				}
		}
		}
		return instance;
	}

	/**
	 * 加密字节数组
	 * @param arrB 需加密的字节数组
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] arrB) throws Exception {
		encryptCipher = Cipher.getInstance(algorithm);
		encryptCipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
		return encryptCipher.doFinal(arrB);
	}

	/**
	 * 加密字符串
	 * @param strIn 需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public String encrypt(String strIn) throws Exception {
		encryptCipher = Cipher.getInstance(algorithm);
		encryptCipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
		return HexUtils.toHexString(encrypt(strIn.getBytes()));
	}

	/**
	 * 解密字节数组
	 * @param arrB 需解密的字节数组
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] arrB) throws Exception {
		decryptCipher = Cipher.getInstance(algorithm);
		decryptCipher.init(Cipher.DECRYPT_MODE, key, ivspec);
		return decryptCipher.doFinal(arrB);
	}

	/**
	 * 解密字符串
	 * @param strIn 需解密的字符串
	 * @return 解密后的字符串
	 */
	public String decrypt(String strIn) throws Exception {
		return HexUtils.toHexString(decrypt(HexUtils.fromHexString(strIn)));
	}

	private Key getKey(String str) {
		Key key = new SecretKeySpec(str.getBytes(), algorithm);
		return key;
	}


	private byte chr2hex(String chr) {
		if (chr.equals("0")) {
			return 0x00;
		} else if (chr.equals("1")) {
			return 0x01;
		} else if (chr.equals("2")) {
			return 0x02;
		} else if (chr.equals("3")) {
			return 0x03;
		} else if (chr.equals("4")) {
			return 0x04;
		} else if (chr.equals("5")) {
			return 0x05;
		} else if (chr.equals("6")) {
			return 0x06;
		} else if (chr.equals("7")) {
			return 0x07;
		} else if (chr.equals("8")) {
			return 0x08;
		} else if (chr.equals("9")) {
			return 0x09;
		} else if (chr.equals("A")) {
			return 0x0a;
		} else if (chr.equals("B")) {
			return 0x0b;
		} else if (chr.equals("C")) {
			return 0x0c;
		} else if (chr.equals("D")) {
			return 0x0d;
		} else if (chr.equals("E")) {
			return 0x0e;
		} else if (chr.equals("F")) {
			return 0x0f;
		}
		return 0x00;
	}

	public String getKeyStr() {
		return keyStr;
	}

	/**
	 * 动态修改key初始值
	 * @param keyStr
	 */
	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
		this.key=getKey(keyStr);
	}

	public String getDesStr() {
		return desStr;
	}

	/**
	 * 动态修改加密向量
	 * @param desStr
	 */
	public void setDesStr(String desStr) {
		this.desStr = desStr;
		this.desInitValue = HexUtils.strToByte(this.desStr);
		this.ivspec = new IvParameterSpec(this.desInitValue);
	}

	public String getAlgorithm() {
		return algorithm;
	}
	/**
	 * 动态修改加密方式/填充方式
	 * @param algorithm
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
		this.key=getKey(this.keyStr);
	}

}
