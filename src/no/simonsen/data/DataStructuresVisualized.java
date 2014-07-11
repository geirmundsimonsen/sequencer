package no.simonsen.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import no.simonsen.midi.ConstantPattern;
import no.simonsen.midi.RandomPattern;
import no.simonsen.midi.ValueSupplier;

public class DataStructuresVisualized {
	public static void main(String[] args) {
		Sequence seq = new Sequence();
		
		List<ValueSupplier> mainList = new ArrayList<>();
		
		Sequence subSeq1 = new Sequence();
		List<ValueSupplier> subSeq1list = new ArrayList<>();
		subSeq1list.add(new ConstantPattern(1));
		subSeq1list.add(new ConstantPattern(2));
		subSeq1list.add(new ConstantPattern(3));
		subSeq1list.add(new ConstantPattern(4));
		subSeq1list.add(new ConstantPattern(5));
		subSeq1list.add(new ConstantPattern(6));
		subSeq1.setSequence(subSeq1list);
		subSeq1.setLength(4);
		subSeq1.setSequenceMode(SequenceMode.RANDOM_WALK);

		Sequence subSeq2 = new Sequence();
		List<ValueSupplier> subSeq2list = new ArrayList<>();
		subSeq2list.add(new ConstantPattern(7));
		subSeq2list.add(new ConstantPattern(8));
		subSeq2list.add(new ConstantPattern(9));
		subSeq2.setSequence(subSeq2list);
		subSeq2.setLength(3);
		subSeq2.setSequenceMode(SequenceMode.REVERSE);
		
		ConstantPattern constant42 = new ConstantPattern(42);
		
		RandomPattern randomBetween80and90 = new RandomPattern(80, 90, 2);
		
		mainList.add(subSeq1);
		mainList.add(subSeq2);
		mainList.add(constant42);
		mainList.add(randomBetween80and90);
		
		seq.setSequence(mainList);
		seq.setSequenceMode(SequenceMode.RANDOM_NO_REPEAT);
		
		
		for (int i = 0; i < 50; i++) {
			System.out.print((int) seq.nextValue() + " ");
		}
		
		System.out.println();
		long timeBefore = System.currentTimeMillis();
		for (int i = 0; i < 500; i++) {
			seq.nextValue();
		}
		long timeAfter = System.currentTimeMillis();
		System.out.println("ms: " + (timeAfter - timeBefore));
	}
}




