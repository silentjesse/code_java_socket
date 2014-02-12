package httpconnection;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
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

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
 
public class TestConn1 {

    public TestConn1() {
        super();
    }

    public static void main(String[] args) throws Exception {
       File file = new File("c:/massInfo" + System.currentTimeMillis() + ".OK");
       FileWriter writer = new FileWriter(file);
       for(int i = 0; i < 10000; i ++){
    	   writer.write(getMassInfo());
       }
     
       writer.flush();
       writer.close();
        
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
		
		
		StringBuffer csvStr = new StringBuffer();
		csvStr.append(record1);
		//加入记录间的分隔符
		csvStr.append("\r\n");
		
		//csvStr.append(record2);
		
		/*for(int i = 0; i < 100000; i ++){
			csvStr.append("\r\n");
			
			csvStr.append(record1);
			//加入记录间的分隔符
			csvStr.append("\r\n");
			
			csvStr.append(record2);
		}
		
		//base64编码
		String massInfo = Base64.encode(csvStr.toString().getBytes("utf-8"));
        */
		targetNumber1 ++;
        return csvStr.toString();
	}
}
