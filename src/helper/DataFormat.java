package helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import crawler.HTMLInterpreter;
import sectic.Concept;

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
			 
			 int actualMood = entry.getValue();
			 
			 if(emotion.equals(HTMLInterpreter.mood_inspired))
				 result += emotion + ", " + actualMood + "\n";
				 
			 else{
				 Float predictMood = predictMap.get(emotion);
				 
				 if(predictMood==null)
					 predictMood = (float)0;
				 
				 predictMood *= 100;
				 
				 Float difference = Math.abs(predictMood - actualMood); 
				 
				 result += emotion + ", " + actualMood + ", " + predictMood + ", " + difference + "\n";
			 }
		 }
		return result;
	}
	
	public HashMap<String, String> generateMoodBasedResult(HashMap<String, HashMap<String, Integer>> actual, HashMap<String, HashMap<String, Float>> predict){
		//mood class, actual + predict + difference
		HashMap<String, String> result = new HashMap<String, String>();
		
		//for all articles
		for(Entry<String, HashMap<String, Integer>> articleEntry: actual.entrySet()){
			String articleID = articleEntry.getKey();
			
			HashMap<String, Integer> actualMood = articleEntry.getValue();
			
			HashMap<String, Float> predictMood = predict.get(articleID);
			
			//for all mood class
			for(Entry<String, Integer> moodEntry: actualMood.entrySet()){
				
				String mood = moodEntry.getKey();
				
				int actualVal = moodEntry.getValue();
				Float predictVal = predictMood.get(mood);
				if(predictVal == null)
					predictVal = (float)0;
				Float dif = Math.abs(predictVal-actualVal);
				
				String tmp = articleID + ", " + actualVal + ", " + predictVal + "," + dif + "\n";
				System.out.println(tmp);
				String s ="";
				switch(mood){
					case HTMLInterpreter.mood_inspired:  
						s = result.get(HTMLInterpreter.mood_afraid);
						s+=tmp;
						result.put(HTMLInterpreter.mood_afraid, s);
					break;
					
					case HTMLInterpreter.mood_amused:  
						s = result.get(HTMLInterpreter.mood_amused);
						s+=tmp;
						result.put(HTMLInterpreter.mood_amused, s);
					break;
					
					case HTMLInterpreter.mood_angry:  
						s = result.get(HTMLInterpreter.mood_angry);
						s+=tmp;
						result.put(HTMLInterpreter.mood_angry, s);
					break;
					
					case HTMLInterpreter.mood_annoyed:  
						s = result.get(HTMLInterpreter.mood_annoyed);
						s+=tmp;
						result.put(HTMLInterpreter.mood_annoyed, s);
					break;
					
					case HTMLInterpreter.mood_dontcare:  
						s = result.get(HTMLInterpreter.mood_dontcare);
						s+=tmp;
						result.put(HTMLInterpreter.mood_dontcare, s);
					break;
					
					case HTMLInterpreter.mood_happy:  
						s = result.get(HTMLInterpreter.mood_happy);
						s+=tmp;
						result.put(HTMLInterpreter.mood_happy, s);
					break;
					
					case HTMLInterpreter.mood_sad:  
						s = result.get(HTMLInterpreter.mood_sad);
						s+=tmp;
						result.put(HTMLInterpreter.mood_sad, s);
					break;
				}
			}
		}
		return result;
	}
	
	public String generateRankBasedResult(HashMap<String, Integer> actual, HashMap<String, Float> predict){
		
		Map<String, Integer> sortedActual = sortMapByInteger(actual);
		Map<String, Float> sortedPredict = sortMapByFloat(predict);
		
		String sActualKey = "";
		String sActualValue = "";
		for(Entry<String, Integer> entry: sortedActual.entrySet()){
			sActualKey += entry.getKey() + ",";
			sActualValue += entry.getValue() + ",";
		}
		
		String sPredictKey = "";
		String sPredictValue = "";
		for(Entry<String, Float> entry: sortedPredict.entrySet()){
			sPredictKey += entry.getKey() + ",";
			sPredictValue += entry.getValue() + ",";
		}
		
		String result = sActualKey + "\n"
				+ sActualValue + "\n"
				+ sPredictKey + "\n"
				+ sPredictValue;
		
		
		return result;
	}
	
	public Map<String, Integer> sortMapByInteger(HashMap<String, Integer> emoMap){
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(emoMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
                    return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
	}
	
	public Map<String, Float> sortMapByFloat(HashMap<String, Float> emoMap){
		List<Entry<String, Float>> list = new LinkedList<Entry<String, Float>>(emoMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Float>>()
        {
            public int compare(Entry<String, Float> o1,
                    Entry<String, Float> o2)
            {
                    return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Float> sortedMap = new LinkedHashMap<String, Float>();
        for (Entry<String, Float> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
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
