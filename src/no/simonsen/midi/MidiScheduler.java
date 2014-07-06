package no.simonsen.midi;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
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
	
	/*
	 * If the first midi event in each batch gets a lock - that is unlocked when that
	 * event ends..?
	 * 
	 * This would work assuming that midi events are fired in chronological order.
	 * Then we'll have a buffer as big as events.size().
	 */
	public void scheduleAndBlock(ArrayList<MidiEvent> events) {
		CountDownLatch cdl = new CountDownLatch(1);
		
		Runnable runnable = events.get(0).getRunnable(receiver, cdl);
		executor.schedule(runnable, (long) (events.get(0).time * 1000) - (System.currentTimeMillis() - unixTimeAtStart), TimeUnit.MILLISECONDS);
		
		for (int i = 1; i < events.size(); i++) {
			runnable = events.get(i).getRunnable(receiver, null);
			executor.schedule(runnable, (long) (events.get(i).time * 1000) - (System.currentTimeMillis() - unixTimeAtStart), TimeUnit.MILLISECONDS);
		}
		
		try {
			cdl.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		/*
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}
}
