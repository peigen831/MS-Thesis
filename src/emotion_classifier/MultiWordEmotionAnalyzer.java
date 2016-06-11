package emotion_classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import crawler.HTMLInterpreter;
import dataprocessor.DataInterpreter;
import dataprocessor.Lemmatizer;
import dataprocessor.SentenceSplitter;
import helper.DataFormat;
import helper.FileIO;
import sectic.Concept;
import sectic.ConceptLoader;
import sectic.MoodConstant;

public class MultiWordEmotionAnalyzer {
	
	private SentenceSplitter splitter = new SentenceSplitter();
		
	
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
	
	public HashMap<String, Integer> computeEmotionFrequency(String filepath){

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
				ArrayList<String> lemmatizeSentence =  Lemmatizer.getInstance().lemmatize(sSentence);
				
				existConcept.addAll(getExistConcept(lemmatizeSentence.toArray(new String[lemmatizeSentence.size()])));
			}
		}
		
		for(Concept c: existConcept){
			String[] emotionVals = ConceptLoader.getInstance().getBasicSenticValue(c);
			
			for(String emotion: emotionVals){
				int count = emoMap.containsKey(emotion) ? emoMap.get(emotion) : 0;
				emoMap.put(emotion, count + 1);
			}
		}
		return emoMap;
	}
	
	public HashSet<Concept> getExistConcept(String[] sSentence){

		Concept[] arrConcept = ConceptLoader.getInstance().getConcept();
		HashSet<Concept> result = new HashSet<Concept>();
		
		for(Concept concept: arrConcept)
		{
			if(hasConcept(concept.getConcept().toLowerCase(), sSentence)){
//				System.out.println(concept.getConcept() + Arrays.toString(sSentence));
				result.add(concept);
			}
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
	
	public int getTop3MatchIndex(HashMap<String, Integer> actual, HashMap<String, Float> predict){
		
		Map<String, Integer> sortedActual = DataFormat.getInstance().sortMapByInteger(actual);
		Map<String, Float> sortedPredict = DataFormat.getInstance().sortMapByFloat(predict);
		
		int count = 0;
		
		int i = 0;
		for(Entry<String, Integer> entryA: sortedActual.entrySet()){
			if(i == 3)
				break;
			if(entryA.getKey().equals(HTMLInterpreter.mood_inspired))
				continue;

			int j = 0;
			for(Entry<String, Float> entryP: sortedPredict.entrySet()){
				if(j == 3)
					break;
				
				if(entryA.getKey().equals(entryP.getKey()))
				{
					count++;
//					System.out.println(entryA.getKey() +" " +entryP.getKey());
				}
				j++;
			}
			i++;
		}
		
		return count;
	}
	
	public static void main(String[] args){
		MultiWordEmotionAnalyzer analyzer = new MultiWordEmotionAnalyzer();
		
		int count = 45;
		
		String sWrite = "";
		int[] nTopMatch = {0 ,0 , 0, 0}; 
		for(int i = 81000; count > 0; i++){
			String filepath = FileIO.dirProcessed +i;
			File f = new File(filepath);
			
			if(f.exists() && !f.isDirectory()) { 
				System.out.println("Start analyze");
				HashMap<String, Integer> emoFreq = analyzer.computeEmotionFrequency(filepath);
				
				HashMap<String, Float> predict = analyzer.getEmotionPercentage(emoFreq);

				HashMap<String, Integer> actual = analyzer.getActualMood(filepath);
				
//				DataFormat.getInstance().printFloatMap(predict);
				int index = analyzer.getTop3MatchIndex(actual, predict);
				
				nTopMatch[index]++;
				
				System.out.println(Arrays.toString(nTopMatch));
				
				sWrite += i + "\n"+ DataFormat.getInstance().generateRankBasedResult(actual, predict)+ "\n\n";
				
				System.out.println("analyzed: " + i);
				
				count--;
			}
		}
		
		String tmp1 = "";
		String tmp2 = "";
		for(int i = 3; i >=0; i--){
			tmp1 += "top " + i + " match ,";
			tmp2 += nTopMatch[i] + ", ";
		}
		System.out.println("To write :" +Arrays.toString(nTopMatch));
		
		sWrite = tmp1 + "\n" + tmp2 + "\n" + sWrite;
		
		FileIO.getInstance().writeFile(FileIO.dirResult + "top3.CSV", sWrite);
	}
	
//	public static void main(String[] args) {
//		MultiWordEmotionAnalyzer analyzer = new MultiWordEmotionAnalyzer();
//		
//		HashMap<String, HashMap<String, Integer>> articlesActualMoodMap = new HashMap<String, HashMap<String, Integer>>();
//		HashMap<String, HashMap<String, Float>> articlesPredictMoodMap = new HashMap<String, HashMap<String, Float>>();
//		
//		int count =45;
//		
//		for(int i = 81000; count > 0; i++){
//			
//			String filepath = FileIO.dirProcessed + i;
//			
//			File f = new File(filepath);
//			
//			if(f.exists() && !f.isDirectory()) { 
//
//				System.out.println("Start analyze");
//				HashMap<String, Integer> emoFreq = analyzer.computeEmotionFrequency(filepath);
//				
//				HashMap<String, Float> predict = analyzer.getEmotionPercentage(emoFreq);
//				
////				DataFormat.getInstance().printFloatMap(predict);
//					
//				HashMap<String, Integer> actual = analyzer.getActualMood(filepath);
//				
//				String foundArticle = Integer.toString(i);
//				
//				articlesActualMoodMap.put(foundArticle, actual);
//				articlesPredictMoodMap.put(foundArticle, predict);
//				
////				String sWrite = DataFormat.getInstance().generateMoodComparisonString(actual, predict);
//				
////				FileIO.getInstance().writeFile(FileIO.dirResult + Integer.toString(i)+ ".CSV", sWrite);
//				
//				System.out.println("analyzed: " + i);
//				
//				count--;
//			}
//		}
//		System.out.println("writing");
//		
//		HashMap<String, String> moodMap = DataFormat.getInstance().generateMoodBasedResult(articlesActualMoodMap, articlesPredictMoodMap);
//				
//		for(Entry<String, String> mood: moodMap.entrySet())
//		{
//			String sWrite = mood.getValue();
//			System.out.println(sWrite);
//			
//			FileIO.getInstance().writeFile(FileIO.dirResult + mood.getKey()+ ".CSV", sWrite);
//			
//			System.out.println("Done write: " + mood.getKey());
//		}
//	}
}
