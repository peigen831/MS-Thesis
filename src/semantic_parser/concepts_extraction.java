/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  edu.stanford.nlp.pipeline.StanfordCoreNLP
 */
package semantic_parser;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import semantic_parser.StanfordCoreNlpDemo;
import semantic_parser.stemmer;

class concepts_extraction {
    ArrayList concepts = new ArrayList();
    ArrayList<String> nsubj = new ArrayList();
    ArrayList<String> articles = new ArrayList();
    HashMap<Integer, String> word_index = null;
    StanfordCoreNLP pipeline = null;
    String _strTkenizedInput = "";

    public concepts_extraction(String parse, String _strTkenizedInput, StanfordCoreNLP pipeline) throws Exception {
        this.pipeline = pipeline;
        this._strTkenizedInput = _strTkenizedInput;
        this.articles.add("the");
        this.articles.add("a");
        this.articles.add("an");
        String[] splited = _strTkenizedInput.split(" ");
        int count = 0;
        while (count < splited.length) {
            if (splited[count].split("/").length >= 2) {
                String concept;
                String concept2;
                String target1;
                String concept3;
                String strcomp1;
                String word = splited[count].split("/")[0];
                String tag = splited[count].split("/")[1];
                if (parse.contains("nsubj(" + word + "-") && (_strTkenizedInput.contains(String.valueOf(target1 = (strcomp1 = parse.substring(parse.indexOf(new StringBuilder("nsubj(").append(word).append("-").toString()), parse.indexOf(")", parse.indexOf(new StringBuilder("nsubj(").append(word).append("-").toString())))).substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim()) + "/NN") || _strTkenizedInput.contains(String.valueOf(target1) + "/NNS") || _strTkenizedInput.contains(String.valueOf(target1) + "/NNP"))) {
                    String target2;
                    String strcomp2;
                    concept2 = this.nsubject(word, parse);
                    if (!this.concepts.contains(concept2)) {
                        this.concepts.add(concept2);
                    }
                    if (parse.contains("acomp(" + word + "-") && !this.concepts.contains(concept2 = String.valueOf(target2 = (strcomp2 = parse.substring(parse.indexOf(new StringBuilder("acomp(").append(word).append("-").toString()), parse.indexOf(")", parse.indexOf(new StringBuilder("acomp(").append(word).append("-").toString())))).substring(strcomp2.indexOf(",") + 1, strcomp2.indexOf("-", strcomp2.indexOf(","))).trim()) + " " + target1)) {
                        this.concepts.add(concept2);
                    }
                }
                if (parse.contains("nsubjpass(" + word + "-") && (_strTkenizedInput.contains(String.valueOf(target1 = (strcomp1 = parse.substring(parse.indexOf(new StringBuilder("nsubjpass(").append(word).append("-").toString()), parse.indexOf(")", parse.indexOf(new StringBuilder("nsubjpass(").append(word).append("-").toString())))).substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim()) + "/NN") || _strTkenizedInput.contains(String.valueOf(target1) + "/NNS") || _strTkenizedInput.contains(String.valueOf(target1) + "/NNP")) && !this.concepts.contains(concept2 = this.nsubjectpass(word, parse))) {
                    this.concepts.add(concept2);
                }
                if (concepts_extraction.hasModifier(word, parse)) {
                    try {
                        concept = this.modifiers(word, parse, word);
                        if (!this.concepts.contains(concept)) {
                            this.concepts.add(concept);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (parse.contains("det(" + word + "-") && !this.concepts.contains(concept = this.det(word, parse))) {
                    this.concepts.add(concept);
                }
                if (parse.contains("prt(" + word + "-") && !this.concepts.contains(concept = this.prt(word, parse))) {
                    this.concepts.add(concept);
                }
                if (parse.contains("nn(" + word + "-") && !this.concepts.contains(concept = this.nn(word, parse))) {
                    this.concepts.add(concept);
                }
                String parse1 = parse;
                while (parse1.contains("dobj(" + word + "-")) {
                    if (!parse1.contains("dobj(" + word + "-")) continue;
                    ArrayList dobj_concepts = this.dobj(word, parse1, _strTkenizedInput);
                    int i = 0;
                    while (i < dobj_concepts.size()) {
                        Object concept4 = dobj_concepts.get(i);
                        if (!this.concepts.contains(concept4)) {
                            this.concepts.add(concept4);
                        }
                        ++i;
                    }
                    String strcomp12 = parse1.substring(parse1.indexOf("dobj(" + word + "-"), parse1.indexOf(")", parse1.indexOf("dobj(" + word + "-")));
                    String target = strcomp12.substring(strcomp12.indexOf(",") + 1, strcomp12.indexOf("-", strcomp12.indexOf(","))).trim();
                    String xx = strcomp12.substring(strcomp12.indexOf("dobj(" + word + "-") + ("dobj(" + word + "-").length(), strcomp12.indexOf(","));
                    int temp1 = parse1.indexOf("-", parse1.indexOf("dobj(" + word + "-" + Integer.parseInt(xx) + ",") + ("dobj(" + word + "-" + Integer.parseInt(xx) + ",").length()) + 1;
                    int index2 = Integer.parseInt(parse1.substring(temp1, parse1.indexOf(")", temp1)).replaceAll("'", ""));
                    String current_dobj = "dobj(" + word + "-" + (count + 1) + ", " + target + "-" + index2 + ")";
                    parse1 = parse1.substring(parse1.indexOf(current_dobj) + current_dobj.length());
                }
                if (parse.contains("xcomp(" + word + "-") && !(concept3 = this.xcomp(word, parse)).equals("") && !this.concepts.contains(concept3)) {
                    this.concepts.add(concept3);
                }
                if (parse.contains("csubj(" + word + "-") && !this.concepts.contains(concept3 = this.csubj(word, parse))) {
                    this.concepts.add(concept3);
                }
                if (parse.contains("csubjpass(" + word + "-") && !this.concepts.contains(concept3 = this.csubjpass(word, parse))) {
                    this.concepts.add(concept3);
                }
                if (parse.contains("prep_" + word + "(") && !this.concepts.contains(concept3 = this.preposition(word, parse))) {
                    this.concepts.add(concept3);
                }
                if (parse.contains("prepc_" + word + "(") && !this.concepts.contains(concept3 = this.preposition_rc(word, parse))) {
                    this.concepts.add(concept3);
                }
            }
            ++count;
        }
    }

    public concepts_extraction(String parse, String _strTkenizedInput) {
        this.articles.add("the");
        this.articles.add("a");
        this.articles.add("an");
        String[] splited = _strTkenizedInput.split(" ");
        int count = 0;
        while (count < splited.length) {
            if (splited[count].split("/").length >= 2) {
                String strcomp1;
                String target1;
                String target2;
                String concept;
                String strcomp2;
                String concept2;
                String concept3;
                Object concept4;
                String word = splited[count].split("/")[0];
                String tag = splited[count].split("/")[1];
                if (parse.contains("nsubj(" + word + "-") && (_strTkenizedInput.contains(String.valueOf(target1 = (strcomp1 = parse.substring(parse.indexOf(new StringBuilder("nsubj(").append(word).append("-").toString()), parse.indexOf(")", parse.indexOf(new StringBuilder("nsubj(").append(word).append("-").toString())))).substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim()) + "/NN") || _strTkenizedInput.contains(String.valueOf(target1) + "/NNS") || _strTkenizedInput.contains(String.valueOf(target1) + "/NNP"))) {
                    String target22;
                    String strcomp22;
                    concept3 = this.nsubject(word, parse);
                    if (!this.concepts.contains(concept3)) {
                        this.concepts.add(concept3);
                    }
                    if (parse.contains("acomp(" + word + "-") && !this.concepts.contains(concept3 = String.valueOf(target22 = (strcomp22 = parse.substring(parse.indexOf(new StringBuilder("acomp(").append(word).append("-").toString()), parse.indexOf(")", parse.indexOf(new StringBuilder("acomp(").append(word).append("-").toString())))).substring(strcomp22.indexOf(",") + 1, strcomp22.indexOf("-", strcomp22.indexOf(","))).trim()) + " " + target1)) {
                        this.concepts.add(concept3);
                    }
                    if (parse.contains("dobj(" + word + "-")) {
                        strcomp22 = parse.substring(parse.indexOf("dobj(" + word + "-"), parse.indexOf(")", parse.indexOf("dobj(" + word + "-")));
                        target22 = strcomp22.substring(strcomp22.indexOf(",") + 1, strcomp22.indexOf("-", strcomp22.indexOf(","))).trim();
                        concept3 = String.valueOf(target1) + " " + target22;
                        if (!this.concepts.contains(concept3)) {
                            this.concepts.add(concept3);
                        }
                    }
                }
                if (parse.contains("vmod(" + word + "-") && !this.concepts.contains(concept3 = String.valueOf(target2 = (strcomp2 = parse.substring(parse.indexOf(new StringBuilder("vmod(").append(word).append("-").toString()), parse.indexOf(")", parse.indexOf(new StringBuilder("vmod(").append(word).append("-").toString())))).substring(strcomp2.indexOf(",") + 1, strcomp2.indexOf("-", strcomp2.indexOf(","))).trim()) + " " + word)) {
                    this.concepts.add(concept3);
                }
                if (parse.contains("nsubjpass(" + word + "-") && (_strTkenizedInput.contains(String.valueOf(target1 = (strcomp1 = parse.substring(parse.indexOf(new StringBuilder("nsubjpass(").append(word).append("-").toString()), parse.indexOf(")", parse.indexOf(new StringBuilder("nsubjpass(").append(word).append("-").toString())))).substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim()) + "/NN") || _strTkenizedInput.contains(String.valueOf(target1) + "/NNS") || _strTkenizedInput.contains(String.valueOf(target1) + "/NNP")) && !this.concepts.contains(concept3 = this.nsubjectpass(word, parse))) {
                    this.concepts.add(concept3);
                }
                if (concepts_extraction.hasModifier(word, parse)) {
                    try {
                        concept2 = this.modifiers(word, parse, word);
                        if (!this.concepts.contains(concept2)) {
                            this.concepts.add(concept2);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (parse.contains("acomp(" + word + "-") && !this.concepts.contains(concept2 = this.acomp(word, parse, this.pipeline))) {
                    this.concepts.add(concept2);
                }
                if (parse.contains("det(" + word + "-") && !this.concepts.contains(concept2 = this.det(word, parse))) {
                    this.concepts.add(concept2);
                }
                if (parse.contains("neg(" + word + "-") && !this.concepts.contains(concept2 = this.neg(word, parse))) {
                    this.concepts.add(concept2);
                }
                if (parse.contains("ccomp(" + word + "-") && !this.concepts.contains(concept2 = this.ccomp(word, parse))) {
                    this.concepts.add(concept2);
                }
                if (parse.contains("nn(" + word + "-") && !this.concepts.contains(concept2 = this.nn(word, parse))) {
                    this.concepts.add(concept2);
                }
                String parse1 = parse;
                while (parse1.contains("dobj(" + word + "-")) {
                    if (!parse1.contains("dobj(" + word + "-")) continue;
                    ArrayList dobj_concepts = this.dobj(word, parse1, _strTkenizedInput);
                    int i = 0;
                    while (i < dobj_concepts.size()) {
                        concept4 = dobj_concepts.get(i);
                        if (!this.concepts.contains(concept4)) {
                            this.concepts.add(concept4);
                        }
                        ++i;
                    }
                    String strcomp12 = parse1.substring(parse1.indexOf("dobj(" + word + "-"), parse1.indexOf(")", parse1.indexOf("dobj(" + word + "-")));
                    String target = strcomp12.substring(strcomp12.indexOf(",") + 1, strcomp12.indexOf("-", strcomp12.indexOf(","))).trim();
                    String xx = strcomp12.substring(strcomp12.indexOf("dobj(" + word + "-") + ("dobj(" + word + "-").length(), strcomp12.indexOf(","));
                    int temp1 = parse1.indexOf("-", parse1.indexOf("dobj(" + word + "-" + Integer.parseInt(xx) + ",") + ("dobj(" + word + "-" + Integer.parseInt(xx) + ",").length()) + 1;
                    int index2 = Integer.parseInt(parse1.substring(temp1, parse1.indexOf(")", temp1)).replaceAll("'", ""));
                    String current_dobj = "dobj(" + word + "-" + (count + 1) + ", " + target + "-" + index2 + ")";
                    parse1 = parse1.substring(parse1.indexOf(current_dobj) + current_dobj.length());
                }
                if (parse.contains("xcomp(" + word + "-") && !this.concepts.contains(concept = this.xcomp(word, parse))) {
                    this.concepts.add(concept);
                }
                if (parse.contains("csubj(" + word + "-") && !this.concepts.contains(concept = this.csubj(word, parse))) {
                    this.concepts.add(concept);
                }
                if (parse.contains("csubjpass(" + word + "-") && !this.concepts.contains(concept = this.csubjpass(word, parse))) {
                    this.concepts.add(concept);
                }
                if (parse.contains("prep_" + word + "(") && !this.concepts.contains(concept = this.preposition(word, parse))) {
                    this.concepts.add(concept);
                }
                if (parse.contains("prepc_" + word + "(") && !this.concepts.contains(concept = this.preposition_rc(word, parse))) {
                    this.concepts.add(concept);
                }
                if (parse.contains("advcl(" + word + "-")) {
                    String strcomp23 = parse.substring(parse.indexOf("advcl(" + word + "-"), parse.indexOf(")", parse.indexOf("advcl(" + word + "-")));
                    String target23 = strcomp23.substring(strcomp23.indexOf(",") + 1, strcomp23.indexOf("-", strcomp23.indexOf(","))).trim();
                    concept4 = String.valueOf(word) + " when " + target23;
                    if (!this.concepts.contains(concept4)) {
                        this.concepts.add(concept4);
                    }
                }
            }
            ++count;
        }
    }

    public ArrayList get_concepts() {
        return this.concepts;
    }

    public String nsubject(String word, String target, String parse, String tokenizer_out) {
        String concept = "";
        if (tokenizer_out.contains(String.valueOf(word) + "/NN") || tokenizer_out.contains(String.valueOf(word) + "/NNS") || tokenizer_out.contains(String.valueOf(word) + "/NNP")) {
            concept = String.valueOf(target) + " " + word;
        }
        return concept;
    }

    private void index_per_word(String parse, String tokenizer_out) {
        String[] splited = tokenizer_out.split(" ");
        int i = 0;
        while (i < splited.length) {
            String[] word_pos = splited[i].split("/");
            String word = word_pos[0];
            String pos = word_pos[1];
            this.word_index.put(i + 1, word);
            ++i;
        }
    }

    public String acomp(String word, String parse, StanfordCoreNLP pipeline) {
        stemmer stm = new stemmer();
        String concept = "";
        if (parse.contains("acomp(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("acomp(" + word + "-"), parse.indexOf(")", parse.indexOf("acomp(" + word + "-")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            String stemmed_w = stm.stem(word);
            if (stemmed_w.equalsIgnoreCase(word)) {
                stemmed_w = StanfordCoreNlpDemo.lemmatize(word, pipeline);
            }
            concept = String.valueOf(stemmed_w) + " " + target1;
        }
        return concept;
    }

    public String det(String word, String parse) {
        String concept = "";
        if (parse.contains("det(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("det(" + word + "-"), parse.indexOf(")", parse.indexOf("det(" + word + "-")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            concept = this.articles.contains(target1) || this._strTkenizedInput.contains(String.valueOf(target1) + "/DT") ? word : String.valueOf(target1) + " " + word;
        }
        return concept;
    }

    public String neg(String word, String parse) {
        String concept = "";
        if (parse.contains("neg(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("neg(" + word + "-"), parse.indexOf(")", parse.indexOf("neg(" + word + "-")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            concept = String.valueOf(target1) + " " + word;
        }
        return concept;
    }

    public String prt(String word, String parse) {
        String concept = "";
        if (parse.contains("prt(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("prt(" + word + "-"), parse.indexOf(")", parse.indexOf("prt(" + word + "-")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            concept = String.valueOf(word) + " " + target1;
        }
        return concept;
    }

    public String ccomp(String word, String parse) {
        stemmer stm = new stemmer();
        String concept = "";
        if (parse.contains("ccomp(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("ccomp(" + word + "-"), parse.indexOf(")", parse.indexOf("ccomp(" + word + "-")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            String stemmed_w = stm.stem(word);
            if (stemmed_w.equalsIgnoreCase(word)) {
                stemmed_w = StanfordCoreNlpDemo.lemmatize(word, this.pipeline);
            }
            concept = String.valueOf(stemmed_w) + " " + target1;
        }
        return concept;
    }

    private static String extract_modifier(String word, String parse) {
        String modd = "";
        if (parse.contains("amod(" + word)) {
            int k = parse.indexOf("amod(" + word) + ("amod(" + word).length();
            modd = parse.substring(parse.indexOf(",", k) + 1, parse.indexOf("-", parse.indexOf(",", k))).trim();
        } else if (parse.contains("advmod(" + word)) {
            int k = parse.indexOf("advmod(" + word) + ("advmod(" + word).length();
            modd = parse.substring(parse.indexOf(",", k) + 1, parse.indexOf("-", parse.indexOf(",", k))).trim();
        }
        return modd;
    }

    public String nsubjectpass(String word, String parse) {
        String concept = "";
        if (parse.contains("nsubjpass(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("nsubjpass(" + word + "-"), parse.indexOf(")", parse.indexOf("nsubjpass(" + word + "-")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            concept = String.valueOf(word) + " " + target1;
        }
        return concept;
    }

    public String nsubject(String word, String parse) {
        String concept = "";
        if (parse.contains("nsubj(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("nsubj(" + word + "-"), parse.indexOf(")", parse.indexOf("nsubj(" + word + "-")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            concept = parse.contains("cop(" + word + "-") ? String.valueOf(word) + " " + target1 : target1;
        }
        return concept;
    }

    public String modifiers(String word, String parse, String concept) throws Exception {
        if (concepts_extraction.hasModifier(word, parse)) {
            String target;
            String parse1 = parse;
            String modifier = "";
            if (parse1.contains("amod(" + word + "-") && this._strTkenizedInput.contains(String.valueOf(word) + "/N")) {
                String strcomp1 = parse1.substring(parse1.indexOf("amod(" + word + "-"), parse1.indexOf(")", parse1.indexOf("amod(" + word + "-")));
                target = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
                String xx = strcomp1.substring(strcomp1.indexOf("amod(" + word + "-") + ("amod(" + word + "-").length(), strcomp1.indexOf(","));
                int temp1 = parse1.indexOf("-", parse1.indexOf("amod(" + word + "-" + Integer.parseInt(xx) + ",") + ("amod(" + word + "-" + Integer.parseInt(xx) + ",").length()) + 1;
                int index2 = Integer.parseInt(parse1.substring(temp1, parse1.indexOf(")", temp1)));
                parse = parse.replace("amod(" + word + "-" + Integer.parseInt(xx) + ", " + target + "-" + index2 + ")", "");
                concept = String.valueOf(target) + " " + word;
                if (!this.concepts.contains(concept)) {
                    this.concepts.add(concept);
                }
            } else {
                if (concept.contains(" ")) {
                    return concept;
                }
                return null;
            }
            modifier = target;
            return this.modifiers(word, parse, word);
        }
        if (concept.contains(" ")) {
            return concept;
        }
        return null;
    }

    private static boolean hasModifier(String word, String parse) {
        if (parse.contains("amod(" + word + "-")) {
            return true;
        }
        return false;
    }

    public String csubjpass(String word, String parse) {
        String concept = "";
        if (parse.contains("csubjpass(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("csubjpass(" + word + "-"), parse.indexOf(")", parse.indexOf("csubjpass(" + word + "-")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            concept = String.valueOf(word) + " " + target1;
        }
        return concept;
    }

    public String csubj(String word, String parse) {
        String concept = "";
        if (parse.contains("csubj(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("csubj(" + word + "-"), parse.indexOf(")", parse.indexOf("csubj(" + word + "-")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            concept = String.valueOf(target1) + " " + word;
        }
        if (this._strTkenizedInput.contains(String.valueOf(word) + "/N")) {
            return concept;
        }
        return null;
    }

    public String preposition(String word, String parse) {
        String concept = "";
        if (parse.contains("prep_" + word + "(")) {
            String target = parse.substring(parse.indexOf("prep_" + word + "(") + ("prep_" + word + "(").length(), parse.indexOf("-", parse.indexOf("prep_" + word + "(")));
            String strcomp1 = parse.substring(parse.indexOf("prep_" + word + "("), parse.indexOf(")", parse.indexOf("prep_" + word + "(")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            concept = String.valueOf(target) + " " + word + " " + target1;
            if (concept.contains("/VB")) {
                concept = concept.replaceAll("/VB", "");
            }
            if (this._strTkenizedInput.contains(String.valueOf(target) + "/N") && this._strTkenizedInput.contains(String.valueOf(target1) + "/N")) {
                return concept;
            }
            if (this._strTkenizedInput.contains(String.valueOf(target) + "/V") && this._strTkenizedInput.contains(String.valueOf(target1) + "/V")) {
                return null;
            }
            if (this._strTkenizedInput.contains(String.valueOf(target) + "/N") || this._strTkenizedInput.contains(String.valueOf(target1) + "/N")) {
                return concept;
            }
        }
        return null;
    }

    public String preposition_rc(String word, String parse) {
        String concept = "";
        if (parse.contains("prepc_" + word + "(")) {
            String target = parse.substring(parse.indexOf("prepc_" + word + "(") + ("prepc_" + word + "(").length(), parse.indexOf("-", parse.indexOf("prepc_" + word + "(")));
            String strcomp1 = parse.substring(parse.indexOf("prepc_" + word + "("), parse.indexOf(")", parse.indexOf("prepc_" + word + "(")));
            String target1 = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            concept = String.valueOf(target) + " " + word + " " + target1;
            if (concept.contains("/VB")) {
                concept = concept.replaceAll("/VB", "");
            }
            if (this._strTkenizedInput.contains(String.valueOf(target) + "/N") && this._strTkenizedInput.contains(String.valueOf(target1) + "/N")) {
                return concept;
            }
        }
        return null;
    }

    public ArrayList dobj(String word, String parse, String _strTkenizedInput) {
        ArrayList<String> concepts = new ArrayList<String>();
        String concept = "";
        String target1 = "";
        String target = "";
        String noun = "";
        if (parse.contains("dobj(" + word + "-")) {
            String strcomp1 = parse.substring(parse.indexOf("dobj(" + word + "-"), parse.indexOf(")", parse.indexOf("dobj(" + word + "-")));
            target = strcomp1.substring(strcomp1.indexOf(",") + 1, strcomp1.indexOf("-", strcomp1.indexOf(","))).trim();
            if (parse.contains("nn(" + word)) {
                String strcomp = parse.substring(parse.indexOf("nn("), parse.indexOf(")", parse.indexOf("nn(")));
                noun = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
            }
            if (!_strTkenizedInput.contains(String.valueOf(target) + "/PRP")) {
                concept = String.valueOf(word) + " " + target;
                concepts.add(concept);
            }
        }
        return concepts;
    }

    public String xcomp(String word, String parse) {
        String concept = "";
        if (parse.contains("xcomp(" + word + "-")) {
            String strcomp = parse.substring(parse.indexOf("xcomp(" + word + "-"), parse.indexOf(")", parse.indexOf("xcomp(" + word + "-")));
            String target = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
            if (this._strTkenizedInput.contains(String.valueOf(word) + "/V") && this._strTkenizedInput.contains(String.valueOf(target) + "/N")) {
                concept = String.valueOf(word) + " " + target;
            } else if (this._strTkenizedInput.contains(String.valueOf(word) + "/JJ") && this._strTkenizedInput.contains(String.valueOf(target) + "/V")) {
                concept = String.valueOf(word) + " " + target;
            }
        }
        return concept;
    }

    public String nn(String word, String parse) {
        String concept = "";
        if (parse.contains("nn(" + word + "-")) {
            String strcomp = parse.substring(parse.indexOf("nn(" + word + "-"), parse.indexOf(")", parse.indexOf("nn(" + word + "-")));
            String noun = strcomp.substring(strcomp.indexOf(",") + 1, strcomp.indexOf("-", strcomp.indexOf(","))).trim();
            concept = String.valueOf(noun) + " " + word;
        }
        return concept;
    }

    private void loadNsubj(String parse) {
        String[] parse_split = parse.split(Pattern.quote("),"));
        int i = 0;
        while (i < parse_split.length) {
            String strcomp;
            if (parse_split[i].contains("nsubj(")) {
                strcomp = parse_split[i].substring(parse_split[i].indexOf(",", parse_split[i].indexOf("nsubj(")) + 1, parse_split[i].lastIndexOf("-"));
                this.nsubj.add(strcomp.trim());
            }
            if (parse_split[i].contains("nsubjpass(")) {
                strcomp = parse_split[i].substring(parse_split[i].indexOf(",", parse_split[i].indexOf("nsubjpass(")) + 1, parse_split[i].lastIndexOf("-"));
                this.nsubj.add(strcomp.trim());
            }
            ++i;
        }
    }
}