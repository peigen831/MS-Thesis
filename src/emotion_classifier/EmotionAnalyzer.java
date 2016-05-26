package emotion_classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Sentic.Concept;
import Sentic.ConceptLoader;
import Sentic.SenticConstant;
import dataprocessor.Lemmatizer;
import dataprocessor.SentenceSplitter;
import helper.DataFormat;

public class EmotionAnalyzer {
	
	private SentenceSplitter splitter = new SentenceSplitter();
	
	private ConceptLoader loader = new ConceptLoader();
	
	Concept[] senticConcept;
	
	
//	public HashMap<String, Integer> computeEmotionFreq(String articleID){
//
//		HashMap<String, Integer>  emoMap;
//		String linedText = io.readText(FileIO.dirProcessed + articleID);
//		interpreter.setRawText(linedText);
//		String[] body = interpreter.getBodyAsArr();
//		
////		emoMap = conceptBasedApproach(body);
//		
//		return null;
//	}
//	
//	public HashMap<String, Integer> basicApproach(String[] body){
//		HashMap<String, Integer>  emoMap = new HashMap<String, Integer>();
//		
//		ArrayList<String> sentenceSplitted = new ArrayList<String>();
//
//		int match = 0;
//		int notMatch = 0;
//		for(String s1: body)
//		{
//			sentenceSplitted = splitter.splitSentence(s1);
//			
//			for(String s2: sentenceSplitted)
//			{
//				Lemmatizer l = Lemmatizer.getInstance();
//				ArrayList<String> sTokenized = l.lemmatize(s2);
//				for(String lemma : sTokenized)
//				{
//					String[] senticVal = model.getSenticValue(lemma);
//					if(senticVal != null){
//						match++;
//						for(String emotion: senticVal){
//							int count = emoMap.containsKey(emotion) ? emoMap.get(emotion) : 0;
//							emoMap.put(emotion, count + 1);
//						}
//					}
//					else
//						notMatch++;
//				}
//			}
//		}
//		System.out.println("Match: "+ match + " Not match: " + notMatch);
//		return emoMap;
//	}
	

	//return present sentic concepts
	//string - emotional dimension
	//arrList - list of concepts with corresponding freq count
	public HashMap<String, ArrayList<Concept>> getDimensionConcept(String[] paragraph){
		
		HashMap<String, ArrayList<Concept>>  resultMap = new HashMap<String, ArrayList<Concept>>();
		
		ArrayList<Concept> arrPleasantness;
		ArrayList<Concept> arrAttention;
		ArrayList<Concept> arrSensitivity;
		ArrayList<Concept> arrAptitude;
		HashMap<String, Integer> conceptFreq = new HashMap<String, Integer>();
		
		
		ArrayList<String> sentence = new ArrayList<String>();

		senticConcept = new ConceptLoader().getSenticConcept();
		
		System.out.println("sentic size: " + senticConcept.length);
		int match = 0;
		for(String s1: paragraph)
		{
			sentence = splitter.splitSentence(s1);
			
			for(String s2: sentence)
			{
				Lemmatizer l = Lemmatizer.getInstance();
				ArrayList<String> arrTokenized = l.lemmatize(s2);
				
				//look for possible combinations from the sentence that match the concept
				for(Concept concept: senticConcept)
				{
					String[] splitConcept = concept.getConcept().split("\\s+");
					
					boolean found;
					for(int i = 0; i< splitConcept.length; i++)
					{	
						found = false;
						for(int j = 0; j < arrTokenized.size(); j++){
							if(splitConcept[i].equals(arrTokenized.get(j))){
								found = true;	
								break;
							}
						}
						
						if(!found)
							break;
						else if(i == splitConcept.length-1)
						{
							int count = conceptFreq.containsKey(concept.getConcept()) ? conceptFreq.get(concept.getConcept()) : 0;
							conceptFreq.put(concept.getConcept(), count + 1);
							match++;
						}
					}
				}
			}
		}
		arrPleasantness = generateEmotionList(conceptFreq, SenticConstant.api_pleasantness);
		arrAptitude = generateEmotionList(conceptFreq, SenticConstant.api_aptitude);
		arrAttention = generateEmotionList(conceptFreq, SenticConstant.api_attention);
		arrSensitivity = generateEmotionList(conceptFreq, SenticConstant.api_sensitivity);
		
		resultMap.put(SenticConstant.api_pleasantness, arrPleasantness);
		resultMap.put(SenticConstant.api_aptitude, arrAptitude);
		resultMap.put(SenticConstant.api_attention, arrAttention);
		resultMap.put(SenticConstant.api_sensitivity, arrSensitivity);
		
		System.out.println("Match: "+ match);
		return resultMap;
	}
	
