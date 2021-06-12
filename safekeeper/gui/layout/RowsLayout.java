
// RowsLayout.java, Gabriel Seaver, 2021

package safekeeper.gui.layout;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import safekeeper.gui.util.GUIUtils;

public class RowsLayout extends BoxLayout {
	
	private final Container host;
	private final int hgap, vgap;
	
	public RowsLayout (Container host) {
		this(host, 0, 0);
	}
	
	public RowsLayout (Container host, int hgap, int vgap) {
		super(host, BoxLayout.Y_AXIS);
		this.host = host;
		this.hgap = hgap;
		this.vgap = vgap;
		addVerticalSpacer();
	}
	
	private void addVerticalSpacer () {
		host.add(GUIUtils.makeVerticalStrut(vgap));
	}
	
	public void addRow (JComponent... components) {
		// Create a new row panel
		JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, hgap, 0));
		
		// Add all row components to the row panel
		for (JComponent component : components)
			rowPanel.add(component);
		
		// Add row panel and next vertical spacer
		host.add(rowPanel);
		addVerticalSpacer();
	}
}
