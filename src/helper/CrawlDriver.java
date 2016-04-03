package helper;

public class CrawlDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//set article id (81000, 10000) crawl range
		
		//store formated file  if( fail continue)
		//store raw format
		int initialID = 81000;
		int range = 5;
		
		JsoupCrawler crawler = new JsoupCrawler();
		
		crawler.setCrawlRange(initialID, range);
		
		crawler.startCrawl();
	}

}
