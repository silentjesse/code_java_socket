package httpconnection.upload; 
 
import java.io.File;
import java.io.IOException;
import java.util.List;
 
/**
 * This program demonstrates a usage of the MultipartUtility class.
 * @author www.codejava.net
 *
 */
public class MultipartFileUploader {
 
    public static void main(String[] args) {
        String charset = "UTF-8";
        File uploadFile1 = new File("c:/massmt.txt");
        File uploadFile2 = new File("c:/massmt2.rar");
        String requestURL = "http://localhost:8080/sms3rd/realTimeMt.do";
 
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
             
            multipart.addFormField("description", "Cool Pictures");
            multipart.addFormField("keywords", "Java,upload,Spring");
            multipart.addFormField("content", "Java,upload,Springs");
             
            multipart.addFilePart("file", uploadFile1);
            multipart.addFilePart("fileUpload", uploadFile2);
 
            List<String> response = multipart.finish();
             
            System.out.println("SERVER REPLIED:");
             
            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
