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
	public static final Font font = new Font("Dialog", 0, 13);
	
	public static final Font fontSmall = new Font("Dialog", 0, 11);
	
	public static final Font fontPassword = new Font("Monospaced", 0, 11);
	
	public static final Font fontButton = fontPassword;
	
	public static final Font fontPrinter = new Font("Monospaced", 0, 14);
	
	public static final ImageIcon lockIcon = new ImageIcon("safekeeper/resources/lock-icon.png");
	
	public static final int MARGIN = 10;
	
	public static final int CLOSED_OPTION = -1;
	
	public static void playErrorSound() {
		Runnable runnable = (Runnable)Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
		if (runnable != null)
			runnable.run();
	}
	
	public static void setWindowsStyle() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception exception) {}
	}
	
	public static void stylizeWindow(Window paramWindow1, Window paramWindow2) {
		if (paramWindow1 instanceof Frame)
			((Frame)paramWindow1).setResizable(false);
		if (paramWindow1 instanceof Dialog)
			((Dialog)paramWindow1).setResizable(false);
		paramWindow1.setIconImage(lockIcon.getImage());
		paramWindow1.setLocationRelativeTo(paramWindow2);
		paramWindow1.setVisible(true);
	}
	
	public static void stylizeWindow(Window paramWindow1, Window paramWindow2, final WindowClosingListener closingListener) {
		stylizeWindow(paramWindow1, paramWindow2);
		paramWindow1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent param1WindowEvent) {
						closingListener.onWindowClosing();
					}
				});
	}
	
	@FunctionalInterface
	public static interface WindowClosingListener {
		void onWindowClosing();
	}
	
	public static JDialog makeNewDialog(String paramString, Window paramWindow, WindowClosingListener paramWindowClosingListener) {
		JDialog jDialog = new JDialog(paramWindow, paramString);
		jDialog.setDefaultCloseOperation(0);
		stylizeWindow(jDialog, paramWindow, paramWindowClosingListener);
		return jDialog;
	}
	
	public static void setSize(JComponent paramJComponent, int paramInt1, int paramInt2) {
		Dimension dimension = new Dimension(paramInt1, paramInt2);
		paramJComponent.setPreferredSize(dimension);
		paramJComponent.setMinimumSize(dimension);
		paramJComponent.setMaximumSize(dimension);
	}
	
	public static Border createMarginBorder(int paramInt) {
		return BorderFactory.createEmptyBorder(paramInt, paramInt, paramInt, paramInt);
	}
	
	public static JLabel makeLabel(String paramString, boolean paramBoolean) {
		if (paramString.contains("\n"))
			paramString = "<html>" + paramString.replaceAll("\n", "<br />") + "</html>";
		JLabel jLabel = new JLabel(paramString);
		jLabel.setFont(paramBoolean ? fontSmall : font);
		return jLabel;
	}
	
	public static JButton makeButton(String paramString, ActionListener paramActionListener) {
		JButton jButton = new JButton(paramString);
		jButton.setFont(fontButton);
		jButton.addActionListener(paramActionListener);
		jButton.setFocusPainted(false);
		return jButton;
	}
	
	public static JTextField makeTextField(boolean paramBoolean, String paramString) {
		JTextField jTextField = new JTextField(paramString, 1);
		jTextField.setFont(paramBoolean ? fontSmall : font);
		return jTextField;
	}
	
	public static JTextField makeTextField(boolean paramBoolean) {
		return makeTextField(paramBoolean, "");
	}
	
	public static Component makeVerticalStrut(int paramInt) {
		return Box.createVerticalStrut(paramInt);
	}
	
	public static Component makeHorizontalStrut(int paramInt) {
		return Box.createHorizontalStrut(paramInt);
	}
	
	public static JPanel makeStretchPanel(Component paramComponent) {
		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(paramComponent, "Center");
		return jPanel;
	}
	
	public static int showOptionChooser(Window paramWindow, String paramString1, String paramString2, String[] paramArrayOfString, int paramInt) {
		return JOptionPane.showOptionDialog(paramWindow, paramString1, paramString2, -1, -1, null, (Object[])paramArrayOfString, paramArrayOfString[paramInt]);
	}
	
	public static void showMessage(Window paramWindow, String paramString1, String paramString2) {
		JOptionPane.showMessageDialog(paramWindow, paramString1, paramString2, -1);
	}
	
	public static void showFatalError(String paramString) {
		playErrorSound();
		showMessage(null, "A fatal error has occurred:\n" + paramString, "Error");
		System.exit(1);
	}
	
	public static void showFatalError(Exception paramException) {
		showFatalError(paramException.toString());
	}
	
	public static void showWarning(String paramString) {
		playErrorSound();
		showMessage(null, paramString, "Warning");
	}
	
	public static void showWarning(Exception paramException) {
		showWarning(paramException.toString());
	}
	
	public static Component addMargin(Component paramComponent, int paramInt) {
		JPanel jPanel = new JPanel();
		jPanel.add(paramComponent);
		jPanel.setBorder(createMarginBorder(paramInt));
		return jPanel;
	}
}
