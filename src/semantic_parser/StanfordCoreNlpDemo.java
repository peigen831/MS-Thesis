/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  edu.stanford.nlp.ling.CoreAnnotations
 *  edu.stanford.nlp.ling.CoreAnnotations$LemmaAnnotation
 *  edu.stanford.nlp.ling.CoreAnnotations$PartOfSpeechAnnotation
 *  edu.stanford.nlp.ling.CoreAnnotations$SentencesAnnotation
 *  edu.stanford.nlp.ling.CoreAnnotations$TextAnnotation
 *  edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation
 *  edu.stanford.nlp.ling.CoreLabel
 *  edu.stanford.nlp.ling.Label
 *  edu.stanford.nlp.parser.lexparser.LexicalizedParser
 *  edu.stanford.nlp.pipeline.Annotation
 *  edu.stanford.nlp.pipeline.StanfordCoreNLP
 *  edu.stanford.nlp.process.CoreLabelTokenFactory
 *  edu.stanford.nlp.process.LexedTokenFactory
 *  edu.stanford.nlp.process.PTBTokenizer
 *  edu.stanford.nlp.process.Tokenizer
 *  edu.stanford.nlp.process.TokenizerFactory
 *  edu.stanford.nlp.trees.GrammaticalStructure
 *  edu.stanford.nlp.trees.GrammaticalStructureFactory
 *  edu.stanford.nlp.trees.PennTreebankLanguagePack
 *  edu.stanford.nlp.trees.Tree
 *  edu.stanford.nlp.trees.TreeCoreAnnotations
 *  edu.stanford.nlp.trees.TreeCoreAnnotations$TreeAnnotation
 *  edu.stanford.nlp.util.CoreMap
 */
package semantic_parser;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.LexedTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import semantic_parser.stemmer;

public class StanfordCoreNlpDemo {
    static StanfordCoreNLP pipeline;
    static LexicalizedParser lp;

