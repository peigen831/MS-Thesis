package rappler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import helper.IO;
import model.SenticConcept;
import model.SenticModel;

public class EmotionAnalyzer {
	private IO io = new IO();
	
	public DataInterpreter interpreter = new DataInterpreter();
	
	private SentenceSplitter splitter = new SentenceSplitter();
	
	private SenticModel model = new SenticModel();
	
	public HashMap<String, Integer> computeEmotionFreq(String articleID){

		HashMap<String, Integer>  emoMap;
		String linedText = io.readText(IO.dirProcessed + articleID);
		interpreter.setRawText(linedText);
		String[] body = interpreter.getBodyAsArr();
		
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
			}
		}
		System.out.println("Match: "+ match + " Not match: " + notMatch);
		return emoMap;
	}
	
	
	public HashMap<String, Integer> conceptBasedApproach(String[] body){
		
		HashMap<String, Integer>  emoMap = new HashMap<String, Integer>();
		
		ArrayList<String> sentenceSplitted = new ArrayList<String>();

		SenticConcept[] senticConcept = new SenticModel().getSenticConcept();
		
		int match = 0;
		int notMatch = 0;
		for(String s1: body)
		{
			sentenceSplitted = splitter.splitSentence(s1);
			
			for(String s2: sentenceSplitted)
			{
				Lemmatizer l = Lemmatizer.getInstance();
				ArrayList<String> sTokenized = l.lemmatize(s2);
				
				for(SenticConcept concept: senticConcept){
					String[] wordFromConcept = concept.getConcept().split("\\s+");
					
					for(String word: wordFromConcept){
						
						for(String lemma: sTokenized){
							//if the current word doesnt match all the lemma, break the concept
						}
						
					}
				}
				
//				for(String lemma : sTokenized)
//				{
//					String[] senticVal = model.getSenticValue(lemma);
//					if(senticVal != null)
//					{
//						match++;
//						for(String emotion: senticVal)
//						{
//							int count = emoMap.containsKey(emotion) ? emoMap.get(emotion) : 0;
//							emoMap.put(emotion, count + 1);
//						}
//					}
//					
//					else
//						notMatch++;
//				}
			}
		}
		System.out.println("Match: "+ match + " Not match: " + notMatch);
		return emoMap;
	}
	
	public HashMap<String, Float> getMapPercentage(HashMap<String, Integer> oriMap){
		HashMap<String, Float> result = new HashMap<String, Float>();
		float total = 0;
		System.out.println("map size: " + oriMap.size() );
		for (Map.Entry<String,Integer> entry : oriMap.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			
			if(key != null)
				total += value;;
		}
		
		
		for (Map.Entry<String,Integer> entry : oriMap.entrySet()) {
			
			String key = entry.getKey();
			int value = entry.getValue();
			
			if(key != null)
				result.put(key, value/total);
		}
		return result;
	}
}
