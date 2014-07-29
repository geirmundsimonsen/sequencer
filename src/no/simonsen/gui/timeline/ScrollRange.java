package no.simonsen.gui.timeline;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ScrollRange extends Region {
	private String scrollRangeID;
	private Region bar;
	private double totalWidthReciprocal;
	private Rectangle scroller;
	private Rectangle scrollerLeftFlash;
	private Rectangle scrollerRightFlash;
	private ScrollRangeListener listener;
	
	public ScrollRange(double width, String id) {
		scrollRangeID = id;

		bar = new Region();
		bar.setMinSize(width, 20);
		bar.setMaxSize(width, 20);
		bar.setPrefSize(width, 20);
		
		totalWidthReciprocal = 1 / (bar.getPrefWidth());
		
		scroller = new Rectangle(0, 0, bar.getPrefWidth(), 20);
		scroller.setFill(Color.LIGHTGRAY);
		scroller.addEventHandler(MouseEvent.ANY, new ScrollRangeMouseHandler());
		
		scrollerLeftFlash = new Rectangle(5, 20);
		scrollerLeftFlash.setFill(Color.DARKGRAY);
		scrollerLeftFlash.setMouseTransparent(true);
		scrollerLeftFlash.setVisible(false);
		scrollerRightFlash = new Rectangle(5, 20);
		scrollerRightFlash.setFill(Color.DARKGRAY);
		scrollerRightFlash.setMouseTransparent(true);
		scrollerRightFlash.setVisible(false);
		
		getChildren().add(bar);
		getChildren().add(scroller);
		getChildren().add(scrollerLeftFlash);
		getChildren().add(scrollerRightFlash);
	}
	
	/** 
	 * Gets the position - a value between 0 and 1.
	 * @return the position
	 */
	public double getPosition() {
		return (scroller.getX() + scroller.getX() + scroller.getWidth()) * 0.5 * totalWidthReciprocal;
	}
	
	/**
	 * Sets the position - a value between 0 and 1, where 0 is the leftmost point and 1 is the rightmost point.
	 * @param position A value between 0 and 1.
	 */
	public void setPosition(double position) {
		 double midPositionInPixels = position * bar.getPrefWidth();
		 double newX = midPositionInPixels - (scroller.getWidth() * 0.5);
		 if (newX < 0) {
			 scroller.setX(0);
		 } else if (newX + scroller.getWidth() > bar.getPrefWidth()) {
			 scroller.setX(bar.getPrefWidth() - scroller.getWidth());
		 } else {
			 scroller.setX(newX);
		 }
		 updateListener(scroller.getX(), scroller.getWidth());
	}
	
	/**
	 * Gets the zoom programmatically. Returns a value between 0 and 1. At 1, the window is fully 
	 * zoomed out.
	 */
	public double getZoom() {
		return scroller.getWidth() * totalWidthReciprocal;
	}
	
	/**
	 * Sets the zoom programmatically. Returns a value between 0 and 1. At 1, the window is fully 
	 * zoomed out.
	 */
	public void setZoom(double zoom) {
		if (zoom > 1)
			zoom = 1;
		else if (zoom < 0.1)
			zoom = 0.1;
		double newWidth = zoom * bar.getPrefWidth();
		scroller.setX(scroller.getX() - ((newWidth - scroller.getWidth()) * 0.5));
		scroller.setWidth(newWidth);
		
		if (scroller.getX() + newWidth > bar.getPrefWidth()) {
			scroller.setX(bar.getPrefWidth() - newWidth);
		} else if (scroller.getX() < 0) {
			scroller.setX(-scroller.getX());
		}
		updateListener(scroller.getX(), scroller.getWidth());

	}
	
	public void refresh() {
		updateListener(scroller.getX(), scroller.getWidth());
	}
	
	public void addListener(ScrollRangeListener listener) {
		this.listener = listener;
		updateListener(scroller.getX(), scroller.getWidth());
	}
	
	private void updateListener(double startX, double width) {
		listener.scrollChanged(startX * totalWidthReciprocal, (startX + width) * totalWidthReciprocal, width * totalWidthReciprocal, this);
	}
	
	class ScrollRangeMouseHandler implements EventHandler<MouseEvent> {
		private boolean dragStart = false;
		private boolean dragMiddle = true;
		private boolean dragEnd = false;
		private double initialX = 0;
		private double initialRectX = 0;
		
		@Override
		public void handle(MouseEvent e) {
			Rectangle source = (Rectangle) e.getSource();
			
			if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
				if (e.getX() < scroller.getBoundsInLocal().getMinX() + 5) {
					scrollerLeftFlash.setX(scroller.getBoundsInLocal().getMinX());
					scrollerLeftFlash.setVisible(true);
					scrollerRightFlash.setVisible(false);
					initialRectX = source.getX();
					initialX = e.getX();
					dragStart = true; dragMiddle = false; dragEnd = false;
				} else if (e.getX() > scroller.getBoundsInLocal().getMaxX() - 5) {
					scrollerRightFlash.setX(scroller.getBoundsInLocal().getMaxX() - 5);
					scrollerRightFlash.setVisible(true);
					scrollerLeftFlash.setVisible(false);
					initialRectX = source.getX() + source.getWidth();
					initialX = e.getX();
					dragStart = false; dragMiddle = false; dragEnd = true;
				} else {
					scrollerLeftFlash.setVisible(false);
					scrollerRightFlash.setVisible(false);
					initialRectX = source.getX();
					initialX = e.getX();
					dragStart = false; dragMiddle = true; dragEnd = false;
				}
				
			} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {				
				if (dragStart) {
					double newWidth = source.getBoundsInLocal().getMaxX() - e.getX();
					if (newWidth > 10 && e.getX() >= 0) {
						source.setX(e.getX());
						source.setWidth(newWidth);
						scrollerLeftFlash.setX(scroller.getBoundsInLocal().getMinX());
					}
					updateListener(source.getX(), source.getWidth());
				} else if (dragEnd) {
					double newWidth = e.getX() - source.getX();
					if (newWidth > 10 && e.getX() <= bar.getPrefWidth()) {
						source.setWidth(newWidth);
						scrollerRightFlash.setX(scroller.getBoundsInLocal().getMaxX() - 5);
						
					} 
					updateListener(source.getX(), source.getWidth());
				} else if (dragMiddle) {
					double xOffset = initialX - initialRectX;
					if (e.getX() - xOffset >= 0 && e.getX() - xOffset + source.getWidth() <= bar.getPrefWidth()) {
						source.setX(e.getX() - xOffset);
					}
					updateListener(source.getX(), source.getWidth());
				}
				
			} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
				scrollerLeftFlash.setVisible(false);
				scrollerRightFlash.setVisible(false);
			}
		}
	}
	
	public String getScrollRangeID() { return scrollRangeID; }
}
