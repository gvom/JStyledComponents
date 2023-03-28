package gmai.jstyledcomponents.util;

import static java.util.Objects.requireNonNull;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The ImageUtil class provides utility methods for working with images.
 */
public class ImageUtil {

	/**
	 * Returns a BufferedImage object from the specified file path.
	 * 
	 * @param path the file path of the image
	 * @return a BufferedImage object of the image, or null if an error occurs
	 */
	public static BufferedImage getImage(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("Error, " + e.getStackTrace()[0].getMethodName() + " : " + e);
			return null;
		}
	}

	/**
	 * Resizes the specified BufferedImage object to the specified dimensions.
	 * 
	 * @param img  the BufferedImage object to resize
	 * @param newW the new width of the image
	 * @param newH the new height of the image
	 * @return a resized BufferedImage object
	 */
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	/**
	 * Returns a resized BufferedImage object from the specified file path and
	 * dimensions.
	 * 
	 * @param path the file path of the image
	 * @param newW the new width of the image
	 * @param newH the new height of the image
	 * @return a resized BufferedImage object of the image
	 */
	public static BufferedImage getImage(String path, int newW, int newH) {
		return resize(requireNonNull(getImage(path)), newW, newH);
	}
}