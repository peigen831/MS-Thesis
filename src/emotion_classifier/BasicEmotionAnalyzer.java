package emotion_classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Sentic.Concept;
import Sentic.ConceptLoader;
import dataprocessor.DataInterpreter;
import dataprocessor.Lemmatizer;
import dataprocessor.SentenceSplitter;
import helper.DataFormat;
import helper.FileIO;

public class BasicEmotionAnalyzer {
	
	private SentenceSplitter splitter = new SentenceSplitter();
	
	private ConceptLoader loader = new ConceptLoader();
	
	Concept[] senticConcept;
	
	public HashMap<String, Integer> computeEmotionFreq(String filepath){

		HashMap<String, Integer>  emoMap;
		String linedText = FileIO.getInstance().readText(filepath);
		String[] body = DataInterpreter.getInstance().getParagraph(linedText);
		
		emoMap = basicApproach(body);
		
		return emoMap;
	}
	
	public HashMap<String, Integer> basicApproach(String[] body){
		HashMap<String, Integer>  emoMap = new HashMap<String, Integer>();
		
		ArrayList<String> sentenceSplitted = new ArrayList<String>();

		int match = 0;
		int notMatch = 0;
		for(String s1: body)
		{
			sentenceSplitted = splitter.splitSentence(s1);
			
			for(String s2: sentenceSplitted)
			{
				Lemmatizer l = Lemmatizer.getInstance();
				ArrayList<String> sTokenized = l.lemmatize(s2);
				for(String lemma : sTokenized)
				{
					//try to get the concept from sentic
					String[] senticVal = loader.getRecalculateSenticValue(lemma);
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
			}
		}
		System.out.println("Match: "+ match + " Not match: " + notMatch);
		return emoMap;
	}
	
	public HashMap<String, Float> getEmotionPercentage(HashMap<String, Integer> emotionMap){
		HashMap<String, Float> result = new HashMap<String, Float>();
		float total = 0;
		for (Map.Entry<String,Integer> entry : emotionMap.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			
			if(key != null)
				total += value;;
		}
		
		
		for (Map.Entry<String,Integer> entry : emotionMap.entrySet()) {
			
			String key = entry.getKey();
			int value = entry.getValue();
			
			if(key != null)
				result.put(key, value/total);
		}
		return result;
	}
	
	public HashMap<String, Integer> getActualMood(String filepath){
		
		String linedText = FileIO.getInstance().readText(filepath);
		
		return DataInterpreter.getInstance().getMood(linedText); 
		
	}
	
	public static void main(String[] args) {
		BasicEmotionAnalyzer analyzer = new BasicEmotionAnalyzer();
		
		HashMap<String, Integer> actual;
		HashMap<String, Float> predict;
		
		for(int i = 81000; i < 81002; i++){
			
			String filepath = FileIO.dirProcessed + i;
			
			File f = new File(filepath);
			
			if(f.exists() && !f.isDirectory()) { 

				HashMap<String, Integer> emoFreq = analyzer.computeEmotionFreq(filepath);
				
				predict = analyzer.getEmotionPercentage(emoFreq);
				
				actual = analyzer.getActualMood(filepath);
				
				String sWrite = DataFormat.getInstance().generateMoodComparisonString(actual, predict);
				
				FileIO.getInstance().writeFile(FileIO.dirResult + Integer.toString(i)+ ".CSV", sWrite);
				System.out.println("Done " + i);

			}
			
		}
	}
}
