package no.simonsen.midi;

/*
 * The interface ValueSupplier specifies the function nextValue(), which is used by classes
 * like PatternCombiner.
 */
interface ValueSupplier {
	double nextValue();
}