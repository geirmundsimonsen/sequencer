package no.simonsen.workbench;

import no.simonsen.gui.style.Styler;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TimeEditorTest extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		
		TimeEditor timeEdit = new TimeEditor();
		root.getChildren().add(timeEdit);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

class TimeEditor extends Pane {
	private Group masterGroup;
	private double offset = 10;
	
	public TimeEditor() {
		Styler.style(this, Styler.Mode.DEFAULT);
		
		masterGroup = new Group();
		masterGroup.setLayoutX(offset);
		masterGroup.setLayoutY(offset);
		
		
		
		masterGroup.getChildren().add(new Rectangle(100, 100));
		
		getChildren().add(masterGroup);
		setPrefSize(masterGroup.getLayoutBounds().getWidth() + offset * 2, masterGroup.getLayoutBounds().getHeight() + offset * 2);

	}
}