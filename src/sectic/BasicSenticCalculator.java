package sectic;

import crawler.HTMLInterpreter;

public class BasicSenticCalculator {
	
	public String computePleasantness(float val){
		if(val > (0.0))
			return HTMLInterpreter.mood_happy;
		else if (val < (0.0))
			return HTMLInterpreter.mood_sad;
		else 
			return MoodConstant.NA;
}

	public String computeAttention(float val){
		if (val < (0.0))
			return HTMLInterpreter.mood_amused;
		else 
			return MoodConstant.NA;
}

	public String computeSensitivity(float val){
		if (val == (0.0))
			return MoodConstant.NA;
		else if(val > (1.0 / 3))
			return HTMLInterpreter.mood_angry;
		else if(val >= -(1.0 / 3) && val <= (1.0 / 3))
			return HTMLInterpreter.mood_annoyed;
		else if(val < -(1.0 / 3))
			return HTMLInterpreter.mood_afraid;
		return MoodConstant.NA;
}

	public String computeAptitude(float val){
		if (val < (0.0))
			return HTMLInterpreter.mood_dontcare;
		else 
			return MoodConstant.NA;
	}
}
