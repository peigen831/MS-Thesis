package helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class IO {
	
	public void writeArticle(String filepath, String html){
		FileWriter fw;
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
//		Writer out;
//		try {
//			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8"));
//		    out.write(html);
//		    out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//		}
		
		try {
			fw = new FileWriter(filepath, true);
			fw.write(html);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Document readArticle(String filepath) {		
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
}
