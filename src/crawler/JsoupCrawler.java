package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.jsoup.Jsoup;

public class JsoupCrawler {

	public static void write(String filepath, String html){
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
		
		try {
			fw = new FileWriter(filepath, true);
			fw.write(html);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException{
		String html = Jsoup.connect("http://www.rappler.com/124623").get().html();
		
		write("src/result.html", html);
		//Document doc = Jsoup.connect("http://www.rappler.com/124623").get();
	}

}
