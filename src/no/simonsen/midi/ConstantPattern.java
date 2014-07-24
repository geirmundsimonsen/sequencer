package no.simonsen.midi;

/**
 * A pattern that outputs a single value.
 */
public class ConstantPattern implements ValueSupplier {
	double value;
	String id;
	
	public ConstantPattern(double value, String id) {
		this.value = value;
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public double nextValue() {
		return value;
	}
	
	@Override
	public boolean hasNext() {
		return false;
	}
	
	@Override
	public void reset() { }
	
	public String toString() {
		return "" + value;
	}
}
