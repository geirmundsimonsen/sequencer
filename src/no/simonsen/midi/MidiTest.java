package no.simonsen.midi;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiTest {
	public static void main(String[] args) {
		try {
			Receiver receiver = MidiSystem.getReceiver();
			MidiEvent me = new MidiEvent(65, 90, 0.2);
			Runnable runnable = me.getRunnable(receiver);
			new Thread(runnable).start();
			Thread.sleep(80);
			new Thread(runnable).start();
			ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(10);
			//stpe.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
			stpe.schedule(runnable, 2000, TimeUnit.MILLISECONDS);
		} catch (MidiUnavailableException e) {
			System.out.println("MIDI Unavailable.");
		} catch (InterruptedException e) {
			System.out.println("Sleep interrupted");
		}
	}
}
