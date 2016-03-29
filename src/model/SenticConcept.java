package model;

import java.util.ArrayList;

public class SenticConcept {
	private String concept;
	private String description;
	private float pleasantness;
	private float attention;
	private float sensitivity;
	private float aptitude;
	
	private float polarity;
	private ArrayList<String> semantics = new ArrayList<String>();
	
	
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getPleasantness() {
		return pleasantness;
	}
	public void setPleasantness(float pleasantness) {
		this.pleasantness = pleasantness;
	}
	public float getAttention() {
		return attention;
	}
	public void setAttention(float attention) {
		this.attention = attention;
	}
	public float getSensitivity() {
		return sensitivity;
	}
	public void setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
	}
	public float getAptitude() {
		return aptitude;
	}
	public void setAptitude(float aptitude) {
		this.aptitude = aptitude;
	}
	public float getPolarity() {
		return polarity;
	}
	public void setPolarity(float polarity) {
		this.polarity = polarity;
	}
	public ArrayList<String> getSemantics() {
		return semantics;
	}

	public void addSemantics(String semantic){
		semantics.add(semantic);
	}
	
	public void checkComplete(){
//		private String concept;
//		private String description;
//		private float pleasantness;
//		private float attention;
//		private float sensitivity;
//		private float aptitude;
//		
//		private float polarity;
//		private ArrayList<String> semantics = new ArrayList<String>();
//		if(concept==null || description ==null || pleasantness == null || attention == null ||sensitivity == null)
	}
	
	public static void main(String args[]){
		SenticConcept c=  new SenticConcept();
		c.checkComplete();
	}
}
