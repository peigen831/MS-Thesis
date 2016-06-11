package sectic;

import java.util.ArrayList;
import java.util.HashMap;

import dataprocessor.DataInterpreter;
import emotion_classifier.EmotionAnalyzer;
import helper.DataFormat;
import helper.FileIO;

public class SenticController {
	
	public static void main(String[] args) {
		EmotionAnalyzer analyzer = new EmotionAnalyzer();
		
//		String articleID = "81007";
		
		for(int i = 81000; i < 81050; i++){

			String linedText = FileIO.getInstance().readText(FileIO.dirProcessed + i);
			
			String[] paragraph = DataInterpreter.getInstance().getParagraph(linedText);
			
			HashMap<String, ArrayList<Concept>> map = analyzer.getDimensionConcept(paragraph);
			
			String sWrite = DataFormat.getInstance().generateConceptCSVString(map);
			
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
	}

}
