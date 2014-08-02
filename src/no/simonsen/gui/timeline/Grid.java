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
	private double width, height;
	private double scale, offset;
	public enum Orientation { VERTICAL, HORIZONTAL }
	private Orientation orientation;
	
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
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public void setOffset(double offset) {
		this.offset = offset;
	}
	
	public double unitToPixel(double unitValue) {
		return (unitValue + offset) * scale;
	}
	
	public double pixelToUnit(double pixel) {
		return pixel / scale - offset;
	}
	
	public double calculateSnapValue(double pixelPos) {
		double startUnit = pixelPos / scale - offset;
		
		// copy
		double spacing;
		//double thickGridSpacing;
		//String textFormat;
		
		if (scale < 20) {
			spacing = 1;
			//thickGridSpacing = 5;
			//textFormat = "%.0f";
		} else if (scale < 50) {
			spacing = 0.5;
			//thickGridSpacing = 1;
			//textFormat = "%.0f";
		} else if (scale < 150) {
			spacing = 0.25;
			//thickGridSpacing = 1;
			//textFormat = "%.0f";
		} else {
			spacing = 0.1;
			//thickGridSpacing = 0.5;
			//textFormat = "%.1f";
		}
		// copy end
		startUnit -= spacing * 0.5;
		return startUnit - (startUnit % spacing) + spacing;
	}
	
	public List<Node> calculateGridLines() {
		List<Node> nodeList = new ArrayList<>();
		
		double startUnit = 0 / scale - offset;
		double endUnit = 0;
		if (orientation == Orientation.VERTICAL)
			endUnit = width / scale - offset;
		else if (orientation == Orientation.HORIZONTAL)
			endUnit = height / scale - offset;
		
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
		
		firstGridLine = startUnit - (startUnit % spacing) + spacing;
		double tempGridLine = firstGridLine;
		while (tempGridLine < endUnit) {
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
			
			line.setMouseTransparent(true);
			
			double thickGridModulo = tempGridLine % thickGridSpacing;
			
			if (thickGridModulo < 0.0000001 || thickGridModulo > thickGridSpacing - 0.0000001) {
				line.setStrokeWidth(0.5);
				Text text = new Text(String.format(textFormat, tempGridLine));
				
				if (orientation == Orientation.VERTICAL) {
					text.setX((tempGridLine + offset) * scale - text.getBoundsInLocal().getWidth() / 2);
					text.setY(-text.getBoundsInLocal().getMinY() - 20);					
				} else if (orientation == Orientation.HORIZONTAL) {
					text.setX(-15 - text.getBoundsInLocal().getWidth() / 2);
					text.setY(height - (tempGridLine + offset) * scale + 4);
				}
				
				nodeList.add(text);
			} else {
				line.setStrokeWidth(0.15);
			}
			nodeList.add(line);
			tempGridLine += spacing;
		}
		
		return nodeList;
	}
}
