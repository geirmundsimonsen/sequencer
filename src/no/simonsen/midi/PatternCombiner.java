package no.simonsen.midi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * PatternCombiner is responsible for generating MIDI events based on the patterns it hold.
 * This responsibility might change, and PatternCombiner would simply be a container for 
 * patterns.
 */
public class PatternCombiner {
	private Map<String, ValueSupplier> valueSuppliers = new HashMap<String, ValueSupplier>();

	public void addValueSupplier(ValueSupplier valueSupplier, String id) {
		valueSuppliers.put(id, valueSupplier);
	}
	
	public MidiEvent getNext() {
		double time = valueSuppliers.get("time").nextValue();
		double length = valueSuppliers.get("length").nextValue();
		double pitch = valueSuppliers.get("pitch").nextValue();
		double velocity = valueSuppliers.get("velocity").nextValue();
		
		return new MidiEvent((int) pitch, (int) velocity, length, time);
	}
	
	public ArrayList<MidiEvent> getNextN(int n) {
		ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();
		for (int i = 0; i < n; i++) { events.add(getNext()); }
		return events;
	}
}
