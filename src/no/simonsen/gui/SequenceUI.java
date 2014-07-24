package no.simonsen.gui;

import java.util.ArrayList;

import no.simonsen.data.Sequence;
import no.simonsen.data.SequenceMode;
import no.simonsen.midi.ConstantPattern;
import no.simonsen.midi.ValueSupplier;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class SequenceUI extends Group {
	private Sequence sequence;
	private ArrayList<NumberField> numberFields = new ArrayList<>();
	private Group numberFieldsUIGroup;
	private int numberFieldCount = 0;
	private TextField startOffsetField;
	private TextField endOffsetField;
	private TextField localOffsetField;
	private TextField lengthField;
	private TextField patternDurationField;
	private ToggleGroup sequenceModeGroup;
	private TextField noRepeatMemoryField;
	
	public SequenceUI() {				
		Rectangle rectangle = new Rectangle(290, 400);
		rectangle.setFill(Color.BLACK);
		getChildren().add(rectangle);
		
		numberFieldsUIGroup = new Group();
		numberFieldsUIGroup.setTranslateX(10);
		numberFieldsUIGroup.setTranslateY(40);
		
		Label startOffsetLabel = new Label("start off.");
		startOffsetLabel.setMinSize(70, 20);
		startOffsetLabel.setMaxSize(70, 20);
		startOffsetLabel.setPrefSize(70, 20);
		startOffsetLabel.setFont(TestUI.labelFont);
		startOffsetLabel.setTextFill(Color.BEIGE);
		startOffsetLabel.setLayoutX(0);
		startOffsetLabel.setLayoutY(0);
		
		startOffsetField = new TextField();
		startOffsetField.setMinSize(40, 20);
		startOffsetField.setMaxSize(40, 20);
		startOffsetField.setPrefSize(40, 20);
		startOffsetField.setFont(TestUI.labelFont);
		startOffsetField.setLayoutX(80);
		startOffsetField.setLayoutY(0);
		startOffsetField.setFocusTraversable(false);
		startOffsetField.setOnKeyReleased((e) -> {
			try {
				sequence.setStartOffset(new Integer(startOffsetField.getText()));				
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button startOffsetIncrease = new Button("+");
		startOffsetIncrease.setMinSize(20, 20);
		startOffsetIncrease.setMaxSize(20, 20);
		startOffsetIncrease.setPrefSize(20, 20);
		startOffsetIncrease.setFont(TestUI.labelFont);
		startOffsetIncrease.setLayoutX(120);
		startOffsetIncrease.setLayoutY(0);
		startOffsetIncrease.setFocusTraversable(false);
		startOffsetIncrease.setOnAction((e) -> {
			try {
				int increasedValue = new Integer(startOffsetField.getText()) + 1;
				sequence.setStartOffset(increasedValue);
				startOffsetField.setText(String.valueOf(increasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button startOffsetDecrease = new Button("-");
		startOffsetDecrease.setMinSize(20, 20);
		startOffsetDecrease.setMaxSize(20, 20);
		startOffsetDecrease.setPrefSize(20, 20);
		startOffsetDecrease.setFont(TestUI.labelFont);
		startOffsetDecrease.setLayoutX(140);
		startOffsetDecrease.setLayoutY(0);
		startOffsetDecrease.setFocusTraversable(false);
		startOffsetDecrease.setOnAction((e) -> {
			try {
				int decreasedValue = new Integer(startOffsetField.getText()) - 1;
				sequence.setStartOffset(decreasedValue);
				startOffsetField.setText(String.valueOf(decreasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Label endOffsetLabel = new Label("end off.");
		endOffsetLabel.setMinSize(70, 20);
		endOffsetLabel.setMaxSize(70, 20);
		endOffsetLabel.setPrefSize(70, 20);
		endOffsetLabel.setFont(TestUI.labelFont);
		endOffsetLabel.setTextFill(Color.BEIGE);
		endOffsetLabel.setLayoutX(0);
		endOffsetLabel.setLayoutY(25);
		
		endOffsetField = new TextField();
		endOffsetField.setMinSize(40, 20);
		endOffsetField.setMaxSize(40, 20);
		endOffsetField.setPrefSize(40, 20);
		endOffsetField.setFont(TestUI.labelFont);
		endOffsetField.setLayoutX(80);
		endOffsetField.setLayoutY(25);
		endOffsetField.setFocusTraversable(false);
		endOffsetField.setOnKeyReleased((e) -> {
			try {
				sequence.setEndOffset(new Integer(endOffsetField.getText()));				
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button endOffsetIncrease = new Button("+");
		endOffsetIncrease.setMinSize(20, 20);
		endOffsetIncrease.setMaxSize(20, 20);
		endOffsetIncrease.setPrefSize(20, 20);
		endOffsetIncrease.setFont(TestUI.labelFont);
		endOffsetIncrease.setLayoutX(120);
		endOffsetIncrease.setLayoutY(25);
		endOffsetIncrease.setFocusTraversable(false);
		endOffsetIncrease.setOnAction((e) -> {
			try {
				int increasedValue = new Integer(endOffsetField.getText()) + 1;
				sequence.setEndOffset(increasedValue);
				endOffsetField.setText(String.valueOf(increasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button endOffsetDecrease = new Button("-");
		endOffsetDecrease.setMinSize(20, 20);
		endOffsetDecrease.setMaxSize(20, 20);
		endOffsetDecrease.setPrefSize(20, 20);
		endOffsetDecrease.setFont(TestUI.labelFont);
		endOffsetDecrease.setLayoutX(140);
		endOffsetDecrease.setLayoutY(25);
		endOffsetDecrease.setFocusTraversable(false);
		endOffsetDecrease.setOnAction((e) -> {
			try {
				int decreasedValue = new Integer(endOffsetField.getText()) - 1;
				sequence.setEndOffset(decreasedValue);
				endOffsetField.setText(String.valueOf(decreasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Label localOffsetLabel = new Label("local off.");
		localOffsetLabel.setMinSize(70, 20);
		localOffsetLabel.setMaxSize(70, 20);
		localOffsetLabel.setPrefSize(70, 20);
		localOffsetLabel.setFont(TestUI.labelFont);
		localOffsetLabel.setTextFill(Color.BEIGE);
		localOffsetLabel.setLayoutX(0);
		localOffsetLabel.setLayoutY(50);
		
		localOffsetField = new TextField();
		localOffsetField.setMinSize(40, 20);
		localOffsetField.setMaxSize(40, 20);
		localOffsetField.setPrefSize(40, 20);
		localOffsetField.setFont(TestUI.labelFont);
		localOffsetField.setLayoutX(80);
		localOffsetField.setLayoutY(50);
		localOffsetField.setFocusTraversable(false);
		localOffsetField.setOnKeyReleased((e) -> {
			try {
				sequence.setLocalOffset(new Integer(localOffsetField.getText()));				
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button localOffsetIncrease = new Button("+");
		localOffsetIncrease.setMinSize(20, 20);
		localOffsetIncrease.setMaxSize(20, 20);
		localOffsetIncrease.setPrefSize(20, 20);
		localOffsetIncrease.setFont(TestUI.labelFont);
		localOffsetIncrease.setLayoutX(120);
		localOffsetIncrease.setLayoutY(50);
		localOffsetIncrease.setFocusTraversable(false);
		localOffsetIncrease.setOnAction((e) -> {
			try {
				int increasedValue = new Integer(localOffsetField.getText()) + 1;
				sequence.setLocalOffset(increasedValue);
				localOffsetField.setText(String.valueOf(increasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button localOffsetDecrease = new Button("-");
		localOffsetDecrease.setMinSize(20, 20);
		localOffsetDecrease.setMaxSize(20, 20);
		localOffsetDecrease.setPrefSize(20, 20);
		localOffsetDecrease.setFont(TestUI.labelFont);
		localOffsetDecrease.setLayoutX(140);
		localOffsetDecrease.setLayoutY(50);
		localOffsetDecrease.setFocusTraversable(false);
		localOffsetDecrease.setOnAction((e) -> {
			try {
				int decreasedValue = new Integer(localOffsetField.getText()) - 1;
				sequence.setLocalOffset(decreasedValue);
				localOffsetField.setText(String.valueOf(decreasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Label lengthLabel = new Label("length");
		lengthLabel.setMinSize(70, 20);
		lengthLabel.setMaxSize(70, 20);
		lengthLabel.setPrefSize(70, 20);
		lengthLabel.setFont(TestUI.labelFont);
		lengthLabel.setTextFill(Color.BEIGE);
		lengthLabel.setLayoutX(0);
		lengthLabel.setLayoutY(75);
		
		lengthField = new TextField();
		lengthField.setMinSize(40, 20);
		lengthField.setMaxSize(40, 20);
		lengthField.setPrefSize(40, 20);
		lengthField.setFont(TestUI.labelFont);
		lengthField.setLayoutX(80);
		lengthField.setLayoutY(75);
		lengthField.setFocusTraversable(false);
		lengthField.setOnKeyReleased((e) -> {
			try {
				sequence.setLength(new Integer(lengthField.getText()));				
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button lengthIncrease = new Button("+");
		lengthIncrease.setMinSize(20, 20);
		lengthIncrease.setMaxSize(20, 20);
		lengthIncrease.setPrefSize(20, 20);
		lengthIncrease.setFont(TestUI.labelFont);
		lengthIncrease.setLayoutX(120);
		lengthIncrease.setLayoutY(75);
		lengthIncrease.setFocusTraversable(false);
		lengthIncrease.setOnAction((e) -> {
			try {
				int increasedValue = new Integer(lengthField.getText()) + 1;
				sequence.setLength(increasedValue);
				lengthField.setText(String.valueOf(increasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button lengthDecrease = new Button("-");
		lengthDecrease.setMinSize(20, 20);
		lengthDecrease.setMaxSize(20, 20);
		lengthDecrease.setPrefSize(20, 20);
		lengthDecrease.setFont(TestUI.labelFont);
		lengthDecrease.setLayoutX(140);
		lengthDecrease.setLayoutY(75);
		lengthDecrease.setFocusTraversable(false);
		lengthDecrease.setOnAction((e) -> {
			try {
				int decreasedValue = new Integer(lengthField.getText()) - 1;
				sequence.setLength(decreasedValue);
				lengthField.setText(String.valueOf(decreasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		RadioButton normalRadio = new RadioButton("NORMAL");
		normalRadio.setMinSize(140, 20);
		normalRadio.setMaxSize(140, 20);
		normalRadio.setPrefSize(140, 20);
		normalRadio.setFont(TestUI.labelFont);
		normalRadio.setTextFill(Color.BEIGE);
		normalRadio.setLayoutX(0);
		normalRadio.setLayoutY(100);
		normalRadio.setFocusTraversable(false);
		normalRadio.setOnAction((e) -> {
			sequence.setSequenceMode(SequenceMode.NORMAL);
		});
		
		RadioButton timeSensitiveRadio = new RadioButton("TIME SENSITIVE");
		timeSensitiveRadio.setMinSize(140, 20);
		timeSensitiveRadio.setMaxSize(140, 20);
		timeSensitiveRadio.setPrefSize(140, 20);
		timeSensitiveRadio.setFont(TestUI.labelFont);
		timeSensitiveRadio.setTextFill(Color.BEIGE);
		timeSensitiveRadio.setLayoutX(0);
		timeSensitiveRadio.setLayoutY(125);
		timeSensitiveRadio.setFocusTraversable(false);
		timeSensitiveRadio.setOnAction((e) -> {
			sequence.setSequenceMode(SequenceMode.TIME_SENSITIVE);
		});
		
		Label patternDurationLabel = new Label("patternDuration");
		patternDurationLabel.setMinSize(70, 20);
		patternDurationLabel.setMaxSize(70, 20);
		patternDurationLabel.setPrefSize(70, 20);
		patternDurationLabel.setFont(TestUI.labelFont);
		patternDurationLabel.setTextFill(Color.BEIGE);
		patternDurationLabel.setLayoutX(0);
		patternDurationLabel.setLayoutY(150);
		
		patternDurationField = new TextField();
		patternDurationField.setMinSize(40, 20);
		patternDurationField.setMaxSize(40, 20);
		patternDurationField.setPrefSize(40, 20);
		patternDurationField.setFont(TestUI.labelFont);
		patternDurationField.setLayoutX(80);
		patternDurationField.setLayoutY(150);
		patternDurationField.setFocusTraversable(false);
		patternDurationField.setOnKeyReleased((e) -> {
			try {
				sequence.setPatternDuration(new Double(patternDurationField.getText()));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button patternDurationIncrease = new Button("+");
		patternDurationIncrease.setMinSize(20, 20);
		patternDurationIncrease.setMaxSize(20, 20);
		patternDurationIncrease.setPrefSize(20, 20);
		patternDurationIncrease.setFont(TestUI.labelFont);
		patternDurationIncrease.setLayoutX(120);
		patternDurationIncrease.setLayoutY(150);
		patternDurationIncrease.setFocusTraversable(false);
		patternDurationIncrease.setOnAction((e) -> {
			try {
				int increasedValue = new Integer(patternDurationField.getText()) + 1;
				sequence.setPatternDuration(increasedValue);
				patternDurationField.setText(String.valueOf(increasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button patternDurationDecrease = new Button("-");
		patternDurationDecrease.setMinSize(20, 20);
		patternDurationDecrease.setMaxSize(20, 20);
		patternDurationDecrease.setPrefSize(20, 20);
		patternDurationDecrease.setFont(TestUI.labelFont);
		patternDurationDecrease.setLayoutX(140);
		patternDurationDecrease.setLayoutY(150);
		patternDurationDecrease.setFocusTraversable(false);
		patternDurationDecrease.setOnAction((e) -> {
			try {
				int decreasedValue = new Integer(patternDurationField.getText()) - 1;
				sequence.setPatternDuration(decreasedValue);
				patternDurationField.setText(String.valueOf(decreasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		RadioButton reverseRadio = new RadioButton("REVERSE");
		reverseRadio.setMinSize(140, 20);
		reverseRadio.setMaxSize(140, 20);
		reverseRadio.setPrefSize(140, 20);
		reverseRadio.setFont(TestUI.labelFont);
		reverseRadio.setTextFill(Color.BEIGE);
		reverseRadio.setLayoutX(0);
		reverseRadio.setLayoutY(175);
		reverseRadio.setFocusTraversable(false);
		reverseRadio.setOnAction((e) -> {
			sequence.setSequenceMode(SequenceMode.REVERSE);
		});
		
		RadioButton randomRadio = new RadioButton("RANDOM");
		randomRadio.setMinSize(140, 20);
		randomRadio.setMaxSize(140, 20);
		randomRadio.setPrefSize(140, 20);
		randomRadio.setFont(TestUI.labelFont);
		randomRadio.setTextFill(Color.BEIGE);
		randomRadio.setLayoutX(0);
		randomRadio.setLayoutY(200);
		randomRadio.setFocusTraversable(false);
		randomRadio.setOnAction((e) -> {
			sequence.setSequenceMode(SequenceMode.RANDOM);
		});
		
		RadioButton randomNoRepeatRadio = new RadioButton("RANDOM NO REPEAT");
		randomNoRepeatRadio.setMinSize(140, 20);
		randomNoRepeatRadio.setMaxSize(140, 20);
		randomNoRepeatRadio.setPrefSize(140, 20);
		randomNoRepeatRadio.setFont(TestUI.labelFont);
		randomNoRepeatRadio.setTextFill(Color.BEIGE);
		randomNoRepeatRadio.setLayoutX(0);
		randomNoRepeatRadio.setLayoutY(225);
		randomNoRepeatRadio.setFocusTraversable(false);
		randomNoRepeatRadio.setOnAction((e) -> {
			sequence.setSequenceMode(SequenceMode.RANDOM_NO_REPEAT);
		});
		
		Label noRepeatMemoryLabel = new Label("no rep.mem.");
		noRepeatMemoryLabel.setMinSize(70, 20);
		noRepeatMemoryLabel.setMaxSize(70, 20);
		noRepeatMemoryLabel.setPrefSize(70, 20);
		noRepeatMemoryLabel.setFont(TestUI.labelFont);
		noRepeatMemoryLabel.setTextFill(Color.BEIGE);
		noRepeatMemoryLabel.setLayoutX(0);
		noRepeatMemoryLabel.setLayoutY(250);
		
		noRepeatMemoryField = new TextField();
		noRepeatMemoryField.setMinSize(40, 20);
		noRepeatMemoryField.setMaxSize(40, 20);
		noRepeatMemoryField.setPrefSize(40, 20);
		noRepeatMemoryField.setFont(TestUI.labelFont);
		noRepeatMemoryField.setLayoutX(80);
		noRepeatMemoryField.setLayoutY(250);
		noRepeatMemoryField.setFocusTraversable(false);
		noRepeatMemoryField.setOnKeyReleased((e) -> {
			try {
				sequence.setNoRepeatMemory(new Integer(noRepeatMemoryField.getText()));				
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button noRepeatMemoryIncrease = new Button("+");
		noRepeatMemoryIncrease.setMinSize(20, 20);
		noRepeatMemoryIncrease.setMaxSize(20, 20);
		noRepeatMemoryIncrease.setPrefSize(20, 20);
		noRepeatMemoryIncrease.setFont(TestUI.labelFont);
		noRepeatMemoryIncrease.setLayoutX(120);
		noRepeatMemoryIncrease.setLayoutY(250);
		noRepeatMemoryIncrease.setFocusTraversable(false);
		noRepeatMemoryIncrease.setOnAction((e) -> {
			try {
				int increasedValue = new Integer(noRepeatMemoryField.getText()) + 1;
				sequence.setNoRepeatMemory(increasedValue);
				noRepeatMemoryField.setText(String.valueOf(increasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		Button noRepeatMemoryDecrease = new Button("-");
		noRepeatMemoryDecrease.setMinSize(20, 20);
		noRepeatMemoryDecrease.setMaxSize(20, 20);
		noRepeatMemoryDecrease.setPrefSize(20, 20);
		noRepeatMemoryDecrease.setFont(TestUI.labelFont);
		noRepeatMemoryDecrease.setLayoutX(140);
		noRepeatMemoryDecrease.setLayoutY(250);
		noRepeatMemoryDecrease.setFocusTraversable(false);
		noRepeatMemoryDecrease.setOnAction((e) -> {
			try {
				int decreasedValue = new Integer(noRepeatMemoryField.getText()) - 1;
				sequence.setNoRepeatMemory(decreasedValue);
				noRepeatMemoryField.setText(String.valueOf(decreasedValue));
			} catch (NumberFormatException ex) { System.out.println("e"); }
		});
		
		RadioButton randomWalkRadio = new RadioButton("RANDOM WALK");
		randomWalkRadio.setMinSize(140, 20);
		randomWalkRadio.setMaxSize(140, 20);
		randomWalkRadio.setPrefSize(140, 20);
		randomWalkRadio.setFont(TestUI.labelFont);
		randomWalkRadio.setTextFill(Color.BEIGE);
		randomWalkRadio.setLayoutX(0);
		randomWalkRadio.setLayoutY(275);
		randomWalkRadio.setFocusTraversable(false);
		randomWalkRadio.setOnAction((e) -> {
			sequence.setSequenceMode(SequenceMode.RANDOM_WALK);
		});
		
		sequenceModeGroup = new ToggleGroup();
		sequenceModeGroup.getToggles().add(normalRadio);
		sequenceModeGroup.getToggles().add(timeSensitiveRadio);
		sequenceModeGroup.getToggles().add(reverseRadio);
		sequenceModeGroup.getToggles().add(randomRadio);
		sequenceModeGroup.getToggles().add(randomNoRepeatRadio);
		sequenceModeGroup.getToggles().add(randomWalkRadio);
		
		Group sequenceControlsUIGroup = new Group();
		sequenceControlsUIGroup.setLayoutX(120);
		sequenceControlsUIGroup.setLayoutY(10);
		
		sequenceControlsUIGroup.getChildren().add(startOffsetLabel);
		sequenceControlsUIGroup.getChildren().add(startOffsetField);
		sequenceControlsUIGroup.getChildren().add(startOffsetIncrease);
		sequenceControlsUIGroup.getChildren().add(startOffsetDecrease);
		sequenceControlsUIGroup.getChildren().add(endOffsetLabel);
		sequenceControlsUIGroup.getChildren().add(endOffsetField);
		sequenceControlsUIGroup.getChildren().add(endOffsetIncrease);
		sequenceControlsUIGroup.getChildren().add(endOffsetDecrease);
		sequenceControlsUIGroup.getChildren().add(localOffsetLabel);
		sequenceControlsUIGroup.getChildren().add(localOffsetField);
		sequenceControlsUIGroup.getChildren().add(localOffsetIncrease);
		sequenceControlsUIGroup.getChildren().add(localOffsetDecrease);
		sequenceControlsUIGroup.getChildren().add(lengthLabel);
		sequenceControlsUIGroup.getChildren().add(lengthField);
		sequenceControlsUIGroup.getChildren().add(lengthIncrease);
		sequenceControlsUIGroup.getChildren().add(lengthDecrease);
		sequenceControlsUIGroup.getChildren().add(normalRadio);
		sequenceControlsUIGroup.getChildren().add(timeSensitiveRadio);
		sequenceControlsUIGroup.getChildren().add(patternDurationLabel);
		sequenceControlsUIGroup.getChildren().add(patternDurationField);
		sequenceControlsUIGroup.getChildren().add(patternDurationIncrease);
		sequenceControlsUIGroup.getChildren().add(patternDurationDecrease);
		sequenceControlsUIGroup.getChildren().add(reverseRadio);
		sequenceControlsUIGroup.getChildren().add(randomRadio);
		sequenceControlsUIGroup.getChildren().add(randomNoRepeatRadio);
		sequenceControlsUIGroup.getChildren().add(noRepeatMemoryLabel);
		sequenceControlsUIGroup.getChildren().add(noRepeatMemoryField);
		sequenceControlsUIGroup.getChildren().add(noRepeatMemoryIncrease);
		sequenceControlsUIGroup.getChildren().add(noRepeatMemoryDecrease);
		sequenceControlsUIGroup.getChildren().add(randomWalkRadio);
		
		Button addNumberField = new Button("+");
		addNumberField.setMinSize(20, 20);
		addNumberField.setMaxSize(20, 20);
		addNumberField.setPrefSize(20, 20);
		addNumberField.setFont(TestUI.labelFont);
		addNumberField.setTranslateX(90);
		addNumberField.setTranslateY(20);
		addNumberField.setFocusTraversable(false);
		addNumberField.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				addNumberField(true);
			}
		});
		
		Button removeNumberField = new Button("-");
		removeNumberField.setMinSize(20, 20);
		removeNumberField.setMaxSize(20, 20);
		removeNumberField.setPrefSize(20, 20);
		removeNumberField.setFont(TestUI.labelFont);
		removeNumberField.setTranslateX(70);
		removeNumberField.setTranslateY(20);
		removeNumberField.setFocusTraversable(false);
		removeNumberField.addEventHandler(ActionEvent.ACTION, (event) -> {
			if (numberFieldCount > 1) {
				removeNumberField();
			}
		});
		
		getChildren().add(addNumberField);
		getChildren().add(removeNumberField);
		getChildren().add(numberFieldsUIGroup);
		getChildren().add(sequenceControlsUIGroup);
	}

	public Sequence getSequence() {
		return sequence;
	}
	
	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
		
		for (int i = 0; i < sequence.size(); i++) {
			addNumberField(false);
			numberFields.get(i).getValueSupplierField().setValueSupplier(sequence.getValueSupplier(i));
			numberFields.get(i).getValueSupplierField().setText(sequence.getValueSupplierInfo(i));
			startOffsetField.setText(String.valueOf(sequence.getStartOffset()));
			endOffsetField.setText(String.valueOf(sequence.getEndOffset()));
			localOffsetField.setText(String.valueOf(sequence.getLocalOffset()));
			patternDurationField.setText(String.valueOf(sequence.getPatternDuration()));
			int length = sequence.getLength();
			if (length != Integer.MAX_VALUE) {
				lengthField.setText(String.valueOf(length));
			} else {
				lengthField.setText("inf");
			}
			noRepeatMemoryField.setText(String.valueOf(sequence.getNoRepeatMemory()));
			SequenceMode sequenceMode = sequence.getSequenceMode();
			String sequenceModeString = "";
			if (sequenceMode == SequenceMode.NORMAL)
				sequenceModeString = "NORMAL";
			else if (sequenceMode == SequenceMode.TIME_SENSITIVE)
				sequenceModeString = "TIME SENSITIVE";
			else if (sequenceMode == SequenceMode.NORMAL)
				sequenceModeString = "REVERSE";
			else if (sequenceMode == SequenceMode.NORMAL)
				sequenceModeString = "RANDOM";
			else if (sequenceMode == SequenceMode.NORMAL)
				sequenceModeString = "RANDOM NO REPEAT";
			else if (sequenceMode == SequenceMode.NORMAL)
				sequenceModeString = "RANDOM WALK";
			for (Toggle toggle : sequenceModeGroup.getToggles()) {
				RadioButton radio = (RadioButton) toggle;
				if (radio.getText().equals(sequenceModeString)) {
					radio.setSelected(true);
					break;
				}
			}
		}
	}
	
	public void addNumberField(boolean createValueSupplier) {
		NumberField numberField = new NumberField();
		numberFields.add(numberField);
		if (createValueSupplier) {			
			ValueSupplier valueSupplier = new ConstantPattern(0, String.valueOf(numberFieldCount));
			sequence.addValueSupplier(valueSupplier);
			numberField.getValueSupplierField().setValueSupplier(valueSupplier);
		}
		numberField.refresh();
		numberFieldsUIGroup.getChildren().add(numberField);
		numberFieldCount++;
	}
	
	public void removeNumberField() {
		if (numberFieldCount <= 1) { 
			System.out.println("List cannot be empty");
			return; 
		}
		sequence.removeValueSupplier(numberFieldCount-1);
		numberFieldsUIGroup.getChildren().remove(numberFields.get(numberFieldCount-1));
		numberFields.remove(numberFieldCount-1);
		numberFieldCount--;
	}
	
	class NumberField extends Group {
		private Rectangle handle;
		private Label label;
		private ValueSupplierField valueSupplierField;
		private double height = 20;
		
		public NumberField() {			
			handle = new Rectangle(20, height);
			handle.setFill(Color.BEIGE);
			handle.addEventHandler(MouseEvent.ANY, new NumberFieldMouseHandler());
			
			label = new Label();
			label.setFont(TestUI.labelFont);
			label.setMinSize(20, height);
			label.setMaxSize(20, height);
			label.setPrefSize(20, height);
			label.setAlignment(Pos.CENTER);
			label.setMouseTransparent(true);
			
			valueSupplierField = new ValueSupplierField();
			valueSupplierField.setValueSupplierHost(sequence);
			valueSupplierField.setLayoutX(20);
			
			getChildren().add(handle);
			getChildren().add(label);
			getChildren().add(valueSupplierField);
		}
		
		public void refresh() {
			label.setText(String.valueOf(getPosition() + 0));
			setTranslateY(getPosition() * height);
		}
		
		public int getPosition() {
			return numberFields.indexOf(this);
		}
		
		public ValueSupplierField getValueSupplierField() {
			return valueSupplierField;
		}
		
		class NumberFieldMouseHandler implements EventHandler<MouseEvent> {
			public void handle(MouseEvent e) {
				if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
					handle.setFill(Color.GREENYELLOW);
				} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
					handle.setFill(Color.BEIGE);
				} else if (e.getEventType() == MouseEvent.DRAG_DETECTED) {
					// change handler entirely!
					handle.removeEventHandler(MouseEvent.ANY, this);
					handle.addEventHandler(MouseEvent.ANY, new NumberFieldDragHandler());
				}
			}
		}
		
		class NumberFieldDragHandler implements EventHandler<MouseEvent> {
			double newY;
			int newPosition;
			int origPosition;
			Line insertIndicator;
			
			public NumberFieldDragHandler() {
				newPosition = origPosition = getPosition();
				insertIndicator = new Line(0, -1, numberFieldsUIGroup.getBoundsInLocal().getWidth(), -1);
				insertIndicator.setStroke(Color.LIMEGREEN);
				insertIndicator.setStrokeWidth(2);
				insertIndicator.setTranslateY(newPosition * height);
				insertIndicator.setEffect(new GaussianBlur(3));
				
				// might be added at first "dragged" event.
				numberFieldsUIGroup.getChildren().add(insertIndicator);
			}
			
			public void handle(MouseEvent e) {
				if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					newY = e.getY() + origPosition * height;
					if (newY < height * 0.5) {
						newPosition = 0;
					} else if (newY > numberFieldCount * height - height * 0.5) {
						newPosition = numberFieldCount;
					} else {
						newPosition = (int) ((newY + height * 0.5) / height);
					}
					insertIndicator.setTranslateY(newPosition * height);
					
				} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {					
					if (newPosition < origPosition) {
						NumberField numberField = numberFields.remove(origPosition);
						ValueSupplier valueSupplier = sequence.removeValueSupplier(origPosition);
						numberFields.add(newPosition, numberField);
						sequence.add(newPosition, valueSupplier);
						for (NumberField nf : numberFields) { nf.refresh(); }
						numberFieldsUIGroup.getChildren().clear(); // workaround - forcing tab focus order
						numberFieldsUIGroup.getChildren().addAll(numberFields); // workaround - forcing tab focus order
					} else if (newPosition > origPosition + 1) {
						NumberField numberField = numberFields.remove(origPosition);
						ValueSupplier valueSupplier = sequence.removeValueSupplier(origPosition);
						numberFields.add(newPosition - 1, numberField);
						sequence.add(newPosition - 1, valueSupplier);
						for (NumberField nf : numberFields) { nf.refresh(); }
						numberFieldsUIGroup.getChildren().clear(); // workaround - forcing tab focus order
						numberFieldsUIGroup.getChildren().addAll(numberFields); // workaround - forcing tab focus order
					}
					
					handle.setFill(Color.BEIGE);
					numberFieldsUIGroup.getChildren().remove(insertIndicator);
					
					// change back to normal handler.
					handle.removeEventHandler(MouseEvent.ANY, this);
					handle.addEventHandler(MouseEvent.ANY, new NumberFieldMouseHandler());
				}
			}
		}
	}
}