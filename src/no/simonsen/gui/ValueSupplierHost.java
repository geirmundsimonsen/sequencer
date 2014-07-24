package no.simonsen.gui;

import no.simonsen.midi.ValueSupplier;

public interface ValueSupplierHost {
	public void addValueSupplier(ValueSupplier valueSupplier);
	public ValueSupplier removeValueSupplier(int index);
	public void replaceValueSupplier(ValueSupplier oldSupplier, ValueSupplier newSupplier);
}
