package feature_extractor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataprocessor.DataInterpreter;
import helper.FileIO;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class ArffBuilder {
	
	public final static String mood_happy = "mood_happy";
	public final static String mood_sad = "mood_sad";
	public final static String mood_inspired = "mood_inspired";
	public final static String mood_dontcare = "mood_dont_care";
	public final static String mood_angry = "mood_angry";
	public final static String mood_amused = "mood_amused";
	public final static String mood_afraid = "mood_afraid";
	public final static String mood_annoyed = "mood_annoyed";
	
	
    private ArrayList<Attribute> atts;
    private ArrayList<String> classVal;
    private Instances arffInstance;
    private ArrayList<String> emoOrder;
    private int emotionThreshold = 33;
    
    public void initialize(){
    	atts = new ArrayList<Attribute>();
        classVal = new ArrayList<String>();
    	classVal.add("0");
    	classVal.add("1");
    	orderMood();
    }
    
    public void orderMood(){
    	emoOrder = new ArrayList<String>();
    	emoOrder.add(mood_happy);
    	emoOrder.add(mood_sad);
    	emoOrder.add(mood_inspired);
    	emoOrder.add(mood_dontcare);
    	emoOrder.add(mood_angry);
    	emoOrder.add(mood_afraid);
    	emoOrder.add(mood_amused);
    	emoOrder.add(mood_annoyed);
    }
    
    public void addClassFeature(String relationName){
    	
    	for(String s: emoOrder){
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
    
    public void addArticleInstance(List<String> nGram){
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
    
    public List<String> getMultiEmotion(HashMap<String, Integer> emoMap){
    	List<String> emotion = new ArrayList<String>();
    	
    	for(String s: emoOrder){
    		if(emoMap.get(s.substring(5)) >= emotionThreshold){
    			emotion.add(s);
    		}
    	}
    	return emotion;
    }
    
    public void printData(){
    	System.out.println(arffInstance);
    }
    
    public void writeArff(){
    	 try {

        	BufferedWriter writer = new BufferedWriter(new FileWriter(FileIO.dirProcessed + "/rappler.arff"));
			writer.write(arffInstance.toString());
	    	writer.flush();
	    	writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public static void main(String args[]){
	

		NgramExtractor featureExtractor = new NgramExtractor();
		
		ArffBuilder builder = new ArffBuilder();
		builder.initialize();
		
		int start = 81000;
		int end = 81050;
		
		String relationName = "Bigram Rappler News";
		
		for(int i = start; i < end; i++){

			String rawText = FileIO.getInstance().readText(FileIO.dirProcessed + i);
			
			
			
			if(rawText != null){

				String sBody = DataInterpreter.getInstance().getBody(rawText);
				List<String> bigram = featureExtractor.ngrams(2, sBody);
				
				builder.addNewFeature(bigram, relationName);
			}
		}

		builder.addClassFeature(relationName);

		for(int i = start; i < end; i++){
			String rawText = FileIO.getInstance().readText(FileIO.dirProcessed + i);
			
		
			if(rawText != null){

				String sBody = DataInterpreter.getInstance().getBody(rawText);

				HashMap<String, Integer> emoMap = DataInterpreter.getInstance().getMood();
				
				List<String> bigram = featureExtractor.ngrams(2, sBody);
				
				System.out.println(bigram.get(0));
				bigram.addAll(builder.getMultiEmotion(emoMap));
				
				builder.addArticleInstance(bigram);
			}	
		}
		builder.writeArff();
		builder.printData();
		
	}
}
