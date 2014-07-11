package no.simonsen.midi;

import static org.junit.Assert.*;

import org.junit.Test;

public class MidiEventTest {

	@Test
	public void testToString() {
		MidiEvent midiEvent = new MidiEvent(60, 120, 0.5, 0);
		midiEvent.toString();
		//fail("Not yet implemented");
	}

}
