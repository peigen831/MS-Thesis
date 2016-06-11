/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  org.tartarus.snowball.ext.PorterStemmer
 */
package semantic_parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Hashtable;
import org.tartarus.snowball.ext.PorterStemmer;

public class stemmer {
    static HashMap<String, String> h = null;

    public stemmer() {
        h = this.doLoad("files/sentic/HTSentiWordnet.ser");
    }

    HashMap doLoad(String f) {
        HashMap h = null;
        try {
            InputStream in1 = this.getClass().getClassLoader().getResourceAsStream("files/sentic/HTSentiWordnet.ser");
            ObjectInputStream in = new ObjectInputStream(in1);
            h = (HashMap)in.readObject();
            in.close();
            in1.close();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return h;
    }

    public String nw_stem(String tokenizer_out, HashMap<String, String> h, String parse) {
        PorterStemmer obj = new PorterStemmer();
        String nw_sen = "";
        String[] splited = tokenizer_out.split(" ");
        int i = 0;
        while (i < splited.length) {
            String word = splited[i].split("/")[0];
            if (h.containsKey(word) || parse.contains("acomp(" + word + "-") || parse.contains("prt(" + word + "-")) {
                nw_sen = String.valueOf(nw_sen) + word + "/" + splited[i].split("/")[1] + " ";
            } else if (word.equals("does")) {
                nw_sen = String.valueOf(nw_sen) + word + "/" + splited[i].split("/")[1] + " ";
            } else {
                obj.setCurrent(word);
                obj.stem();
                nw_sen = h.containsKey(obj.getCurrent()) ? String.valueOf(nw_sen) + obj.getCurrent() + "/" + splited[i].split("/")[1] + " " : String.valueOf(nw_sen) + word + "/" + splited[i].split("/")[1] + " ";
            }
            ++i;
        }
        return nw_sen.trim();
    }

    public String stem(String tokenizer_out, HashMap<String, String> h, String parse) {
        PorterStemmer obj = new PorterStemmer();
        String nw_sen = "";
        String[] splited = tokenizer_out.split(" ");
        int i = 0;
        while (i < splited.length) {
            String word = splited[i].split("/")[0];
            if (h.containsKey(word) || parse.contains("acomp(" + word + "-")) {
                nw_sen = String.valueOf(nw_sen) + word + " ";
            } else if (word.equals("does")) {
                nw_sen = String.valueOf(nw_sen) + word + " ";
            } else {
                obj.setCurrent(word);
                obj.stem();
                System.out.println(obj.getCurrent());
                nw_sen = h.containsKey(obj.getCurrent()) ? String.valueOf(nw_sen) + obj.getCurrent() + " " : String.valueOf(nw_sen) + word + " ";
            }
            ++i;
        }
        return nw_sen.trim();
    }

    public String stem(String word, Hashtable<String, String> h2) {
        PorterStemmer obj = new PorterStemmer();
        obj.setCurrent(word);
        obj.stem();
        h2.remove("bui");
        h2.remove("thi");
        h2.remove("veri");
        if (h2.containsKey(obj.getCurrent().toLowerCase())) {
            return obj.getCurrent();
        }
        return word;
    }

    public String stem(String word) {
        PorterStemmer obj = new PorterStemmer();
        if (h.containsKey(word)) {
            return word;
        }
        if (word.equals("does")) {
            return word;
        }
        obj.setCurrent(word);
        obj.stem();
        if (h.containsKey(obj.getCurrent())) {
            return obj.getCurrent();
        }
        return word;
    }
}