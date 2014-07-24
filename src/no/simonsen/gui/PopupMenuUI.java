package no.simonsen.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
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
			popupWindow.show(TestUI.stage, popupXPos, popupYPos);
		});
	}
	
	public void addMenuItem(String command, EventHandler<? super ActionEvent> handler) {
		Button button = new Button(command);
		button.setPrefSize(130, 20);
		button.setLayoutY(popupWindow.getContent().size() * 20);
		button.getStyleClass().add("buttonFauxMenuItem");
		button.setOnAction((e) -> {
			popupWindow.hide();
		});
		button.addEventHandler(ActionEvent.ACTION, handler);
		
		popupWindow.getContent().add(button);
	}
}
