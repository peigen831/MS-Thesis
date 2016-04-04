package rappler;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;

public class SentenceSplitter {

	public ArrayList<String> splitSentence(String paragraph){
		
//		String P = "My 1st sentence. “Does it work for questions?” My third sentence.";
//		String P = "This is a test? This is a T.L.A. test! Now with a Dr. in it.";
		Reader reader = new StringReader(paragraph);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		ArrayList<String> sentenceList = new ArrayList<String>();
		
		for (List<HasWord> sentence : dp) {
		   String sentenceString = Sentence.listToString(sentence);
		   sentenceList.add(sentenceString.toString());
		}
		
		return sentenceList;
	}
}
