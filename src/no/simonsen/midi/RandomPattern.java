package no.simonsen.midi;

import java.util.Random;

/*
 * RandomPattern outputs a random value between min and max.
 */
public class RandomPattern implements ValueSupplier {
	int counter;
	int length;
	double min;
	double max;
	Random random = new Random();
	
	public RandomPattern(double min, double max, int length) {
		this.min = min;
		this.max = max;
		this.length = length;
		counter = 0;
	}
	
	public double nextValue() {
		counter++;
		return random.nextDouble() * (max - min) + min;
	}
	
	public boolean hasNext() {
		return counter < length;
	}
	
	public void reset() {
		counter = 0;
	}
}
