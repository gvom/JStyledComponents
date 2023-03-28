package gmai.jstyledcomponents;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import com.jhlabs.image.GaussianFilter;

/*
 * Rounded panel with gradient shadow behind
 */
public class JRoundedCard extends JCard {

	// Corners radius size
	private int shadowSize = 5;

	public JRoundedCard() {
		setOpaque(false);
	}

	// Define the sides of the shadow
	@Override
	public Insets getInsets() {
		return new Insets(0, 0, 10, 10);
	}

	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth() - 1;
		int height = getHeight() - 1;

		Graphics2D g2d = (Graphics2D) g.create();
		applyQualityProperties(g2d);
		Insets insets = getInsets();
		Rectangle bounds = getBounds();
		bounds.x = insets.left;
		bounds.y = insets.top;
		bounds.width = width - (insets.left + insets.right);
		bounds.height = height - (insets.top + insets.bottom);

		RoundRectangle2D shape = new RoundRectangle2D.Float(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);

		// Update only when the size change
		BufferedImage img = createCompatibleImage(bounds.width, bounds.height);
		Graphics2D tg2d = img.createGraphics();
		applyQualityProperties(g2d);
		tg2d.setColor(Color.BLACK);
		tg2d.translate(-bounds.x, -bounds.y);
		tg2d.fill(shape);
		tg2d.dispose();
		BufferedImage shadow = generateShadow(img, shadowSize, Color.BLACK, 0.5f);

		g2d.drawImage(shadow, shadowSize, shadowSize, this);

		g2d.setColor(getBackground());
		g2d.fill(shape);

		/**
		 * DON'T CALL super.paintComponent MAY CAUSE CYCLING ERROR
		 */
		getUI().paint(g2d, this);

		g2d.dispose();
	}

	/**
	 * Get the graphics configuration of the environment
	 */
	public GraphicsConfiguration getGraphicsConfiguration() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	}

	/**
	 * Creates a new translucent image with the defined width and height
	 *
	 * @param width  the image width
	 * @param height the image height
	 * @return a translucent empty image
	 */
	public BufferedImage createCompatibleImage(int width, int height) {
		BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		image.coerceData(true);
		return image;
	}

	/**
	 * Define the image render properties
	 * 
	 * @param g2 the render
	 */
	public void applyQualityProperties(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}

	/**
	 * Generates a shadow to the panel
	 * 
	 * @param imgSource the image to add the shadow
	 * @param size      the shadow size
	 * @param color     the shadow color
	 * @param alpha     the transparency percentage
	 * @return a image containing the panel shadow
	 */
	public BufferedImage generateShadow(BufferedImage imgSource, int size, Color color, float alpha) {

		int imgWidth = imgSource.getWidth() + (size * 2);
		int imgHeight = imgSource.getHeight() + (size * 2);

		BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight);
		Graphics2D g2 = imgMask.createGraphics();
		applyQualityProperties(g2);

		int x = Math.round((imgWidth - imgSource.getWidth()) / 2f);
		int y = Math.round((imgHeight - imgSource.getHeight()) / 2f);

		g2.drawImage(imgSource, 0, 0, null);
		g2.dispose();

		// Blur the shadow
		BufferedImage imgShadow = generateBlur(imgMask, size, color, alpha);

		return imgShadow;

	}

	/**
	 * Blur the panel shadow
	 * 
	 * @param imgSource the image containing the shadow
	 * @param size      the size of the blur effect
	 * @param color     the color
	 * @param alpha     the transparency percentage
	 * @return a image containing the panel shadow blurred
	 */
	public BufferedImage generateBlur(BufferedImage imgSource, int size, Color color, float alpha) {

		GaussianFilter filter = new GaussianFilter(size);

		int imgWidth = imgSource.getWidth();
		int imgHeight = imgSource.getHeight();

		BufferedImage imgBlur = createCompatibleImage(imgWidth, imgHeight);
		Graphics2D g2d = imgBlur.createGraphics();
		applyQualityProperties(g2d);

		g2d.drawImage(imgSource, 0, 0, null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
		g2d.setColor(color);

		g2d.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
		g2d.dispose();

		imgBlur = filter.filter(imgBlur, null);

		return imgBlur;

	}
}
