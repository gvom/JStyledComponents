package gmai.jstyledcomponents;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.accessibility.Accessible;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.plaf.basic.ComboPopup;

/**
 * JCheckedComboBox is a custom JComboBox that allows users to select multiple
 * items using checkboxes. It extends the JComboBox class and uses a custom
 * ListCellRenderer to display checkboxes next to each item. The selected items
 * are displayed in the JComboBox as a comma-separated string.
 * 
 * @param <E> the type of CheckableItem used in the JCheckedComboBox
 */
public class JCheckedComboBox<E extends CheckableItem> extends JComboBox<E> {

	private boolean keepOpen;
	private transient ActionListener listener;

	/**
	 * Default constructor for JCheckedComboBox. Sets the ITEMS_LIMIT to 0.
	 */
	protected JCheckedComboBox() {
		super();
		CCBVAR.ITEMS_LIMIT = 0;
	}

	/**
	 * Constructor for JCheckedComboBox that takes an array of CheckableItems. Sets
	 * the ITEMS_LIMIT to 0.
	 * 
	 * @param m an array of CheckableItems to be added to the JCheckedComboBox
	 */
	protected JCheckedComboBox(CheckableItem... m) {
		super((ComboBoxModel<E>) new DefaultComboBoxModel<>(m));
		CCBVAR.ITEMS_LIMIT = 0;
	}

	/**
	 * Constructor for JCheckedComboBox that takes an integer limit and an array of
	 * CheckableItems. Sets the ITEMS_LIMIT to the given limit.
	 * 
	 * @param limit the maximum number of items that can be selected in the
	 *              JCheckedComboBox
	 * @param m     an array of CheckableItems to be added to the JCheckedComboBox
	 */
	protected JCheckedComboBox(Integer limit, CheckableItem... m) {
		super((ComboBoxModel<E>) new DefaultComboBoxModel<>(m));
		CCBVAR.ITEMS_LIMIT = limit;
	}

	/**
	 * Constructor for JCheckedComboBox that takes a separator string and an array
	 * of CheckableItems. Sets the ITEM_SEPARATOR to the given separator string and
	 * the ITEMS_LIMIT to 0.
	 * 
	 * @param separator the string used to separate selected items in the
	 *                  JCheckedComboBox display
	 * @param m         an array of CheckableItems to be added to the
	 *                  JCheckedComboBox
	 */
	protected JCheckedComboBox(String separator, CheckableItem... m) {
		super((ComboBoxModel<E>) new DefaultComboBoxModel<>(m));
		CCBVAR.ITEM_SEPARATOR = separator;
		CCBVAR.ITEMS_LIMIT = 0;
	}

	/**
	 * Constructor for JCheckedComboBox that takes an integer limit, a separator
	 * string, and an array of CheckableItems. Sets the ITEM_SEPARATOR to the given
	 * separator string and the ITEMS_LIMIT to the given limit.
	 * 
	 * @param limit     the maximum number of items that can be selected in the
	 *                  JCheckedComboBox
	 * @param separator the string used to separate selected items in the
	 *                  JCheckedComboBox display
	 * @param m         an array of CheckableItems to be added to the
	 *                  JCheckedComboBox
	 */
	protected JCheckedComboBox(Integer limit, String separator, CheckableItem... m) {
		super((ComboBoxModel<E>) new DefaultComboBoxModel<>(m));
		CCBVAR.ITEM_SEPARATOR = separator;
		CCBVAR.ITEMS_LIMIT = limit;
	}

