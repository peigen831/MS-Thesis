package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupCrawler {
	
	final static String baseURL = "http://www.rappler.com/";
	final static String moodURL = "http://mm.rappler.com/moodmeter/ranking?rsort=1&content_id=";
	final static String dirStorage = "src/article/";

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
		
		try {
			fw = new FileWriter(filepath, true);
			fw.write(html);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Document readArticle(){
		return null;
	}
	
	public Document fetchHTML(int articleID){
		try {
			return Jsoup.connect(baseURL+articleID).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String fetchMoodJSON(int articleID){
		try {
			return Jsoup.connect(moodURL+articleID).ignoreContentType(true).execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException{
		JsoupCrawler crawler = new JsoupCrawler();

		int articleID = 124623;
		String targetURL;
		
//		for(int i = 0 ; i < 10; i++)
//		{
//			try{
//				Document doc = jCrawler.getHTML(baseURL + (articleID + i));
//				jCrawler.writeArticle(dirStorage + (articleID + i), doc.html());
//			}catch(Exception e){
//				System.out.println("Error: " + articleID);
//			}
//		}

		System.out.println("Getting content...");
		
		Document doc = crawler.fetchHTML(articleID);
		//TODO ignore article that has videos (BIAS)
		//TODO filter from indonesia - based on baseURL 
		
		String sMood = crawler.fetchMoodJSON(articleID);
		
		NewsInterpreter interpreter = new NewsInterpreter();
		
		
		System.out.println(doc.title());
		System.out.println(doc.baseUri());
		System.out.println(interpreter.getCategory(doc));
		System.out.println(interpreter.getContent(doc));
		System.out.println("Moods:");
		interpreter.printAllMoods(sMood);
		
		//TODO output: 1. baseURL, 2. title, 3. category 4. content, 5. moods
	}
}
