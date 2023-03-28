/**
 * This package contains utility classes for creating styled Swing components.
 */
package gmai.jstyledcomponents;

import static gmai.jstyledcomponents.util.ImageUtil.getImage;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A transparent JButton with custom styling and mouse events.
 */
public class JButtonTransparent extends JButton implements MouseListener, MouseMotionListener {

	// Constants for button styling
	private static final int RED = 0;
	private static final int GREEN = 0;
	private static final int BLUE = 0;
	private static final int ALPHA = 70;
	private static final int WIDTH = 50;
	private static final int HEIGHT = 50;

	private JCard parent; // The parent JCard component

	/**
	 * Constructs a new JButtonTransparent with the given text, image path, and
	 * parent JCard.
	 * 
	 * @param text   the text to display on the button
	 * @param path   the path to the image to use as the button icon
	 * @param parent the parent JCard component
	 */
	public JButtonTransparent(String text, String path, JCard parent) {
		Icon icon = new ImageIcon(getImage(path, WIDTH, HEIGHT)); // Create the button icon
		this.parent = parent;

		setText(text);
		setIcon(icon);
		addActionListener((ActionListener) this.parent); // Add the parent as an ActionListener
		addMouseListener(this); // Add this button as a MouseListener
		setHorizontalTextPosition(JButton.CENTER);
		setVerticalTextPosition(JButton.BOTTOM);
		setForeground(Color.WHITE);
		setBackground(new Color(RED, GREEN, BLUE, ALPHA)); // Set the button background color
		setOpaque(false);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setRolloverEnabled(false);
	}

	/**
	 * Sets the opacity of the button and repaints the parent JCard.
	 * 
	 * @param opaque true if the button should be opaque, false if it should be
	 *               transparent
	 */
	private void setButtonOpacity(boolean opaque) {
		setOpaque(opaque);
		parent.revalidate();
		parent.repaint();
	}

	// MouseListener and MouseMotionListener methods
	@Override
	public void mouseClicked(MouseEvent e) {
		setButtonOpacity(false);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setButtonOpacity(false);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setButtonOpacity(false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setButtonOpacity(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setButtonOpacity(false);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		setButtonOpacity(false);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setButtonOpacity(true);
	}
}