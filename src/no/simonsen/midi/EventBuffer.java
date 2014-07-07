package no.simonsen.midi;

/*
 * EventBuffer should ideally buffer events from a PatternCombiner.
 * However, this responsibility is moved (for now) to MidiScheduler.
 */
public class EventBuffer extends Thread {
	MidiScheduler midiScheduler = new MidiScheduler();
	PatternCombiner patternCombiner;
	
	public EventBuffer(PatternCombiner patternCombiner) { 
		this.patternCombiner = patternCombiner;
	}
	
	public void run() {
		while (true) {
			midiScheduler.scheduleAndBlock(patternCombiner.getNextN(10));
		}
	}
}
