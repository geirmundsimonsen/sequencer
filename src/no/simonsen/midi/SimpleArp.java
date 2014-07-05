package no.simonsen.midi;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class SimpleArp extends Group {
	public SimpleArp() {
		Slider midiNote = new Slider();
		midiNote.setMaxHeight(700);
		midiNote.setMinHeight(700);
		midiNote.setMin(24);
		midiNote.setMax(100);
		midiNote.setBlockIncrement(1);
		midiNote.setMinorTickCount(11);
		midiNote.setShowTickMarks(true);
		midiNote.setMajorTickUnit(12);
		midiNote.setOrientation(Orientation.VERTICAL);
		midiNote.setSnapToTicks(true);
		midiNote.addEventHandler(MouseEvent.ANY, (event) -> { System.out.println(midiNote.getValue()); });
		
		ToggleButton toggleButton = new ToggleButton("Start");
		toggleButton.setTranslateX(100);
		toggleButton.setOnAction(new ArpHandler(toggleButton, midiNote));
				
		getChildren().add(toggleButton);
		getChildren().add(midiNote);
	}
}

class ArpHandler implements EventHandler<ActionEvent> {
	ScheduledThreadPoolExecutor stpe;
	ToggleButton tb;
	Slider note;
	
	public ArpHandler(ToggleButton tb, Slider note) {
		this.tb = tb;
		this.note = note;
	}
	
	public void handle(ActionEvent e) {
		if (tb.isSelected()) {
			System.out.println("on");
			stpe = new ScheduledThreadPoolExecutor(10);
			MidiEvent me = new MidiEvent((int) note.getValue(), 80, 0.3);
			Runnable runnable = me.getRunnable(MidiApp.receiver);
			stpe.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
		} else {
			stpe.shutdown();
			System.out.println("off");
		}
	}
}