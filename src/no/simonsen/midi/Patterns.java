package no.simonsen.midi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

/*
 * A test class for pattern functionality.
 */
public class Patterns {
	public static void main(String[] args) {
		ListPattern timingPattern = new ListPattern();
		timingPattern.addValue(0);
		timingPattern.addValue(0.25);
		timingPattern.addValue(0.5);
		timingPattern.addValue(0.75);
		
		ListPattern lengthPattern = new ListPattern();
		lengthPattern.addValue(0.2);
		lengthPattern.addValue(0.1);
		
		ListPattern velocityPattern = new ListPattern();
		velocityPattern.addValue(80);
		velocityPattern.addValue(50);
		velocityPattern.addValue(40);
		velocityPattern.addValue(30);

		RandomPattern pitchPattern = new RandomPattern(60, 72, 1);
		
		PatternCombiner patternCombiner = new PatternCombiner();
		
		PatternMediator timingPatternMediator = new PatternMediator(timingPattern);
		timingPatternMediator.setTimeSensitive(1);
		PatternMediator lengthPatternMediator = new PatternMediator(lengthPattern);
		lengthPatternMediator.setRandom();
		
		PatternMediator velocityPatternMediator = new PatternMediator(velocityPattern);
				
		patternCombiner.addValueSupplier(timingPatternMediator, "time");
		patternCombiner.addValueSupplier(lengthPatternMediator, "length");
		patternCombiner.addValueSupplier(pitchPattern, "pitch");
		patternCombiner.addValueSupplier(velocityPatternMediator, "velocity");

		EventBuffer eventBuffer = new EventBuffer(patternCombiner);
		eventBuffer.start();
	}
}