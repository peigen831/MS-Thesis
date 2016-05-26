package crawler;

import org.jsoup.nodes.Document;

import helper.FileIO;

public class CrawlDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//set article id (81000, 10000) crawl range
		
		//store formated file  if( fail continue)
		//store raw format
		int initialID = 81000;
		int range = 10;
		
		JsoupCrawler crawler = new JsoupCrawler();
		
//		crawler.printDocument(initialID);
		
		crawler.setCrawlRange(initialID, range);
		
		crawler.startCrawl();
//		Document doc = FileIO.getInstance().readFile(FileIO.dirRaw+initialID);
//		
//		HTMLInterpreter i = new HTMLInterpreter();
//		
//		System.out.println("temp");
//		System.out.println(i.getBodyAsString(doc));
	}

}
