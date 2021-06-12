
// GUIUtils.java, Gabriel Seaver, 2021

package safekeeper.gui.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class GUIUtils {
	
	public static final Font
		font = new Font(Font.DIALOG, Font.PLAIN, 13),
		fontSmall = new Font(Font.DIALOG, Font.PLAIN, 11),
		fontPassword = new Font(Font.MONOSPACED, Font.PLAIN, 11),
		fontButton = fontPassword,
		fontPrinter = new Font(Font.MONOSPACED, Font.PLAIN, 14);
	
	public static final ImageIcon lockIcon = new ImageIcon("safekeeper/resources/lock-icon.png");
	
	public static final int MARGIN = 10;
	
	public static void playErrorSound () {
		Runnable sound = (Runnable)Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
		if (sound != null) sound.run();
	}
	
	public static void setWindowsStyle () {
		try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception exception) { }
	}
	
	public static void stylizeWindow (Window window, Window parentWindow, WindowClosingListener closingListener) {
		// Set not resizeable
		if (window instanceof Frame) ((Frame)window).setResizable(false);
		else if (window instanceof Dialog) ((Dialog)window).setResizable(false);
		
		// Set icon
		window.setIconImage(lockIcon.getImage());
		
		// Add window closing listener
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing (WindowEvent e) {
				closingListener.onWindowClosing();
			}
		});
	}
	
	@FunctionalInterface
	public static interface WindowClosingListener {
		abstract void onWindowClosing();
	}
	
	public static void finalizeWindow (Window window, Window parentWindow) {
		window.setLocationRelativeTo(parentWindow);
		window.setVisible(true);
	}
	
	public static JDialog makeNewDialog (String title, Window parentWindow, WindowClosingListener closingListener) {
		JDialog dialog = new JDialog(parentWindow, title);
		
		// Set to not do anything on close that closingListener doesn't have to override
		// default operations
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		stylizeWindow(dialog, parentWindow, closingListener);
		
		return dialog;
	}
	
	public static void setSize (JComponent c, int width, int height) {
		Dimension d = new Dimension(width, height);
		c.setPreferredSize(d);
		c.setMinimumSize(d);
		c.setMaximumSize(d);
	}
	
	public static Border createMarginBorder (int margin) {
		return BorderFactory.createEmptyBorder(margin, margin, margin, margin);
	}
	
	public static JLabel makeLabel (String text, boolean useSmallFont) {
		if (text.contains("\n"))
			text = "<html>" + text.replaceAll("\n", "<br />") + "</html>";
		JLabel label = new JLabel(text);
		label.setFont(useSmallFont ? fontSmall : font);
		return label;
	}
	
	public static JButton makeButton (String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.setFont(fontButton);
		button.addActionListener(listener);
		button.setFocusPainted(false);
		return button;
	}
	
	public static JTextField makeTextField (String text, boolean useSmallFont) {
		JTextField field = new JTextField(text, 1);
		field.setFont(useSmallFont ? fontSmall : font);
		return field;
	}
	
	public static JTextField makeTextField (boolean useSmallFont) {
		return makeTextField("", useSmallFont);
	}
	
	public static Component makeVerticalStrut (int height) {
		return Box.createVerticalStrut(height);
	}
	
	public static Component makeHorizontalStrut (int width) {
		return Box.createHorizontalStrut(width);
	}
	
	public static JPanel makeStretchPanel (Component c) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(c, BorderLayout.CENTER);
		return panel;
	}
	
	public static final int CLOSED_OPTION = JOptionPane.CLOSED_OPTION;
	
	public static int showOptionChooser (
			Window parentWindow,
			String message,
			String title,
			String[] options,
			int defaultOptionIndex) {
		return JOptionPane.showOptionDialog(
			parentWindow,
			message,
			title,
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.PLAIN_MESSAGE,
			null,
			options, options[defaultOptionIndex]);
	}
	
	public static void showMessage (Window parentWindow, String message, String title) {
		JOptionPane.showMessageDialog(parentWindow, message, title, JOptionPane.PLAIN_MESSAGE);
	}
	
	public static void showFatalError (String message) {
		playErrorSound();
		showMessage(null, "A fatal error has occurred:\n" + message, "Error");
		System.exit(1);
	}
	
	public static void showFatalError (Exception e) {
		showFatalError(e.toString());
	}
	
	public static void showWarning (String message) {
		playErrorSound();
		showMessage(null, message, "Warning");
	}
	
	public static void showWarning(Exception e) {
		showWarning(e.toString());
	}
	
	/**
	 * Adds a JPanel with a margin around the given component, returning the surrounding JPanel.
	 */
	public static Component addMargin (Component c, int margin) {
		JPanel panel = new JPanel();
		panel.add(c);
		panel.setBorder(createMarginBorder(margin));
		return panel;
	}
	
}