// GUI
/*
public void start(Stage primaryStage) {

List<ValueSupplier> list = new ArrayList<>();
list.add(new ConstantPattern(1));
list.add(new ConstantPattern(2));
list.add(new ConstantPattern(3));
list.add(new ConstantPattern(4));

SequenceManipulator sm = new SequenceManipulator();
List<ValueSupplier> list2 = new ArrayList<>();
list2.add(new ConstantPattern(7));
list2.add(new ConstantPattern(9));
sm.setSequence(list2);
sm.setLength(2);
list.add(sm);


Rectangle background = new Rectangle(550, 400);

SequenceManipulator sequenceManipulator = new SequenceManipulator();
sequenceManipulator.setSequence(list);

Label startOffsetLabel = new Label("start offset");
startOffsetLabel.setLayoutX(10);
startOffsetLabel.setLayoutY(10);
startOffsetLabel.setTextFill(Color.BISQUE);

TextField startOffsetField = new TextField("0");
startOffsetField.setLayoutX(110);
startOffsetField.setLayoutY(10);
startOffsetField.setOnKeyReleased((e) -> {
	try {
		sequenceManipulator.setStartOffset(new Integer(startOffsetField.getText()));
	} catch (NumberFormatException ex) { }
});

Label lengthLabel = new Label("length");
lengthLabel.setLayoutX(280);
lengthLabel.setLayoutY(10);
lengthLabel.setTextFill(Color.BISQUE);

TextField lengthField = new TextField("0");
lengthField.setLayoutX(380);
lengthField.setLayoutY(10);
lengthField.setOnKeyReleased((e) -> {
	try {
		sequenceManipulator.setLength(new Integer(lengthField.getText()));
	} catch (NumberFormatException ex) { }
});

Label endOffsetLabel = new Label("end offset");
endOffsetLabel.setLayoutX(10);
endOffsetLabel.setLayoutY(40);
endOffsetLabel.setTextFill(Color.BISQUE);

TextField endOffsetField = new TextField("0");
endOffsetField.setLayoutX(110);
endOffsetField.setLayoutY(40);
endOffsetField.setOnKeyReleased((e) -> {
	try {
		sequenceManipulator.setEndOffset(new Integer(endOffsetField.getText()));
	} catch (NumberFormatException ex) { }
});

Label localOffsetLabel = new Label("local offset");
localOffsetLabel.setLayoutX(10);
localOffsetLabel.setLayoutY(70);
localOffsetLabel.setTextFill(Color.BISQUE);

TextField localOffsetField = new TextField("0");
localOffsetField.setLayoutX(110);
localOffsetField.setLayoutY(70);
localOffsetField.setOnKeyReleased((e) -> {
	try {
		sequenceManipulator.setLocalOffset(new Integer(localOffsetField.getText()));
	} catch (NumberFormatException ex) { }
});

RadioButton normalRadio = new RadioButton("normal");
normalRadio.setLayoutX(10);
normalRadio.setLayoutY(100);
normalRadio.setTextFill(Color.BISQUE);
normalRadio.setOnAction((e) -> {
	sequenceManipulator.setSequenceMode(SequenceMode.NORMAL);
});

RadioButton reverseRadio = new RadioButton("reverse");
reverseRadio.setLayoutX(10);
reverseRadio.setLayoutY(130);
reverseRadio.setTextFill(Color.BISQUE);
reverseRadio.setOnAction((e) -> {
	sequenceManipulator.setSequenceMode(SequenceMode.REVERSE);	
});

RadioButton randomRadio = new RadioButton("random");
randomRadio.setLayoutX(10);
randomRadio.setLayoutY(160);
randomRadio.setTextFill(Color.BISQUE);
randomRadio.setOnAction((e) -> {
	sequenceManipulator.setSequenceMode(SequenceMode.RANDOM);				
});

RadioButton randomNoRepeatRadio = new RadioButton("random, no repeat");
randomNoRepeatRadio.setLayoutX(10);
randomNoRepeatRadio.setLayoutY(190);
randomNoRepeatRadio.setTextFill(Color.BISQUE);
randomNoRepeatRadio.setOnAction((e) -> {
	sequenceManipulator.setSequenceMode(SequenceMode.RANDOM_NO_REPEAT);
});

Label noRepeatMemoryLabel = new Label("no repeat mem");
noRepeatMemoryLabel.setLayoutX(10);
noRepeatMemoryLabel.setLayoutY(220);
noRepeatMemoryLabel.setTextFill(Color.BISQUE);

TextField noRepeatMemoryField = new TextField("1");
noRepeatMemoryField.setLayoutX(110);
noRepeatMemoryField.setLayoutY(220);
noRepeatMemoryField.setOnKeyReleased((e) -> {
	try {
		sequenceManipulator.setNoRepeatMemory(new Integer(noRepeatMemoryField.getText()));
	} catch (NumberFormatException ex) { }
});

RadioButton randomWalkRadio = new RadioButton("random walk");
randomWalkRadio.setLayoutX(10);
randomWalkRadio.setLayoutY(250);
randomWalkRadio.setTextFill(Color.BISQUE);
randomWalkRadio.setOnAction((e) -> {
	sequenceManipulator.setSequenceMode(SequenceMode.RANDOM_WALK);
});

ToggleGroup radioGroup = new ToggleGroup();
radioGroup.getToggles().add(normalRadio);
radioGroup.getToggles().add(reverseRadio);
radioGroup.getToggles().add(randomRadio);
radioGroup.getToggles().add(randomNoRepeatRadio);
radioGroup.getToggles().add(randomWalkRadio);
radioGroup.selectToggle(normalRadio);

Button getNextValue = new Button("get next values");
getNextValue.setLayoutX(10);
getNextValue.setLayoutY(background.getHeight() - 50);
getNextValue.setOnAction((e) -> {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < 40; i++) {
		if (!sequenceManipulator.hasNext()) break;
		double value = sequenceManipulator.nextValue();
		sb.append((int) value);
		sb.append(' ');
	}
	showFirst40.setText(sb.toString());
});

showFirst40 = new Label();
showFirst40.setText("First 40 values");
showFirst40.setTextFill(Color.BISQUE);
showFirst40.setLayoutX(10);
showFirst40.setLayoutY(background.getHeight() - 20);

Group root = new Group();
root.getChildren().add(background);
root.getChildren().add(startOffsetLabel);
root.getChildren().add(startOffsetField);
root.getChildren().add(lengthLabel);
root.getChildren().add(lengthField);
root.getChildren().add(endOffsetLabel);
root.getChildren().add(endOffsetField);
root.getChildren().add(localOffsetLabel);
root.getChildren().add(localOffsetField);
root.getChildren().add(normalRadio);
root.getChildren().add(reverseRadio);
root.getChildren().add(randomRadio);
root.getChildren().add(randomNoRepeatRadio);
root.getChildren().add(noRepeatMemoryLabel);
root.getChildren().add(noRepeatMemoryField);
root.getChildren().add(randomWalkRadio);
root.getChildren().add(getNextValue);
root.getChildren().add(showFirst40);

Scene scene = new Scene(root);
primaryStage.setScene(scene);
primaryStage.setX(10);
primaryStage.setY(40);
primaryStage.show();
}
*/