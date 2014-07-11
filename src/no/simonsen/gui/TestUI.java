package no.simonsen.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TestUI extends Application {
	public static Stage stage;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		
		Group root = new Group();
		PatternCombinerUI pcUI = new PatternCombinerUI();
		root.getChildren().add(pcUI);
		Scene scene = new Scene(root);
		scene.getStylesheets().add("css/test.css");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
}
