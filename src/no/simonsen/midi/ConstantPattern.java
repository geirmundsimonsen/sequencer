package no.simonsen.midi;

/*
 * A pattern that outputs a single value.
 */
public class ConstantPattern implements ValueSupplier {
	double value;
	String id;
	
	public ConstantPattern(double value, String id) {
		this.value = value;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public double nextValue() {
		return value;
	}
	
	public boolean hasNext() {
		return false;
	}
	
	public void reset() { }
	
	public String toString() {
		return "" + value;
	}
}
