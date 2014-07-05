package no.simonsen.midi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MidiApp extends Application {
	public static Receiver receiver = null;

	public static void main(String[] args) {
		try { receiver = MidiSystem.getReceiver(); } catch (MidiUnavailableException e) { System.out.println("MIDI Receiver failed to init!"); }
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		
		SimpleArp simpleArp = new SimpleArp();
		root.getChildren().add(simpleArp);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
