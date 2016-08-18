package emotion_classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import dataprocessor.DataInterpreter;
import helper.DataFormat;
import helper.FileIO;

public class ConceptExtract {
	public static void main(String args[]){
	
	SenticEmotionAnalyzer analyzer = new SenticEmotionAnalyzer();
	
	int count = 1;
	
	String sWrite = "";
	for(int i = 81000; count > 0; i++){
		String filepath = FileIO.dirProcessed +i;
		File f = new File(filepath);
		
		if(f.exists() && !f.isDirectory()) { 
			System.out.println("Start analyze");
			String linedText = FileIO.getInstance().readText(filepath);
			String[] body = DataInterpreter.getInstance().getParagraph(linedText);
			
			ArrayList<String> concept = analyzer.getAllExistConcept(body);
			
			String	s = "";
			
			for(String con: concept){
				s+=con +"\n";
			}
			
			
			count--;
			FileIO.getInstance().writeFile(FileIO.dirResult + i + " no lemmatize.CSV", s);
			
		}
	}
	}
}
