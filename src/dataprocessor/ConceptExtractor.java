package dataprocessor;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;


public class ConceptExtractor {
	private LexicalizedParser lp;
	
	public ConceptExtractor(){
		initialize();
	}
	
	//initialize lexical parser
	public void initialize(){
	    String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	    String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
	    lp = LexicalizedParser.loadModel(grammar, options);
//	    TreebankLanguagePack tlp = lp.getOp().langpack();
//	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	}
	
	public void parseClause(String input){
//	    String[] sent = { "This", "is", "an", "easy", "sentence", "." };
//	    List<CoreLabel> rawWords = Sentence.toCoreLabelList(sent);
//	    Tree parse = lp.apply(rawWords);
//	    parse.pennPrint();
//	    System.out.println();

	    // This option shows loading and using an explicit tokenizer
//	    String sent2 = "and though the book puts great emphasis on mathematics and even includes a big section on important mathematical background knowledge, it contains too many errors in the mathematical formulas, so they are of little use.";
	    String sent2 = input;
	    TokenizerFactory<CoreLabel> tokenizerFactory =
	        PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    Tokenizer<CoreLabel> tok =
	        tokenizerFactory.getTokenizer(new StringReader(sent2));
	    List<CoreLabel> rawWords2 = tok.tokenize();
	    Tree parse = lp.apply(rawWords2);
	    
//	    Tree np = getSubjectNounPhraseFromSentence(parse);
//	    np.pennPrint();
	    traverseTree(parse);

	    TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
	    
//	    System.out.println(tdl);
	    System.out.println();

	    // You can also use a TreePrint object to print trees and dependencies
	    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
	    tp.printTree(parse);
	}
	
	public void traverseTree(Tree tree){
		System.out.println("TRAVERSE TREE");
		if(tree.value().equals("ROOT"));
		tree = tree.firstChild();
		for(Tree t: tree.children())
			System.out.println(t.toString());
	}
	
//	public ArrayList<Tree> getPhrasesFromSentence(ArrayList<Tree> t, Tree sentenceTree){
//		for(Tree t: sentenceTree.children()){
//			t.
//		}
//		
//		return null;
//	}
	
//	public Tree getIndependentClause
	
	public Tree getSubjectNounPhraseFromSentence(Tree sentenceTree) {
		for (Tree t : sentenceTree.children()) {
			if (t.value().equals("S"))
				return getNounPhraseFromSubject(t);
		}
	return null;
}

	public Tree getNounPhraseFromSubject(Tree subjectTree){
	for (Tree t : subjectTree.children()) {
		System.out.println(t.value());
		if (t.value().equals("VP"))
			return t;
	}
	return null;
}
	
	//“I went for a walk in the park” -> go walk and go park
	
//	for each phrase
//	for each word
//		get the available concept contains the word in senticnet
//			if there are words in sentence that can form the sentence
//				add the concept
//				APPROACH 1: emotion weight per concept
	public static void main(String[] args) throws IOException {

		String clause = "I went for a walk in the park";
		
		String sentence = "I am going to the market to buy vegetables and some fruits";
		
		String test = "How I enjoy feeding you up, Mel! Don't you like your ice-cream, Mel? What do you think of your ice-cream, Mel? How I enjoy feeding you up, Mel! Eat some ice-cream, Mel.";
		
		ConceptExtractor c = new ConceptExtractor();
		
		c.parseClause(sentence);;
	}
}
