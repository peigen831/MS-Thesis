package modeler;

import org.jsoup.nodes.Document;

import crawler.IO;
import crawler.NewsInterpreter;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "src/article/124623";
		IO io = new IO();
		
		Document doc = io.readArticle(path);
		
		NewsInterpreter interpreter = new NewsInterpreter();
		
		System.out.println(interpreter.getContent(doc));
	}

}
