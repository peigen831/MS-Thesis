package Sentic;

import helper.FileIO;

public class SenticCalculator {
	
	private static SenticCalculator instance = null;
	
	public static SenticCalculator getInstance(){
		if(instance == null)
			instance = new SenticCalculator();
		return instance;
	}
	
	public String computePleasantness(float val){
		if(val > (float)(2.0 /3))
			return SenticConstant.pleasantness_ecstacy;
		else if (val > (float)(1.0 /3) && val <= (float)(2.0 /3))
			return SenticConstant.pleasantness_joy;
		else if (val > (float)(0.0) && val <= (float)(1.0 /3))
			return SenticConstant.pleasantness_serenity;
		else if (val >= (float)-(1.0/ 3) && val < (float)(0.0))
			return SenticConstant.pleasantness_pensiveness;
		else if (val >= (float)-(2.0 /3) && val < (float)-(1.0 /3))
			return SenticConstant.pleasantness_sadness;
		else if(val < (float)-(2.0 /3))
			return SenticConstant.pleasantness_grief;
		else
			return SenticConstant.NA;
}

	public String computeAttention(float val){
		if(val > (float)(2.0 /3))
			return SenticConstant.attention_vigilance;
		else if (val > (float)(1.0 /3) && val <= (float)(2.0 /3))
			return SenticConstant.attention_anticipation;
		else if (val > (float)(0.0) && val <= (float)(1.0 /3))
			return SenticConstant.attention_interest;
		else if (val >= (float)-(1.0/ 3) && val < (float)(0.0))
			return SenticConstant.attention_distraction;
		else if (val >= (float)-(2.0 /3) && val < (float)-(1.0 /3))
			return SenticConstant.attention_surprise;
		else if(val < (float)-(2.0 /3))
			return SenticConstant.attention_amazement;
		else
			return SenticConstant.NA;
}

	public String computeSensitivity(float val){
		if(val > (float)(2.0 /3))
			return SenticConstant.sensitivity_rage;
		else if (val > (float)(1.0 /3) && val <= (float)(2.0 /3))
			return SenticConstant.sensitivity_anger;
		else if (val > (float)(0.0) && val <= (float)(1.0 /3))
			return SenticConstant.sensitivity_annoyance;
		else if (val >= (float)-(1.0/ 3) && val < (float)(0.0))
			return SenticConstant.sensitivity_apprehension;
		else if (val >= (float)-(2.0 /3) && val < (float)-(1.0 /3))
			return SenticConstant.sensitivity_fear;
		else if(val < (float)-(2.0 /3))
			return SenticConstant.sensitivity_terror;
		else
			return SenticConstant.NA;
}

	public String computeAptitude(float val){
		if(val > (float)(2.0 /3))
			return SenticConstant.aptitude_admiration;
		else if (val > (float)(1.0 /3) && val <= (float)(2.0 /3))
			return SenticConstant.aptitude_trust;
		else if (val > (float)(0.0) && val <= (float)(1.0 /3))
			return SenticConstant.aptitude_acceptance;
		else if (val >= (float)-(1.0/ 3) && val < (float)(0.0))
			return SenticConstant.aptitude_boredom;
		else if (val >= (float)-(2.0 /3) && val < (float)-(1.0 /3))
			return SenticConstant.aptitude_disgust;
		else if(val < (float)-(2.0 /3))
			return SenticConstant.aptitude_loathing;
		else
			return SenticConstant.NA;
	}
}
