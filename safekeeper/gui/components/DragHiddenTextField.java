package safekeeper.gui.components;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;

public class DragHiddenTextField extends JTextField {
	public DragHiddenTextField(final String password) {
		super(password, 1);
		setToolTipText("<html>Use with caution, as this will copy your password:<br/>Drag from the black box into another application to<br/>copy your password securely.</html>");
		setDragEnabled(true);
		setForeground(Color.BLACK);
		setBackground(Color.BLACK);
		setSelectionColor(Color.BLACK);
		setSelectedTextColor(Color.BLACK);
		addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent param1MouseEvent) {
						DragHiddenTextField.this.setText(password);
						DragHiddenTextField.this.selectAll();
						DragHiddenTextField.this.getTransferHandler().exportAsDrag(DragHiddenTextField.this, param1MouseEvent, 1);
					}
				});
	}
}
