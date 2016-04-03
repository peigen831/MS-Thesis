package helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class IO {
	
	public void writeArticle(String filepath, String html){
		boolean alreadyExists = new File(filepath).exists();
		
		if(alreadyExists){
			File file = new File(filepath);
			try {
				Files.deleteIfExists(file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// TODO TO TEST - write in UTF8 format 
		Writer out;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8"));
		    out.write(html);
		    out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		
//		try {
//			fw = new FileWriter(filepath, true);
//			fw.write(html);
//			fw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public Document readDocument(String filepath) {		
		File input = new File(filepath);
		Document doc = null;
		try {
			doc = Jsoup.parse(input, "UTF-8", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return doc;
	}
	
	public String readText(String filepath){
	    StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
}
