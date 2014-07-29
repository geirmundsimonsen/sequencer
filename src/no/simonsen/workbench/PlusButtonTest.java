package no.simonsen.workbench;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Control;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PlusButtonTest extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		
		PlusButton plus = new PlusButton();
		root.getChildren().add(plus);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

class PlusButton extends Group {
	public PlusButton() {
		Rectangle rectangle = new Rectangle(20, 20);
		
		this.getChildren().add(new Rectangle(100, 100));
	}
}