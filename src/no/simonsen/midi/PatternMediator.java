package no.simonsen.midi;

import java.util.Random;

/*
 * PatternMediator holds a pattern, and logic that decides what value should be output
 * in response to getValue(). Right now it is particularly tied to list patterns, it makes
 * no sense to have this class be responsible for the output of generator patterns.
 */
class PatternMediator implements ValueSupplier {
	private ListPattern pattern;
	private boolean normal = true;
	private boolean reversed = false;
	private boolean random = false;
	private boolean timeSensitive = false;
	private double patternLength;
	private Random randomGen;
	private int index = 0;
	
	public PatternMediator(ListPattern pattern) {
		this.pattern = pattern;
	}
	
	public double nextValue() {
		double value = 0;
		
		if (normal) {
			value = pattern.values.get(index % pattern.values.size());
			index++;
		} else if (reversed) {
			value = pattern.values.get((pattern.values.size() - 1) - index % pattern.values.size());
			index++;
		} else if (random) {
			value = pattern.values.get(randomGen.nextInt(pattern.values.size()));
		} else if (timeSensitive) {
			value = pattern.values.get(index % pattern.values.size()) + 
					index / pattern.values.size() * patternLength;
			index++;
		}
		
		return value;
	}
	
	public void setNormal() { normal = true; reversed = false; random = false; timeSensitive = false; }
	public void setReversed() { normal = false; reversed = true; random = false; timeSensitive = false; }
	public void setRandom() { normal = false; reversed = false; random = true; timeSensitive = false; randomGen = new Random(); }
	public void setTimeSensitive(double length) { 
		normal = false; reversed = false; random = false; timeSensitive = true; 
		patternLength = length;
	}
	
	public int getPatternSize() { return pattern.values.size(); }
}