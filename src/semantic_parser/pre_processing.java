package semantic_parser;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import semantic_parser.StanfordCoreNlpDemo;

class pre_processing {
    String dependency = "";
    StanfordCoreNlpDemo stan = null;
    String pos_tags = "";
    Tokenizer tokenizer;
    static Hashtable h = null;

    public pre_processing() throws IOException {
        this.stan = new StanfordCoreNlpDemo();
    }

    private void doLoad() {
        try {
            InputStream in1 = this.getClass().getClassLoader().getResourceAsStream("files/sentic/words.ser");
            ObjectInputStream in = new ObjectInputStream(in1);
            h = (Hashtable)in.readObject();
            in.close();
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
    }

    public void processing(String input) {
        try {
            this.dependency = this.stan.corenlp(input);
            String line_stan = StanfordCoreNlpDemo.lemmatize((String)input);
            this.pos_tags = this.stan.pos_tag(line_stan);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public pre_processing(String input) {
        this.doLoad();
        this.stan = new StanfordCoreNlpDemo();
        try {
            String line_stan = StanfordCoreNlpDemo.lemmatize((String)input);
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("files/sentic/en-token.bin");
            TokenizerModel model = new TokenizerModel(in);
            this.tokenizer = new TokenizerME(model);
            String[] words = this.tokenizer.tokenize(line_stan);
            String nw_line = line_stan;
            this.pos_tags = this.stan.pos_tag(nw_line.trim());
            this.dependency = this.stan.corenlp(input);
            this.dependency = StanfordCoreNlpDemo.lemmatize_parse((String)this.dependency, (Hashtable)h, (String)this.pos_tags);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public pre_processing(String input, LexicalizedParser lp, StanfordCoreNLP pipeline) {
        this.doLoad();
        this.stan = new StanfordCoreNlpDemo(lp);
        try {
            String line_stan = StanfordCoreNlpDemo.lemmatize((String)input, (StanfordCoreNLP)pipeline);
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("files/sentic/en-token.bin");
            TokenizerModel model = new TokenizerModel(in);
            this.tokenizer = new TokenizerME(model);
            String nw_line = line_stan;
            this.pos_tags = this.stan.pos_tag(nw_line.trim(), pipeline);
            this.dependency = this.stan.corenlp(input, lp, pipeline);
            this.dependency = StanfordCoreNlpDemo.lemmatize_parse((String)this.dependency, (Hashtable)h, (String)this.pos_tags, (StanfordCoreNLP)pipeline);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get_parse_tree() {
        return this.dependency;
    }

    public String get_pos_tags() {
        return this.pos_tags;
    }
}