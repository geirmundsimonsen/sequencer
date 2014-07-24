package no.simonsen.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ErrorPopup {
	Group popup;
	Group parent;
	double x;
	double y;
	String message;
	
	public ErrorPopup(Group parent, double x, double y, String message) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		
		popup = new Group();
		popup.setOpacity(0.85);
		popup.setLayoutX(x);
		popup.setLayoutY(y);
		
		double inset = 10;
		
		Label messageLabel = new Label(message);
		messageLabel.setWrapText(true);
		messageLabel.setPrefWidth(180);
		messageLabel.setLayoutX(inset);
		messageLabel.setLayoutY(inset);

		Rectangle background = new Rectangle(180 + inset * 2, message.length() / 24 * 20 + inset * 2);
		background.setFill(Color.LEMONCHIFFON);
		
		popup.getChildren().add(background);
		popup.getChildren().add(messageLabel);
		parent.getChildren().add(popup);

		KeyValue keyValue1 = new KeyValue(popup.opacityProperty(), popup.getOpacity());
		KeyFrame keyFrame1 = new KeyFrame(Duration.millis(2500), keyValue1);
		KeyValue keyValue2 = new KeyValue(popup.opacityProperty(), 0);
		KeyFrame keyFrame2 = new KeyFrame(Duration.millis(4000), keyValue2);
		
		Timeline timeline = new Timeline(keyFrame1, keyFrame2);
		timeline.play();
		timeline.setOnFinished((e) -> {
			parent.getChildren().remove(popup);
		});
	}
	
	
}

class ExtendedThread extends Thread {
	
}