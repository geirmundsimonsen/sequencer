package no.simonsen.gui.style;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BackgroundPool {
	public static Background window;
	public static Background error;
	public static Background white;
	
	public static void init() {
		createWindow();
		createError();
		createWhite();
		System.out.println("Backgrounds loaded");
	}
	
	private static void createWindow() {
		Color bgColor = Color.SNOW;
		BackgroundFill bgFill = new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY);
		
		Image image = new Image("images/cloud1.jpg", false);
		
		/*
		 * Doing this to an opaque image of modest size (300 x 200) still takes ~100 ms, no doubt because this
		 * way to deal with images is probably not intended. But you can still merge a BackgroundFill with
		 * an image if the image itself is transparent. 
		 */
		PixelReader pixelReader = image.getPixelReader();     
		WritableImage wImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
		PixelWriter pixelWriter = wImage.getPixelWriter();		
		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				Color color = pixelReader.getColor(readX, readY);
				pixelWriter.setColor(readX, readY, color.deriveColor(1, 1, 1, 0.2));
			}
		}
		
		BackgroundImage bgImage = new BackgroundImage(wImage, null, null, null, null);
		window = new Background(new BackgroundFill[] { bgFill }, new BackgroundImage[] { bgImage });
	}
	
	private static void createError() {
		Color bgColor = Color.SALMON;
		BackgroundFill bgFill = new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY);
		error = new Background(bgFill);
	}
	
	private static void createWhite() {
		Color bgColor = Color.WHITE;
		BackgroundFill bgFill = new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY);
		white = new Background(bgFill);
	}
}
