package sectic;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

public class ConceptLoader {
	
	private String modelPath = "senticnet3.rdf.xml";
	private Concept[] arrConcept = new Concept[30000];
	
	private static ConceptLoader instance = null;
	
	public static ConceptLoader getInstance(){
		if(instance == null)
			instance = new ConceptLoader();
		return instance;
	}
	
	
	public void setModelPath(String path){
		modelPath = path;
	}
	
	public ConceptLoader(){
		loadConcept();
	}
	
	public Concept[] getConcept(){
		return this.arrConcept;
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
	
	//Add another function for multiple words
	public String[] getBasicSenticValue(String word){
		String[] result = null;
		RangeSpecificCalculator_noInspired calcu = new RangeSpecificCalculator_noInspired();
		
		for(Concept c: arrConcept){
			if(word.equals(c.getConcept()))
			{
				result = new String[4];
				result[0] = calcu.computePleasantness(c.getPleasantness());
				result[1] = calcu.computeAttention(c.getAttention());
				result[2] = calcu.computeSensitivity(c.getSensitivity());
				result[3] = calcu.computeAptitude(c.getAptitude());
				break;
			}
		}
		return result;
	}
	
	public String[] getBasicSenticValue(Concept concept){
		String[] result = null;
//		RangeSpecificCalculator_noInspired calcu = new RangeSpecificCalculator_noInspired();
//		RangeSpecificCalculator_withInspired calcu = new RangeSpecificCalculator_withInspired();
		ValueSpecificCalculator calcu = new ValueSpecificCalculator();
		
		result = new String[4];
		result[0] = calcu.computePleasantness(concept.getPleasantness());
		result[1] = calcu.computeAttention(concept.getAttention());
		result[2] = calcu.computeSensitivity(concept.getSensitivity());
		result[3] = calcu.computeAptitude(concept.getAptitude());
		
		return result;
	}
	
	public String[] getSenticValue(String word){
		String[] result = null;
		
		for(Concept c: arrConcept){
			if(word.equals(c.getConcept()))
			{
				result = new String[4];
				result[0] = SenticCalculator.getInstance().computePleasantness(c.getPleasantness());
				result[1] = SenticCalculator.getInstance().computeAttention(c.getAttention());
				result[2] = SenticCalculator.getInstance().computeSensitivity(c.getSensitivity());
				result[3] = SenticCalculator.getInstance().computeAptitude(c.getAptitude());
				break;
			}
		}
		return result;
	}
	
//	
//	public String computePleasantness(float val){
//		if(val > 0)
//			return HTMLInterpreter.mood_happy;
//		else if (val < 0)
//			return HTMLInterpreter.mood_sad;
//		return null;
//	}
//	
//	public String computeAttention(float val){
////		if(val > 0)
////			return HTMLInterpreter.mood_happy;
//		if (val < 0)
//			return HTMLInterpreter.mood_amused;
//		return null;
//	}
//	
//	public String computeSensitivity(float val){
//		if(val > (1.0/3))
//			return HTMLInterpreter.mood_angry;
//		else if (val >= -(1.0/3) && val <= (1.0/3) && val!=0)
//			return HTMLInterpreter.mood_annoyed;
//		else if (val < -(1.0/3))
//			return HTMLInterpreter.mood_afraid;
//		return null;
//	}
//	
//	public String computeAptitude(float val){
//		if (val < 0)
//			return HTMLInterpreter.mood_dontcare;
//		return null;
//	}
	
	public void loadConcept(){
		Model model = getModel();
        
        // list the statements in the graph
        StmtIterator iter = model.listStatements();
        
        // print out the predicate, subject and object of each statement
        String curConcept = "";
        Concept concept = new Concept();
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
            		concept = new Concept();
            		i++;
            	}
            	concept.setDescription(subject.getLocalName());
            	curConcept = subject.getLocalName();
            }

            switch(predicate.getLocalName()){
            	case SenticConstant.api_concept: concept.setConcept(object.asLiteral().getString()); break;
            	case SenticConstant.api_pleasantness: concept.setPleasantness(object.asLiteral().getFloat()); break;
            	case SenticConstant.api_attention: concept.setAttention(object.asLiteral().getFloat()); break;
            	case SenticConstant.api_aptitude: concept.setAptitude(object.asLiteral().getFloat()); break;
            	case SenticConstant.api_sensitivity: concept.setSensitivity(object.asLiteral().getFloat()); break;
            	case SenticConstant.api_semantics: concept.addSemantics(object.toString()); break;
            }
        }
        arrConcept[i] = concept;
	}
	
	public void printAll(){
		int i =0;
		for(Concept c: arrConcept ){
			System.out.println(i+" "+c.getConcept());
			i++;
		}
	}

	public static void main(String args[]){
		ConceptLoader model = new ConceptLoader();
		model.printAll();
	}
}
