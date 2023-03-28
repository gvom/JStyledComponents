package gmai.jstyledcomponents;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

/**
 * The JToggleButton class extends the JToggleButton class and implements
 * several listener interfaces. It creates a custom toggle button with two
 * states, ON and OFF, and a sliding animation when toggled. The button can be
 * dragged to change its state, and it changes color based on its state.
 */
public class JToogleButton extends JToggleButton
		implements ActionListener, Runnable, MouseMotionListener, MouseListener, HierarchyListener {

	private int buttonX;
	private int deltaX = -1;
	private boolean drag;
	private String on = "ON", off = "OFF";

	/**
	 * When the user selects the button, start a new thread to load the data.
	 *
	 * @param selected The boolean value that determines whether the button is
	 *                 selected or not.
	 */
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		new Thread(this).start();
	}

	/**
	 * A constructor that sets the active and inactive text of the button
	 * 
	 * @param activeText  the custom text to the active mode.
	 * @param disableText the custom text to the disabled mode.
	 */
	public JToogleButton(String activeText, String disableText) {
		super();
		this.addActionListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addHierarchyListener(this);
		this.on = activeText;
		this.off = disableText;
	}

	/**
	 * This is the default constructor for the JToogleButton class. That sets the
	 * default active and inactive text of the button.
	 */
	public JToogleButton() {
		super();
		this.addActionListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addHierarchyListener(this);
	}

	/**
	 * Draws the button and its animation
	 *
	 * @param g The Graphics object to paint to.
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int width = Math.round(getWidth() / 2);
		int round = 3;

		if (buttonX >= width) {
			g2d.setColor(UIManager.getColor("Actions.Blue"));
		} else if (buttonX < width) {
			g2d.setColor(UIManager.getColor("Actions.Red"));
		}

		g2d.fillRoundRect(0, 0, width * 2, getHeight(), round, round);

		g2d.setColor(UIManager.getColor("Button.foreground"));
		FontMetrics fm = g2d.getFontMetrics();
		g.setFont(UIManager.getDefaults().getFont("ToggleButton.font"));

		String mode = on.toUpperCase();
		Rectangle2D r = fm.getStringBounds(mode, g2d);
		int x = (width - (int) r.getWidth()) / 2;
		int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
		g.drawString(mode, x, y);

		mode = off.toUpperCase();
		r = fm.getStringBounds(mode, g2d);
		x = width + (width - (int) r.getWidth()) / 2;
		g.drawString(mode, x, y);

		g2d.setColor(UIManager.getColor("Button.background"));
		g2d.fillRoundRect(buttonX, 0, width, getHeight(), round, round);

		g2d.setColor(UIManager.getColor("ComboBox.disabledForeground"));
		g2d.drawRoundRect(0, 0, (width * 2) - 1, getHeight() - 1, round, round);
	}

	/**
	 * If the user is not dragging the mouse, then start a new thread that will run
	 * the run() function
	 *
	 * @param e The event that triggered the action.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!drag) {
			// threadStop = true;
			new Thread(this).start();
		}
	}

	/**
	 * Runnable interface to animate the button's sliding animation. If the button
	 * is selected, move the button to the right, otherwise move it to the left
	 */
	@Override
	public void run() {
		// while (true) {
		// if (threadStop) {
		if (this.isSelected()) {
			for (; buttonX <= Math.round(getWidth() / 2) - 1; buttonX++) {
				this.repaint();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} else {
			for (; buttonX > 0; buttonX--) {
				this.repaint();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		repaint();
	}

	/**
	 * If the mouse is being dragged, set the button's x position to the mouse's x
	 * position minus the deltaX value
	 *
	 * @param evt The mouse event that triggered the method.
	 */
	@Override
	public void mouseDragged(MouseEvent evt) {
		drag = true;
		// threadStop = false;
		if (deltaX == -1) {
			deltaX = evt.getX() - buttonX;
		}

		buttonX = evt.getX() - deltaX;

		if (buttonX < 0) {
			buttonX = 0;
		}
		if (buttonX > this.getWidth() / 2) {
			buttonX = this.getWidth() / 2;
		}

		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// threadStop = false;
	}

	/**
	 * If the mouse is released, and the button was being dragged, then the button
	 * is set to the state that it was dragged to
	 *
	 * @param arg0 The mouse event that triggered the method.
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		deltaX = -1;
		if (drag) {
			if (buttonX < this.getWidth() / 4) {
				this.setSelected(false);
			} else {
				this.setSelected(true);
			}
		}
		drag = false;
	}

	/**
	 * When the hierarchy of the component changes, start a new thread that will run
	 * the run() function.
	 *
	 * @param arg0 The event that triggered this method.
	 */
	@Override
	public void hierarchyChanged(HierarchyEvent arg0) {
		new Thread(this).start();
	}
}
