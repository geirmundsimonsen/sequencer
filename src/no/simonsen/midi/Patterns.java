package no.simonsen.midi;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

public class Patterns {
	public static void main(String[] args) {
		TimingPattern timingPattern = new TimingPattern();
		timingPattern.addValue(0);
		timingPattern.addValue(0.5);
		timingPattern.addValue(0.75);
		
		LengthPattern lengthPattern = new LengthPattern();
		lengthPattern.addValue(0.25);
		lengthPattern.addValue(0.1);
		lengthPattern.addValue(0.1);
		
		VelocityPattern velocityPattern = new VelocityPattern();
		velocityPattern.addValue(80);
		velocityPattern.addValue(50);
		velocityPattern.addValue(40);
		velocityPattern.addValue(30);

		PitchPattern pitchPattern = new PitchPattern();
		pitchPattern.addValue(63); // Eb4
		pitchPattern.addValue(60); // C4
		
		PatternCombiner patternCombiner = new PatternCombiner(0, 1);
		/*
		patternCombiner.addPattern(timingPattern);
		patternCombiner.addPattern(lengthPattern);
		patternCombiner.addPattern(velocityPattern);
		patternCombiner.addPattern(pitchPattern);
		*/
		PatternMediator timingPatternMediator = new PatternMediator(timingPattern, "time");
		patternCombiner.addPatternMediator(timingPatternMediator);
		PatternMediator lengthPatternMediator = new PatternMediator(lengthPattern, "length");
		patternCombiner.addPatternMediator(lengthPatternMediator);
		PatternMediator pitchPatternMediator = new PatternMediator(pitchPattern, "pitch");
		pitchPatternMediator.setRandom();
		patternCombiner.addPatternMediator(pitchPatternMediator);
		PatternMediator velocityPatternMediator = new PatternMediator(velocityPattern, "velocity");
		velocityPatternMediator.setRandom();
		patternCombiner.addPatternMediator(velocityPatternMediator);
		
		//for (int i = 0; i < 40; i++) patternCombiner.printNext();
		
		EventBuffer eventBuffer = new EventBuffer(patternCombiner);
		eventBuffer.start();
	}
}

class Pattern {
	public ArrayList<Double> values = new ArrayList<>();
	
	public void addValue(double value) {
		values.add(value);
	}
}

class TimingPattern extends Pattern { }

class LengthPattern extends Pattern { }

class VelocityPattern extends Pattern { }

class PitchPattern extends Pattern { }

/*
class PatternCombiner {
	private ArrayList<Pattern> patterns = new ArrayList<Pattern>();
	private int index = 0;
	private double start;
	private double end;
	private double patternLength; // what role should pattern length have?
	private double repetitions = Double.POSITIVE_INFINITY; // defaults to infinity.
	
	public PatternCombiner(double start, double end) {
		this.start = start;
		this.end = end;
		patternLength = end - start;
	}

	public void addPattern(Pattern pattern) {
		patterns.add(pattern);
	}
	
	
	public void printNext() { 
		for (Pattern pattern : patterns) {
			if (pattern instanceof TimingPattern)
				System.out.print(", Time: " + (pattern.values.get(index % pattern.values.size()) + 
						(index / pattern.values.size()) * patternLength));
			else if (pattern instanceof LengthPattern)
				System.out.print(", Length: " + pattern.values.get(index % pattern.values.size()));
			else if (pattern instanceof PitchPattern)
				System.out.print(", Pitch: " + pattern.values.get(index % pattern.values.size()));
			else if (pattern instanceof VelocityPattern)
				System.out.print(", Velocity: " + pattern.values.get(index % pattern.values.size()));
		}
		System.out.println();
		index++;
	}
	
	
	// we basically just want to call getValue() on a pattern mediator and make it spit out the values, whether
	// they be normal, reversed, or random.
	public MidiEvent getNext() {
		double time = 0, length = 0, pitch = 0, velocity = 0;
		for (Pattern pattern : patterns) {
			if (pattern instanceof TimingPattern)
				time = pattern.values.get(index % pattern.values.size()) + 
					(index / pattern.values.size()) * patternLength;
			else if (pattern instanceof LengthPattern)
				length = pattern.values.get(index % pattern.values.size());
			else if (pattern instanceof PitchPattern)
				pitch = pattern.values.get(index % pattern.values.size());
			else if (pattern instanceof VelocityPattern)
				velocity = pattern.values.get(index % pattern.values.size());
		}
		index++;
		return new MidiEvent((int) pitch, (int) velocity, length, time);
	}
	
	public ArrayList<MidiEvent> getNextN(int n) {
		ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();
		for (int i = 0; i < n; i++) { events.add(getNext()); }
		return events;
	}
}
*/

