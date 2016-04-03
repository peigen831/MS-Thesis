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
		model.loadConcept();
		model.printAll();
	}
}
