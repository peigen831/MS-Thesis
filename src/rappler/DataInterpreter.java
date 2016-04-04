package rappler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DataInterpreter {
	
	private String rawText;
	
	public DataInterpreter(String rawText){
		this.rawText = rawText;
	}
	
	public DataInterpreter() {
		// TODO Auto-generated constructor stub
	}

	public void setRawText(String rawText){
		this.rawText = rawText;
	}
	
	public HashMap<String, Integer> getMood(){  
		String[] arrS = rawText.split("\n");
		String mood = arrS[2];
		
		String[] tmp = mood.split(" ");
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
	
	public String[] getBodyAsArr(){
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
