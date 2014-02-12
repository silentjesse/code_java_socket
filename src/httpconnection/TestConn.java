package httpconnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class TestConn {

    public TestConn() {
        super();
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.tdtchina.cn/mrm/servlet/getLocation";
        String str = getHttpText(url);
        System.out.println("abc:" + str);
    }

    public static String getHttpText(String str) {
        URL url = null;
        try {
            url = new URL(str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection huc = null;
        try {

            huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.setRequestProperty("Content-Type",
                    "application/vnd.syncml.dm+xml");
            // application/x-www-form-urlencoded
            // application/vnd.syncml.dm+xml
            huc.setDoOutput(true);
            huc.setRequestProperty("Cache-Control", "private");
            huc.setRequestProperty("Accept-Charset", "utf-8");
            huc.setRequestProperty("Accept", "application/vnd.syncml.dm+xml");
            huc.setRequestProperty("Content-Length", "3");
            DataOutputStream printout;
            printout = new DataOutputStream(huc.getOutputStream());
            printout.writeBytes("<?xml version=\"1.0\"?>" +
            		" <ANS VER=\"1.0\"> <RESULT>0</RESULT>	" +
            		" <LIA> <MSIDS> <MSID>460XXXXXXXXXXXX</MSID>  " +
            		"  <MSID_TYPE>2</MSID_TYPE>        " +
            		" </MSIDS> <POSINFOS> <POSINFO>" +
            		" <POSITIONRESULT>0</POSITIONRESULT>  " +
            		" <FIXMODE>1</FIXMODE>  " +
            		"<FIXTIME>20130625190109</FIXTIME>" +
            		" <LATITUDETYPE>1</LATITUDETYPE>   <LATITUDE>36.7911</LATITUDE>   " +
            		" <LONGITUDETYPE></LONGITUDETYPE>    " +
            		"<LONGITUDE>117.2897</LONGITUDE>  " +
            		" <ALTITUDE>0</ALTITUDE> <DIRECTION>170</DIRECTION>" +
            		" <VELOCITY>36.5</VELOCITY>    <PRECISION>60</PRECISION>    </POSINFO> </POSINFOS> </LIA> </ANS>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            is = huc.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
