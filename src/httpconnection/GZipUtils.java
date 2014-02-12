package httpconnection;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
 
public class GZipUtils {

    public GZipUtils() {
        super();
    }

    public static void main(String[] args) throws Exception {
      /* File file = new File("c:/massmt.txt");
       FileWriter writer = new FileWriter(file);
       for(int i = 0; i < 100000; i ++){
    	   writer.write(getMassInfo());
       }
     
       writer.flush();
       writer.close();*/
    	 System.out.println( getMassInfo()  );
    	 System.out.println("----------------");
       System.out.println(gzip(getMassInfo()) );
       System.out.println("----------------");
       System.out.println(gunzip(gzip(getMassInfo())) );
       System.out.println("=====================================>");
       StringBuffer buffer = new StringBuffer();
       for(int i = 0; i < 50000; i ++){
    	   buffer.append( (getMassInfo()));
    	   buffer.append("\r\n");
       }
       String compressed = gzip(buffer.toString());
       System.out.println(gunzip(compressed));
        
    }

    
    static long targetNumber1 = 13459000001l;

	public static String getMassInfo() throws UnsupportedEncodingException {
		//短信1
		String sequenceId3th1 = "402890a0403d6f6301403d912474001e";
		
		String sendNumber1 = "106589658";
		String content1 = "这是一条群发测试短信,短信2这是一条群发测试短信,短信2这是一条群发测试短信,短信2这是一条群发测试短信,短信2这是一条群发测试短信,短信2这是一条群发测试短信,短信2";
		
		//短信2
		String sequenceId3th2 = "402890a0403de5db01403e0937840068";
		String targetNumber2 = "13459265223";
		String sendNumber2 = "106589659";
		String content2 = "这是一条群发测试短信,短信2这是一条群发测试短信,短信2这是一条群发测试短信,短信2这是一条群发测试短信,短信2这是一条群发测试短信,短信2这是一条群发测试短信,短信2";
		
		StringBuffer record1 = new StringBuffer();
		//组装短信1
		try {
			record1.append(sequenceId3th1).append("|")
				.append(targetNumber1).append("|")
				.append(sendNumber1).append("|")
				.append(URLEncoder.encode(content1, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*StringBuffer record2 = new StringBuffer();
		
		//组装短信2
		try {
			record2.append(sequenceId3th2).append("|")
				.append(targetNumber2).append("|")
				.append(sendNumber2).append("|")
				.append(URLEncoder.encode(content2, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/*
		StringBuffer csvStr = new StringBuffer();
		csvStr.append(record1);
		//加入记录间的分隔符
		csvStr.append("\r\n");
		
		//csvStr.append(record2);
		
		 for(int i = 0; i < 100000; i ++){
			csvStr.append("\r\n");
			
			csvStr.append(record1);
			 
		}*/
		
		//base64编码
		//String massInfo = Base64.encode(csvStr.toString().getBytes("utf-8"));
        
		targetNumber1 ++;
        return record1.toString();
	}
	
	public static String gzip(String primStr) {
		if (primStr == null || primStr.length() == 0) {
		return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip=null;
		try {
		gzip = new GZIPOutputStream(out);
		gzip.write(primStr.getBytes());
		} catch (IOException e) {
		e.printStackTrace();
		}finally{
		if(gzip!=null){
		try {
		gzip.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
		}


		return new sun.misc.BASE64Encoder().encode(out.toByteArray());
		}
	/**
	* 
	* <p>Description:使用gzip进行解压缩</p>
	* @param compressedStr
	* @return 
	*/
	public static String gunzip(String compressedStr){
	if(compressedStr==null){
	return null;
	}

	ByteArrayOutputStream out= new ByteArrayOutputStream();
	ByteArrayInputStream in=null;
	GZIPInputStream ginzip=null;
	byte[] compressed=null;
	String decompressed = null;
	try {
	compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
	in=new ByteArrayInputStream(compressed);
	ginzip=new GZIPInputStream(in);

	byte[] buffer = new byte[1024];
	int offset = -1;
	while ((offset = ginzip.read(buffer)) != -1) {
	out.write(buffer, 0, offset);
	}
	decompressed=out.toString();
	} catch (IOException e) {
	e.printStackTrace();
	} finally {
	if (ginzip != null) {
	try {
	ginzip.close();
	} catch (IOException e) {
	}
	}
	if (in != null) {
	try {
	in.close();
	} catch (IOException e) {
	}
	}
	if (out != null) {
	try {
	out.close();
	} catch (IOException e) {
	}
	}
	}

	return decompressed;
	}
}
