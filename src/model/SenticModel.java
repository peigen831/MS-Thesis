package model;

import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

public class SenticModel {
	
	private Model getModel(){
		String inputFileName = "senticnet3.rdf.xml";
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }
        
        Model model = ModelFactory.createDefaultModel();
        // read the RDF/XML file
        model.read(in, "");
        
        return model;
	}
	
	private void getSentic(){
		Model model = getModel();
        
        // list the statements in the graph
        StmtIterator iter = model.listStatements();
        
        // print out the predicate, subject and object of each statement
        int i = 0;
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();         // get next statement
            Resource  subject   = stmt.getSubject();   // get the subject
            Property  predicate = stmt.getPredicate(); // get the predicate
            RDFNode   object    = stmt.getObject();    // get the object

            System.out.print(subject.getLocalName());
            System.out.print(" "+ predicate.getLocalName() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.asLiteral().getValue() + "\"");
            }
            System.out.println(" .");
            
            i++;
            if(i==20)
            	break;
        }
	}
	
	private void navigateResource(){
		Model model = getModel();
		System.out.println(model.size());
		Resource r = model.getResource("http://sentic.net/api/en/concept/baby_need");
		System.out.println(r.getLocalName());
		
		StmtIterator itr = r.listProperties();
		
		
		while(itr.hasNext()){
			Statement stmt = itr.nextStatement();
			Property p = stmt.getPredicate();
			RDFNode o = stmt.getObject();
			System.out.print(p.getLocalName());
			if (o instanceof Resource) {
                System.out.print(o.toString());
            } else {
                // object is a literal
                System.out.print("    " + o.asLiteral().getValue() + "");
            }
			System.out.println();
		}
		
		System.out.println(itr.toList().size());
	}
	
	public static void main(String args[]){
		SenticModel model = new SenticModel();
		model.navigateResource();
	}
}
