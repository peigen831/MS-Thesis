package semantic_parser;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Properties;
import semantic_parser.concepts_extraction;
import semantic_parser.pre_processing;

public class concept_parser {
	Properties props;
	StanfordCoreNLP pipeline;
	LexicalizedParser lp;
	
	public concept_parser(){
		props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
	    pipeline = new StanfordCoreNLP(props);
	    lp = LexicalizedParser.loadModel((String)"edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz", (String[])new String[0]);
	}
	
    public ArrayList get_concepts(String line, LexicalizedParser lp, StanfordCoreNLP pipeline) throws IOException {
        ArrayList<String> final_concepts = new ArrayList<String>();
        try {
            String tag;
            String word;
            int count;
            String ss;
            if (!(line.endsWith(".") || line.endsWith("?") || line.endsWith("!"))) {
                line = String.valueOf(line) + ".";
            }
            line = line.replaceAll("[  ]+", " ");
            pre_processing pp = new pre_processing(line, lp, pipeline);
            String parse = pp.get_parse_tree();
            String pos = pp.get_pos_tags();
            concepts_extraction ce = new concepts_extraction(parse, pos, pipeline);
            ArrayList concepts = ce.get_concepts();
            System.out.println(concepts);
            String[] splited = pos.split(" ");
            if (concepts.size() == 0) {
                count = 0;
                while (count < splited.length) {
                    if (splited[count].split("/").length >= 2) {
                        word = splited[count].split("/")[0];
                        tag = splited[count].split("/")[1];
                        if (!(tag.equals("CC") || tag.equals("DT") || tag.equals("ON") || tag.equals("TO") || tag.equals("IN") || tag.equals("FW") || tag.equals("LW") || tag.equals("LS") || tag.equals(".") || word.equals("be") || tag.equals("MD") || tag.equals("?") || tag.equals("!") || final_concepts.contains(word))) {
                            final_concepts.add(word);
                        }
                    }
                    ++count;
                }
            }
            count = 0;
            while (count < splited.length) {
                if (splited[count].split("/").length >= 2) {
                    word = splited[count].split("/")[0];
                    tag = splited[count].split("/")[1];
                    String concept = "";
                    if (parse.contains("nn(" + word + "-")) {
                        String strcomp = parse.substring(parse.indexOf("nn(" + word + "-"), parse.indexOf(")", parse.indexOf("nn(" + word + "-")));
                        String noun = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
                        concept = String.valueOf(noun) + " " + word;
                        int i = 0;
                        while (i < concepts.size()) {
                            String ss2;
                            if (concepts.get(i) != null && (ss2 = ((String)concepts.get(i)).toString()).contains(word) && !ss2.contains(concept) && !final_concepts.contains(ss2)) {
                                ss2 = ss2.replaceAll(word, concept);
                                final_concepts.add(ss2);
                                concepts.remove(i);
                                i = 0;
                            }
                            ++i;
                        }
                    }
                }
                ++count;
            }
            int i = 0;
            while (i < concepts.size()) {
                if (concepts.get(i) != null && !final_concepts.contains(ss = ((String)concepts.get(i)).toString())) {
                    final_concepts.add(ss);
                }
                ++i;
            }
            i = 0;
            while (i < final_concepts.size()) {
                ss = final_concepts.get(i).toString();
                System.out.println(ss.replaceAll(" ", "_"));
                ++i;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return final_concepts;
    }

    public ArrayList get_concepts(String line) throws IOException {
        ArrayList<String> final_concepts = new ArrayList<String>();
        try {
            String tag;
            int count;
            String word;
            System.out.println("Input is ---- " + line);
            if (!(line.endsWith(".") || line.endsWith("?") || line.endsWith("!"))) {
                line = String.valueOf(line) + ".";
            }
            line = line.toLowerCase();
            line = line.replaceAll("[  ]+", " ");
            line = line.replaceAll("-", "_");
            line = line.replaceAll("/", " and ");
//            Properties props = new Properties();
//            props.put("annotators", "tokenize, ssplit, pos, lemma");
//            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//            LexicalizedParser lp = LexicalizedParser.loadModel((String)"edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz", (String[])new String[0]);
            pre_processing pp = new pre_processing(line, lp, pipeline);
            String pos = pp.get_pos_tags();
            String parse = pp.get_parse_tree();
            concepts_extraction ce = new concepts_extraction(parse, pos, pipeline);
            ArrayList concepts = ce.get_concepts();
            String[] splited = pos.split(" ");
            if (concepts.size() == 0) {
                count = 0;
                while (count < splited.length) {
                    if (splited[count].split("/").length >= 2) {
                        word = splited[count].split("/")[0];
                        tag = splited[count].split("/")[1];
                        if (!(tag.equals("CC") || tag.equals("DT") || tag.equals("ON") || tag.equals("TO") || tag.equals("IN") || tag.equals("FW") || tag.equals("LW") || tag.equals("LS") || tag.equals(".") || word.equals("be") || tag.equals("MD") || tag.equals("?") || tag.equals("!") || final_concepts.contains(word))) {
                            final_concepts.add(word);
                        }
                    }
                    ++count;
                }
            }
            count = 0;
            while (count < splited.length) {
                if (splited[count].split("/").length >= 2) {
                    String ss;
                    int i;
                    String noun;
                    String strcomp;
                    word = splited[count].split("/")[0];
                    tag = splited[count].split("/")[1];
                    String concept = "";
                    if (parse.contains("and(" + word + "-")) {
                        strcomp = parse.substring(parse.indexOf("and(" + word + "-"), parse.indexOf(")", parse.indexOf("and(" + word + "-")));
                        noun = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
                        concept = String.valueOf(noun) + " and " + word;
                        i = 0;
                        while (i < concepts.size()) {
                            if (concepts.get(i) != null) {
                                ss = ((String)concepts.get(i)).toString();
                                String[] splits = ss.split(" ");
                                boolean flag = false;
                                boolean flag2 = false;
                                int j1 = 0;
                                while (j1 < splits.length) {
                                    if (splits[j1].equals(noun)) {
                                        flag = true;
                                    }
                                    if (splits[j1].equals(word)) {
                                        flag2 = true;
                                    }
                                    ++j1;
                                }
                                if (ss.split(" ").length > 0 && flag2 && !flag) {
                                    if (!(ss.contains(" between ") || ss.contains(" for ") || ss.contains(" over ") || ss.contains(" with ") || ss.contains(" on ") || ss.contains(" to ") || ss.contains(" of ") || ss.contains(" into ") || ss.contains(" in ") || ss.contains(" at "))) {
                                        if (!final_concepts.contains(ss = ss.replaceAll(word, noun))) {
                                            final_concepts.add(ss);
                                        }
                                    } else if (ss.contains(" between ") || ss.contains(" over ") || ss.contains(" with ") || ss.contains(" on ") || ss.contains(" to ") || ss.contains(" of ") || ss.contains(" into ") || ss.contains(" in ") || ss.contains(" at ")) {
                                        concept = String.valueOf(noun) + " and " + word;
                                        if (!final_concepts.contains(ss = ss.replaceAll(word, concept))) {
                                            final_concepts.add(ss);
                                        }
                                        concepts.remove(i);
                                    }
                                }
                            }
                            ++i;
                        }
                    }
                    if (parse.contains("nn(" + word + "-")) {
                        strcomp = parse.substring(parse.indexOf("nn(" + word + "-"), parse.indexOf(")", parse.indexOf("nn(" + word + "-")));
                        noun = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
                        concept = String.valueOf(noun) + " " + word;
                        i = 0;
                        while (i < concepts.size()) {
                            if (concepts.get(i) != null && (ss = ((String)concepts.get(i)).toString()).contains(word) && !ss.contains(concept) && !final_concepts.contains(ss)) {
                                ss = ss.replaceAll(word, concept);
                                final_concepts.add(ss);
                                concepts.remove(i);
                                i = 0;
                            }
                            ++i;
                        }
                    }
                }
                ++count;
            }
            int i = 0;
            while (i < concepts.size()) {
                String ss;
                if (concepts.get(i) != null && !final_concepts.contains(ss = ((String)concepts.get(i)).toString())) {
                    final_concepts.add(ss);
                }
                ++i;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return final_concepts;
    }

    public ArrayList concept(String input, pre_processing pp, ArrayList ar, StanfordCoreNLP pipeline) {
        String tag;
        String word;
        int count;
        String parse = pp.get_parse_tree();
        String pos = pp.get_pos_tags();
        concepts_extraction ce = null;
        try {
            System.out.println(parse);
            ce = new concepts_extraction(parse, pos, pipeline);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList concepts = ce.get_concepts();
        String[] splited = pos.split(" ");
        ArrayList final_concepts = new ArrayList();
        final_concepts = ar;
        if (concepts.size() == 0) {
            count = 0;
            while (count < splited.length) {
                if (splited[count].split("/").length >= 2) {
                    word = splited[count].split("/")[0];
                    tag = splited[count].split("/")[1];
                    if (!(tag.equals("CC") || tag.equals("DT") || tag.equals("ON") || tag.equals("TO") || tag.equals("IN") || tag.equals("FW") || tag.equals("LW") || tag.equals("LS") || tag.equals(".") || word.equals("be") || tag.equals("MD") || tag.equals("?") || tag.equals("!") || final_concepts.contains(word))) {
                        final_concepts.add(word);
                    }
                }
                ++count;
            }
        }
        count = 0;
        while (count < splited.length) {
            if (splited[count].split("/").length >= 2) {
                word = splited[count].split("/")[0];
                tag = splited[count].split("/")[1];
                String concept = "";
                if (parse.contains("nn(" + word + "-")) {
                    String strcomp = parse.substring(parse.indexOf("nn(" + word + "-"), parse.indexOf(")", parse.indexOf("nn(" + word + "-")));
                    String noun = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
                    concept = String.valueOf(noun) + " " + word;
                    int i = 0;
                    while (i < concepts.size()) {
                        String ss = ((String)concepts.get(i)).toString();
                        if (ss.contains(word) && !ss.contains(concept) && !final_concepts.contains(ss)) {
                            ss = ss.replaceAll(word, concept);
                            final_concepts.add(ss);
                            concepts.remove(i);
                            i = 0;
                        }
                        ++i;
                    }
                }
            }
            ++count;
        }
        int i = 0;
        while (i < concepts.size()) {
            String ss = ((String)concepts.get(i)).toString();
            if (!final_concepts.contains(ss = ss.replaceAll("/JJ", ""))) {
                final_concepts.add(ss);
            }
            ++i;
        }
        return final_concepts;
    }

    public ArrayList concept(String input, pre_processing pp, ArrayList ar) {
        String tag;
        String word;
        int count;
        String parse = pp.get_parse_tree();
        String pos = pp.get_pos_tags();
        System.out.println(parse);
        concepts_extraction ce = new concepts_extraction(parse, pos);
        ArrayList concepts = ce.get_concepts();
        String[] splited = pos.split(" ");
        ArrayList final_concepts = new ArrayList();
        final_concepts = ar;
        if (concepts.size() == 0) {
            count = 0;
            while (count < splited.length) {
                if (splited[count].split("/").length >= 2) {
                    word = splited[count].split("/")[0];
                    tag = splited[count].split("/")[1];
                    if (!(tag.equals("CC") || tag.equals("DT") || tag.equals("ON") || tag.equals("TO") || tag.equals("IN") || tag.equals("FW") || tag.equals("LW") || tag.equals("LS") || tag.equals(".") || word.equals("be") || tag.equals("MD") || tag.equals("?") || tag.equals("!") || final_concepts.contains(word))) {
                        final_concepts.add(word);
                    }
                }
                ++count;
            }
        }
        count = 0;
        while (count < splited.length) {
            if (splited[count].split("/").length >= 2) {
                word = splited[count].split("/")[0];
                tag = splited[count].split("/")[1];
                String concept = "";
                if (parse.contains("nn(" + word + "-")) {
                    String strcomp = parse.substring(parse.indexOf("nn(" + word + "-"), parse.indexOf(")", parse.indexOf("nn(" + word + "-")));
                    String noun = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
                    concept = String.valueOf(noun) + " " + word;
                    int i = 0;
                    while (i < concepts.size()) {
                        String ss = ((String)concepts.get(i)).toString();
                        if (ss.contains(word) && !ss.contains(concept) && !final_concepts.contains(ss)) {
                            ss = ss.replaceAll(word, concept);
                            final_concepts.add(ss);
                            concepts.remove(i);
                            i = 0;
                        }
                        ++i;
                    }
                }
            }
            ++count;
        }
        int i = 0;
        while (i < concepts.size()) {
            String ss = ((String)concepts.get(i)).toString();
            if (!final_concepts.contains(ss)) {
                final_concepts.add(ss);
            }
            ++i;
        }
        return final_concepts;
    }

    public static void display_concepts(String[] args) throws IOException {
        String line = "";
        if (args.length > 0) {
            line = args[0];
            line = line.toLowerCase();
            line = line.replaceAll("-", "_");
            line = line.replaceAll("/", " and ");
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            line = br.readLine();
            line = line.toLowerCase();
            line = line.replaceAll("-", "_");
            line = line.replaceAll("/", " and ");
        }
        try {
            String tag;
            String ss;
            int count;
            String word;
            System.out.println("Input is ---- " + line);
            if (!(line.endsWith(".") || line.endsWith("?") || line.endsWith("!"))) {
                line = String.valueOf(line) + ".";
            }
            line = line.replaceAll("[  ]+", " ");
            pre_processing pp = new pre_processing(line);
            String parse = pp.get_parse_tree();
            String pos = pp.get_pos_tags();
            Properties props = new Properties();
            props.put("annotators", "tokenize, ssplit, pos, lemma");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            LexicalizedParser lp = LexicalizedParser.loadModel((String)"edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz", (String[])new String[0]);
            concepts_extraction ce = new concepts_extraction(parse, pos, pipeline);
            ArrayList concepts = ce.get_concepts();
            String[] splited = pos.split(" ");
            ArrayList<String> final_concepts = new ArrayList<String>();
            if (concepts.size() == 0) {
                count = 0;
                while (count < splited.length) {
                    if (splited[count].split("/").length >= 2) {
                        word = splited[count].split("/")[0];
                        tag = splited[count].split("/")[1];
                        if (!(tag.equals("CC") || tag.equals("DT") || tag.equals("ON") || tag.equals("TO") || tag.equals("IN") || tag.equals("FW") || tag.equals("LW") || tag.equals("LS") || tag.equals(".") || word.equals("be") || tag.equals("MD") || tag.equals("?") || tag.equals("!") || final_concepts.contains(word))) {
                            final_concepts.add(word);
                        }
                    }
                    ++count;
                }
            }
            count = 0;
            while (count < splited.length) {
                if (splited[count].split("/").length >= 2) {
                    String ss2;
                    int i;
                    String noun;
                    String strcomp;
                    word = splited[count].split("/")[0];
                    tag = splited[count].split("/")[1];
                    String concept = "";
                    if (parse.contains("and(" + word + "-")) {
                        strcomp = parse.substring(parse.indexOf("and(" + word + "-"), parse.indexOf(")", parse.indexOf("and(" + word + "-")));
                        noun = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
                        concept = String.valueOf(noun) + " and " + word;
                        i = 0;
                        while (i < concepts.size()) {
                            if (concepts.get(i) != null) {
                                ss2 = ((String)concepts.get(i)).toString();
                                String[] splits = ss2.split(" ");
                                boolean flag = false;
                                boolean flag2 = false;
                                int j1 = 0;
                                while (j1 < splits.length) {
                                    if (splits[j1].equals(noun)) {
                                        flag = true;
                                    }
                                    if (splits[j1].equals(word)) {
                                        flag2 = true;
                                    }
                                    ++j1;
                                }
                                if (ss2.split(" ").length > 0 && flag2 && !flag) {
                                    if (!(ss2.contains(" between ") || ss2.contains(" for ") || ss2.contains(" over ") || ss2.contains(" with ") || ss2.contains(" on ") || ss2.contains(" to ") || ss2.contains(" of ") || ss2.contains(" into ") || ss2.contains(" in ") || ss2.contains(" at "))) {
                                        if (!final_concepts.contains(ss2 = ss2.replaceAll(word, noun))) {
                                            final_concepts.add(ss2);
                                        }
                                    } else if (ss2.contains(" between ") || ss2.contains(" over ") || ss2.contains(" with ") || ss2.contains(" on ") || ss2.contains(" to ") || ss2.contains(" of ") || ss2.contains(" into ") || ss2.contains(" in ") || ss2.contains(" at ")) {
                                        concept = String.valueOf(noun) + " and " + word;
                                        if (!final_concepts.contains(ss2 = ss2.replaceAll(word, concept))) {
                                            final_concepts.add(ss2);
                                        }
                                        concepts.remove(i);
                                    }
                                }
                            }
                            ++i;
                        }
                    }
                    if (parse.contains("nn(" + word + "-")) {
                        strcomp = parse.substring(parse.indexOf("nn(" + word + "-"), parse.indexOf(")", parse.indexOf("nn(" + word + "-")));
                        noun = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
                        concept = String.valueOf(noun) + " " + word;
                        i = 0;
                        while (i < concepts.size()) {
                            if (concepts.get(i) != null && (ss2 = ((String)concepts.get(i)).toString()).contains(word) && !ss2.contains(concept) && !final_concepts.contains(ss2)) {
                                ss2 = ss2.replaceAll(word, concept);
                                final_concepts.add(ss2);
                                concepts.remove(i);
                                i = 0;
                            }
                            ++i;
                        }
                    }
                }
                ++count;
            }
            int i = 0;
            while (i < concepts.size()) {
                if (concepts.get(i) != null && !final_concepts.contains(ss = ((String)concepts.get(i)).toString())) {
                    final_concepts.add(ss);
                }
                ++i;
            }
            i = 0;
            while (i < final_concepts.size()) {
                ss = final_concepts.get(i).toString();
                System.out.println(ss.replaceAll(" ", "_"));
                ++i;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String[] line = {"X and though the book puts great emphasis on mathematics and even includes a big section on important mathematical background knowledge, it contains too many errors in the mathematical formulas, so they are of little use."};
        concept_parser.display_concepts(line);
    }
}