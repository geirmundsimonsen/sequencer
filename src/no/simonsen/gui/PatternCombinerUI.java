package no.simonsen.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.simonsen.data.Sequence;
import no.simonsen.midi.ValueSupplier;
import no.simonsen.midi.PatternCombiner;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PatternCombinerUI extends Group {
	private PatternCombiner patternCombiner;
	private Font labelFont;
	private Group patternCellsUIGroup; 
	private Logger logger;
	
	public PatternCombinerUI() {
		patternCombiner = new PatternCombiner();
		
		this.setOnKeyPressed((e) -> {
			if (e.getCode() == KeyCode.F) {
				logger.info("{}", patternCombiner.getNext());
			}
		});
		
		labelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/TypewriterScribbled.ttf"), 10);
		logger = LoggerFactory.getLogger("no.simonsen.gui.PatternCombiner");
		
		Rectangle background = new Rectangle(500, 210);
		background.setFill(Color.SNOW);
		getChildren().add(background);
		
		patternCellsUIGroup = new Group();
		patternCellsUIGroup.setLayoutX(10);
		patternCellsUIGroup.setLayoutY(50);
		
		Button addPatternCell = new Button("add pattern cell");
		addPatternCell.setLayoutX(10);
		addPatternCell.setLayoutY(10);
		addPatternCell.setFont(labelFont);
		addPatternCell.setOnAction((e) -> {
			PatternCell patternCell = new PatternCell();
			patternCellsUIGroup.getChildren().add(patternCell);
		});
		patternCellsUIGroup.getChildren().add(new PatternCell("time"));
		patternCellsUIGroup.getChildren().add(new PatternCell("length"));
		patternCellsUIGroup.getChildren().add(new PatternCell("pitch"));
		patternCellsUIGroup.getChildren().add(new PatternCell("velocity"));
		
		CheckBox sendMidiMessages = new CheckBox("Send MIDI messages");
		sendMidiMessages.setLayoutX(10);
		sendMidiMessages.setLayoutY(150);
		sendMidiMessages.setFont(labelFont);
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
		
		Button play = new Button("Play");
		play.setLayoutX(10);
		play.setLayoutY(180);
		play.setFont(labelFont);
		play.setTooltip(new Tooltip("Press 'space' to play"));
		play.setOnAction((e) -> {
			// play patterncombiner
		});
		
		getChildren().add(addPatternCell);
		getChildren().add(patternCellsUIGroup);
		getChildren().add(sendMidiMessages);
		getChildren().add(play);
		
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
		private ValueSupplier valueSupplier;
		private Label patternLabel;
		private TextField patternField;
		private PopupMenuUI patternPopup;
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
			patternLabel.setFont(labelFont);
			patternLabel.setLayoutX(calculatedLayoutX);
			patternLabel.setLayoutY(0);
			
			patternField = new TextField();
			patternField.getStyleClass().add("textField");
			patternField.setPrefSize(80, 20);
			patternField.setLayoutX(calculatedLayoutX);
			patternField.setLayoutY(20);
			
			patternPopup = new PopupMenuUI(12);
			patternPopup.setLayoutX(calculatedLayoutX + patternField.getPrefWidth());
			patternPopup.setLayoutY(30);
			patternPopup.addMenuItem("add sequence", (e) -> {
				ListPatternUI listPatternUI = new ListPatternUI();
				listPatternUI.setSequence(new Sequence());
				patternCombiner.addValueSupplier(listPatternUI.getSequence(),
						getPatternId());
				
				Stage editWindow = new Stage();
				editWindow.initModality(Modality.WINDOW_MODAL);
				editWindow.initOwner(TestUI.stage);
				editWindow.initStyle(StageStyle.UTILITY);
				editWindow.setScene(new Scene(listPatternUI));
				editWindow.setX(TestUI.stage.getX() + patternPopup.getLayoutX() + 20);
				editWindow.setY(TestUI.stage.getY() + patternPopup.getLayoutY() - 20);
				
				// Don't know why we must do this.
				// But not doing it causes the primary stage to retain focus
				// 50% of the times.
				Platform.runLater(() -> {
					editWindow.show();
				});
			});
			
			patternPopup.addMenuItem("edit...", (e) -> {
				ValueSupplier valueSupplier = patternCombiner.getValueSupplier(getPatternId());
				if (valueSupplier instanceof Sequence) {
					Sequence sequence = (Sequence) valueSupplier;
					ListPatternUI listPatternUI = new ListPatternUI();
					listPatternUI.setSequence(sequence);
					
					Stage editWindow = new Stage();
					editWindow.initModality(Modality.WINDOW_MODAL);
					editWindow.initOwner(TestUI.stage);
					editWindow.initStyle(StageStyle.UTILITY);
					editWindow.setScene(new Scene(listPatternUI));
					editWindow.setX(TestUI.stage.getX() + patternPopup.getLayoutX() + 20);
					editWindow.setY(TestUI.stage.getY() + patternPopup.getLayoutY() - 20);
					
					// Don't know why we must do this.
					// But not doing it causes the primary stage to retain focus
					// 50% of the times.
					Platform.runLater(() -> {
						editWindow.show();
					});
				}
			});
			
			
			patternIdLabel = new Label("id");
			patternIdLabel.setPrefSize(80, 20);
			patternIdLabel.setAlignment(Pos.CENTER);
			patternIdLabel.setFont(labelFont);
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
			
			getChildren().add(patternLabel);
			getChildren().add(patternField);
			getChildren().add(patternPopup);
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