package no.simonsen.gui.timeline;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * Grid draws grid lines constrained within a rectangle, in both orientations. It also draws the legend,
 * which shows the numbers associated with the lines. The origo point is strictly at the top left corner of
 * the (imagined) rectangle, so the numbers are drawn in the negative area.
 */
public class Grid {
	double width, height;
	enum Orientation { VERTICAL, HORIZONTAL }
	Orientation orientation;
	
	/**
	 * If timeline gets resizable, at it most likely will at some point, the easiest thing
	 * would be to move them directly to calculateGridLines
	 * @param width
	 * @param height
	 */
	public Grid(double width, double height, Orientation orientation) {
		this.width = width;
		this.height = height;
		this.orientation = orientation;
	}
	
	public List<Node> calculateGridLines(double scale, double offset) {
		List<Node> nodeList = new ArrayList<>();
		
		double startPixel = 0 / scale - offset;
		double endPixel = 0;
		if (orientation == Orientation.VERTICAL)
			endPixel = width / scale - offset;
		if (orientation == Orientation.HORIZONTAL)
			endPixel = height / scale - offset;
		
		// move this to a grid template.
		double spacing;
		double firstGridLine;
		double thickGridSpacing;
		String textFormat;
		
		if (scale < 20) {
			spacing = 1;
			thickGridSpacing = 5;
			textFormat = "%.0f";
		} else if (scale < 50) {
			spacing = 0.5;
			thickGridSpacing = 1;
			textFormat = "%.0f";
		} else if (scale < 150) {
			spacing = 0.25;
			thickGridSpacing = 1;
			textFormat = "%.0f";
		} else {
			spacing = 0.1;
			thickGridSpacing = 0.5;
			textFormat = "%.1f";
		}
		// move end
		
		firstGridLine = startPixel - (startPixel % spacing) + spacing; // check
		double tempGridLine = firstGridLine; // check
		while (tempGridLine < endPixel) { // check
			Line line = null;
			
			if (orientation == Orientation.VERTICAL) {
				line = new Line(
						(tempGridLine + offset) * scale, 0, 
						(tempGridLine + offset) * scale, height);				
			} else if (orientation == Orientation.HORIZONTAL) {
				line = new Line(
						0, height - (tempGridLine + offset) * scale,
						width, height - (tempGridLine + offset) * scale);
			}
			
			line.setMouseTransparent(true); // check
			
			double thickGridModulo = tempGridLine % thickGridSpacing; // check
			
			if (thickGridModulo < 0.0000001 || thickGridModulo > thickGridSpacing - 0.0000001) { // check
				line.setStrokeWidth(0.5); // check
				Text text = new Text(String.format(textFormat, tempGridLine)); // check
				
				if (orientation == Orientation.VERTICAL) {
					text.setX((tempGridLine + offset) * scale - text.getBoundsInLocal().getWidth() / 2);
					text.setY(-text.getBoundsInLocal().getMinY() - 20);					
				} else if (orientation == Orientation.HORIZONTAL) {
					text.setX(-15 - text.getBoundsInLocal().getWidth() / 2);
					text.setY(height - (tempGridLine + offset) * scale + 4);
				}
				
				nodeList.add(text); // check
			} else {
				line.setStrokeWidth(0.15); // check
			}
			nodeList.add(line); // check
			tempGridLine += spacing; // check
		}
		
		return nodeList;
	}
}
