package no.simonsen.gui;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class EditWindow extends Stage {
	public EditWindow(Parent root, double x, double y, Window parentWindow) {
		initModality(Modality.WINDOW_MODAL);
		initOwner(parentWindow);
		initStyle(StageStyle.UTILITY);
		setScene(new Scene(root));
		setX(parentWindow.getX() + x);
		setY(parentWindow.getY() + y);
		
		Platform.runLater(() -> {
			show();
		});
	}
}
