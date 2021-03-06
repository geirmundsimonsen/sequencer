package no.simonsen.midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Receiver;
import no.simonsen.gui.ValueSupplierHost;


/*
 * PatternCombiner is responsible for generating MIDI events based on the patterns it hold.
 * This responsibility might change, and PatternCombiner would simply be a container for 
 * patterns.
 */
public class PatternCombiner implements ValueSupplierHost {
	private Receiver midiReceiver = null;
	private List<ValueSupplier> valueSuppliers = new ArrayList<ValueSupplier>();
	private Integer timeIndex = null;
	private Integer lengthIndex = null;
	private Integer pitchIndex = null;
	private Integer velocityIndex = null;
	
	public PatternCombiner() {
		midiReceiver = MidiReceivers.getDefaultReceiver();
	}
	
	public ValueSupplier getValueSupplier(String id) {
		for (ValueSupplier valueSupplier : valueSuppliers)
			if (valueSupplier.getId().equals(id))
				return valueSupplier;
		System.out.println("PatternCombiner's getValueSupplier() returned null");
		return null;
	}
	
	public void resetValueSuppliers() {
		for (ValueSupplier valueSupplier : valueSuppliers) {
			valueSupplier.reset();
		}
	}
	
	public MidiEvent getNext() {
		double time = valueSuppliers.get(timeIndex).nextValue();
		double length = valueSuppliers.get(lengthIndex).nextValue();
		double pitch = valueSuppliers.get(pitchIndex).nextValue();
		double velocity = valueSuppliers.get(velocityIndex).nextValue();
		
		return new MidiEvent((int) pitch, (int) velocity, length, time);
	}
	
	public ArrayList<MidiEvent> getNextN(int n) {
		ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();
		for (int i = 0; i < n; i++) { events.add(getNext()); }
		return events;
	}
	
	private void mapIndexToId(String id, int index) {
		if (id.equals("time")) {
			timeIndex = index;
		} else if (id.equals("length")) {
			lengthIndex = index;
		} else if (id.equals("pitch")) {
			pitchIndex = index;
		} else if (id.equals("velocity")) {
			velocityIndex = index;
		}
	}

	@Override
	public void addValueSupplier(ValueSupplier valueSupplier) {
		if (valueSupplier != null) {
			valueSuppliers.add(valueSupplier);
			mapIndexToId(valueSupplier.getId(), valueSuppliers.indexOf(valueSupplier));
		} else {
			System.out.println("PatternCombiner: Isn't any sense to add an empty valuesupplier, is it?");
		}
	}

	@Override
	public ValueSupplier removeValueSupplier(int index) {
		System.out.println("PatternCombiner's removeValueSupplier(int) isn't supported");
		return null;
	}
	
	@Override
	public void replaceValueSupplier(ValueSupplier oldSupplier, ValueSupplier newSupplier) {
		valueSuppliers.set(valueSuppliers.indexOf(oldSupplier), newSupplier);
	}
	
	public Receiver getMidiReceiver() {
		return midiReceiver;
	}
	
	public void setMidiReceiver(Receiver midiReceiver) {
		this.midiReceiver = midiReceiver;
	}
}
