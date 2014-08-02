package no.simonsen.gui;

import no.simonsen.gui.style.BackgroundPool;
import no.simonsen.midi.MidiReceivers;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TestUI extends Application {
	public static Font labelFont;
	
	public static void main(String[] args) {
		BackgroundPool.init();
		//MidiReceivers.init();
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		labelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/TypewriterScribbled.ttf"), 10);
		if (labelFont == null) {
			System.out.println("Couldn't load font.");
			System.exit(1);
		}
				
		Group root = new Group();
		
		//PatternCombinerUI pcUI = new PatternCombinerUI();
		//root.getChildren().add(pcUI);
		
		RhythmUI rUI = new RhythmUI();
		root.getChildren().add(rUI);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add("css/test.css");
		scene.setOnKeyReleased((e) -> {
			if (e.getCode() == KeyCode.Q) {
				primaryStage.close();
			}
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
