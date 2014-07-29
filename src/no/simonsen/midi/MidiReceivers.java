package no.simonsen.midi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

/**
 * The class MidiReceivers will attempt to load all possible MIDI receivers, and make them available for the
 * program.
 */
public class MidiReceivers {
	private static Map<String, Receiver> receivers = new HashMap<>();
	private static String defaultReceiver = null;
	
	public static void init() {
		MidiDevice.Info[] midiDevices = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info info : midiDevices) {
			try {
				MidiDevice midiDevice = MidiSystem.getMidiDevice(info);
				if (midiDevice.getMaxReceivers() != 0) { // -1 means unlimited receivers.
					receivers.put(info.getDescription(), midiDevice.getReceiver());
					midiDevice.open();
				}
			} catch (MidiUnavailableException e) {
				System.out.println("Couldn't load this midi device: " + info.getDescription());
			}
		}
		
		for (Map.Entry<String, Receiver> entry : receivers.entrySet()) {
			if (defaultReceiver == null) 
				defaultReceiver = entry.getKey();
			System.out.println("Loaded MIDI receiver: " + entry.getKey());
		}
		
		System.out.println("Default MIDI receiver is: " + defaultReceiver);
	}
	
	public static List<String> getReceiverNames() {
		List<String> names = new ArrayList<>();
		receivers.forEach((string, receiver) -> { names.add(string); });
		return names;
	}
	
	public static Receiver getReceiver(String name) {
		return receivers.get(name);
	}
	
	public static Receiver getDefaultReceiver() {
		return receivers.get(defaultReceiver);
	}
	
	public static String getDefaultReceiverName() {
		return defaultReceiver;
	}
	
	/**
	 * Sets the default MIDI receiver. Call MidiReceivers.getReceiverNames() to obtain the list of possible 
	 * receivers.
	 * @param name receiver name
	 */
	public static void setDefaultReceiver(String name) {
		defaultReceiver = name;
	}
}
