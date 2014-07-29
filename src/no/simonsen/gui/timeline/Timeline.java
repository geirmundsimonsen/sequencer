package no.simonsen.gui.timeline;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Timeline extends Pane implements ScrollRangeListener {
	private double offsetX;
	private double scaleX;
	private double userSpecifiedOffsetX = 0;
	private double userSpecifiedScaleX = 1;
	private double offsetY;
	private double scaleY;
	private double userSpecifiedOffsetY = 0;
	private double userSpecifiedScaleY = 1;
	private Rectangle editField;
	private Grid verticalGrid;
	private Grid horizontalGrid;
	private Axis axisSelection;
			
	private Group masterGroup;
	private Group xGrid = new Group();
	private Group yGrid = new Group();
	private ScrollRange hScrollRange;
	private ScrollRange vScrollRange;
	
	public Timeline(double width, double height) {
		axisSelection = Axis.XY;
		
		masterGroup = new Group();
		masterGroup.setLayoutX(50);
		masterGroup.setLayoutY(50);
		
		editField = new Rectangle(width - 80, height - 80);
		editField.setFill(Color.SNOW);
		
		Rectangle editFieldBorder = new Rectangle(editField.getWidth(), editField.getHeight());
		editFieldBorder.setFill(Color.TRANSPARENT);
		editFieldBorder.setStroke(Color.BURLYWOOD);
		editFieldBorder.setStrokeWidth(0.5);
		
		verticalGrid = new Grid(editField.getWidth(), editField.getHeight(), Grid.Orientation.VERTICAL);
		horizontalGrid = new Grid(editField.getWidth(), editField.getHeight(), Grid.Orientation.HORIZONTAL);
								
		hScrollRange = new ScrollRange(editField.getWidth(), "HORIZONTAL");
		hScrollRange.addListener(this);
		hScrollRange.setLayoutY(editField.getHeight());
		
		vScrollRange = new ScrollRange(editField.getHeight(), "VERTICAL");
		vScrollRange.addListener(this);
		vScrollRange.getTransforms().add(new Rotate(-90, 0, 0));
		vScrollRange.setLayoutX(editField.getWidth());
		vScrollRange.setLayoutY(editField.getHeight());
		
		masterGroup.getChildren().add(editField);
		masterGroup.getChildren().add(editFieldBorder);
		masterGroup.getChildren().add(xGrid);
		masterGroup.getChildren().add(yGrid);
		masterGroup.getChildren().add(hScrollRange);
		masterGroup.getChildren().add(vScrollRange);
		
		getChildren().add(masterGroup);
		System.out.println(masterGroup.getLayoutBounds());
		this.setPrefSize(width, height);
	}
	
	public Axis getAxisSelection() {
		return axisSelection;
	}
	
	/**
	 * Sets the active axises for this timeline.
	 * @param axis XY, X, Y and NONE
	 */
	public void setAxisSelection(Axis axis) {
		if (axis == Axis.XY) {
			if (!masterGroup.getChildren().contains(xGrid)) { masterGroup.getChildren().add(xGrid); }
			if (!masterGroup.getChildren().contains(yGrid)) { masterGroup.getChildren().add(yGrid); }
			if (!masterGroup.getChildren().contains(hScrollRange)) { masterGroup.getChildren().add(hScrollRange); }
			if (!masterGroup.getChildren().contains(vScrollRange)) { masterGroup.getChildren().add(vScrollRange); }
		} else if (axis == Axis.X) {
			if (!masterGroup.getChildren().contains(xGrid)) { masterGroup.getChildren().add(xGrid); }
			if (masterGroup.getChildren().contains(yGrid)) { masterGroup.getChildren().remove(yGrid); }
			if (!masterGroup.getChildren().contains(hScrollRange)) { masterGroup.getChildren().add(hScrollRange); }
			if (masterGroup.getChildren().contains(vScrollRange)) { masterGroup.getChildren().remove(vScrollRange); }
		} else if (axis == Axis.Y) {
			if (masterGroup.getChildren().contains(xGrid)) { masterGroup.getChildren().remove(xGrid); }
			if (!masterGroup.getChildren().contains(yGrid)) { masterGroup.getChildren().add(yGrid); }
			if (masterGroup.getChildren().contains(hScrollRange)) { masterGroup.getChildren().remove(hScrollRange); }
			if (!masterGroup.getChildren().contains(vScrollRange)) { masterGroup.getChildren().add(vScrollRange); }
		} else if (axis == Axis.NONE) {
			if (masterGroup.getChildren().contains(xGrid)) { masterGroup.getChildren().remove(xGrid); }
			if (masterGroup.getChildren().contains(yGrid)) { masterGroup.getChildren().remove(yGrid); }
			if (masterGroup.getChildren().contains(hScrollRange)) { masterGroup.getChildren().remove(hScrollRange); }
			if (masterGroup.getChildren().contains(vScrollRange)) { masterGroup.getChildren().remove(vScrollRange); }
		}	
	}
	
	/**
	 * The scale value on the X-axis
	 * @return Number of units displayed when fully zoomed out.
	 */
	public double getScalingX() { return userSpecifiedScaleX; }
	
	/**
	 * Scales the edit window along the X-axis.
	 * @param scale Number of units to display when fully zoomed out.
	 */
	public void setScalingX(double scale) {
		if (scale < 1)
			scale = 1;
		userSpecifiedScaleX = scale;
		hScrollRange.refresh();
	}
	
	/**
	 * The scale value on the Y-axis
	 * @return Number of units displayed when fully zoomed out.
	 */
	public double getScalingY() { return userSpecifiedScaleY; }
	
	/**
	 * Scales the edit window along the Y-axis.
	 * @param scale Number of units to display when fully zoomed out.
	 */
	public void setScalingY(double scale) {
		if (scale < 1)
			scale = 1;
		userSpecifiedScaleY = scale;
		vScrollRange.refresh();
	}
	
	/**
	 * Offset from 0 on the X-axis.
	 * @return X offset in units.
	 */
	public double getOffsetX() { return userSpecifiedOffsetX; }
	
	/**
	 * Sets offset from 0 on the X-axis
	 * @param offset The offset value in units
	 */
	public void setOffsetX(double offset) {
		userSpecifiedOffsetX = offset;
		hScrollRange.refresh();
	}
	
	/**
	 * Offset from 0 on the Y-axis.
	 * @return Y offset in units.
	 */
	public double getOffsetY() { return userSpecifiedOffsetY; }
	
	/**
	 * Sets offset from 0 on the Y-axis
	 * @param offset The offset value in units
	 */
	public void setOffsetY(double offset) { 
		userSpecifiedOffsetY = offset;
		vScrollRange.refresh();
	}
	
	
	public void addEditFieldEventHandler(EventType<Event> eventType, EventHandler<? super Event> handler) {
		editField.addEventHandler(eventType, handler);
	}
	
	public void removeEditFieldEventHandler(EventType<Event> eventType, EventHandler<? super Event> handler) {
		editField.removeEventHandler(eventType, handler);
	}
	
	/**
	 * This is the only function allowed to change scale and offset.
	 */
	public void scrollChanged(double start, double end, double width, ScrollRange scrollRange) {
		if (scrollRange == hScrollRange) System.out.println(start + " " + width + " " + end);
		// fraction of window width determines the unit size of the window when zoomed fully out.
		// 0.1 of 200 means the space is 20 units wide, 10 of 200 means the space is 2000 units wide.
		// units is for simplicty seconds, although it could be musical beats as well.
		// units = fractionOfWidth * windowWidth OR fractionOfWidth = units / windowWidth
		if (scrollRange == hScrollRange) {
			double fractionOfWindowWidth = userSpecifiedScaleX / editField.getWidth(); 
			scaleX = 1 / (width * fractionOfWindowWidth);
			offsetX = -start * editField.getWidth() * fractionOfWindowWidth - userSpecifiedOffsetX;
		} else if (scrollRange == vScrollRange) {
			double fractionOfWindowWidth = userSpecifiedScaleY / editField.getHeight(); 
			scaleY = 1 / (width * fractionOfWindowWidth);
			offsetY = -start * editField.getHeight() * fractionOfWindowWidth - userSpecifiedOffsetY;
		}
		redraw();
	}
	
	/**
	 * Redraws the content of the editfield.
	 */
	public void redraw() {
		/*
		 * Redraw nodes in the edit field according to their own logic
		 */
		if (axisSelection == Axis.XY) {
			redrawXGrid();			
			redrawYGrid();
		} else if (axisSelection == Axis.X) {
			redrawXGrid();
		} else if (axisSelection == Axis.Y){
			redrawYGrid();			
		}
	}
	
	private void redrawXGrid() {
		xGrid.getChildren().clear();
		xGrid.getChildren().addAll(verticalGrid.calculateGridLines(scaleX, offsetX));
	}
	
	private void redrawYGrid() {
		yGrid.getChildren().clear();
		yGrid.getChildren().addAll(horizontalGrid.calculateGridLines(scaleY, offsetY));
		/*
		 * Keep this for later.
		if (scaleY < 8) {
			spacing = 6;
			thickGridSpacing = 12;
			textFormat = "%.0f";
		} else if (scaleY < 14) {
			spacing = 1;
			thickGridSpacing = 12;
			textFormat = "%.0f";
		} else if (scaleY < 35) {
			spacing = 1;
			thickGridSpacing = 1;
			textFormat = "%.0f";
		} else if (scaleY < 75) {
			spacing = 0.25;
			thickGridSpacing = 1;
			textFormat = "%.0f";
		} else {
			spacing = 0.1;
			thickGridSpacing = 1;
			textFormat = "%.0f";
		}
		*/
	}
	
	enum Axis {
		XY, X, Y, NONE
	}
}