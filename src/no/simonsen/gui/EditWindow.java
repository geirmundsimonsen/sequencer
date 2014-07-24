package no.simonsen.gui;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditWindow extends Stage {
	public EditWindow(Parent root, double x, double y) {
		initModality(Modality.WINDOW_MODAL);
		initOwner(TestUI.stage);
		initStyle(StageStyle.UTILITY);
		setScene(new Scene(root));
		setX(TestUI.stage.getX() + x);
		setY(TestUI.stage.getY() + y);
		
		Platform.runLater(() -> {
			show();
		});
	}
}
