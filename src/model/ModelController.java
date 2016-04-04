package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import crawler.JsoupCrawler;
import helper.IO;
import rappler.DataInterpreter;
import rappler.Lemmatizer;
import rappler.SentenceSplitter;

public class ModelController {
	
	private IO io = new IO();
	
	private DataInterpreter interpreter = new DataInterpreter();
	
	private SentenceSplitter splitter = new SentenceSplitter();
	
	private String[] body;
	
	private SenticModel model = new SenticModel();
	
	public HashMap<String, Integer> computeEmotionFreq(String articleID){
		HashMap<String, Integer>  emoMap = new HashMap<String, Integer>();
		
		String linedText = io.readText(JsoupCrawler.dirProcessed + articleID);
		
		interpreter.setRawText(linedText);
		
		body = interpreter.getBodyAsArr();

		ArrayList<String> arrSplitted = new ArrayList<String>();
		
		int match = 0;
		int notMatch = 0;
		
		for(String s1: body)
		{
			arrSplitted = splitter.splitSentence(s1);
			
			for(String s2: arrSplitted)
			{
//				System.out.println(s2);
				Lemmatizer l = Lemmatizer.getInstance();
				ArrayList<String> arr = l.lemmatize(s2);
				for(String lemma : arr)
				{
					//if match increment counter
					String[] senticVal = model.getSenticValue(lemma);
					if(senticVal != null){
						match++;
						for(String emotion: senticVal){
							int count = emoMap.containsKey(emotion) ? emoMap.get(emotion) : 0;
							emoMap.put(emotion, count + 1);
						}
					}
					else
						notMatch++;
				}
				//correlate with sentic concept
				System.out.println("Match: "+ match + " Not match: " + notMatch);
			}
		}
		return emoMap;
	}
	
	public void printMap(HashMap<String, Integer> map){

		for (Map.Entry<String,Integer> entry : map.entrySet()) {
		  String key = entry.getKey();
		  int value = entry.getValue();
		  System.out.println(key + ": " + value);
		  // do stuff
		}
	}

	public static void main(String[] args) {
		ModelController control = new ModelController();
		
		HashMap<String, Integer> emoMap = control.computeEmotionFreq("81000");
		
		control.printMap(emoMap);
		
		
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
