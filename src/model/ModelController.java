package model;

import java.util.HashMap;

import dataprocessor.EmotionAnalyzer;

public class ModelController {
	
	
	

	public static void main(String[] args) {
		EmotionAnalyzer analyzer = new EmotionAnalyzer();
		
		String articleID = "81007";
		
		HashMap<String, Integer> emoMap = analyzer.computeEmotionFreq(articleID);
		
		HashMap<String, Float> percentMap = analyzer.getMapPercentage(emoMap);
		
		String result = analyzer.interpreter.floatMapAsString(percentMap);
		
		HashMap<String, Integer> oriMap = analyzer.interpreter.getMood();
		
		String rappler = analyzer.interpreter.intMapAsString(oriMap);
		
		System.out.println(rappler);
		System.out.println(result);
		
		
		//TODO change article ID 
//		IO.getInstance().writeFile(IO.dirResult + articleID, rappler+result);
		
		
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
