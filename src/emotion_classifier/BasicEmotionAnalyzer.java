package emotion_classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dataprocessor.DataInterpreter;
import dataprocessor.Lemmatizer;
import dataprocessor.SentenceSplitter;
import helper.DataFormat;
import helper.FileIO;
import sectic.Concept;
import sectic.ConceptLoader;
import sectic.MoodConstant;

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
				ArrayList<String> wordsInSentence = l.lemmatize(s2);
				for(String word : wordsInSentence)
				{
					//try to get the concept from sentic
					String[] senticVal = loader.getBasicSenticValue(word);
					
					//if senticnet has the word
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
			
			if(!(key == null || key.equals(MoodConstant.NA)))
				total += value;;
		}
		
		
		for (Map.Entry<String,Integer> entry : emotionMap.entrySet()) {
			
			String key = entry.getKey();
			int value = entry.getValue();
			
			if(!(key == null || key.equals(MoodConstant.NA)))
				result.put(key, value/total * 100);
		}
		return result;
	}
	
	public HashMap<String, Integer> getActualMood(String filepath){
		
		String linedText = FileIO.getInstance().readText(filepath);
		
		return DataInterpreter.getInstance().getMood(linedText); 
		
	}
	
	public static void main(String[] args) {
		BasicEmotionAnalyzer analyzer = new BasicEmotionAnalyzer();
		
		HashMap<String, HashMap<String, Integer>> allArticleActualMood = new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, HashMap<String, Float>> allArticlePredictMood = new HashMap<String, HashMap<String, Float>>();
		
		HashMap<String, Integer> actual;
		HashMap<String, Float> predict;
		int count = 2;
		
		for(int i = 81000; count > 0; i++){
			
			String filepath = FileIO.dirProcessed + i;
			
			File f = new File(filepath);
			
			if(f.exists() && !f.isDirectory()) { 

				HashMap<String, Integer> emoFreq = analyzer.computeEmotionFreq(filepath);
				
				predict = analyzer.getEmotionPercentage(emoFreq);
				
				actual = analyzer.getActualMood(filepath);
				
				String foundArticle = Integer.toString(i);
				
				allArticleActualMood.put(foundArticle, actual);
				allArticlePredictMood.put(foundArticle, predict);
				
//				String sWrite = DataFormat.getInstance().generateMoodComparisonString(actual, predict);
//				
//				FileIO.getInstance().writeFile(FileIO.dirResult + Integer.toString(i)+ ".CSV", sWrite);
//				
				System.out.println("analyzed: " + i);
				
				
				count--;
			}
		}
		System.out.println("writing");
		
		HashMap<String, String> moodMap = DataFormat.getInstance().generateMoodBasedResult(allArticleActualMood, allArticlePredictMood);
				
		for(Entry<String, String> mood: moodMap.entrySet())
		{
			String sWrite = mood.getValue();
			System.out.println(sWrite);
			
			FileIO.getInstance().writeFile(FileIO.dirResult + mood.getKey()+ ".CSV", sWrite);
			
			System.out.println("Done write: " + mood.getKey());
		}
	}
}
