package helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import dataprocessor.Lemmatizer;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class FileIO {
	
	//TODO change directory
	public final static String dirRaw = "raw/";
	public final static String dirProcessed = "processed/";
	public final static String dirResult = "analyzeResult/";
	public final static String charset16 = "UTF-16";
	public final static String charset8 = "UTF-8";
	
	private static FileIO instance = null;
	
	public static FileIO getInstance(){
		if(instance == null)
			instance = new FileIO();
		return instance;
	}
	
	public void writeFile(String filepath, String text){
		//TODO fix writing format
		boolean alreadyExists = new File(filepath).exists();
		
		if(alreadyExists){
			File file = new File(filepath);
			try {
				Files.deleteIfExists(file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Writer out;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), Charset.forName(FileIO.charset8)));
		    out.write(text);
		    out.close();
		} catch (IOException e) {
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
	
	public Document readFile(String filepath) {		
		File input = new File(filepath);
		Document doc = null;
		try {
			doc = Jsoup.parse(input, FileIO.charset8, "");
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
			return null;
		}
		return sb.toString();
	}
	
	
}
