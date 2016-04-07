package crawler;

import org.jsoup.nodes.Document;

import helper.IO;

public class CrawlDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//set article id (81000, 10000) crawl range
		
		//store formated file  if( fail continue)
		//store raw format
		int initialID = 81007;
		int range = 1;
		
//		JsoupCrawler crawler = new JsoupCrawler();
//		
//		crawler.printDocument(initialID);
//		
//		crawler.setCrawlRange(initialID, range);
//		
//		crawler.startCrawl();
		Document doc = IO.getInstance().readFile(IO.dirRaw+initialID);
		
		HTMLInterpreter i = new HTMLInterpreter();
		
		System.out.println("temp");
		System.out.println(i.getBodyAsString(doc));
	}

}
