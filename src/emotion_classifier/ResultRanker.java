package emotion_classifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import crawler.HTMLInterpreter;
import helper.DataFormat;

public class ResultRanker {
	public int getTopNMatch(HashMap<String, Integer> actual, HashMap<String, Float> predict, int n){
		
		Map<String, Integer> sortedActual = DataFormat.getInstance().sortMapByInteger(actual);
		Map<String, Float> sortedPredict = DataFormat.getInstance().sortMapByFloat(predict);
		
		int count = 0;
		
		int i = 0;
		for(Entry<String, Integer> entryA: sortedActual.entrySet()){
			if(i == n)
				break;

			int j = 0;
			for(Entry<String, Float> entryP: sortedPredict.entrySet()){
				if(j == n)
					break;
				
				if(entryA.getKey().equals(entryP.getKey()))
				{
					count++;
				}
				j++;
			}
			i++;
		}
		
		return count;
	}
	
	public int getTop3MatchNoInspired(HashMap<String, Integer> actual, HashMap<String, Float> predict){
		
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
}
