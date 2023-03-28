package gmai.jstyledcomponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;

/**
 * The JPromptProcess class provides a dialog window to display the output of a
 * process or command. It extends JDialog and implements ActionListener.
 */
public class JPromptProcess extends JDialog {

	private String content;
	private Process proc;
	private JTextArea textArea;
	private boolean process;

	public static String BUILD_ERROR_TEXT;

	/**
	 * This is a constructor for the JPromptProcess class. Creates a dialog window
	 * with the given title and content
	 * 
	 * @param parent  the parent component.
	 * @param title   the window title.
	 * @param content the initial text.
	 */
	public JPromptProcess(JFrame parent, String title, String content) {
		super(parent, title);
		this.content = content;
		this.proc = null;
		this.process = false;
		loadGUI();
		setModal(true);
		setVisible(true);
	}

	/**
	 * This is a constructor for the JPromptProcess class. Creates a dialog window
	 * with the given title and displays the output of the given process.
	 * 
	 * @param parent the parent component.
	 * @param title  the window title.
	 * @param proc   the process object to get the execution log.
	 */
	public JPromptProcess(JFrame parent, String title, Process proc) {
		super(parent, title);
		this.content = "Loading...";
		this.proc = proc;
		this.process = true;
		loadGUI();
		// setModal(true);
		setVisible(true);
	}

	/**
	 * Creates and initializes the GUI components of the dialog window.
	 */
	private void loadGUI() {
		Container c = getContentPane();

		textArea = new JTextArea(content);
		textArea.setBorder(new EmptyBorder(0, 10, 0, 0));
		textArea.setEditable(false);

		JScrollPane sp = new JScrollPane(textArea);

		if (this.process) {
			DefaultCaret caret = (DefaultCaret) textArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		} else {
			JToolBar toolBar = new JToolBar();
			toolBar.setRollover(true);
			toolBar.setFloatable(false);

			// Left Side of toolBar
			// Add before create horixontal glue to be on the left side
			toolBar.add(Box.createHorizontalGlue());

			JButton copyButton = new JButton("  Copy  ");
			copyButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String text = textArea.getText();
					if (text != null && text != "") {
						StringSelection selection = new StringSelection(text);
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);

						BalloonTip ballon = new BalloonTip(copyButton, new JLabel("Copied!"),
								new RoundedBalloonStyle(5, 5, UIManager.getColor("Button.background"),
										UIManager.getColor("ComboBox.disabledForeground")),
								BalloonTip.Orientation.RIGHT_ABOVE, BalloonTip.AttachLocation.ALIGNED, 15, 15, false);

						// Waits x mili to hide the ballon
						SwingUtilities.invokeLater(() -> {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException s) {
							}
							ballon.closeBalloon();
						});
					}
				}
			});

			toolBar.add(copyButton);
			toolBar.add(Box.createHorizontalStrut(10));
			c.add(toolBar, BorderLayout.SOUTH);
		}

		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		c.add(sp);

		setSize(1000, 600);
		centerWindow(this);
	}

	/**
	 * If the dialog window is displaying the output of a process, this method reads
	 * the output of the process and displays it in the textArea.
	 */
	public void process() {
		if (this.process) {

			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {

					if (proc == null) {
						textArea.setText(JPromptProcess.BUILD_ERROR_TEXT);
						setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						reload();
						return null;
					}

					BufferedReader stdInput = new BufferedReader(
							new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8));
					BufferedReader stdError = new BufferedReader(
							new InputStreamReader(proc.getErrorStream(), StandardCharsets.UTF_8));

					// Read the output from the command
					log(stdInput);
					// Read any errors from the attempted command
					log(stdError);

					if (proc.exitValue() != 0) {
						JOptionPane.showMessageDialog(new JFrame(),
								"Error in the build process.\n Check the build log.", "Build Status",
								JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "Build finished with success.", "Build Status",
								JOptionPane.INFORMATION_MESSAGE);
					}

					return null;
				}
			}.execute();
		}
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/**
	 * Reads the output of the process and appends it to the textArea.
	 * 
	 * @param bfReader The BufferedReader that will be used to read the output of
	 *                 the process.
	 */
	private void log(BufferedReader bfReader) throws InvocationTargetException, IOException, InterruptedException {
		String s = null;

		while ((s = bfReader.readLine()) != null) {
			final String _s = s.replaceAll("\u001B\\[[\\d;]*[^\\d;]", "");
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					String ret = "\n" + _s;
					textArea.append(ret);
					textArea.setCaretPosition(textArea.getDocument().getLength() - ret.length() + 1);
					reload();
				}
			});
		}
	}

	/**
	 * Centers the dialog window on the screen
	 */
	private void centerWindow(Window window) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - window.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - window.getHeight()) / 2);
		window.setLocation(x, y);
		window.validate();
	}

	/**
	 * Refreshes the GUI components of the dialog window.
	 */
	private void reload() {
		textArea.revalidate();
		textArea.repaint();
		getContentPane().revalidate();
		getContentPane().repaint();
	}
}