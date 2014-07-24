package no.simonsen.gui;

import no.simonsen.data.Sequence;
import no.simonsen.midi.ConstantPattern;
import no.simonsen.midi.ValueSupplier;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 * The ValueSupplierField is responsible for creating a ValueSupplier and registering it with its
 * associated host.
 * 
 * Entering numbers in the field will generate a constant pattern with that value and register it,
 * activating the popupmenu provides the option of more complex patterns to be associated with the host.
 * 
 * With the ValueSupplierField, each ValueSupplier is linked to a host which knows how to use it. Hosts
 * have to implement ValueSupplierHost which has functions for managing ValueSuppliers.
 */
public class ValueSupplierField extends Group {
	private ValueSupplierHost valueSupplierHost;
	private ValueSupplier valueSupplier;
	private String valueSupplierId;
	private TextField numberField;
	private PopupMenuUI popupMenu;
	
	/*
	 * ValueSupplierField should NOT create a ValueSupplier when it is instantiated - however, by associating
	 * it with a ValueSupplierHost it is possible to replace one ValueSupplier for another. Look into this.
	 */
	
	public ValueSupplierField() {
		numberField = new TextField();
		numberField.setFont(TestUI.labelFont);
		numberField.setAlignment(Pos.CENTER);
		numberField.setPrefSize(80, 20);
		numberField.setOnKeyPressed((e) -> {
			try {
				if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB) {	
					numberField.setText(String.valueOf(valueSupplier.toString()));
				} else {
					Platform.runLater(() -> {
						try {
							double value = new Double(numberField.getText());
							ValueSupplier newSupplier = new ConstantPattern(value, valueSupplierId);
							valueSupplierHost.replaceValueSupplier(valueSupplier, newSupplier);
							valueSupplier = newSupplier;
						} catch (NumberFormatException ex) {
							
						}
					});
				}
			} catch(NumberFormatException ex) { }
		});
		
		popupMenu = new PopupMenuUI(12);
		popupMenu.setLayoutX(numberField.getPrefWidth());
		popupMenu.setLayoutY(10);
		popupMenu.addMenuItem("add sequence...", (e) -> {
			SequenceUI sequenceUI = new SequenceUI();
			Sequence sequence = new Sequence(valueSupplierId);
			sequenceUI.setSequence(sequence);
			numberField.setText(sequenceUI.getSequence().toString());
			ValueSupplier newSupplier = sequenceUI.getSequence();
			valueSupplierHost.replaceValueSupplier(valueSupplier, newSupplier);
			valueSupplier = newSupplier;
			new EditWindow(sequenceUI, popupMenu.getLayoutX() + 20, popupMenu.getLayoutY() - 20);			
		});
		
		popupMenu.addMenuItem("edit...", (e) -> {
			if (valueSupplier instanceof Sequence) {
				Sequence sequence = (Sequence) valueSupplier;
				SequenceUI listPatternUI = new SequenceUI();
				listPatternUI.setSequence(sequence);
				new EditWindow(listPatternUI, popupMenu.getLayoutX() + 20, popupMenu.getLayoutY() - 20);
			}
		});
		
		getChildren().add(numberField);
		getChildren().add(popupMenu);
	}
	
	public void setValueSupplierHost(ValueSupplierHost host) {
		valueSupplierHost = host;
	}
	
	public ValueSupplier getValueSupplier() {
		return valueSupplier;
	}
	
	public void setValueSupplier(ValueSupplier valueSupplier) {
		this.valueSupplier = valueSupplier;
		numberField.setText(String.valueOf(valueSupplier.toString()));
	}
	
	public void setText(String value) {
		numberField.setText(value);
	}
}
