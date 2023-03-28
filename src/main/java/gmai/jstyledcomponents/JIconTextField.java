package gmai.jstyledcomponents;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * A custom JTextField that allows an icon to be displayed on the left side of
 * the text field. The icon can be set and retrieved using the setIcon() and
 * getIcon() methods respectively. The text field's margin is adjusted to
 * accommodate the icon.
 */
public class JIconTextField extends JTextField {

	private Icon icon;
	private Insets dummyInsets;

	/**
	 * Constructs a new JIconTextField with the specified text and number of
	 * columns.
	 * 
	 * @param text    The initial text to be displayed in the text field.
	 * @param columns The number of columns to be displayed in the text field.
	 */
	public JIconTextField(String text, int columns) {
		super();
		this.icon = null;

		Border border = UIManager.getBorder("TextField.border");
		JTextField dummy = new JTextField();
		this.dummyInsets = border.getBorderInsets(dummy);
		this.setColumns(columns);
		this.setText(text);
	}

	/**
	 * Sets the icon to be displayed on the left side of the text field.
	 * 
	 * @param icon The icon to be displayed.
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	/**
	 * Retrieves the icon currently displayed on the left side of the text field.
	 * 
	 * @return The icon currently displayed.
	 */
	public Icon getIcon() {
		return this.icon;
	}

	/**
	 * Overrides the paintComponent method to paint the icon and adjust the text
	 * field's margin.
	 * 
	 * @param g The Graphics object used for painting.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int textX = 2;

		if (this.icon != null) {
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			int iconX = dummyInsets.left + 5;
			textX = iconX + iconWidth + 2;
			int iconY = (this.getHeight() - iconHeight) / 2;
			icon.paintIcon(this, g, iconX, iconY);
		}

		setMargin(new Insets(2, textX, 2, 2));

	}

}