class PatternCombiner {
	private ArrayList<PatternMediator> patternMediators = new ArrayList<PatternMediator>();
	private int index = 0;
	private double start;
	private double end;
	private double patternLength; // what role should pattern length have?
	private double repetitions = Double.POSITIVE_INFINITY; // defaults to infinity.
	
	public PatternCombiner(double start, double end) {
		this.start = start;
		this.end = end;
		patternLength = end - start;
	}

	public void addPatternMediator(PatternMediator patternMediator) {
		patternMediators.add(patternMediator);
	}
	
	public MidiEvent getNext() {
		double time = 0, length = 0, pitch = 0, velocity = 0;
		for (PatternMediator patternMediator : patternMediators) {
			if (patternMediator.getId().equals("time"))
				time = patternMediator.getValue() + 
					(index / patternMediator.getPatternSize()) * patternLength;
			else if (patternMediator.getId().equals("length"))
				length = patternMediator.getValue();
			else if (patternMediator.getId().equals("pitch"))
				pitch = patternMediator.getValue();
			else if (patternMediator.getId().equals("velocity"))
				velocity = patternMediator.getValue();
		}
		index++;
		return new MidiEvent((int) pitch, (int) velocity, length, time);
	}
	
	public ArrayList<MidiEvent> getNextN(int n) {
		ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();
		for (int i = 0; i < n; i++) { events.add(getNext()); }
		return events;
	}
}

class EventBuffer extends Thread {
	MidiScheduler midiScheduler = new MidiScheduler();
	//int bufferSize;
	PatternCombiner patternCombiner;
	//LinkedBlockingQueue<MidiEvent> events = new LinkedBlockingQueue<MidiEvent>();
	
	// small buffersizes would make patterns more reactive to change.
	public EventBuffer(PatternCombiner patternCombiner) { 
		//this.bufferSize = bufferSize;
		this.patternCombiner = patternCombiner;
	}
	
	public void run() {
		while (true) {
			midiScheduler.scheduleAndBlock(patternCombiner.getNextN(10));
		}
	}
}

// Let's include a comment that git will pick up.
// Changing file again after we've added it to git. 
// is it staged? 
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class PatternMediator {
	private Pattern pattern;
	private String id;
	private boolean normal = true;
	private boolean reversed = false;
	private boolean random = false;
	private Random randomGen;
	private int index = 0;
	
	public PatternMediator(Pattern pattern, String id) {
		this.pattern = pattern;
		this.id = id;
	}
	
	public double getValue() {
		double value = 0;
		
		if (normal) {
			value = pattern.values.get(index % pattern.values.size());
			index++;
		} else if (reversed) {
			value = pattern.values.get((pattern.values.size() - 1) - index % pattern.values.size());
			index++;
		} else if (random) {
			value = pattern.values.get(randomGen.nextInt(pattern.values.size()));
		}
		
		return value;
	}
	
	public void setNormal() { normal = true; reversed = false; random = false; }
	public void setReversed() { normal = false; reversed = true; random = false; }
	public void setRandom() { normal = false; reversed = false; random = true; randomGen = new Random(); }
	
	public String getId() { return id; }
	public int getPatternSize() { return pattern.values.size(); }
}