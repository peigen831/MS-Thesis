import java.util.ArrayList;
import java.util.HashSet;

import dataprocessor.Lemmatizer;
import emotion_classifier.MultiWordEmotionAnalyzer;
import sectic.Concept;

public class TestClass {
	public static void main(String args[]){
//		MultiWordEmotionAnalyzer ea = new MultiWordEmotionAnalyzer();
		
//		ea.checkLemmatize(FileIO.dirProcessed+81000);
		
//		Concept[] arrCon = ConceptLoader.getInstance().getSenticConcept();
//		
//		String[] s = {"``", "twenty", "year", "ago", "", "January", "of", "1995", "", "St.", "John", "Paul", "II", "visit", "this", "nation", "for", "the", "second", "time", "for", "the", "celebration", "of", "the", "World", "Youth", "Day", "here", "in", "Manila", "."};
//		String concept = "hollow";
//		MultiWordEmotionAnalyzer ea = new MultiWordEmotionAnalyzer();
//		
//		System.out.println(ea.hasConcept(concept, s));
		
//		String a = "aaaaaaa\nbbbbbbbb\n"
//		HashMap<String, Integer> actual = new LinkedHashMap<String, Integer>();
//		actual.put(HTMLInterpreter.mood_amused, 122);
//		actual.put(HTMLInterpreter.mood_inspired, 122);
//		actual.put(HTMLInterpreter.mood_annoyed, 122);
//		actual.put(HTMLInterpreter.mood_angry, 122);
//		actual.put(HTMLInterpreter.mood_afraid, 122);
//		actual.put(HTMLInterpreter.mood_happy, 122);
//		actual.put(HTMLInterpreter.mood_sad, 122);
//		actual.put(HTMLInterpreter.mood_dontcare, 122);
//		
//		HashMap<String, Float> predict = new LinkedHashMap<String, Float>();
//		predict.put(HTMLInterpreter.mood_annoyed, (float)122);
//		predict.put(HTMLInterpreter.mood_happy, (float)122);
//		predict.put(HTMLInterpreter.mood_sad, (float)122);
//		predict.put(HTMLInterpreter.mood_dontcare, (float)122);
//		predict.put(HTMLInterpreter.mood_amused, (float)122);
//		predict.put(HTMLInterpreter.mood_angry, (float)122);
//		predict.put(HTMLInterpreter.mood_afraid, (float)122);
		
		MultiWordEmotionAnalyzer ea = new MultiWordEmotionAnalyzer();
		String s = "I am going to the market to buy vegetables and some fruits.";
		
		ArrayList<String> arrSentence = Lemmatizer.getInstance().lemmatize(s);
		
		
		
		HashSet<Concept> set = ea.getExistConcept(arrSentence.toArray(new String[arrSentence.size()]));
		
		for(Concept c: set){
			System.out.println(c.getConcept());
		}
		
//		System.out.println(ea.getTop3MatchIndex(actual, predict));

		
	}
}
