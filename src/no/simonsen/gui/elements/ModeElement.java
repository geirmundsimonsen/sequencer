package no.simonsen.gui.elements;

import java.util.function.Consumer;

import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import no.simonsen.gui.TestUI;

public class ModeElement extends Group {
	public <T> ModeElement(String label, ToggleGroup toggleGroup, Consumer<T> consumer, T mode) {
		RadioButton radio = new RadioButton(label);
		radio.setMinSize(140, 20); radio.setMaxSize(140, 20); radio.setPrefSize(140, 20);
		radio.setFont(TestUI.labelFont);
		radio.setFocusTraversable(false);
		radio.setOnAction((e) -> {
			consumer.accept(mode);
		});
		getChildren().add(radio);
		toggleGroup.getToggles().add(radio);
	}
}
