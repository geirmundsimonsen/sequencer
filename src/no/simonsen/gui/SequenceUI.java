package no.simonsen.gui;

import java.util.ArrayList;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

import no.simonsen.data.Sequence;
import no.simonsen.data.SequenceMode;
import no.simonsen.midi.ConstantPattern;
import no.simonsen.midi.ValueSupplier;
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

public class SequenceUI extends Group {
	private Sequence sequence;
	private ArrayList<NumberField> numberFields = new ArrayList<>();
	private Group numberFieldsUIGroup;
	private int numberFieldCount = 0;
	private ParameterElement startOffsetElement;
	private ParameterElement endOffsetElement;
	private ParameterElement localOffsetElement;
	private ParameterElement lengthElement;
	private ParameterElement patternDurationElement;
	private ParameterElement noRepeatMemoryElement;
	private ToggleGroup sequenceModeGroup;
	
	public SequenceUI() {				
		Rectangle rectangle = new Rectangle(290, 400);
		rectangle.setFill(Color.BLACK);
		getChildren().add(rectangle);
		
		numberFieldsUIGroup = new Group();
		numberFieldsUIGroup.setTranslateX(10);
		numberFieldsUIGroup.setTranslateY(40);
		
		sequenceModeGroup = new ToggleGroup();
		
		startOffsetElement = new ParameterElement("start off.", (value) -> { sequence.setStartOffset(value); }, null);
		startOffsetElement.setLayoutY(0);
		endOffsetElement = new ParameterElement("end off.", (value) -> { sequence.setEndOffset(value); }, null);
		endOffsetElement.setLayoutY(25);
		localOffsetElement = new ParameterElement("local off.", (value) -> { sequence.setLocalOffset(value); }, null);
		localOffsetElement.setLayoutY(50);
		lengthElement = new ParameterElement("length", (value) -> { sequence.setLength(value); }, null);
		lengthElement.setLayoutY(75);
		ModeElement normalModeElement = new ModeElement("NORMAL", sequenceModeGroup, SequenceMode.NORMAL);
		normalModeElement.setLayoutY(100);
		ModeElement timeSensitiveModeElement = new ModeElement("TIME SENSITIVE", sequenceModeGroup, SequenceMode.TIME_SENSITIVE);
		timeSensitiveModeElement.setLayoutY(125);
		patternDurationElement = new ParameterElement("duration", null, (value) -> { sequence.setPatternDuration(value); });
		patternDurationElement.setLayoutY(150);
		ModeElement reverseModeElement = new ModeElement("REVERSE", sequenceModeGroup, SequenceMode.REVERSE);
		reverseModeElement.setLayoutY(175);
		ModeElement randomModeElement = new ModeElement("RANDOM", sequenceModeGroup, SequenceMode.RANDOM);
		randomModeElement.setLayoutY(200);	
		ModeElement randomNoRepeatModeElement = new ModeElement("RANDOM NO REPEAT", sequenceModeGroup, SequenceMode.RANDOM_NO_REPEAT);
		randomNoRepeatModeElement.setLayoutY(225);
		noRepeatMemoryElement = new ParameterElement("no mem rep", (value) -> { sequence.setNoRepeatMemory(value); }, null);
		noRepeatMemoryElement.setLayoutY(250);
		ModeElement randomWalkModeElement = new ModeElement("RANDOM WALK", sequenceModeGroup, SequenceMode.RANDOM_WALK);
		randomWalkModeElement.setLayoutY(275);
		
		Group sequenceControlsUIGroup = new Group();
		sequenceControlsUIGroup.setLayoutX(120);
		sequenceControlsUIGroup.setLayoutY(10);
		sequenceControlsUIGroup.getChildren().add(startOffsetElement);
		sequenceControlsUIGroup.getChildren().add(endOffsetElement);
		sequenceControlsUIGroup.getChildren().add(localOffsetElement);
		sequenceControlsUIGroup.getChildren().add(lengthElement);
		sequenceControlsUIGroup.getChildren().add(normalModeElement);
		sequenceControlsUIGroup.getChildren().add(timeSensitiveModeElement);
		sequenceControlsUIGroup.getChildren().add(patternDurationElement);
		sequenceControlsUIGroup.getChildren().add(reverseModeElement);
		sequenceControlsUIGroup.getChildren().add(randomModeElement);
		sequenceControlsUIGroup.getChildren().add(randomNoRepeatModeElement);
		sequenceControlsUIGroup.getChildren().add(noRepeatMemoryElement);
		sequenceControlsUIGroup.getChildren().add(randomWalkModeElement);
		
		Button addNumberField = new Button("+");
		addNumberField.setMinSize(20, 20); addNumberField.setMaxSize(20, 20); addNumberField.setPrefSize(20, 20);
		addNumberField.setFont(TestUI.labelFont);
		addNumberField.setTranslateX(90);
		addNumberField.setTranslateY(20);
		addNumberField.setFocusTraversable(false);
		addNumberField.setOnAction((e) -> { addNumberField(true); });
		
		Button removeNumberField = new Button("-");
		removeNumberField.setMinSize(20, 20); removeNumberField.setMaxSize(20, 20); removeNumberField.setPrefSize(20, 20);
		removeNumberField.setFont(TestUI.labelFont);
		removeNumberField.setTranslateX(70);
		removeNumberField.setTranslateY(20);
		removeNumberField.setFocusTraversable(false);
		removeNumberField.setOnAction((e) -> { if (numberFieldCount > 1) removeNumberField(); });
		
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
			startOffsetElement.setText(String.valueOf(sequence.getStartOffset()));
			endOffsetElement.setText(String.valueOf(sequence.getEndOffset()));
			localOffsetElement.setText(String.valueOf(sequence.getLocalOffset()));
			patternDurationElement.setText(String.valueOf(sequence.getPatternDuration()));
			int length = sequence.getLength();
			if (length != Integer.MAX_VALUE) {
				lengthElement.setText(String.valueOf(length));
			} else {
				lengthElement.setText("inf");
			}
			noRepeatMemoryElement.setText(String.valueOf(sequence.getNoRepeatMemory()));
			SequenceMode sequenceMode = sequence.getSequenceMode();
			String sequenceModeString = "";
			if (sequenceMode == SequenceMode.NORMAL)
				sequenceModeString = "NORMAL";
			else if (sequenceMode == SequenceMode.TIME_SENSITIVE)
				sequenceModeString = "TIME SENSITIVE";
			else if (sequenceMode == SequenceMode.REVERSE)
				sequenceModeString = "REVERSE";
			else if (sequenceMode == SequenceMode.RANDOM)
				sequenceModeString = "RANDOM";
			else if (sequenceMode == SequenceMode.RANDOM_NO_REPEAT)
				sequenceModeString = "RANDOM NO REPEAT";
			else if (sequenceMode == SequenceMode.RANDOM_WALK)
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
	
	class ParameterElement extends Group {
		Label label;
		TextField field;
		Button increase;
		Button decrease;
		
		private ParameterElement(String text) {
			double height = 20;
			label = new Label(text);
			label.setMinSize(70, height); label.setMaxSize(70, height); label.setPrefSize(70, height);
			label.setFont(TestUI.labelFont);
			label.setTextFill(Color.BEIGE);
			label.setLayoutX(0);
			
			field = new TextField();
			field.setMinSize(40, height); field.setMaxSize(40, height); field.setPrefSize(40, height);
			field.setFont(TestUI.labelFont);
			field.setLayoutX(80);
			field.setFocusTraversable(false);
			
			increase = new Button("+");
			increase.setMinSize(20, height); increase.setMaxSize(20, height); increase.setPrefSize(20, height);
			increase.setFont(TestUI.labelFont);
			increase.setLayoutX(120);
			increase.setFocusTraversable(false);
			
			decrease = new Button("-");
			decrease.setMinSize(20, height); decrease.setMaxSize(20, height); decrease.setPrefSize(20, height);
			decrease.setFont(TestUI.labelFont);
			decrease.setLayoutX(140);
			decrease.setFocusTraversable(false);
			
			getChildren().add(label);
			getChildren().add(field);
			getChildren().add(increase);
			getChildren().add(decrease);
		}
		
		public ParameterElement(String text, IntConsumer setParameterIfInt, DoubleConsumer setParameterIfDouble) {
			this(text);
			
			if (setParameterIfInt != null && setParameterIfDouble == null) {
				field.setOnKeyReleased((e) -> {
					try {
						setParameterIfInt.accept(new Integer(field.getText()));
					} catch (NumberFormatException ex) { }
				});

				increase.setOnAction((e) -> {
					try {
						Integer increasedValue = new Integer(field.getText()) + 1;
						setParameterIfInt.accept(increasedValue);
						field.setText(String.valueOf(increasedValue));
					} catch (NumberFormatException ex) { }
				});

				decrease.setOnAction((e) -> {
					try {
						Integer decreasedValue = new Integer(field.getText()) - 1;
						setParameterIfInt.accept(decreasedValue);
						field.setText(String.valueOf(decreasedValue));
					} catch (NumberFormatException ex) { }
				});
			} else if (setParameterIfInt == null && setParameterIfDouble != null) {
				field.setOnKeyReleased((e) -> {
					try {
						setParameterIfDouble.accept(new Double(field.getText()));
					} catch (NumberFormatException ex) { }
				});

				increase.setOnAction((e) -> {
					try {
						Double increasedValue = new Double(field.getText()) + 1;
						setParameterIfDouble.accept(increasedValue);
						field.setText(String.valueOf(increasedValue));
					} catch (NumberFormatException ex) { }
				});

				decrease.setOnAction((e) -> {
					try {
						Double decreasedValue = new Double(field.getText()) - 1;
						setParameterIfDouble.accept(decreasedValue);
						field.setText(String.valueOf(decreasedValue));
					} catch (NumberFormatException ex) { }
				});
			} else {
				System.out.println("SequenceUI.ParameterElement API Error: Only one function can be specified.");
				System.exit(1);
			}
		}
		
		public void setText(String text) {
			field.setText(text);
		}
	}
	
	class ModeElement extends Group {
		public ModeElement(String label, ToggleGroup toggleGroup, SequenceMode sequenceMode) {
			RadioButton radio = new RadioButton(label);
			radio.setMinSize(140, 20); radio.setMaxSize(140, 20); radio.setPrefSize(140, 20);
			radio.setFont(TestUI.labelFont);
			radio.setTextFill(Color.BEIGE);
			radio.setFocusTraversable(false);
			radio.setOnAction((e) -> {
				sequence.setSequenceMode(sequenceMode);
			});
			getChildren().add(radio);
			toggleGroup.getToggles().add(radio);
		}
	}
}