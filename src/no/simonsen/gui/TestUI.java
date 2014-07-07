package no.simonsen.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TestUI extends Application {
	public static void main(String[] args) {
		System.out.println("Testing ListPatternUI.");
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		Group root = new Group();
		root.getChildren().add(new ListPatternUI());
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add("css/test.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
