package model;

import java.util.ArrayList;

import org.jsoup.nodes.Document;

import helper.IO;
import helper.NewsInterpreter;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "src/article/124623";
		
		IO io = new IO();
		
		Document doc = io.readArticle(path);
		
		NewsInterpreter interpreter = new NewsInterpreter();
		
		ArrayList<String> arrParagraph = interpreter.getContent(doc);
		
		for(String s: arrParagraph)
		{
//			System.out.println(s);
			Lemmatizer l = Lemmatizer.getInstance();
			ArrayList<String> arr = l.lemmatize(s);
			System.out.println(s);
			for(String a : arr)
				System.out.print(a + " ");
			System.out.println();
//			sentence split for each paragraph
			
		}
			
	}

}
