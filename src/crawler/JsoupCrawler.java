package crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import helper.FileIO;

public class JsoupCrawler {
	
	public final static String baseURL = "http://www.rappler.com/";
	public final static String moodURL = "http://mm.rappler.com/moodmeter/ranking?rsort=1&content_id=";

	int initialID;
	int nArticle;

	FileIO io = new FileIO();
	HTMLInterpreter interpreter = new HTMLInterpreter();
	
	//TODO get 10000 article
	//TODO separate files into different categories
	
	public Document fetchHTML(int articleID){
		try {
			return Jsoup.connect(baseURL+articleID).get();
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	public String fetchMoodJSON(int articleID){
		try {
			return Jsoup.connect(moodURL+articleID).ignoreContentType(true).execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setCrawlRange(int initialID, int range){
		this.initialID = initialID;
		this.nArticle = range;
	}
	
	public void startCrawl(){
		System.out.println("Getting content...");
		
		for(int i = 0; i < nArticle; i++)
		{
			startCrawl(initialID+i);
		}			
	}
	
	public void printDocument(int articleID){
		Document doc = this.fetchHTML(articleID);
		System.out.println(interpreter.getBodyAsString(doc));
	}
	
	public void startCrawl(int articleID){
		Document docContent = this.fetchHTML(articleID);
		//TODO filter if URL contains indonesia or no articleID or video
		//TODO filter if file contains video
		//TODO filter no emotion article
		
		if(docContent != null && !docContent.baseUri().contains("/indonesia/"))
		{
			String sAllmood = this.fetchMoodJSON(articleID);
			sAllmood = interpreter.formatAllMood(sAllmood);
			String sFormat = "";
			sFormat += docContent.baseUri() + "\n";
			sFormat += docContent.title() + "\n";
//			sFormat += interpreter.getCategory(docContent); //where to save
			sFormat += sAllmood + "\n";
			sFormat += interpreter.getBodyAsString(docContent);
			
			//write formated article content
			io.writeFile(FileIO.dirProcessed + articleID, sFormat);
			
			//write raw article conten
			io.writeFile(FileIO.dirRaw + articleID, docContent.html());
			System.out.println("Done write: " + articleID);
		}
	}
	

	
	public static void main(String[] args) throws IOException{

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


		
//		NewsInterpreter interpreter = new NewsInterpreter();
//		
//		
//		System.out.println(doc.title());
//		System.out.println(doc.baseUri());
//		System.out.println(interpreter.getCategory(doc));
//		System.out.println(interpreter.getContent(doc));
//		System.out.println("Moods:");
//		interpreter.printAllMoods(sMood);
		
		//TODO output: 1. baseURL, 2. title, 3. category 4. content, 5. moods
	}
}