	//get the concept list corresponds to the emotion dimension
	//filter out the dimension with 0 score
	public ArrayList<Concept> generateEmotionList(HashMap<String, Integer> conceptFreq, String conceptDimension){
		ArrayList<Concept> result = new ArrayList<Concept>();
		
		for(Map.Entry<String, Integer> set: conceptFreq.entrySet())
		{
			Concept c = getConcept(set.getKey());
			c.setfrequency(set.getValue());
			if(conceptDimension.equals(SenticConstant.api_pleasantness) && c.getPleasantness() != 0)
				result.add(c);
			else if(conceptDimension.equals(SenticConstant.api_attention) && c.getAttention() != 0)
				result.add(c);
			else if(conceptDimension.equals(SenticConstant.api_sensitivity) && c.getSensitivity() != 0)
				result.add(c);
			else if(conceptDimension.equals(SenticConstant.api_aptitude) && c.getAptitude() != 0)
				result.add(c);
		}
		return result;
	}
	
	public Concept getConcept(String concept){
		for(Concept c: senticConcept){
			if(c.getConcept().equals(concept))
				return c;
		}
		return null;
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
	
	
	public HashMap<String, ArrayList<Concept>> extractConcept(String[] paragraph){
		
		HashMap<String, ArrayList<Concept>>  resultMap = new HashMap<String, ArrayList<Concept>>();
		
		ArrayList<Concept> arrPleasantness;
		ArrayList<Concept> arrAttention;
		ArrayList<Concept> arrSensitivity;
		ArrayList<Concept> arrAptitude;
		HashMap<String, Integer> conceptFreq = new HashMap<String, Integer>();
		
		
		ArrayList<String> sentence = new ArrayList<String>();

		senticConcept = new ConceptLoader().getSenticConcept();
		
		for(String s1: paragraph)
		{
			sentence = splitter.splitSentence(s1);
			
			for(String s2: sentence)
			{
				//1. break into clauses, seprate into verb and noun chunks
				//	- if a piece of text contains a preposition or subordinating conjunctions, the words preceding these words are OBJECTS
				//2. clause normalization
//					a. Lancaster stemming algo
//					b. potential noun chunk associate with individual verb chunks (stemmed verb) to detect multi-work expressions of the form "verb + object"
//					Object -> POS based bigram algo. to checks noun phrases for stopwords and adjectives
				
			}
		}
		return resultMap;
	}
	

	
	public static void main(String args[]){
		EmotionAnalyzer e = new EmotionAnalyzer();
//		String[] sTest = {"book", "i buy a lot of book"};
		
		//go walk, go park
		String clause = "I went for a walk in the park";
		
		String sentence = "I am going to the market to buy vegetables and some fruits";
		
		String[] sTest = {"and though the book puts great emphasis on mathematics and even includes a big section on important mathematical background knowledge, it contains too many errors in the mathematical formulas, so they are of little use."};
		
		HashMap<String, ArrayList<Concept>> map = e.getDimensionConcept(sTest);

		DataFormat.getInstance().printMap(map);
		
	}
}
