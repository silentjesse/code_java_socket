package test;

import java.io.UnsupportedEncodingException;
import java.security.Security;

import org.apache.commons.codec.binary.Base64;
/**
 * 
 * @author Qintianxiang
 * @date：Apr 28, 2010
 */

public class EncrypteDecrypte3DES {
	
	final static String Algorithm = "DESede/CBC/NoPadding";//加密方法／运算模式／填充模式
	private static byte[] key;
	private static byte[] iv;
	
	public static void setKey() {
		try {
			EncrypteDecrypte3DES.key = Base64.decodeBase64("MzI0OGNkemR5NzMxMjMxMzI0MjMxMzI0".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void setIv(byte[] iv) {
		EncrypteDecrypte3DES.iv = iv;
	}
	static {
		//添加jce支持(sun有其默认实现)
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	/**
	 * 3DES加密
	 * @param data
	 * @return
	 */
	public static String encrypt(String data) {
		String code = null;
		try {
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(Algorithm);
			javax.crypto.SecretKeyFactory skf = javax.crypto.SecretKeyFactory.getInstance("DESede");
			javax.crypto.SecretKey sk = skf.generateSecret(new javax.crypto.spec.DESedeKeySpec(key));
			javax.crypto.spec.IvParameterSpec ips = new javax.crypto.spec.IvParameterSpec(iv);
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, sk, ips);
			byte[] b = cipher.doFinal(data.getBytes("UTF-8"));
			code=new String(Base64.encodeBase64(b),"UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return code;
	}
	/**
	 * 3DES加密
	 * @param data
	 * @return
	 */
	public static String encrypt(byte [] data) {
		String code = null;
		try {
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(Algorithm);
			javax.crypto.SecretKeyFactory skf = javax.crypto.SecretKeyFactory.getInstance("DESede");
			javax.crypto.SecretKey sk = skf.generateSecret(new javax.crypto.spec.DESedeKeySpec(key));
			javax.crypto.spec.IvParameterSpec ips = new javax.crypto.spec.IvParameterSpec(iv);
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, sk, ips);
			byte[] b = cipher.doFinal(data);
			code=new String(Base64.encodeBase64(b),"UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return code;
	}
	/**
	 * 3DES解密
	 * @param data
	 * @return
	 */
	public static String decrypt(String data) {
		String code = null;
		try {
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(Algorithm);
			javax.crypto.SecretKeyFactory skf = javax.crypto.SecretKeyFactory.getInstance("DESede");
			javax.crypto.SecretKey sk = skf.generateSecret(new javax.crypto.spec.DESedeKeySpec(key));
			javax.crypto.spec.IvParameterSpec ips = new javax.crypto.spec.IvParameterSpec(iv);
			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, sk, ips);
			byte [] arr = cipher.doFinal(Base64.decodeBase64(data.getBytes("UTF-8")));
			int i = 0;
			for (; i < arr.length; i++) {
				if(arr[i] == new Byte("0")){
					break;
				}
			}
			byte [] result = new byte [i];
			for (int j = 0; j < i; j++) {
				result[j] = arr[j];
			}
			code = new String(result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return code;
	}
	
	
	public static void main(String[] args) throws Exception {
		setKey();
		setIv("c89dkh3eq".getBytes());
		String string = "police20120202093352policetelecomnavigator";
		String e=encrypt(FormateData(string));
		//String d=decrypt("+4a+0Zi4zks=");
		String d=decrypt(e);
        System.out.println("加密前:"+string+" 加密后:"+e+" 解密后:"+d);  
      
	}
	public static byte [] FormateData(String str) throws UnsupportedEncodingException{
		
		int yu = str.length() % 8;
		if(yu != 0){
			int size = 8 - yu;
			byte [] arr = new byte [str.length() + size];
			byte [] data = str.getBytes("UTF-8");
			int i = 0;
			for (; i < data.length; i++) {
				arr[i] = data[i];
			}
			for (int j = 0; j < size; j++,i++) {
				arr[i] = new byte [] {0}[0];
			}
			return arr;
		}
		return str.getBytes("UTF-8");
	}
}
