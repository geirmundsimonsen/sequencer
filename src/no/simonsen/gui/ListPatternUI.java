package no.simonsen.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import no.simonsen.midi.ListPattern;
import no.simonsen.midi.PatternMediator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/*
 *  ___________
 * |           |
 * |  _____-+  |
 * | |_______| |  <- room for 6 digits
 * | |_______| |
 * | |_______| |
 * | |_______| |
 * | |_______| |
 * | norm ◎   |
 * | rev ­­­◉    |
 * | rand ◎   |
 * | time ◎   |
 * |___________|
 * The UI representation of a ListPattern/PatternMediator is roughly like above.
 * As a convenience, TAB or ENTER can be used to further expand the array, and
 * BACKSPACE contracts it, in addition to the explicit +/- buttons on top.
 * Radio buttons mark the mode of the pattern.
 * Value nodes are draggable, drag can either reorder or swap.
 */
public class ListPatternUI extends Group {
	private ListPattern listPattern;
	private PatternMediator patternMediator;
	private ArrayList<NumberField> numberFields = new ArrayList<>();
	private Group numberFieldsUIGroup;
	private int numberFieldCount = 0;
	private Font labelFont;
	private Font valueFont;
	
	public ListPatternUI() {
		listPattern = new ListPattern();
		
		labelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/TypewriterScribbled.ttf"), 10);
		valueFont = Font.loadFont(getClass().getResourceAsStream("/fonts/TypewriterScribbled.ttf"), 15);
		
		Rectangle rectangle = new Rectangle(200, 400);
		rectangle.setFill(Color.BLACK);
		getChildren().add(rectangle);
		
		numberFieldsUIGroup = new Group();
		numberFieldsUIGroup.setTranslateX(10);
		numberFieldsUIGroup.setTranslateY(40);
		
		Button addNumberField = new Button("+");
		addNumberField.setMinSize(20, 20);
		addNumberField.setMaxSize(20, 20);
		addNumberField.setPrefSize(20, 20);
		addNumberField.setFont(labelFont);
		addNumberField.setTranslateX(90);
		addNumberField.setTranslateY(20);
		addNumberField.setFocusTraversable(false);
		addNumberField.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				NumberField numberField = new NumberField("0");
				numberFields.add(numberField);
				numberField.refresh();
				listPattern.addValue(0);
				numberFieldsUIGroup.getChildren().add(numberField);
				numberFieldCount++;
			}
		});
		
		Button removeNumberField = new Button("-");
		removeNumberField.setMinSize(20, 20);
		removeNumberField.setMaxSize(20, 20);
		removeNumberField.setPrefSize(20, 20);
		removeNumberField.setFont(labelFont);
		removeNumberField.setTranslateX(70);
		removeNumberField.setTranslateY(20);
		removeNumberField.setFocusTraversable(false);
		removeNumberField.addEventHandler(ActionEvent.ACTION, (event) -> {
			if (numberFieldCount <= 1) { return; }
			numberFieldsUIGroup.getChildren().remove(numberFields.get(numberFieldCount-1));
			numberFields.remove(numberFieldCount-1);
			listPattern.removeValue(numberFieldCount-1);
			numberFieldCount--;
		});
		
		addNumberField.fire();
		getChildren().add(addNumberField);
		getChildren().add(removeNumberField);
		getChildren().add(numberFieldsUIGroup);
	}
	
	class NumberField extends Group {
		private Rectangle handle;
		private Label label;
		private TextField textField;
		private double height = 20;
		
		public NumberField(String value) {			
			handle = new Rectangle(20, height);
			handle.setFill(Color.BEIGE);
			handle.addEventHandler(MouseEvent.ANY, new NumberFieldMouseHandler());
			
			label = new Label();
			label.setFont(labelFont);
			label.setMinSize(20, height);
			label.setMaxSize(20, height);
			label.setPrefSize(20, height);
			label.setAlignment(Pos.CENTER);
			label.setMouseTransparent(true);
			
			textField = new TextField(value);
			textField.getStyleClass().add("valueField");
			textField.setPrefSize(80, height);
			textField.setMinSize(80, height);
			textField.setMaxSize(80, height);
			textField.setTranslateX(20);
			textField.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
				if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
					try {
						listPattern.setValue(getPosition(), new Double(textField.getText()));
					} catch (NumberFormatException ex) { /*ignore*/ }
				}
			});
			
			getChildren().add(handle);
			getChildren().add(label);
			getChildren().add(textField);
		}
		
		public void refresh() {
			label.setText(String.valueOf(getPosition() + 0));
			setTranslateY(getPosition() * height);
		}
		
		public int getPosition() {
			return numberFields.indexOf(this);
		}

		public void addTextKeyEventHandler(EventType<KeyEvent> eventType, EventHandler<? super KeyEvent> handler) {
			textField.addEventHandler(eventType, handler);
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
						Double value = listPattern.removeValue(origPosition);
						numberFields.add(newPosition, numberField);
						listPattern.addValue(newPosition, value);
						for (NumberField nf : numberFields) { nf.refresh(); }
						numberFieldsUIGroup.getChildren().clear(); // workaround - forcing tab focus order
						numberFieldsUIGroup.getChildren().addAll(numberFields); // workaround - forcing tab focus order
					} else if (newPosition > origPosition + 1) {
						NumberField numberField = numberFields.remove(origPosition);
						Double value = listPattern.removeValue(origPosition);
						numberFields.add(newPosition - 1, numberField);
						listPattern.addValue(newPosition - 1, value);
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
