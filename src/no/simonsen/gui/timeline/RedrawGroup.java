package no.simonsen.gui.timeline;

import javafx.scene.Group;
import javafx.scene.Node;

public class RedrawGroup extends Group implements Redrawable {
	public void redraw(double scaleX, double scaleY, double offsetX,
			double offsetY) {
		for (Node nodeToBeCasted : getChildren()) {
			Redrawable redrawable = (Redrawable) nodeToBeCasted;
			redrawable.redraw(scaleX, scaleY, offsetX, offsetY);
		}
	}
	
	public <T extends Node & Redrawable> void addRedrawable(T redrawable) {
		getChildren().add(redrawable);
	}
	
	public <T extends Node & Redrawable> void removeRedrawable(T redrawable) {
		getChildren().remove(redrawable);
	}
}