	/**
	 * Overrides the updateUI method of JComboBox to set a custom ListCellRenderer
	 * and ActionListener. The custom ListCellRenderer displays checkboxes next to
	 * each item in the JCheckedComboBox. The ActionListener updates the selected
	 * state of the CheckableItems when a checkbox is clicked.
	 */
	@Override
	public void updateUI() {
		setRenderer(null);
		removeActionListener(listener);
		super.updateUI();

		listener = e -> {
			if ((e.getModifiers() & AWTEvent.MOUSE_EVENT_MASK) != 0) {
				updateItem(getSelectedIndex());
				keepOpen = true;
			}
		};

		setRenderer(new CheckBoxCellRenderer<>());
		addActionListener(listener);
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "checkbox-select");
		getActionMap().put("checkbox-select", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Accessible a = getAccessibleContext().getAccessibleChild(0);
				if (a instanceof ComboPopup) {
					updateItem(((ComboPopup) a).getList().getSelectedIndex());
				}
			}
		});
	}

	/**
	 * Updates the selected state of the CheckableItem at the given index. If the
	 * ITEMS_LIMIT has been reached, the selected state will not be changed.
	 * 
	 * @param index the index of the CheckableItem to update
	 */
	protected void updateItem(int index) {
		if (isPopupVisible()) {
			E item = getItemAt(index);
			if (CCBVAR.ITEMS_LIMIT == 0 || CCBVAR.ITEMS_LIMIT > CCBVAR.N_SELECTED_ITEMS
					|| (CCBVAR.ITEMS_LIMIT == CCBVAR.N_SELECTED_ITEMS && item.isSelected())) {
				item.setSelected(!item.isSelected());
				setSelectedIndex(-1);
				setSelectedItem(item);
			}
		}
	}

	/**
	 * Sets the ITEMS_LIMIT to the given limit.
	 * 
	 * @param limit the maximum number of items that can be selected in the
	 *              JCheckedComboBox
	 */
	public void setLimit(int limit) {
		CCBVAR.ITEMS_LIMIT = limit;
	}

	/**
	 * Returns a List of all CheckableItems that are currently selected in the
	 * JCheckedComboBox.
	 * 
	 * @return a List of all selected CheckableItems
	 */
	public List<CheckableItem> getAllSelectedItems() {
		return IntStream.range(0, getModel().getSize()).mapToObj(getModel()::getElementAt)
				.filter(CheckableItem::isSelected).collect(Collectors.toList());
	}

	/**
	 * Causes the combo box to display its popup window.
	 * 
	 * @see #setPopupVisible
	 */
	@Override
	public void setPopupVisible(boolean v) {
		if (keepOpen) {
			keepOpen = false;
		} else {
			super.setPopupVisible(v);
		}
	}
}

/**
 * Represents an item that can be checked or unchecked.
 */
class CheckableItem {
	/**
	 * The text associated with the item.
	 */
	public final String text;

	/**
	 * Whether the item is currently selected or not.
	 */
	private boolean selected;

	/**
	 * Additional data associated with the item.
	 */
	public final Object data;

	/**
	 * Constructs a new CheckableItem with the given text.
	 * 
	 * @param text The text associated with the item.
	 */
	protected CheckableItem(String text) {
		this.text = text;
		this.selected = false;
		this.data = null;
	}

	/**
	 * Constructs a new CheckableItem with the given text and selection status.
	 * 
	 * @param text     The text associated with the item.
	 * @param selected Whether the item is initially selected or not.
	 */
	protected CheckableItem(String text, boolean selected) {
		this.text = text;
		this.selected = selected;
		this.data = null;
	}

	/**
	 * Constructs a new CheckableItem with the given text and additional data.
	 * 
	 * @param text The text associated with the item.
	 * @param data Additional data associated with the item.
	 */
	protected CheckableItem(String text, Object data) {
		this.text = text;
		this.selected = false;
		this.data = data;
	}

	/**
	 * Constructs a new CheckableItem with the given text, additional data, and
	 * selection status.
	 * 
	 * @param text     The text associated with the item.
	 * @param data     Additional data associated with the item.
	 * @param selected Whether the item is initially selected or not.
	 */
	protected CheckableItem(String text, Object data, boolean selected) {
		this.text = text;
		this.selected = selected;
		this.data = data;
	}

	/**
	 * Returns whether the item is currently selected or not.
	 * 
	 * @return Whether the item is currently selected or not.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the selection status of the item.
	 * 
	 * @param selected Whether the item should be selected or not.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Returns the additional data associated with the item.
	 * 
	 * @return The additional data associated with the item.
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Returns the text associated with the item.
	 * 
	 * @return The text associated with the item.
	 */
	@Override
	public String toString() {
		return text;
	}
}

/**
 * Renders a CheckableItem as a checkbox in a JList.
 * 
 * @param <E> The type of CheckableItem being rendered.
 */
class CheckBoxCellRenderer<E extends CheckableItem> implements ListCellRenderer<E> {

	/**
	 * The label used to display the selected items.
	 */
	private final JLabel label = new JLabel(" ");

	/**
	 * The checkbox used to display each item.
	 */
	private final JCheckBox check = new JCheckBox(" ");

