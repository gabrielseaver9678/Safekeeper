package safekeeper.gui.layout;

import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import safekeeper.gui.util.GUIUtils;

public class RowsLayout extends BoxLayout {
  private final Container host;
  
  private final int hgap;
  
  private final int vgap;
  
  public RowsLayout(Container paramContainer) {
    this(paramContainer, 0, 0);
  }
  
  public RowsLayout(Container paramContainer, int paramInt1, int paramInt2) {
    super(paramContainer, 1);
    this.host = paramContainer;
    this.hgap = paramInt1;
    this.vgap = paramInt2;
    addVerticalSpacer();
  }
  
  private void addVerticalSpacer() {
    this.host.add(GUIUtils.makeVerticalStrut(this.vgap));
  }
  
  public void addRow(JComponent... paramVarArgs) {
    JPanel jPanel = new JPanel(new FlowLayout(1, this.hgap, 0));
    for (JComponent jComponent : paramVarArgs)
      jPanel.add(jComponent); 
    this.host.add(jPanel);
    addVerticalSpacer();
  }
}
