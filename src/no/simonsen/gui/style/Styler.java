package no.simonsen.gui.style;

import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class Styler {
	public static void style(Node node, Styler.Mode mode) {
		if (node instanceof Pane) {
			styleRegion((Pane) node, mode);
		}
	}
	
	private static void styleRegion(Pane pane, Styler.Mode mode) {
		if (mode == Styler.Mode.DEFAULT) {
			pane.setBackground(BackgroundPool.window);
			pane.setEffect(new ColorAdjust(0, 0.0, 0.0, 0.05));
		}
	}
	
	public enum Mode {
		DEFAULT
	}
}
