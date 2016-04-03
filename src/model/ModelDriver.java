package model;

import helper.IO;
import helper.JsoupCrawler;

public class ModelDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		IO io = new IO();
		
		String linedText = io.readText(JsoupCrawler.dirProcessed+81000);
		
		DataInterpreter interpreter = new DataInterpreter(linedText);
		
		System.out.println(interpreter.getBody());
		
		
//		IO io = new IO();
		
//		Document doc = io.readArticle(path);
		
//		NewsInterpreter interpreter = new NewsInterpreter();
//		
//		ArrayList<String> arrParagraph = interpreter.getContent(doc);
//		
//		for(String s: arrParagraph)
//		{
//			System.out.println(s);
//			Lemmatizer l = Lemmatizer.getInstance();
//			ArrayList<String> arr = l.lemmatize(s);
//			System.out.println(s);
//			for(String a : arr)
//				System.out.print(a + " ");
//			System.out.println();
//			sentence split for each paragraph
//			
//		}
			
	}

}
