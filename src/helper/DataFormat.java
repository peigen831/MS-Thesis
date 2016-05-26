package helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Sentic.Concept;

public class DataFormat {
	
	private static DataFormat instance = null;
	
	public static DataFormat getInstance(){
		if(instance==null)
			instance = new DataFormat();
		return instance;
	}
	
	public String generateConceptCSV(HashMap<String, ArrayList<Concept>> map){
		
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
