package parser;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.jena.query.ResultSet;

import Sentic.Concept;
import Sentic.ConceptLoader;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class SenticConceptPattern {
	
	public Set<String> getPossibleRelation(Concept[] arrConcept){
		  // This option shows loading and using an explicit tokenizer
		
		String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	    LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
	    
	    Set<String> resultSet = new HashSet<>();

	    for(Concept c: arrConcept)
	    {
	//	    String sent2 = "and though the book puts great emphasis on mathematics and even includes a big section on important mathematical background knowledge, it contains too many errors in the mathematical formulas, so they are of little use.";
		    TokenizerFactory<CoreLabel> tokenizerFactory =
		        PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		    Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(c.getConcept()));
		    List<CoreLabel> rawWords2 = tok.tokenize();
		    Tree parse = lp.apply(rawWords2);
	
		    TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
		    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		    String s = "";
		    
		    for(TypedDependency td: tdl){
		    	s += td.reln() + " ";
		    }
		    
//		    for(TypedDependency td : tdl)
//		    {
//		    	if(td.gov().toString().equals("ROOT"))
//		    		s += td.gov().toString()+" ";
//		    	else
//		    	{
//		    		String a = td.gov().toString().split("/")[1];
//		    		s +=  a + " ";
//		    	}
//		    }

//		    TreePrint tp = new TreePrint("typedDependenciesCollapsed");
//		    tp.printTree(parse);
//		    System.out.println(tdl);
//		    System.out.println();
		    resultSet.add(s);
	    }
	    System.out.println("Size: " + resultSet.size());
	    return resultSet;
	}
	
//TODO REMOVE OUT OF SCOPE
    public static String input(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        return line;
    }

//TODO REMOVE OUT OF SCOPE
    public static Set<String> tokens(String line) {
        String[] tokens = line.split(" ");
        Set<String> words = new HashSet<>();
        for(String token : tokens) {
            words.add(token);
        }
        return words;
    }
	
	public static void main(String args[]){
		//load concept
		//pass to dependency tree
		//store the result to hashset
		//print hashset
		ConceptLoader loader = new ConceptLoader();
		Concept[] arrConcept = loader.getSenticConcept();
		
		Set<String> uniqRelation = new SenticConceptPattern().getPossibleRelation(arrConcept);
		
		for(String s: uniqRelation){
			System.out.println(s);	
		}
		System.out.println("Size: "+uniqRelation.size());
	}
}
