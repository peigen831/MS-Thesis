package helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import Sentic.Concept;

public class PrintIO {
	public static void printMap(HashMap<String, ArrayList<Concept>> emoMap){
		 ArrayList<Concept> arr;
		 
		 for(Entry<String, ArrayList<Concept>> entry: emoMap.entrySet())
		 {
			 String dimension = entry.getKey();
					 
			 arr = entry.getValue();
			 
			 System.out.println("DIMENSION: "+ dimension);
			 
			 for(Concept c: arr)
				 System.out.println(c.getConcept());
		 }
	}
}
