package no.simonsen.gui.timeline.elements;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import no.simonsen.gui.timeline.Redrawable;
import no.simonsen.gui.timeline.Timeline;

public class PosLength extends Group implements Redrawable {
	private Timeline timeline;
	private double start;
	private double length;
	private Rectangle posLength;
	private Rectangle handle;
	private double handleWidth = 5;
	
	public PosLength(Timeline timeline, double pixelX, double pixelY, double pixelWidth, double pixelHeight) {
		this.timeline = timeline;
		
		start = pixelX / timeline.scaleX - timeline.offsetX;
		length = pixelWidth / timeline.scaleX;
		
		posLength = new Rectangle();
		posLength.setY(pixelY);
		posLength.setHeight(pixelHeight);
		posLength.setFill(Color.CRIMSON);
		posLength.setStroke(Color.DARKRED);
		posLength.setOpacity(0.8);
		posLength.addEventHandler(MouseEvent.ANY, new MouseHandler());
		
		handle = new Rectangle();
		handle.setY(pixelY);
		handle.setHeight(pixelHeight);
		handle.setWidth(handleWidth);
		handle.setFill(Color.DARKRED);
		handle.setMouseTransparent(true);
		handle.setVisible(false);
		
		getChildren().add(posLength);
		getChildren().add(handle);
		
		redraw();
	}
	
	public void redraw() {
		redraw(timeline.scaleX, timeline.scaleY, timeline.offsetX, timeline.offsetY);
	}
	
	public void redraw(double scaleX, double scaleY, double offsetX, double offsetY) {
		posLength.setX((start + offsetX) * scaleX);
		posLength.setWidth(length * scaleX);
	}
	
	private class MouseHandler implements EventHandler<MouseEvent> {
		private double handleOffset;
		private double moveOffset;

		public void handle(MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
				if (event.getX() > posLength.getLayoutBounds().getMaxX() - handleWidth) {
					handle.setX(posLength.getLayoutBounds().getMaxX() - handleWidth);
					handle.setVisible(true);
					handleOffset = handle.getX() + handle.getWidth() - event.getX();
				} else {
					handle.setVisible(false);
					moveOffset = event.getX() - posLength.getX();
				}
			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				if (handle.isVisible()) {
					double lengthInPixels = event.getX() + handleOffset - posLength.getX();
					System.out.println(lengthInPixels);
					if (lengthInPixels >= handleWidth * 2)
						length = lengthInPixels / timeline.scaleX;
					else 
						length = handleWidth * 2 / timeline.scaleX;
					redraw();
					handle.setX(posLength.getLayoutBounds().getMaxX() - handleWidth);
				} else {
					start = (event.getX() - moveOffset) / timeline.scaleX - timeline.offsetX;
					redraw();
				}
			} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				if (!handle.contains(event.getX(), event.getY())) {
					handle.setVisible(false);
				}
			} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
				if (!event.isPrimaryButtonDown()) {
					handle.setVisible(false);
				}
			}
		}
	}
}