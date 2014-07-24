package no.simonsen.physical;

import java.util.ArrayList;
import java.util.LinkedList;


import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

public class LinkedSteps extends Group {
	private ArrayList<Circle> stepList = new ArrayList<>();
	
	public LinkedSteps() {
		for (int i = 0; i < 100; i++) {
			Circle circle = new Circle(4);
			circle.setLayoutX(i*10);
			circle.setId("" + i);
			circle.setOnMousePressed((e) -> {
				System.out.println("pressed");
				circle.setOnMouseDragged((ev) -> {
					circle.setLayoutY(circle.getLayoutY() + ev.getY());
					int stepNumber = new Integer(circle.getId());
					double currentY = circle.getLayoutY();
					for (int j = 0; j < stepNumber; j++) {
						stepList.get(j).setLayoutY(j * currentY / stepNumber);
					}
					System.out.println(stepList.size() - 1 - stepNumber);
					for (int j = 0; j < stepList.size() - 1 - stepNumber; j++) {
						stepList.get(stepList.size() - 1 - j).setLayoutY(j * currentY / (stepList.size() - 1 - stepNumber));
					}
				}); 
			});
			circle.setOnMouseReleased((e) -> {
				System.out.println("released");
				circle.setOnMouseMoved(null);
			});
			
			getChildren().add(circle);
			stepList.add(circle);
		}
	}
}

class Step {
	
}