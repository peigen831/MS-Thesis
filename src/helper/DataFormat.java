package helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Sentic.Concept;
import Sentic.MoodConstant;
import crawler.HTMLInterpreter;

public class DataFormat {
	
	private static DataFormat instance = null;
	
	public static DataFormat getInstance(){
		if(instance==null)
			instance = new DataFormat();
		return instance;
	}
	
	public String generateConceptCSVString(HashMap<String, ArrayList<Concept>> map){
		
		String result = "";
		// word, frequency, dimension, weight
		 
		for(Entry<String, ArrayList<Concept>> entry: map.entrySet())
		{
			String dimension = entry.getKey();
					 
			ArrayList<Concept> arr = entry.getValue();
			 
			for(Concept c: arr)
				result += c.getConcept()+ ", "+ c.getfrequency() + ", " + dimension+ ", " + c.getDimensionValue(dimension) + "\n";
		 }
		
		return result;
	}
	
	public String generateMoodComparisonString(HashMap<String, Integer> actualMap, HashMap<String, Float> predictMap){
		String result = "";
		for(Entry<String, Integer> entry: actualMap.entrySet())
		 {
			 String emotion = entry.getKey();
			 
			 if(!emotion.equals(MoodConstant.NA) && !emotion.equals(HTMLInterpreter.mood_inspired))
			 {	 
				 int actualMood = entry.getValue();
				 
				 Float predictMood = predictMap.get(emotion);
				 System.out.println(emotion);
				 
				 Float difference = Math.abs(predictMood - actualMood); 
				 
				 result += emotion + ", " + actualMood + ", " + (predictMood * 100) + ", " + difference +"\n";
			 }
		 }
		return result;
	}
	
	public void printFloatMap(HashMap<String, Float> floatMap){
		for(Entry<String, Float> entry: floatMap.entrySet())
		 {
			 String emotion = entry.getKey();
					 
			 Float percentage = entry.getValue();
			 
			 System.out.println(emotion + ": " + percentage);
		 }
	}
	
	public void printMap(HashMap<String, ArrayList<Concept>> emoMap){
		 ArrayList<Concept> arr;
		 
		 for(Entry<String, ArrayList<Concept>> entry: emoMap.entrySet())
		 {
			 String dimension = entry.getKey();
					 
			 arr = entry.getValue();
			 
			 System.out.println("DIMENSION: "+ dimension);
			 
			 for(Concept c: arr)
				 System.out.println(c.getConcept() + " " + c.getfrequency());
		 }
	}
}
