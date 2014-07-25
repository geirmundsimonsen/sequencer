package no.simonsen.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.simonsen.gui.style.Styler;
import no.simonsen.midi.ConstantPattern;
import no.simonsen.midi.EventBuffer;
import no.simonsen.midi.ValueSupplier;
import no.simonsen.midi.PatternCombiner;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PatternCombinerUI extends Pane {
	private Group masterGroup;
	private double offset = 10;
	private PatternCombiner patternCombiner;
	private EventBuffer eventBuffer;
	private Group patternCellsUIGroup; 
	private Logger logger;

	
	public PatternCombinerUI() {
		Styler.style(this, Styler.Mode.DEFAULT);
		
		/*
		 * Actually, when using Region/Pane directly or subclassing it, the pane is supposed to 
		 * honor the insets. It doesn't seem to happen automatically. Here, a master group is used
		 * to hold all nodes in the pane. We then position it and size the pane in order to "fake"
		 * insets. To comply with the API, we should use the pane's inset values to create this effect
		 * instead of using offset.
		 * 
		 * We might extend Pane so this functionality might be reused - but first we have to verify
		 * that this code actually works as intended.
		 * 
		 * An AnchorPane might give us the functionality we need.
		 */
		masterGroup = new Group();
		masterGroup.setLayoutX(offset);
		masterGroup.setLayoutY(offset);
		masterGroup.setBlendMode(BlendMode.MULTIPLY);
		
		patternCombiner = new PatternCombiner();
		
		this.setOnKeyPressed((e) -> {
			if (e.getCode() == KeyCode.F) {
				logger.info("{}", patternCombiner.getNext());
			}
		});
		
		logger = LoggerFactory.getLogger("no.simonsen.gui.PatternCombinerUI");
		
		patternCellsUIGroup = new Group();
		patternCellsUIGroup.setLayoutX(0);
		patternCellsUIGroup.setLayoutY(40);
		
		Button addPatternCell = new Button("add pattern cell");
		addPatternCell.setLayoutX(0);
		addPatternCell.setLayoutY(0);
		addPatternCell.setFont(TestUI.labelFont);
		addPatternCell.setOnAction((e) -> {
			PatternCell patternCell = new PatternCell();
			patternCellsUIGroup.getChildren().add(patternCell);
		});
		patternCellsUIGroup.getChildren().add(new PatternCell("time"));
		patternCellsUIGroup.getChildren().add(new PatternCell("length"));
		patternCellsUIGroup.getChildren().add(new PatternCell("pitch"));
		patternCellsUIGroup.getChildren().add(new PatternCell("velocity"));
		
		CheckBox sendMidiMessages = new CheckBox("Send MIDI messages");
		sendMidiMessages.setLayoutX(0);
		sendMidiMessages.setLayoutY(140);
		sendMidiMessages.setFont(TestUI.labelFont);
		sendMidiMessages.setOnAction((e) -> {
			if (sendMidiMessages.isSelected()) {
				if (canSendMidiMessages()) {
					lockMidiPatternIds();
				} else {
					sendMidiMessages.setSelected(false);
					new ErrorPopup(this, sendMidiMessages.getLayoutX(), sendMidiMessages.getLayoutY() + 25, 
							"Please set MIDI message pattern parameters: time, length, pitch and velocity.");
	
				}
			} else {
				unlockMidiPatternIds();
			}
		});
		sendMidiMessages.fire();
		
		ToggleButton play = new ToggleButton("Play");
		play.setLayoutX(0);
		play.setLayoutY(170);
		play.setMinHeight(20); // needed to set layout bounds
		play.setFont(TestUI.labelFont);
		play.setTooltip(new Tooltip("Press 'space' to play"));
		play.setOnAction((e) -> {
			if (play.isSelected()) {
				eventBuffer = new EventBuffer(patternCombiner);
				eventBuffer.start();
			} else {
				eventBuffer.halt();
				patternCombiner.resetValueSuppliers();
			}
		});
		
		masterGroup.getChildren().add(addPatternCell);
		masterGroup.getChildren().add(patternCellsUIGroup);
		masterGroup.getChildren().add(sendMidiMessages);
		masterGroup.getChildren().add(play);
		
		getChildren().add(masterGroup);
		setPrefSize(masterGroup.getLayoutBounds().getWidth() + offset * 2, masterGroup.getLayoutBounds().getHeight() + offset * 2);
	}
	
	private boolean canSendMidiMessages() {
		boolean time = false, length = false, pitch = false, velocity = false;
		
		for (Node node : patternCellsUIGroup.getChildren()) {
			PatternCell patternCell = (PatternCell) node;
			if (patternCell.getPatternId().equals("time")) time = true;
			else if (patternCell.getPatternId().equals("length")) length = true;
			else if (patternCell.getPatternId().equals("pitch")) pitch = true;
			else if (patternCell.getPatternId().equals("velocity")) velocity = true;
		}
		
		return time && length && pitch && velocity;
	}
	
	private void lockMidiPatternIds() {
		for (Node node : patternCellsUIGroup.getChildren()) {
			PatternCell patternCell = (PatternCell) node;
			if (patternCell.getPatternId().equals("time")) patternCell.setPatternIdLock(true);
			else if (patternCell.getPatternId().equals("length")) patternCell.setPatternIdLock(true);
			else if (patternCell.getPatternId().equals("pitch")) patternCell.setPatternIdLock(true);
			else if (patternCell.getPatternId().equals("velocity")) patternCell.setPatternIdLock(true);
		}
	}
	
	private void unlockMidiPatternIds() {
		for (Node node : patternCellsUIGroup.getChildren()) {
			PatternCell patternCell = (PatternCell) node;
			if (patternCell.getPatternId().equals("time")) patternCell.setPatternIdLock(false);
			else if (patternCell.getPatternId().equals("length")) patternCell.setPatternIdLock(false);
			else if (patternCell.getPatternId().equals("pitch")) patternCell.setPatternIdLock(false);
			else if (patternCell.getPatternId().equals("velocity")) patternCell.setPatternIdLock(false);
		}
	}
	
	class PatternCell extends Group {
		private Label patternLabel;
		private ValueSupplierField valueSupplierField;
		private Label patternIdLabel;
		private TextField patternIdField;
		private Rectangle patternIdLockIndicator;
		
		public PatternCell() {
			this("");
		}
		
		public PatternCell(String id) {
			double width = 100;
			double calculatedLayoutX = patternCellsUIGroup.getChildren().size() * width;
						
			patternLabel = new Label("pattern");
			patternLabel.setPrefSize(80, 20);
			patternLabel.setAlignment(Pos.CENTER);
			patternLabel.setFont(TestUI.labelFont);
			patternLabel.setLayoutX(calculatedLayoutX);
			patternLabel.setLayoutY(0);
			
			patternIdLabel = new Label("id");
			patternIdLabel.setPrefSize(80, 20);
			patternIdLabel.setAlignment(Pos.CENTER);
			patternIdLabel.setFont(TestUI.labelFont);
			patternIdLabel.setLayoutX(calculatedLayoutX);
			patternIdLabel.setLayoutY(40);
			
			patternIdField = new TextField(id);
			patternIdField.getStyleClass().add("textField");
			patternIdField.setPrefSize(80, 20);
			patternIdField.setLayoutX(calculatedLayoutX);
			patternIdField.setLayoutY(60);
			
			patternIdLockIndicator = new Rectangle();
			patternIdLockIndicator.setLayoutX(patternIdField.getLayoutX());
			patternIdLockIndicator.setLayoutY(patternIdField.getLayoutY());
			patternIdLockIndicator.setWidth(patternIdField.getPrefWidth());
			patternIdLockIndicator.setHeight(patternIdField.getPrefHeight());
			patternIdLockIndicator.setFill(Color.RED);
			patternIdLockIndicator.setOpacity(0.3);
			patternIdLockIndicator.setMouseTransparent(true);
			patternIdLockIndicator.setVisible(false);
			
			valueSupplierField = new ValueSupplierField();
			valueSupplierField.setValueSupplierHost(patternCombiner);
			valueSupplierField.setLayoutY(20);
			valueSupplierField.setLayoutX(calculatedLayoutX);
			
			ValueSupplier valueSupplier = new ConstantPattern(0, getPatternId());
			patternCombiner.addValueSupplier(valueSupplier);
			valueSupplierField.setValueSupplier(valueSupplier);
			
			getChildren().add(patternLabel);
			getChildren().add(valueSupplierField);
			getChildren().add(patternIdLabel);
			getChildren().add(patternIdField);
			getChildren().add(patternIdLockIndicator);
		}
		
		public void setPatternIdLock(boolean bool) {
			patternIdField.setEditable(!bool);
			patternIdLockIndicator.setVisible(bool);
		}
		
		public String getPatternId() {
			return patternIdField.getText();
		}
	}
}