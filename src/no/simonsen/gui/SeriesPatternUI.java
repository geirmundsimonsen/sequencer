package no.simonsen.gui;

import no.simonsen.gui.style.Styler;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;

public class SeriesPatternUI extends Pane {
	private Group masterGroup;
	private double offset = 10;
	
	public SeriesPatternUI() {
		Styler.style(this, Styler.Mode.DEFAULT);
		
		masterGroup = new Group();
		masterGroup.setLayoutX(offset);
		masterGroup.setLayoutY(offset);
		masterGroup.setBlendMode(BlendMode.MULTIPLY);
		
		
		
		getChildren().add(masterGroup);
		setPrefSize(masterGroup.getLayoutBounds().getWidth() + offset * 2, masterGroup.getLayoutBounds().getHeight() + offset * 2);
	}
}
