package no.simonsen.midi;

import java.util.Random;

/*
 * RandomPattern outputs a random value between min and max.
 */
public class RandomPattern implements ValueSupplier {
	double min;
	double max;
	Random random = new Random();
	
	public RandomPattern(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	public double nextValue() {
		return random.nextDouble() * (max - min) + min;
	}
}