    public StanfordCoreNlpDemo() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props);
        lp = LexicalizedParser.loadModel((String)"edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz", (String[])new String[0]);
    }

    public StanfordCoreNlpDemo(LexicalizedParser lp1) {
        lp = lp1;
    }

    public static void tree_traverse(Tree parse) {
        for (Tree subtree : parse) {
            System.out.println((Object)subtree);
        }
    }

    public static List<Tree> GetADJPPhrases(Tree parse) {
        ArrayList<Tree> phraseList = new ArrayList<Tree>();
        for (Tree subtree : parse) {
            if (!subtree.label().value().equals("ADJP")) continue;
            phraseList.add(subtree);
        }
        return phraseList;
    }

    public static List<Tree> GetVerbPhrases(Tree parse) {
        ArrayList<Tree> phraseList = new ArrayList<Tree>();
        for (Tree subtree : parse) {
            if (!subtree.label().value().equals("VP")) continue;
            phraseList.add(subtree);
        }
        return phraseList;
    }

    public static List<Tree> GetNounPhrases(Tree parse) {
        ArrayList<Tree> phraseList = new ArrayList<Tree>();
        for (Tree subtree : parse) {
            if (!subtree.label().value().equals("NP")) continue;
            phraseList.add(subtree);
        }
        return phraseList;
    }

    public String pos_tag(String text) {
        String pos = "X";
        Annotation document = pipeline.process(text);
        String line = "";
        for (CoreMap sentence : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : (List<CoreLabel>)sentence.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                String word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                pos = (String)token.get((Class)CoreAnnotations.PartOfSpeechAnnotation.class);
                line = String.valueOf(line) + " " + word + "/" + pos;
            }
        }
        return line.trim();
    }

    public String pos_tag(String text, StanfordCoreNLP pipeline1) {
        String pos = "X";
        Annotation document = pipeline1.process(text);
        String line = "";
        for (CoreMap sentence : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : (List<CoreLabel>)sentence.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                String word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                pos = (String)token.get((Class)CoreAnnotations.PartOfSpeechAnnotation.class);
                line = String.valueOf(line) + " " + word + "/" + pos;
            }
        }
        return line.trim();
    }

    public String lemmatize_pos(String text, HashMap<String, String> h, String tokenizer_out, String parse) {
        Annotation document = pipeline.process(text);
        String line = "";
        for (CoreMap sentence : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : (List<CoreLabel>)sentence.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                String word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                int index = tokenizer_out.indexOf(String.valueOf(word) + "/") + (String.valueOf(word) + "/").length();
                String pos = tokenizer_out.substring(index, index + 1);
                if (h.containsKey(String.valueOf(word) + "#" + pos) || h.containsKey(word) || word.equals("been") || word.equals("are") || word.equals("am") || word.equals("was") || word.equals("is") || parse.contains("acomp(" + word + "-") || word.equals("me") || word.equals("him")) {
                    line = String.valueOf(line) + " " + (Object)token;
                    continue;
                }
                String lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                line = String.valueOf(line) + lemma + "/" + pos + " ";
            }
        }
        return line.trim();
    }

    public String lemmatize(String text, HashMap<String, String> h, String tokenizer_out, String parse) {
        Annotation document = pipeline.process(text);
        String line = "";
        for (CoreMap sentence : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : (List<CoreLabel>)sentence.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                String word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                if (word.equals("gone")) {
                    line = String.valueOf(line) + " " + "go";
                    continue;
                }
                if (h.containsKey(word) || word.equals("been") || word.equals("are") || word.equals("am") || word.equals("was") || word.equals("is") || parse.contains("acomp(" + word + "-") || word.equals("me") || word.equals("him") || parse.contains("prt(" + word + "-")) {
                    line = String.valueOf(line) + " " + (Object)token;
                    continue;
                }
                String lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                line = String.valueOf(line) + " " + lemma;
            }
        }
        return line.trim();
    }

    public String lemmatizer(String text) {
        String lemma = "";
        Annotation document = pipeline.process(text);
        for (CoreMap sentence : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : (List<CoreLabel>)sentence.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                String word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                System.out.println("lemmatized version :" + lemma);
            }
        }
        return lemma;
    }

    public String lemmatize(String text, HashMap<String, String> h) {
        Annotation document = pipeline.process(text);
        String line = "";
        for (CoreMap sentence : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : (List<CoreLabel>)sentence.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                String word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                if (word.equalsIgnoreCase("me") || word.equalsIgnoreCase("he")) {
                    line = String.valueOf(line) + " " + word;
                    continue;
                }
                String lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                line = String.valueOf(line) + " " + lemma;
            }
        }
        return line.trim();
    }

    public static String lemmatize_parse(String parse, Hashtable<String, String> h, String words, StanfordCoreNLP pipeline1) {
        String[] indices = words.split(" ");
        String text = parse.substring(parse.indexOf("[") + 1, parse.indexOf("]"));
        String[] tokens = text.split(Pattern.quote("),"));
        String nw_text = "";
        int i = 0;
        while (i < tokens.length) {
            String word;
            String lemma;
            String relation = tokens[i].substring(0, tokens[i].indexOf("("));
            String first_ = tokens[i].substring(tokens[i].indexOf(String.valueOf(relation) + "(") + (String.valueOf(relation) + "(").length(), tokens[i].indexOf("-"));
            String second_ = tokens[i].substring(tokens[i].indexOf(",") + 1, tokens[i].lastIndexOf("-"));
            String index_1 = tokens[i].substring(tokens[i].indexOf("-") + 1, tokens[i].indexOf(","));
            String index_2 = "";
            index_2 = tokens[i].indexOf(")", tokens[i].lastIndexOf("-")) > -1 ? tokens[i].substring(tokens[i].lastIndexOf("-") + 1, tokens[i].lastIndexOf(")") - 1) : tokens[i].substring(tokens[i].lastIndexOf("-") + 1);
            Annotation document = pipeline1.process(first_);
            String line = "";
            for (CoreMap sentence2 : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
                for (CoreLabel token : (List<CoreLabel>)sentence2.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                    word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                    if (word.equals("been") || word.equals("are") || word.equals("am") || word.equals("is") || word.equals("me") || word.equals("him") || word.equals("was") || words.contains((Object)token + "/JJ")) {
                        line = String.valueOf(line) + " " + (Object)token;
                        continue;
                    }
                    lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                    line = String.valueOf(line) + " " + lemma;
                }
            }
            first_ = line.trim();
            if (Integer.parseInt(index_1) != 0 && !first_.equals(indices[Integer.parseInt(index_1) - 1].split("/")[0])) {
                first_ = indices[Integer.parseInt(index_1) - 1].split("/")[0];
            }
            document = pipeline1.process(second_);
            line = "";
            for (CoreMap sentence2 : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
                for (CoreLabel token : (List<CoreLabel>)sentence2.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                    word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                    if (word.equals("been") || word.equals("are") || word.equals("am") || word.equals("is") || parse.contains("acomp(" + word + "-") || word.equals("me") || word.equals("him") || word.equals("was") || words.contains((Object)token + "/JJ")) {
                        line = String.valueOf(line) + " " + (Object)token;
                        continue;
                    }
                    lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                    line = String.valueOf(line) + " " + lemma;
                }
            }
            second_ = line;
            if (Integer.parseInt(index_2) != 0 && !second_.equals(indices[Integer.parseInt(index_2) - 1].split("/")[0])) {
                second_ = indices[Integer.parseInt(index_2) - 1].split("/")[0];
            }
            nw_text = String.valueOf(nw_text) + relation + "(" + first_ + "-" + index_1 + "," + " " + second_ + "-" + index_2 + ")" + ",";
            ++i;
        }
        nw_text = nw_text.substring(0, nw_text.length() - 1);
        nw_text = "[" + nw_text + "]";
        return nw_text;
    }

    public static String lemmatize_parse(String parse, Hashtable<String, String> h, String words) {
        String[] indices = words.split(" ");
        String text = parse.substring(parse.indexOf("[") + 1, parse.indexOf("]"));
        String[] tokens = text.split(Pattern.quote("),"));
        String nw_text = "";
        int i = 0;
        while (i < tokens.length) {
            String word;
            String lemma;
            String relation = tokens[i].substring(0, tokens[i].indexOf("("));
            String first_ = tokens[i].substring(tokens[i].indexOf(String.valueOf(relation) + "(") + (String.valueOf(relation) + "(").length(), tokens[i].indexOf("-"));
            String second_ = tokens[i].substring(tokens[i].indexOf(",") + 1, tokens[i].lastIndexOf("-"));
            String index_1 = tokens[i].substring(tokens[i].indexOf("-") + 1, tokens[i].indexOf(","));
            String index_2 = "";
            index_2 = tokens[i].indexOf(")", tokens[i].lastIndexOf("-")) > -1 ? tokens[i].substring(tokens[i].lastIndexOf("-") + 1, tokens[i].lastIndexOf(")") - 1) : tokens[i].substring(tokens[i].lastIndexOf("-") + 1);
            Annotation document = pipeline.process(first_);
            String line = "";
            for (CoreMap sentence2 : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
                for (CoreLabel token : (List<CoreLabel>)sentence2.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                    word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                    if (word.equals("been") || word.equals("are") || word.equals("am") || word.equals("is") || word.equals("me") || word.equals("him") || word.equals("was") || words.contains((Object)token + "/JJ")) {
                        line = String.valueOf(line) + " " + (Object)token;
                        continue;
                    }
                    lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                    line = String.valueOf(line) + " " + lemma;
                }
            }
            first_ = line.trim();
            if (Integer.parseInt(index_1) != 0 && !first_.equals(indices[Integer.parseInt(index_1) - 1].split("/")[0])) {
                first_ = indices[Integer.parseInt(index_1) - 1].split("/")[0];
            }
            document = pipeline.process(second_);
            line = "";
            for (CoreMap sentence2 : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
                for (CoreLabel token : (List<CoreLabel>)sentence2.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                    word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                    if (word.equals("been") || word.equals("are") || word.equals("am") || word.equals("is") || parse.contains("acomp(" + word + "-") || word.equals("me") || word.equals("him") || word.equals("was") || words.contains((Object)token + "/JJ")) {
                        line = String.valueOf(line) + " " + (Object)token;
                        continue;
                    }
                    lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                    line = String.valueOf(line) + " " + lemma;
                }
            }
            second_ = line;
            if (Integer.parseInt(index_2) != 0 && !second_.equals(indices[Integer.parseInt(index_2) - 1].split("/")[0])) {
                second_ = indices[Integer.parseInt(index_2) - 1].split("/")[0];
            }
            nw_text = String.valueOf(nw_text) + relation + "(" + first_ + "-" + index_1 + "," + " " + second_ + "-" + index_2 + ")" + ",";
            ++i;
        }
        nw_text = nw_text.substring(0, nw_text.length() - 1);
        nw_text = "[" + nw_text + "]";
        return nw_text;
    }

    public static String lemmatize_parse(String parse, StanfordCoreNLP pipeline) {
        stemmer stm = new stemmer();
        String text = parse.substring(parse.indexOf("[") + 1, parse.indexOf("]"));
        String[] tokens = text.split(Pattern.quote("),"));
        String nw_text = "";
        int i = 0;
        while (i < tokens.length) {
            String lemma;
            String word;
            String relation = tokens[i].substring(0, tokens[i].indexOf("("));
            String first_ = tokens[i].substring(tokens[i].indexOf(String.valueOf(relation) + "(") + (String.valueOf(relation) + "(").length(), tokens[i].indexOf("-"));
            String second_ = tokens[i].substring(tokens[i].indexOf(",") + 1, tokens[i].lastIndexOf("-"));
            String index_1 = tokens[i].substring(tokens[i].indexOf("-") + 1, tokens[i].indexOf(","));
            String index_2 = tokens[i].substring(tokens[i].lastIndexOf("-") + 1);
            Annotation document = pipeline.process(first_);
            String line = "";
            for (CoreMap sentence2 : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
                for (CoreLabel token : (List<CoreLabel>)sentence2.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                    word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                    if (word.equals("been") || word.equals("are") || word.equals("am") || word.equals("is") || word.equals("me") || word.equals("him") || word.equals("was")) {
                        line = String.valueOf(line) + " " + (Object)token;
                        continue;
                    }
                    lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                    line = String.valueOf(line) + " " + stm.stem(lemma);
                }
            }
            first_ = stm.stem(line.trim());
            document = pipeline.process(second_);
            line = "";
            for (CoreMap sentence2 : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
                for (CoreLabel token : (List<CoreLabel>)sentence2.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                    word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                    if (word.equals("been") || word.equals("are") || word.equals("am") || word.equals("is") || parse.contains("acomp(" + word + "-") || word.equals("me") || word.equals("him") || word.equals("was")) {
                        line = String.valueOf(line) + " " + (Object)token;
                        continue;
                    }
                    lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                    line = String.valueOf(line) + " " + stm.stem(lemma);
                }
            }
            second_ = stm.stem(line.trim());
            nw_text = String.valueOf(nw_text) + relation + "(" + first_ + "-" + index_1 + "," + " " + second_ + "-" + index_2 + ")" + ",";
            ++i;
        }
        nw_text = nw_text.substring(0, nw_text.length() - 1);
        nw_text = "[" + nw_text + "]";
        return nw_text;
    }

    public static String lemmatize_parse(String parse) {
        stemmer stm = new stemmer();
        String text = parse.substring(parse.indexOf("[") + 1, parse.indexOf("]"));
        String[] tokens = text.split(Pattern.quote("),"));
        String nw_text = "";
        int i = 0;
        while (i < tokens.length) {
            String word;
            String lemma;
            String relation = tokens[i].substring(0, tokens[i].indexOf("("));
            String first_ = tokens[i].substring(tokens[i].indexOf(String.valueOf(relation) + "(") + (String.valueOf(relation) + "(").length(), tokens[i].indexOf("-"));
            String second_ = tokens[i].substring(tokens[i].indexOf(",") + 1, tokens[i].lastIndexOf("-"));
            String index_1 = tokens[i].substring(tokens[i].indexOf("-") + 1, tokens[i].indexOf(","));
            String index_2 = tokens[i].substring(tokens[i].lastIndexOf("-") + 1);
            Annotation document = pipeline.process(first_);
            String line = "";
            for (CoreMap sentence2 : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
                for (CoreLabel token : (List<CoreLabel>)sentence2.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                    word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                    if (word.equals("been") || word.equals("are") || word.equals("am") || word.equals("is") || word.equals("me") || word.equals("him") || word.equals("was")) {
                        line = String.valueOf(line) + " " + (Object)token;
                        continue;
                    }
                    lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                    line = String.valueOf(line) + " " + stm.stem(lemma);
                }
            }
            first_ = stm.stem(line.trim());
            document = pipeline.process(second_);
            line = "";
            for (CoreMap sentence2 : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
                for (CoreLabel token : (List<CoreLabel>)sentence2.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                    word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                    if (word.equals("been") || word.equals("are") || word.equals("am") || word.equals("is") || parse.contains("acomp(" + word + "-") || word.equals("me") || word.equals("him") || word.equals("was")) {
                        line = String.valueOf(line) + " " + (Object)token;
                        continue;
                    }
                    lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                    line = String.valueOf(line) + " " + stm.stem(lemma);
                }
            }
            second_ = stm.stem(line.trim());
            nw_text = String.valueOf(nw_text) + relation + "(" + first_ + "-" + index_1 + "," + " " + second_ + "-" + index_2 + ")" + ",";
            ++i;
        }
        nw_text = nw_text.substring(0, nw_text.length() - 1);
        nw_text = "[" + nw_text + "]";
        return nw_text;
    }

    public String corenlp(String s) throws IOException {
        TokenizerFactory tokenizerFactory = PTBTokenizer.factory((LexedTokenFactory)new CoreLabelTokenFactory(), (String)"");
        List rawWords2 = tokenizerFactory.getTokenizer((Reader)new StringReader(s.trim())).tokenize();
        Tree parse = lp.apply(rawWords2);
        PennTreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List tdl = gs.typedDependenciesCCprocessed();
        String pd = StanfordCoreNlpDemo.lemmatize_parse(tdl.toString());
        return pd;
    }

    public String corenlp(String s, LexicalizedParser lp1, StanfordCoreNLP pipeline) throws IOException {
        TokenizerFactory tokenizerFactory = PTBTokenizer.factory((LexedTokenFactory)new CoreLabelTokenFactory(), (String)"");
        List rawWords2 = tokenizerFactory.getTokenizer((Reader)new StringReader(s.trim())).tokenize();
        Tree parse = lp1.apply(rawWords2);
        PennTreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List tdl = gs.typedDependenciesCCprocessed();
        String pd = StanfordCoreNlpDemo.lemmatize_parse(tdl.toString(), pipeline);
        return pd;
    }

    public String corenlp(String s, int x) throws IOException {
        TokenizerFactory tokenizerFactory = PTBTokenizer.factory((LexedTokenFactory)new CoreLabelTokenFactory(), (String)"");
        List rawWords2 = tokenizerFactory.getTokenizer((Reader)new StringReader(s.trim())).tokenize();
        Tree parse = lp.apply(rawWords2);
        PennTreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List tdl = gs.typedDependenciesCCprocessed();
        return tdl.toString();
    }

    private static Tree return_Tree(CoreMap sentence) {
        return (Tree)sentence.get((Class)TreeCoreAnnotations.TreeAnnotation.class);
    }

    public static String lemmatize(String text) {
        Annotation document = pipeline.process(text);
        String line = "";
        for (CoreMap sentence : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : (List<CoreLabel>)sentence.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                String word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                String lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                line = String.valueOf(line) + " " + lemma;
            }
        }
        return line.trim();
    }

    public static String lemmatize(String text, StanfordCoreNLP pipeline1) {
        Annotation document = pipeline1.process(text);
        String line = "";
        for (CoreMap sentence : (List<CoreMap>)document.get((Class)CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : (List<CoreLabel>)sentence.get((Class)CoreAnnotations.TokensAnnotation.class)) {
                String word = (String)token.get((Class)CoreAnnotations.TextAnnotation.class);
                String lemma = (String)token.get((Class)CoreAnnotations.LemmaAnnotation.class);
                line = String.valueOf(line) + " " + lemma;
            }
        }
        return line.trim();
    }

    public static String getOutput(String s) throws IOException {
        StanfordCoreNlpDemo stan = new StanfordCoreNlpDemo();
        return stan.corenlp(s);
    }

    public static void main(String[] args) throws IOException {
        StanfordCoreNlpDemo stan = new StanfordCoreNlpDemo();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine().toLowerCase();
        String parse = stan.corenlp(line);
        System.out.println(StanfordCoreNlpDemo.lemmatize_parse(parse));
    }
}