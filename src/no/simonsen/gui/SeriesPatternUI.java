package no.simonsen.gui;

import no.simonsen.gui.elements.ModeElement;
import no.simonsen.gui.elements.ParameterElement;
import no.simonsen.gui.style.Styler;
import no.simonsen.midi.SeriesPattern;
import no.simonsen.midi.SeriesPatternMode;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;

public class SeriesPatternUI extends Pane {
	private Group masterGroup;
	private double offset = 10;
	private SeriesPattern seriesPattern;
	private ParameterElement initialValueElement;
	private ParameterElement lengthElement;
	private ParameterElement stepElement;
	private ParameterElement factorElement;
	private ToggleGroup seriesModeGroup;
	
	public SeriesPatternUI() {
		Styler.style(this, Styler.Mode.DEFAULT);
		
		masterGroup = new Group();
		masterGroup.setLayoutX(offset);
		masterGroup.setLayoutY(offset);
		masterGroup.setBlendMode(BlendMode.MULTIPLY);
		
		seriesModeGroup = new ToggleGroup();
		
		initialValueElement = new ParameterElement("initial", null, (value) -> { seriesPattern.setInitialValue(value); });
		initialValueElement.setLayoutY(0);
		lengthElement = new ParameterElement("length", (value) -> { seriesPattern.setLength(value); }, null);
		lengthElement.setLayoutY(25);
		ModeElement constantModeElement = new ModeElement("CONSTANT", seriesModeGroup, 
				(mode) -> { seriesPattern.setMode(mode); }, SeriesPatternMode.CONSTANT);
		constantModeElement.setLayoutY(50);
		stepElement = new ParameterElement("step", null, (value) -> { seriesPattern.setStep(value); });
		stepElement.setLayoutY(75);
		ModeElement exponentialModeElement = new ModeElement("EXPONENTIAL", seriesModeGroup, 
				(mode) -> { seriesPattern.setMode(mode); }, SeriesPatternMode.EXPONENTIAL);
		exponentialModeElement.setLayoutY(100);
		factorElement = new ParameterElement("factor", null, (value) -> { seriesPattern.setFactor(value); });
		factorElement.setLayoutY(125);
		
		masterGroup.getChildren().add(initialValueElement);
		masterGroup.getChildren().add(lengthElement);
		masterGroup.getChildren().add(constantModeElement);
		masterGroup.getChildren().add(stepElement);
		masterGroup.getChildren().add(exponentialModeElement);
		masterGroup.getChildren().add(factorElement);
		
		getChildren().add(masterGroup);
		setPrefSize(masterGroup.getLayoutBounds().getWidth() + offset * 2, masterGroup.getLayoutBounds().getHeight() + offset * 2);
	}
	
	public SeriesPattern getSeriesPattern() {
		return seriesPattern;
	}
	
	public void setSeriesPattern(SeriesPattern seriesPattern) {
		this.seriesPattern = seriesPattern;
		
		initialValueElement.setText(String.valueOf(seriesPattern.getInitialValue()));
		if (seriesPattern.getLength() != Integer.MAX_VALUE) {			
			lengthElement.setText(String.valueOf(seriesPattern.getLength()));
		} else {
			lengthElement.setText("inf");
		}
		stepElement.setText(String.valueOf(seriesPattern.getStep()));
		factorElement.setText(String.valueOf(seriesPattern.getFactor()));
		
		SeriesPatternMode seriesPatternMode = seriesPattern.getMode();
		String seriesPatternModeString = "";
		if (seriesPatternMode == SeriesPatternMode.CONSTANT)
			seriesPatternModeString = "CONSTANT";
		else if (seriesPatternMode == SeriesPatternMode.EXPONENTIAL)
			seriesPatternModeString = "EXPONENTIAL";
		for (Toggle toggle : seriesModeGroup.getToggles()) {
			RadioButton radio = (RadioButton) toggle;
			if (radio.getText().equals(seriesPatternModeString)) {
				radio.setSelected(true);
				break;
			}
		}
	}
}
