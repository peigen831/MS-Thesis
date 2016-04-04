package model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

import crawler.HTMLInterpreter;

public class SenticModel {
	
	public static final String api_concept = "apitext";
	public static final String api_semantics = "apisemantics";
	public static final String api_pleasantness = "apipleasantness";
	public static final String api_aptitude = "apiaptitude";
	public static final String api_attention = "apiattention";
	public static final String api_sensitivity = "apisensitivity";
	 
	private String modelPath = "senticnet3.rdf.xml";
	private SenticConcept[] arrConcept = new SenticConcept[30000];
	
	public void setModelPath(String path){
		modelPath = path;
	}
	
	public SenticModel(){
		loadConcept();
	}
	
	
	public Model getModel(){
        InputStream in = FileManager.get().open(modelPath);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + modelPath + " not found");
        }
        
        Model model = ModelFactory.createDefaultModel();
        // read the RDF/XML file
        model.read(in, "");
        
        return model;
	}
	
//	public SenticConcept[] getSenticConcept(){
//		if(arrConcept[0]==null)
//			loadConcept();
//		return arrConcept;
//	}
	
	//Add another function for multiple words
	public String[] getSenticValue(String word){
		String[] result = null;
		
		for(SenticConcept c: arrConcept){
			if(word.equals(c.getConcept()))
			{
				result = new String[4];
				result[0] = computePleasantness(c.getPleasantness());
				result[1] = computeAttention(c.getAttention());
				result[2] = computeSensitivity(c.getSensitivity());
				result[3] = computeAptitude(c.getAptitude());
				break;
			}
		}
		return result;
	}
	
	public String computePleasantness(float val){
		if(val > 0)
			return HTMLInterpreter.mood_happy;
		else if (val < 0)
			return HTMLInterpreter.mood_sad;
		return null;
	}
	
	public String computeAttention(float val){
//		if(val > 0)
//			return HTMLInterpreter.mood_happy;
		if (val < 0)
			return HTMLInterpreter.mood_amused;
		return null;
	}
	
	public String computeSensitivity(float val){
		if(val > (1.0/3))
			return HTMLInterpreter.mood_angry;
		else if (val >= -(1.0/3) && val <= (1.0/3) && val!=0)
			return HTMLInterpreter.mood_annoyed;
		else if (val < -(1.0/3))
			return HTMLInterpreter.mood_afraid;
		return null;
	}
	
	public String computeAptitude(float val){
		if (val < 0)
			return HTMLInterpreter.mood_dontcare;
		return null;
	}
	
	public void loadConcept(){
		Model model = getModel();
        
        // list the statements in the graph
        StmtIterator iter = model.listStatements();
        
        // print out the predicate, subject and object of each statement
        String curConcept = "";
        SenticConcept concept = new SenticConcept();
        int i = 0;
        while (iter.hasNext()) {
        	
            Statement stmt      = iter.nextStatement();
            Resource  subject   = stmt.getSubject();
            Property  predicate = stmt.getPredicate();
            RDFNode   object    = stmt.getObject();
            
            if(!curConcept.equals(subject.getLocalName()))
            {
            	if(!curConcept.equals(""))
            	{
            		arrConcept[i] = concept;
            		concept = new SenticConcept();
            		i++;
            	}
            	concept.setDescription(subject.getLocalName());
            	curConcept = subject.getLocalName();
            }

            switch(predicate.getLocalName()){
            	case api_concept: concept.setConcept(object.asLiteral().getString()); break;
            	case api_pleasantness: concept.setPleasantness(object.asLiteral().getFloat()); break;
            	case api_attention: concept.setAttention(object.asLiteral().getFloat()); break;
            	case api_aptitude: concept.setAptitude(object.asLiteral().getFloat()); break;
            	case api_sensitivity: concept.setSensitivity(object.asLiteral().getFloat()); break;
            	case api_semantics: concept.addSemantics(object.toString()); break;
            }
        }
        arrConcept[i] = concept;
	}
	
	public void printAll(){
		int i =0;
		for(SenticConcept c: arrConcept ){
			System.out.println(i+" "+c.getConcept());
			i++;
		}
	}

	public static void main(String args[]){
		SenticModel model = new SenticModel();
		float a = (float) 0.33;
		float b = (float) -0.33;
		float c = (float) 0.67;
		float d = (float) -0.67;
		float e = 0;
		
		System.out.println(model.computeSensitivity(a));
		System.out.println(model.computeSensitivity(b));
		System.out.println(model.computeSensitivity(c));
		System.out.println(model.computeSensitivity(d));
		System.out.println(model.computeSensitivity(e));
	}
}
