package emotion_classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Sentic.Concept;
import Sentic.ConceptLoader;
import Sentic.MoodConstant;
import dataprocessor.DataInterpreter;
import dataprocessor.Lemmatizer;
import dataprocessor.SentenceSplitter;
import helper.DataFormat;
import helper.FileIO;

public class MultiWordEmotionAnalyzer {
	
	private SentenceSplitter splitter = new SentenceSplitter();
	
	private ConceptLoader loader = new ConceptLoader();
	
	Concept[] senticConcept;
	
	public static void buildOneString(String[] body){
		String wholeArticle = "";
		for(String s: body){
			ArrayList<String> lemma = Lemmatizer.getInstance().lemmatize(s);
			for(String s1: lemma)
				wholeArticle += s1 + " ";
			wholeArticle += "\n";
		}
		System.out.println(wholeArticle);
	}
	
	public HashMap<String, Integer> computeEmotionFreq(String filepath){

		HashMap<String, Integer>  emoMap;
		String linedText = FileIO.getInstance().readText(filepath);
		String[] body = DataInterpreter.getInstance().getParagraph(linedText);
		
		emoMap = MultiWordApproach(body);
		
		return emoMap;
	}
	
	public HashMap<String, Integer> MultiWordApproach(String[] body){
		HashMap<String, Integer>  emoMap = new HashMap<String, Integer>();
		
		HashSet<Concept> existConcept = new HashSet<Concept>();
		
		ArrayList<String> sentenceSplitted = new ArrayList<String>();

		for(String sBody: body)
		{
			sentenceSplitted = splitter.splitSentence(sBody);
			
			for(String sSentence: sentenceSplitted)
			{
				Lemmatizer l = Lemmatizer.getInstance();
				ArrayList<String> lemmatizeSentence = l.lemmatize(sSentence);
				
				existConcept.addAll(getExistConcept(lemmatizeSentence.toArray(new String[lemmatizeSentence.size()])));
				System.out.println("Done: " + lemmatizeSentence);
			}
		}
		
		for(Concept c: existConcept){
			String[] emotionVals = loader.getBasicSenticValue(c);
			
			for(String emotion: emotionVals){
				int count = emoMap.containsKey(emotion) ? emoMap.get(emotion) : 0;
				emoMap.put(emotion, count + 1);
			}
		}
		return emoMap;
	}
	
	public HashSet<Concept> getExistConcept(String[] sSentence){

		ConceptLoader loader = new ConceptLoader();
		loader.loadConcept();
		Concept[] arrConcept = loader.getSenticConcept();
		HashSet<Concept> result = new HashSet<Concept>();
		
		for(Concept concept: arrConcept)
		{
			if(hasConcept(concept.getConcept().toLowerCase(), sSentence));
				result.add(concept);
		}
		return result;
	}
	
	public boolean hasConcept(String concept, String[] sSentence){
		String [] splitConcept = concept.split(" ");
		for(int i = 0; i < splitConcept.length; i++)
		{	
			for(int j = 0; j < sSentence.length; j++)
			{
				if(splitConcept[i].equals(sSentence[j]))
					break;

				if(j == sSentence.length-1)
					return false;
			}
		}
		return true;
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
		MultiWordEmotionAnalyzer analyzer = new MultiWordEmotionAnalyzer();
		
		HashMap<String, HashMap<String, Integer>> allArticleActualMood = new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, HashMap<String, Float>> allArticlePredictMood = new HashMap<String, HashMap<String, Float>>();
		
		HashMap<String, Integer> actual;
		HashMap<String, Float> predict;
		int count = 1;
		
		for(int i = 81000; count > 0; i++){
			
			String filepath = FileIO.dirProcessed + i;
			
			File f = new File(filepath);
			
			if(f.exists() && !f.isDirectory()) { 

				System.out.println("Start analyze");
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
