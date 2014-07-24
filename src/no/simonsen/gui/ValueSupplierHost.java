package no.simonsen.gui;

import no.simonsen.midi.ValueSupplier;

/**
 * The ValueSupplierHost is an interface for structures which contain and manipulate ValueSuppliers. Current
 * implementors of this interface is Sequence and PatternCombiner.
 */
public interface ValueSupplierHost {
	/**
	 * Adds a ValueSupplier to the end of the list of ValueSuppliers.
	 * @param valueSupplier The ValueSupplier to be addded.
	 */
	public void addValueSupplier(ValueSupplier valueSupplier);
	
	/**
	 * Removes the ValueSupplier at the specified position of the list of ValueSuppliers.
	 * @param index the index of the ValueSupplier.
	 * @return the removed ValueSupplier.
	 */
	public ValueSupplier removeValueSupplier(int index);
	
	/**
	 * Replaces one ValueSupplier for another.
	 * @param oldSupplier the ValueSupplier to be replaced
	 * @param newSupplier the ValueSupplier to be inserted
	 */
	public void replaceValueSupplier(ValueSupplier oldSupplier, ValueSupplier newSupplier);
}
