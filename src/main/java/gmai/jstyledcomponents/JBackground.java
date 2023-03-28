package gmai.jstyledcomponents;

import static gmai.jstyledcomponents.util.ImageUtil.getImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The JBackground class extends JPanel and provides a background image for a
 * JPanel. The background image is specified by the imagePath parameter in the
 * constructor. The panelDimension parameter specifies the size of the JPanel.
 */
public class JBackground extends JPanel {

	private String imagePath;
	private Dimension panelDimension;

	/**
	 * Constructs a JBackground object with the specified imagePath and
	 * panelDimension.
	 * 
	 * @param imagePath the path to the background image
	 * @param dimension the size of the JPanel
	 */
	public JBackground(String imagePath, Dimension dimension) {
		this.imagePath = imagePath;
		this.panelDimension = dimension;

		setPreferredSize(dimension);
		setLayout(new BorderLayout());
	}

	/**
	 * Overrides the paintComponent method to draw the background image.
	 * 
	 * @param g the Graphics object to draw with
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		final int width, height, x = 0, y = 0;
		width = (int) panelDimension.getWidth();
		height = (int) panelDimension.getHeight();

		g.drawImage(getImage(imagePath, width, height), x, y, null);
	}
}