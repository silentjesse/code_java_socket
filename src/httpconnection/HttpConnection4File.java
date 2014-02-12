package httpconnection;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection4File {

	public static void main(String[] args) {
		  try {
		   URL url = new URL("http://localhost:8080/sms3rd/realTimeMt.do");
		   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		   conn.setDoOutput(true);
		   conn.setDoInput(true);
		   conn.setChunkedStreamingMode(1024*1024);  
		   conn.setRequestMethod("POST");
		   conn.setRequestProperty("connection", "Keep-Alive");
		   conn.setRequestProperty("Charsert", "UTF-8");
		   String fname = "c:/massmt.rar";
		   File file = new File(fname);
		   System.out.print(file.exists());
		  
		   conn.setRequestProperty("Content-Type","multipart/form-data;file="+file.getName());
		   conn.setRequestProperty("filename",file.getName());
		   OutputStream out = new DataOutputStream(conn.getOutputStream());
		   DataInputStream in = new DataInputStream(new FileInputStream(file));
		   int bytes = 0;
		   byte[] bufferOut = new byte[1024];
		   while ((bytes = in.read(bufferOut)) != -1) {
		    out.write(bufferOut, 0, bytes);
		   }
		   in.close();
		   out.flush();
		   out.close();
		   BufferedReader reader = new BufferedReader(new InputStreamReader(
		     conn.getInputStream()));
		   String line = null;
		   while ((line = reader.readLine()) != null) {
		    System.out.println(line);
		   }
		  } catch (Exception e) {
		   System.out.println("发送POST请求出现异常！" + e);
		   e.printStackTrace();
		  }
		 }
}
