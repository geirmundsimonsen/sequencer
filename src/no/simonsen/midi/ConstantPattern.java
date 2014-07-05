package no.simonsen.midi;

/*
 * A pattern that outputs a single value.
 */
class ConstantPattern implements ValueSupplier {
	double value;
	
	public ConstantPattern(double value) {
		this.value = value;
	}
	
	public double nextValue() {
		return value;
	}
}
