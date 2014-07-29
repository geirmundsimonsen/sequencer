package no.simonsen.gui.elements;

import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import no.simonsen.gui.TestUI;

public class ParameterElement extends Group {
	private Label label;
	private TextField field;
	private Button increase;
	private Button decrease;
	private Border errorBorder;
	
	private ParameterElement(String text) {
		double height = 20;
		label = new Label(text);
		label.setMinSize(70, height); label.setMaxSize(70, height); label.setPrefSize(70, height);
		label.setFont(TestUI.labelFont);
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
		
		errorBorder = new Border(new BorderStroke(Color.hsb(0, 1, 1, 1), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN));
		
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
					if (field.getBorder() != null)
						field.setBorder(null);
				} catch (NumberFormatException ex) {
					if (field.getBorder() == null)
						field.setBorder(errorBorder);
				}
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
					if (field.getBorder() != null)
						field.setBorder(null);
				} catch (NumberFormatException ex) { 
					if (field.getBorder() == null)
						field.setBorder(errorBorder);
				}
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