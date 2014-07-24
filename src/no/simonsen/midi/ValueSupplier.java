package no.simonsen.midi;

/**
 * The interface ValueSupplier is implemented by patterns which are expected to return a stream of
 * double values. As of now, Sequence, ConstantPattern, SeriesPattern and RandomPattern are implementors
 * of this interface.
 */
public interface ValueSupplier {
	/**
	 * The ValueSuppliers id, if applicable.
	 * @return
	 */
	public String getId();
	
	/**
	 * Output the next value of the pattern, as defined by the implementor of the interface.
	 * @return the next value.
	 */
	public double nextValue();
	
	/**
	 * Checks whether a ValueSupplier has a next value. If not, the stream has ended, and the
	 * ValueSupplier should be reset.
	 * @return
	 */
	public boolean hasNext();
	
	/**
	 * Resets the ValueSupplier to its initial state.
	 */
	public void reset();
}