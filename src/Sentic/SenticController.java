package Sentic;

import java.util.ArrayList;
import java.util.HashMap;

import EmotionClassifier.EmotionAnalyzer;
import dataprocessor.DataInterpreter;
import helper.DataFormat;
import helper.FileIO;

public class SenticController {
	
	public static void main(String[] args) {
		EmotionAnalyzer analyzer = new EmotionAnalyzer();
		
//		String articleID = "81007";
		
		for(int i = 81000; i < 81050; i++){

			String linedText = FileIO.getInstance().readText(FileIO.dirProcessed + i);
			DataInterpreter.getInstance().setRawText(linedText);
			String[] paragraph = DataInterpreter.getInstance().getParagraph();
			
			HashMap<String, ArrayList<Concept>> map = analyzer.getDimensionConcept(paragraph);
			
			String sWrite = DataFormat.getInstance().generateConceptCSV(map);
			
			FileIO.getInstance().writeFile(FileIO.dirResult + Integer.toString(i)+ ".CSV", sWrite);
			System.out.println("Done " + i);
		}
		System.out.println(FileIO.getInstance().readText(FileIO.dirProcessed + 1000));
		

		
//		HashMap<String, Integer> emoMap = analyzer.computeEmotionsFreq(articleID);
		
//		FileIO.getInstance().writeFile(FileIO.dirResult , text);
		
//		HashMap<String, Float> percentMap = analyzer.getMapPercentage(emoMap);
//		
//		String result = analyzer.interpreter.floatMapAsString(percentMap);
//		
//		HashMap<String, Integer> oriMap = analyzer.interpreter.getMood();
//		
//		String rappler = analyzer.interpreter.intMapAsString(oriMap);
//		
//		System.out.println(rappler);
//		System.out.println(result);
		
		
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
