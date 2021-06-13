
// DragHiddenTextField.java, Gabriel Seaver, 2021

package safekeeper.gui.components;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.TransferHandler;

public class DragHiddenTextField extends JTextField {
	
	public DragHiddenTextField (String password) {
		super(password, 1);
		
		// Tool-tip
		setToolTipText("<html>" +
			"Use with caution, as this will copy your password:<br/>" +
			"Drag from the black box into another application to<br/>" +
			"copy your password securely.</html>");
		
		// Enables drag-and-drop
		setDragEnabled(true);
		
		// Sets colors
		setForeground(Color.BLACK);
		setBackground(Color.BLACK);
		setSelectionColor(Color.BLACK);
		setSelectedTextColor(Color.BLACK);
		
		// Sets the mouse listener
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed (MouseEvent e) {
				setText(password);
				selectAll();
				getTransferHandler().exportAsDrag(DragHiddenTextField.this, e, TransferHandler.COPY);
			}
		});
	}
	
}
