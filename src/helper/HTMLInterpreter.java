package helper;

import java.util.ArrayList;

import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLInterpreter {
	final static String moods = "moods";
	final static String mood_happy = "happy";
	final static String mood_sad = "sad";
	final static String mood_inspired = "inspired";
	final static String mood_dontcare = "dont_care";
	final static String mood_angry = "angry";
	final static String mood_amused = "amused";
	final static String mood_afraid = "afraid";
	final static String mood_annoyed = "annoyed";
	
	public ArrayList<String> getBody(Document doc){
		ArrayList<String> arrParagraph = new ArrayList<String>();
		
		Element element = doc.select("div.storypage-divider").first();
		
		Elements eParagraph = element.select("p:not([^])");
		
		for(Element e: eParagraph){
	        arrParagraph.add(e.text());
		}
		
		return arrParagraph;
	}
	
	public String getBodyAsString(Document doc){
		
		ArrayList<String> arrContent = this.getBody(doc);
		
		String result = "";
		
		for(String s: arrContent)
			result += (s + "\n");
		
		return result;
	}
	
	public String getCategory(Document doc){
		return doc.baseUri().split("/")[3];
	}
	
	public int getMoodValue(String json, String mood){
		JSONObject obj = new JSONObject(json);
		int mVal = obj.getJSONObject(moods).getInt(mood);
		return mVal;
	}
	
	public String formatAllMood(String allMood){
		String s = "";
		
		s += (mood_happy + "," + getMoodValue(allMood,mood_happy)) + " ";
		s += (mood_sad + "," + getMoodValue(allMood,mood_sad)) + " ";
		s += (mood_inspired + "," + getMoodValue(allMood,mood_inspired)) + " ";
		s += (mood_dontcare + "," + getMoodValue(allMood,mood_dontcare)) + " ";
		s += (mood_angry + "," + getMoodValue(allMood,mood_angry)) + " ";
		s += (mood_afraid + "," + getMoodValue(allMood,mood_afraid)) + " ";
		s += (mood_amused + "," + getMoodValue(allMood,mood_amused)) + " ";
		s += (mood_annoyed + "," + getMoodValue(allMood,mood_annoyed));

		return s;
	}
	
	public void printAllMoods(String Mood){
		int nHappy = getMoodValue(Mood, mood_happy);
		int nSad = getMoodValue(Mood, mood_sad);
		int nInspired = getMoodValue(Mood, mood_inspired);
		int nDontcare = getMoodValue(Mood, mood_dontcare);
		int nAngry = getMoodValue(Mood, mood_angry);
		int nAfraid = getMoodValue(Mood, mood_afraid);
		int nAmused = getMoodValue(Mood, mood_amused);
		int nAnnoyed = getMoodValue(Mood, mood_annoyed);
		
		System.out.println("happy: " + nHappy );
		System.out.println("sad: " + nSad );
		System.out.println("inspired: " + nInspired );
		System.out.println("dontcare: " + nDontcare );
		System.out.println("angry: " + nAngry );
		System.out.println("afraid: " + nAfraid );
		System.out.println("amused: " + nAmused );
		System.out.println("annoyed: " + nAnnoyed );
		
	}
}
