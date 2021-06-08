package safekeeper.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.GroupLayout;

public class MatchedPairsLayout extends GroupLayout {
  private final List<Component> leftComponents = new ArrayList<>(), rightComponents = new ArrayList<>();
  
  public MatchedPairsLayout(Container paramContainer) {
    super(paramContainer);
  }
  
  public void addMatch(Container paramContainer1, Container paramContainer2) {
    this.leftComponents.add(paramContainer1);
    this.rightComponents.add(paramContainer2);
  }
  
  public void finalizeLayout() {
    int i = this.leftComponents.size();
    GroupLayout.SequentialGroup sequentialGroup1 = createSequentialGroup();
    GroupLayout.ParallelGroup parallelGroup1 = createParallelGroup(GroupLayout.Alignment.TRAILING);
    for (Component component : this.leftComponents)
      parallelGroup1.addComponent(component); 
    sequentialGroup1.addGroup(parallelGroup1);
    GroupLayout.ParallelGroup parallelGroup2 = createParallelGroup(GroupLayout.Alignment.LEADING);
    for (Component component : this.rightComponents)
      parallelGroup2.addComponent(component); 
    sequentialGroup1.addGroup(parallelGroup2);
    setHorizontalGroup(sequentialGroup1);
    GroupLayout.SequentialGroup sequentialGroup2 = createSequentialGroup();
    for (byte b = 0; b < i; b++)
      sequentialGroup2.addGroup(createParallelGroup(GroupLayout.Alignment.CENTER)
          .addComponent(this.leftComponents.get(b))
          .addComponent(this.rightComponents.get(b))); 
    setVerticalGroup(sequentialGroup2);
  }
}
