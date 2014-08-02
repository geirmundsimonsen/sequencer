package no.simonsen.gui.timeline.elements;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import no.simonsen.gui.timeline.Redrawable;
import no.simonsen.gui.timeline.Timeline;

public class HalfRange extends Group implements Redrawable {
	private Timeline timeline;
	private Rectangle rangeRect;
	private double position = 5;
	private double padding = -500; // draw well beyond X-boundary
	private Rectangle handle;
	private double handleWidth = 10;
	
	public HalfRange(Timeline timeline) {
		this.timeline = timeline;
		
		rangeRect = new Rectangle();
		rangeRect.setOpacity(0.4);
		rangeRect.setY(0);
		rangeRect.setHeight(timeline.getEditField().getHeight());
		rangeRect.addEventHandler(MouseEvent.ANY, new MouseHandler());
		
		handle = new Rectangle(0, rangeRect.getY(), handleWidth, rangeRect.getHeight());
		handle.setFill(Color.DARKGREEN);
		handle.setMouseTransparent(true);
		handle.setVisible(false);
		
		getChildren().add(rangeRect);
		getChildren().add(handle);
		
		redraw();
	}
	
	private void redraw() {
		redraw(timeline.scaleX, timeline.scaleY, timeline.offsetX, timeline.offsetY);
	}
	
	public void redraw(double scaleX, double scaleY, double offsetX, double offsetY) {
		rangeRect.setX(padding);
		rangeRect.setWidth(((position + offsetX) * scaleX) - padding);
	}
	
	private class MouseHandler implements EventHandler<MouseEvent> {
		// a handleOffset is the distance in the handle from the timing position. This value is important when 
		// you move the handle, since you always want the handle to move relative to the mouse. Otherwise, the
		// handle will "skip" when initially moved, which I think is bad UI behavior.
		private double handleOffset;
		
		public void handle(MouseEvent event) {			
			/*
			 * This sequence encapsulates well the implentation of a dragged handle. Since JavaFX doesn't have
			 * support for OS X three-finger-drag, we have to consider a possible drag at any time, using MOUSE_MOVED.
			 * MOUSE_MOVED will make the handle visible (but handles shouldn't capture mouse events) if it is within
			 * a certain space.
			 * MOUSE_DRAGGED locks on to a handle if it is set visible - this can only happen if the mouse is within
			 * the defined space for the handle. What happens here is user-defined, but here the position is updated
			 * along the x-axis (we first translate it from pixels to units). Then the rectangle representation
			 * (rangeRect) is redrawn - it should always be according to its underlying values. Lastly, the handle
			 * position is updated. 
			 * MOUSE_RELEASED tests whether the mouse is released outside the handle. If it is, the handle is hidden.
			 * MOUSE_EXITED potentially interferes with MOUSE_DRAGGED. A handle is hidden only if the primary
			 * button is NOT down.
			 *
			 * This sequence also covers normal drag operations with a mouse.
			 * 
			 * The "real" operation, that is, where the operation actually causes a change to the underlying value - 
			 * position - is only one line (which is immediately followed by a redraw). 
			 */
			if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
				if (event.getX() > rangeRect.getLayoutBounds().getMaxX() - handleWidth) {
					handle.setX(rangeRect.getLayoutBounds().getMaxX() - handleWidth);
					handle.setVisible(true);
					handleOffset = handle.getX() + handle.getWidth() - event.getX();
				} else {
					handle.setVisible(false);
				}
			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				if (handle.isVisible()) {
					// this operation sets the actual value, position. Call redraw to update the representation.
					position = (event.getX() + handleOffset) / timeline.scaleX - timeline.offsetX;
					redraw();
					// update handle position
					handle.setX(rangeRect.getLayoutBounds().getMaxX() - handleWidth);
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