package no.simonsen.midi;

/*
 * The interface ValueSupplier specifies the function nextValue(), which is used by classes
 * like PatternCombiner.
 */
public interface ValueSupplier {
	public String getId();
	public double nextValue();
	public boolean hasNext();
	public void reset();
}