package parser;

import java.util.ArrayList;
import java.util.List;

import dataprocessor.Lemmatizer;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class ClauseParser {

//	  (S
//			    (NP (PRP I))
//			    (VP (VBP am)
//			      (VP (VBG going)
//			        (PP (TO to)
//			          (NP (DT the) (NN market)))
//			        (S
//			          (VP (TO to)
//			            (VP (VB buy)
//			              (NP
//			                (NP (NNS vegetables))
//			                (CC and)
//			                (NP (DT some) (NNS fruits))))))))))
	
	public static Tree[] getClauses(CoreMap sentence){
		Tree rootTree = sentence.get(TreeAnnotation.class);
		List<Tree> clauses = new ArrayList<>();
		
		clauses = getClauses(rootTree, new ArrayList<Tree>());
		
		return clauses.toArray(new Tree[clauses.size()]);
	}
	
	public static ArrayList<Tree> getClauses(Tree rootTree, ArrayList<Tree> clauseTree){
		ArrayList<Tree> newClauses = new ArrayList<Tree>();
		for(Tree child: rootTree.children()){		
			if(child.value().equals("S"))
			{
				newClauses.addAll(getClauses(child, clauseTree));
//				isNew = true;
//				continue;
			}
			else if(child.value().equals("VP") || child.value().equals("NP"))
			{
				if(clauseTree.size()==0)
				{
					Tree t = child.localTree();
					clauseTree.add(t);
					return getClauses(child, clauseTree);
				}
				else 
				{
					Tree t = child.localTree();
					Tree parent = clauseTree.get(clauseTree.size()-1);
					parent.addChild(t);
					clauseTree.remove(clauseTree.size()-1);
					clauseTree.add(parent);
					return getClauses(child, clauseTree);
				}
			}
		
			else{
				if(clauseTree.size()!=0){
					Tree t = child.localTree();
					Tree parent = clauseTree.get(clauseTree.size()-1);
					parent.addChild(t);
					clauseTree.remove(clauseTree.size()-1);
					clauseTree.add(parent);
					return getClauses(child, clauseTree);
				}
				else
				{
					System.out.println("CLAUSE TREE EMPTY: " + child.localTree() );
				}
			}
		}
		return newClauses;
	}
	
	public static void printPhrase(CoreMap sentence){
		Tree rootTree = sentence.get(TreeAnnotation.class);
		for(Tree child: rootTree.children())
		{
			printPhrase(child);
		}
		System.out.println("END PRINTPHRASE");
	}
	
	public static void printPhrase(Tree parent){
		for(Tree child: parent.children()){
//			if(child.value().equals("VP")|| child.value().equals("NP"))
//				System.out.println(child.nodeString() + "  " + child.pennString());
				child.printLocalTree();
				printPhrase(child);
		}
	}
	
	

	public static Tree[] getIndependentClauses(CoreMap sentence) {
		Tree rootTree = sentence.get(TreeAnnotation.class);
		// I'm assuming an independent clause to have its first child as a NP
		// and second child as VP. If it does not meet this criteria, its not
		// considered an independent clause.
		List<Tree> subjectTrees = new ArrayList<>();
		for (Tree t : rootTree) {
//			System.out.println(t.toString());
//			if (t.value().equals("S") && t.children().length > 1 && hasNounPhraseBeforeVerbPhraseAtTop(t)) {
				subjectTrees.add(t);
//			}
		}
		return subjectTrees.toArray(new Tree[subjectTrees.size()]);
	}

	private static boolean hasNounPhraseBeforeVerbPhraseAtTop(Tree tree) {
		int npIndex = Integer.MAX_VALUE;
		for (int i = 0; i < tree.children().length; i++) {
			Tree t = tree.children()[i];
			if (t.value().equals("NP"))
				npIndex = i;
			if (t.value().equals("VP")) {
				if (i > npIndex)
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	public static void main(String args[]){
		String a = "I am going to the market to buy vegetables and some fruits.";
		List<CoreMap> sentenceList = Lemmatizer.getInstance().getCoreMap(a);
		
//		for(CoreMap c: sentenceList){
//			System.out.println(c.toString());
//		}
		
		
		int i = 0;
		for(CoreMap sentence: sentenceList){
			System.out.println(sentence.get(TreeAnnotation.class).nodeString());
			Tree[] clauses = getIndependentClauses(sentence);
			System.out.println(clauses.length);
			for(Tree clause: clauses)	
				System.out.println(clause.toString());
			
//			printPhrase(sentence);
//			
//			Tree[] tl= getClauses(sentence);
//			for(Tree t: tl){
//				System.out.println("NEW TREE: ");
//				System.out.println(t.pennString());
//			}
		    
		 // Alternately, to only run e.g., the clause splitter:
			
//			Properties props = PropertiesUtils.asProperties(
//		            "annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");
//
//		    List<SentenceFragment> clauses = new OpenIE(props).clausesInSentence(sentence);
//		    for (SentenceFragment clause : clauses) {
//		        System.out.println(clause.parseTree.toString(SemanticGraph.OutputFormat.LIST));
//		    }
//		    System.out.println(i);
		}
	}
}
