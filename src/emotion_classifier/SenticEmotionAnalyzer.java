package emotion_classifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import dataprocessor.DataInterpreter;
import dataprocessor.Lemmatizer;
import dataprocessor.SentenceSplitter;
import helper.DataFormat;
import helper.FileIO;
import sectic.Concept;
import sectic.ConceptLoader;
import sectic.MoodConstant;
import semantic_parser.concept_parser;

public class SenticEmotionAnalyzer {
	
	private SentenceSplitter splitter = new SentenceSplitter();
	private concept_parser parser =new concept_parser();
	private ConceptLoader conceptLoader = ConceptLoader.getInstance();
	
	public HashMap<String, Integer> computeEmotionFrequency(String filepath){

		HashMap<String, Integer>  emoMap;
		String linedText = FileIO.getInstance().readText(filepath);
		String[] body = DataInterpreter.getInstance().getParagraph(linedText);
		
		emoMap = SenticApproach(body);
		
		return emoMap;
	}
	
	public ArrayList<String> getAllExistConcept(String[] body){
		
		ArrayList<String> existConceptString = new ArrayList<String>();
		
		ArrayList<String> sentenceSplitted = new ArrayList<String>();

		for(String sBody: body)
		{
			sentenceSplitted = splitter.splitSentence(sBody);
			
			for(String sSentence: sentenceSplitted)
			{
//				ArrayList<String> lemmatizeSentence =  Lemmatizer.getInstance().lemmatize(sSentence);
//				
//				sSentence = rebuildSentence(lemmatizeSentence);
				
				try {
					existConceptString.addAll(parser.get_concepts(sSentence));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return existConceptString;
	}
	
	public HashMap<String, Integer> SenticApproach(String[] body){
		HashMap<String, Integer>  emoMap = new HashMap<String, Integer>();
		
		ArrayList<String> existConceptString = getAllExistConcept(body);
		
		HashSet<Concept> existConcept = getConceptFromString(existConceptString);
		
		for(Concept c: existConcept){
			String[] emotionVals = conceptLoader.getBasicSenticValue(c);
			
			for(String emotion: emotionVals){
				int count = emoMap.containsKey(emotion) ? emoMap.get(emotion) : 0;
				emoMap.put(emotion, count + 1);
			}
		}
		return emoMap;
	}
	
	public HashSet<Concept> getConceptFromString(ArrayList<String> conceptString){
		HashSet<Concept> result = new HashSet<Concept>();
		for(String s: conceptString){
			for(Concept c: conceptLoader.getConcept()){
				if(c.getConcept().equals(s))
					result.add(c);
			}
		}
		return result;
	}
	
	public String rebuildSentence(ArrayList<String> lemmaSentence){
		String result = "";
		
		for(String s: lemmaSentence){
			result += s + " ";
		}
		return result;
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
	
	
	public static void main(String[] args){
		
		SenticEmotionAnalyzer analyzer = new SenticEmotionAnalyzer();
		ResultRanker ranker = new ResultRanker();
		
		int count = 10;
		
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
				int index = ranker.getTopNMatch(actual, predict,3);
				
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
		
		FileIO.getInstance().writeFile(FileIO.dirResult + "top3-ValSpecificWithInspired.CSV", sWrite);
	}
}
