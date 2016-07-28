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
    	String b = "i have to go to 7-11. And his birthday is Aug 20,1997";
    	
    	System.out.println(n.ngrams(3, a+b));
    	
    }
}