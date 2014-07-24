package no.simonsen.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Popup;

public class PopupMenuUI extends Group {
	private Circle circle;
	private Polygon triangle;
	private Popup popupWindow;
	
	public PopupMenuUI(double size) { 
		circle = new Circle(size * 0.5);
		circle.setFill(Color.GRAY);
		
		triangle = new Polygon();
		triangle.getPoints().add(-size / 6); 
		triangle.getPoints().add(-size / 6);
		triangle.getPoints().add(size / 6);
		triangle.getPoints().add(-size / 6);
		triangle.getPoints().add(0.0);
		triangle.getPoints().add(size / 3);
		triangle.setFill(Color.LIGHTGRAY);
				
		popupWindow = new Popup();
		popupWindow.setAutoHide(true);
		
		getChildren().add(circle);
		getChildren().add(triangle);
		
		this.setOnMouseClicked((e) -> {
			double popupXPos = e.getScreenX() - e.getX() - size * 0.5;
			double popupYPos = e.getScreenY() - e.getY() + size * 0.5;
			popupWindow.show(this, popupXPos, popupYPos);
		});
	}
	
	public void addMenuItem(String command, EventHandler<? super ActionEvent> handler) {
		Button button = new Button(command);
		button.setPrefSize(130, 20);
		button.setLayoutY(popupWindow.getContent().size() * 20);
		button.setFont(TestUI.labelFont);
		
		// consider storing this rather than create it each time.
		Stop[] stops = { new Stop(0, Color.ALICEBLUE), new Stop(0.05, Color.LIGHTBLUE), new Stop(0.95, Color.LIGHTBLUE), new Stop(1, Color.ALICEBLUE) };
		LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
		BackgroundFill bgFill = new BackgroundFill(lg, CornerRadii.EMPTY, Insets.EMPTY);
		Background bg = new Background(bgFill);
		
		button.setBackground(bg);
		button.setOnAction((e) -> {
			popupWindow.hide();
		});
		
		button.addEventHandler(ActionEvent.ACTION, handler); // custom behavior
		
		popupWindow.getContent().add(button);
	}
}
