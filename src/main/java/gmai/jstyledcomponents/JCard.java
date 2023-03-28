/**
 * This package contains utility classes and components for creating styled Swing components.
 */
package gmai.jstyledcomponents;

import static gmai.jstyledcomponents.util.CommonsUtil.isFalse;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * A custom JPanel that can be styled with a transparent background color and
 * border.
 */
public class JCard extends JPanel implements ComponentListener {

	/**
	 * The current background color of the JCard.
	 */
	private Color color;

	/**
	 * The default color values for the JCard's background.
	 */
	private static final int RED = 0;
	private static final int GREEN = 0;
	private static final int BLUE = 0;
	private static final int ALPHA = 0;

	/**
	 * The default border for the JCard.
	 */
	protected static final Border BORDER = BorderFactory.createEmptyBorder(0, 30, 0, 0);

	/**
	 * The default background color for the JCard.
	 */
	protected static final Color DEFAULT_COLOR = new Color(RED, GREEN, BLUE, ALPHA);

	/**
	 * Creates a new JCard with the default background color and no layout manager.
	 */
	public JCard() {
		setOpaque(true);
		setBackground(DEFAULT_COLOR);
	}

	/**
	 * Creates a new JCard with the default background color and the specified
	 * layout manager.
	 * 
	 * @param layout The layout manager to use for the JCard.
	 */
	public JCard(LayoutManager layout) {
		setOpaque(true);
		setBackground(DEFAULT_COLOR);
		setLayout(layout);
	}

	/**
	 * Resets the JCard's background color to the default color if the reset
	 * parameter is true, otherwise sets the background color to the previously set
	 * color.
	 * 
	 * @param reset Whether to reset the background color to the default color.
	 */
	public void resetColor(boolean reset) {
		if (reset) {
			setBackground(DEFAULT_COLOR);
		} else {
			setBackground(color);
		}
	}

	/**
	 * Overrides the default setBackground method to store the current background
	 * color and repaint the JCard if the new background color is different from the
	 * old one.
	 * 
	 * @param bg The new background color for the JCard.
	 */
	@Override
	public void setBackground(Color bg) {
		this.color = bg;
		Color oldBg = getBackground();
		super.setBackground(bg);
		if ((oldBg != null) ? !isFalse(oldBg.equals(bg)) : ((bg != null) && !isFalse(bg.equals(oldBg)))) {
			// background already bound in AWT1.2
			repaint();
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

}