package no.simonsen.midi;

import java.util.ArrayList;

/*
 * ListPattern has a backing array of values. It doesn't implement ValueSupplier's nextValue(),
 * and cannot by itself output values. It has to be used in conjunction with PatternMediator,
 * the main reason is that we don't want state directly in patterns. This allows for the same
 * pattern to be re-used elsewhere. PatternMediator typically cycles through the values in
 * the ListPattern, with options to output its values in reverse or output them randomly.
 */
public class ListPattern {
	public ArrayList<Double> values = new ArrayList<>();
	
	public void addValue(double value) {
		values.add(value);
	}
	
	public void addValue(int index, double value) {
		values.add(index, value);
	}
	
	public void setValue(int index, double value) {
		values.set(index, value);
	}
	
	public Double removeValue(int index) {
		return values.remove(index);
	}
}