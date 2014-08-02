package no.simonsen.gui;

import no.simonsen.gui.style.Styler;
import no.simonsen.gui.timeline.Grid;
import no.simonsen.gui.timeline.RedrawGroup;
import no.simonsen.gui.timeline.Timeline;
import no.simonsen.gui.timeline.elements.HalfRange;
import no.simonsen.gui.timeline.elements.PosLength;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RhythmUI extends Pane {
	private Group masterGroup;
	private double offset = 10;
	private Timeline timeline;
	
	private RedrawGroup delimiters;
	private HalfRange leftHalfRange;
	private RedrawGroup events;
	private double posLengthY = 10;
	private double posLengthHeight = 20;
	private Grid grid;
	
	public RhythmUI() {
		masterGroup = new Group();
		masterGroup.setLayoutX(offset);
		masterGroup.setLayoutY(offset);
		
		Styler.style(this, Styler.Mode.DEFAULT);
		
		timeline = new Timeline(700, 120);
		timeline.setAxisSelection(Timeline.Axis.X);
		timeline.setScalingX(10);
		timeline.getEditField().addEventHandler(MouseEvent.ANY, new MouseHandler());
		
		grid = timeline.getVerticalGrid();
		
		leftHalfRange = new HalfRange(timeline);
		delimiters = new RedrawGroup();
		delimiters.addRedrawable(leftHalfRange);
		
		events = new RedrawGroup();
		
		timeline.getRedrawGroup().addRedrawable(events);
		timeline.getRedrawGroup().addRedrawable(delimiters);
		
		masterGroup.getChildren().add(timeline);
		
		getChildren().add(masterGroup);
		setPrefSize(masterGroup.getLayoutBounds().getWidth() + offset * 2, masterGroup.getLayoutBounds().getHeight() + offset * 2);
	}
	
	private class MouseHandler implements EventHandler<MouseEvent> {
		Rectangle tempPosLength = null;

		@Override
		public void handle(MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
				timeline.getScene().setCursor(ImageCursor.CROSSHAIR);
			} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
				timeline.getScene().setCursor(ImageCursor.DEFAULT);
			} else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				System.out.println(grid.calculateSnapValue(event.getX()));
				tempPosLength = new Rectangle(event.getX(), posLengthY, 20, posLengthHeight);
				tempPosLength.setFill(Color.TRANSPARENT);
				tempPosLength.setStroke(Color.GRAY);
				timeline.getMasterGroup().getChildren().add(tempPosLength);
			} else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
				System.out.println("Pixel to unit " + grid.pixelToUnit(event.getX()));
				System.out.println("Raw pixel: " + event.getX());
				System.out.println("Pixel to unit to pixel: " + grid.unitToPixel(grid.pixelToUnit(event.getX())));
			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				tempPosLength.setWidth(event.getX() - tempPosLength.getX());
			} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				if (tempPosLength.getWidth() <= 0.5) {
					// don't add near-zero-width PosLength
				} else if (tempPosLength.getWidth() <= 2) {
					events.addRedrawable(new PosLength(timeline, tempPosLength.getX(), posLengthY, 2, posLengthHeight));					
				} else {					
					events.addRedrawable(new PosLength(timeline, tempPosLength.getX(), posLengthY, tempPosLength.getWidth(), posLengthHeight));
				}
				timeline.getMasterGroup().getChildren().remove(tempPosLength);
			}
		}
	}
}