	/**
	 * Renders the given CheckableItem as a checkbox in a JList.
	 * 
	 * @param list         The JList being rendered.
	 * @param value        The CheckableItem being rendered.
	 * @param index        The index of the item being rendered.
	 * @param isSelected   Whether the item is currently selected or not.
	 * @param cellHasFocus Whether the cell has focus or not.
	 * @return The rendered component.
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected,
			boolean cellHasFocus) {

		// If this is the label for the selected items
		if (index < 0) {
			// If there is no item limit or the limit has not been reached
			if (CCBVAR.ITEMS_LIMIT == 0 || CCBVAR.ITEMS_LIMIT >= CCBVAR.N_SELECTED_ITEMS) {
				// Get the string representation of the selected items
				String txt = getCheckedItemString(list.getModel());
				// Set the label text to the selected items string or a space if there are no
				// selected items
				label.setText(txt.isEmpty() ? " " : txt);
				// Update the number of selected items
				CCBVAR.N_SELECTED_ITEMS = IntStream.range(0, list.getModel().getSize())
						.mapToObj(list.getModel()::getElementAt).filter(CheckableItem::isSelected)
						.collect(Collectors.toList()).size();
			}
			// Return the label
			return label;
		} else {
			// Set the selected icon for the checkbox
			check.setSelectedIcon(icon(list.getSelectionBackground()));

			// Set the text and selection status of the checkbox
			check.setText(Objects.toString(value, ""));
			check.setSelected(value.isSelected());

			// If the item is selected
			if (isSelected) {
				// Set the background and foreground colors and icon for the checkbox
				check.setBackground(list.getSelectionBackground());
				check.setForeground(list.getSelectionForeground());
				check.setIcon(icon(list.getSelectionBackground()));
			} else {
				// Set the icon and background color for the checkbox based on its selection
				// status and the list background color
				check.setIcon(icon(list.getBackground()));
				if (value.isSelected()) {
					if (!isDark(list.getBackground()))
						check.setBackground(Color.LIGHT_GRAY);
					else
						check.setBackground(Color.DARK_GRAY);
				} else {
					check.setBackground(list.getBackground());
				}
				// Set the foreground color for the checkbox
				check.setForeground(list.getForeground());
			}
			// Return the checkbox
			return check;
		}
	}

	/**
	 * Returns whether the given color is considered "dark" or not.
	 * 
	 * @param color The color to check.
	 * @return Whether the color is "dark" or not.
	 */
	public static boolean isDark(Color color) {
		float r = color.getRed() / 255.0f;
		float g = color.getGreen() / 255.0f;
		float b = color.getBlue() / 255.0f;

		// measure distance to white and black respectively
		double dWhite = colorDistance(r, g, b, 1.0, 1.0, 1.0);
		double dBlack = colorDistance(r, g, b, 0.0, 0.0, 0.0);
		return dBlack < dWhite;
	}

	/**
	 * Calculates the distance between two colors.
	 * 
	 * @param r1 The red component of the first color.
	 * @param g1 The green component of the first color.
	 * @param b1 The blue component of the first color.
	 * @param r2 The red component of the second color.
	 * @param g2 The green component of the second color.
	 * @param b2 The blue component of the second color.
	 * @return The distance between the two colors.
	 */
	public static double colorDistance(double r1, double g1, double b1, double r2, double g2, double b2) {
		double a = r2 - r1;
		double b = g2 - g1;
		double c = b2 - b1;
		return Math.sqrt(a * a + b * b + c * c);
	}

	/**
	 * Creates an icon filled with the given color.
	 * 
	 * @param filled The color to fill the icon with.
	 * @return The created icon.
	 */
	protected Icon icon(Color filled) {
		BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(filled);
		g2d.fillRoundRect(0, 0, 10, 10, 4, 4);
		g2d.dispose();
		return new ImageIcon(img);
	}

	/**
	 * Returns a string representation of the selected items in the given model.
	 * 
	 * @param model The model to get the selected items from.
	 * @return A string representation of the selected items.
	 */
	private static <E extends CheckableItem> String getCheckedItemString(ListModel<E> model) {
		return IntStream.range(0, model.getSize()).mapToObj(model::getElementAt).filter(CheckableItem::isSelected)
				.map(Objects::toString).sorted().collect(Collectors.joining(CCBVAR.ITEM_SEPARATOR));
	}
}

/**
 * Contains global variables used by the checkbox list.
 */
class CCBVAR {
	/**
	 * The maximum number of items that can be selected.
	 */
	public static int ITEMS_LIMIT;

	/**
	 * The number of currently selected items.
	 */
	public static int N_SELECTED_ITEMS = 0;

	/**
	 * The separator used between selected items in the label.
	 */
	public static String ITEM_SEPARATOR = ", ";
}