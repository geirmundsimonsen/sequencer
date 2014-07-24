package no.simonsen.physical;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class BridgeApp extends Application {

	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		
		Rectangle background = new Rectangle(1010, 200);
		background.setFill(Color.LINEN);
		root.getChildren().add(background);
		
		LinkedSteps linkedSteps = new LinkedSteps();
		linkedSteps.setLayoutX(10);
		linkedSteps.setLayoutY(100);
		root.getChildren().add(linkedSteps);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
