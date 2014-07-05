package no.simonsen.midi;

import java.util.ArrayList;

/*
 * ListPattern has a backing array of values. It doesn't implement ValueSupplier's nextValue(),
 * and cannot by itself output values. It has to be used in conjunction with PatternMediator,
 * the main reason is that we don't want state directly in patterns. This allows for the same
 * pattern to be re-used elsewhere. PatternMediator typically cycles through the values in
 * the ListPattern, with options to output its values in reverse or output them randomly.
 */
class ListPattern {
	public ArrayList<Double> values = new ArrayList<>();
	
	public void addValue(double value) {
		values.add(value);
	}
}