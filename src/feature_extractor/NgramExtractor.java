package feature_extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NgramExtractor {
	
	public String[] removePunctuation(String input){
		return input.replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase().split("\\s+");
	}

    public List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<String>();
//      String[] words = str.split(" ");
        String[] words = removePunctuation(str);
        for (int i = 0; i < words.length - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams;
    }

    public String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words[i]);
        return sb.toString();
    }

//    public void main(String[] args) {
//        for (int n = 1; n <= 3; n++) {
//            for (String ngram : ngrams(n, "This is my car."))
//                System.out.println(ngram);
//            System.out.println();
//        }
//    }
    
    public static void main(String args[]){
    	NgramExtractor n = new NgramExtractor();
    	String a = "ThiS Is my car! Not,    yours.";
    	String b = "CONTRASTING SPEECHES? Pope Francis stands next to Philippine president Benigno Aquino III during arrival honors at Malacanang presidential palace, Manila, Philippines, 16 January 2015. The two later delivered separate speeches tackling issues affecting Philippine society. Photo by Dennis M. Sabangan/EPA"
+ "\n\n MANILA, Philippines (UPDATED) – Ouch, that hurt. Of all the topics Pope Francis touched on in his Malacañang speech – from Super Typhoon Yolanda (Haiyan) to migration and indigenous peoples – one comment drew the most praise from Twitter-savvy Filipinos. I hope that this prophetic summons will challenge everyone, at all levels of society, to reject every form of corruption which diverts resources from the poor,” the Pope said before an audience that included the country’s top politicians."
+ "The pontiff’s remark in his first ever speech in the Philippines on Friday, January 16, hit a nerve for Filipino Twitter users, with the message resonating in a country outraged by corruption scandals at the highest levels. The Pope made the comment as he stressed the importance of social justice, criticizing what he called the “glaring and indeed scandalous, social inequalities.” Watch his whole speech below.";
    	
    	System.out.println(n.ngrams(3, b));
    	
    }
}