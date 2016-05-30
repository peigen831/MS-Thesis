package crawler;

public class CrawlDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int initialID = 81000;
		int range = 100;
		
		JsoupCrawler crawler = new JsoupCrawler();
		
//		crawler.printDocument(81019);
		
		crawler.setCrawlRange(initialID, range);
		
		crawler.startCrawl();
	}

}
