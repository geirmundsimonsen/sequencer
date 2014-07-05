package no.simonsen.midi;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

/*
 * MidiScheduler holds the midi in receiver, and schedules midi messages to it.
 * It also buffers the messages, since patterns are potentially infinite.
 * The buffering system isn't coded yet, therefore the dumb Thread.sleep() limiting function.
 */
class MidiScheduler {
	Receiver receiver = null;
	ScheduledThreadPoolExecutor executor;
	long unixTimeAtStart;
	
	public MidiScheduler() {
		try {
			receiver = MidiSystem.getReceiver();
		} catch (MidiUnavailableException e) {
			System.out.println("MIDI Unavailable.");
		}
		executor = new ScheduledThreadPoolExecutor(10);
		unixTimeAtStart = System.currentTimeMillis();
	}
	
	public void scheduleAndBlock(ArrayList<MidiEvent> events) {
		for (MidiEvent event : events) {
			Runnable runnable = event.getRunnable(receiver);
			executor.schedule(runnable, (long) (event.time * 1000) - (System.currentTimeMillis() - unixTimeAtStart), TimeUnit.MILLISECONDS);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
