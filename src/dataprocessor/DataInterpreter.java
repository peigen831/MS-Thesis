package dataprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DataInterpreter {
	
	private String rawText;
	
	private static DataInterpreter instance = null;
	
	public static DataInterpreter getInstance(){
		if(instance==null)
			instance = new DataInterpreter();
		return instance;
	}

	public void setRawText(String rawText){
		this.rawText = rawText;
	}
	
	public HashMap<String, Integer> getMood(){  
		String[] arrS = rawText.split("\n");
		String mood = arrS[2];
		
		String[] tmp = mood.split("\\s+");
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		for(String s: tmp){
			String[] moodVal = s.split(",");
			result.put(moodVal[0], Integer.valueOf(moodVal[1]));
		}
		
		return result;
	}
	
	public HashMap<String, Integer> getMood(String rawText){  
		String[] arrS = rawText.split("\n");
		String mood = arrS[2];
		
		String[] tmp = mood.split("\\s+");
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		for(String s: tmp){
			String[] moodVal = s.split(",");
			result.put(moodVal[0], Integer.valueOf(moodVal[1]));
		}
		
		return result;
	}

	
	public String getBody(){
		String[] arrS = rawText.split("\n");
		String result = "";
		for(int i = 3; i < arrS.length; i++){
			if(!arrS[i].trim().equals(""))
				result += arrS[i] + "\n";
		}
		return result;
	}
	
	public String getBody(String text){
		this.rawText =  text;
		return getBody();
	}
	
	
	public String intMapAsString(HashMap<String, Integer> map){
		TreeMap<String, Integer> tMap = new TreeMap<String, Integer>(map);
		String result = "";
		
		for (Map.Entry<String,Integer> entry : tMap.entrySet()) {
		  String key = entry.getKey();
		  int value = entry.getValue();
		  System.out.println(key + ": " + value);
		  
		  result+=key + "," + value + "\n"; 
		}
		return result;
	}

	public String floatMapAsString(HashMap<String, Float> map){
		TreeMap<String, Float> tMap = new TreeMap<String, Float>(map);
		String result = "";
		
		for (Map.Entry<String,Float> entry : tMap.entrySet()) {
		  String key = entry.getKey();
		  float value = entry.getValue();
		  System.out.println(key + ": " + (value*100));
		  result+=key + "," + (value*100) + "\n";
		}
		
		return result;
	}
	
	public String[] getParagraph(){
		String[] arrS = rawText.split("\n");
		ArrayList<String> result  = new ArrayList<String>();
		for(int i = 3; i < arrS.length; i++){
			if(!arrS[i].trim().equals(""))
				result.add(arrS[i]);
		}
		return result.toArray(new String[0]);
	}
	
	public String[] getParagraph(String rawText){
		String[] arrS = rawText.split("\n");
		ArrayList<String> result  = new ArrayList<String>();
		for(int i = 3; i < arrS.length; i++){
			if(!arrS[i].trim().equals(""))
				result.add(arrS[i]);
		}
		return result.toArray(new String[0]);
	}
	
	public String getUrl(){
		String[] arrS = rawText.split("\n");
		return arrS[0];
	}
	
	public String getTitle(){
		String[] arrS = rawText.split("\n");
		return arrS[1];
	}
}
