package no.simonsen.midi;

import java.util.concurrent.CountDownLatch;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiEvent {
	int pitch;
	int velocity;
	double length; // in seconds
	double time;
	
	public MidiEvent(int pitch, int velocity, double length) {
		this.pitch = pitch;
		this.velocity = velocity;
		this.length = length;
		time = 0;
	}
	
	public MidiEvent(int pitch, int velocity, double length, double time) {
		this(pitch, velocity, length);
		this.time = time;
	}
	
	public Runnable getRunnable(Receiver midiIn, CountDownLatch cdl) {
		
		Runnable runnable = () -> {
			try {
				ShortMessage sm = new ShortMessage(ShortMessage.NOTE_ON, pitch, velocity);
				midiIn.send(sm, -1);
				Thread.sleep((long) (length * 1000));
				sm = new ShortMessage(ShortMessage.NOTE_OFF, pitch, velocity);
				midiIn.send(sm, -1);
				
				if (cdl != null) {
					cdl.countDown();
				}
				
			} catch (InvalidMidiDataException e) {
				System.out.println("Failed sending midi data.");
			} catch (InterruptedException e) {
				System.out.println("Sleep interrupted, why?");
			}
		};
		/*
		Runnable runnable = () -> {
			System.out.println("time: " + time + ", length: " + length + ", pitch: " + pitch + ", velocity: " + velocity);
		};
		*/
		return runnable;
	}
	
	public String toString() {
		return String.format("MidiEvent - time: %f, length: %f, pitch: %d, velocity: %d.", time, length, pitch, velocity);
	}
}
