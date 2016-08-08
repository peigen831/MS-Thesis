package feature_extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import crawler.HTMLInterpreter;
import dataprocessor.DataInterpreter;
import helper.FileIO;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class ArffBuilder {
	
    ArrayList<Attribute> atts;
    ArrayList<String> classVal;
    Instances arffInstance;
    ArrayList<String> arrMood;
    
    public void initialize(){
    	atts = new ArrayList<Attribute>();
        classVal = new ArrayList<String>();
    	classVal.add("0");
    	classVal.add("1");
    	orderMood();
    }
    
    public void orderMood(){
    	arrMood = new ArrayList<String>();
    	arrMood.add(HTMLInterpreter.mood_happy);
    	arrMood.add(HTMLInterpreter.mood_sad);
    	arrMood.add(HTMLInterpreter.mood_inspired);
    	arrMood.add(HTMLInterpreter.mood_dontcare);
    	arrMood.add(HTMLInterpreter.mood_angry);
    	arrMood.add(HTMLInterpreter.mood_afraid);
    	arrMood.add(HTMLInterpreter.mood_amused);
    	arrMood.add(HTMLInterpreter.mood_annoyed);
    }
    
    public void addClassFeature(String relationName){
    	
    	for(String s: arrMood){
    		Attribute tmp =  new Attribute(s, classVal);
    		atts.add(tmp);
    	}
    	arffInstance = new Instances(relationName, atts, 0);
    }
    
    public void addNewFeature(List<String> lFeature, String relationName){
         for(String s: lFeature)
         {
        	 Attribute tmp = new Attribute(s, classVal);
        	 if(!atts.contains(tmp))
        		 atts.add(tmp);
         }
         
         arffInstance = new Instances(relationName, atts, 0);
    }
    
    public void addNewInstance(List<String> nGram){
    	double[] instanceVal = new double[arffInstance.numAttributes()];
    	
    	for(int i = 0; i < atts.size(); i++)
    	{
    		if(nGram.contains(atts.get(i).name()))
    			instanceVal[i] = 1;
    		else
    			instanceVal[i] = 0;
    	}
    	
    	arffInstance.add(new DenseInstance(1.0, instanceVal));
    }
    
    public void printData(){
    	System.out.println(arffInstance);
    }
	
	public static void main(String args[]){
	

		NgramExtractor featureExtractor = new NgramExtractor();
		
		ArffBuilder builder = new ArffBuilder();
		builder.initialize();
		
		int start = 0;
		int end = 4;
		
		String relationName = "Bigram Rappler News";
		
		for(int i = start; i < end; i++){

			String rawText = FileIO.getInstance().readText(FileIO.dirProcessed + i);
			
			String sBody = DataInterpreter.getInstance().getBody(rawText);
			
			
			if(sBody != null){
				List<String> bigram = featureExtractor.ngrams(2, sBody);
				
				builder.addNewFeature(bigram, relationName);
			}
		}

		//TODO add class label
		builder.addClassFeature(relationName);
		
		
		for(int i = start; i < end; i++){
			String rawText = FileIO.getInstance().readText(FileIO.dirProcessed + i);
			
			String sBody = DataInterpreter.getInstance().getBody(rawText);

			HashMap<String, Integer> moodMap = DataInterpreter.getInstance().getMood();
			
			if(rawText != null){
				List<String> bigram = featureExtractor.ngrams(2, rawText);
				
				builder.addNewInstance(bigram);
			}	
		}
		
		builder.printData();
		
	}
}
