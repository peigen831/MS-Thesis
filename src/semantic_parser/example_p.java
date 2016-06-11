package semantic_parser;
import java.util.ArrayList;
import semantic_parser.concept_parser;
import java.io.IOException;
public class example_p {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		concept_parser cp =new concept_parser();
		ArrayList<String> concepts=cp.get_concepts("I am going to the market to buy vegetables and some fruits.");
		for(int i=0;i<concepts.size();i++)
			System.out.println(concepts.get(i));
	}

}
