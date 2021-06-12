
// MatchedPairsLayout.java, Gabriel Seaver, 2021

package safekeeper.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;

public class MatchedPairsLayout extends GroupLayout {
	
	private final List<Component>
		leftComponents = new ArrayList<Component>(),
		rightComponents = new ArrayList<Component>();
	
	public MatchedPairsLayout (Container host) {
		super(host);
	}
	
	public void addMatch (Component left, Component right) {
		leftComponents.add(left);
		rightComponents.add(right);
	}
	
	public void finalizeLayout () {
		final int rows = leftComponents.size();
		
		// Make the horizontal group
		SequentialGroup horizontalGroup = createSequentialGroup();
		
		// Left vertical group
		ParallelGroup leftGroup = createParallelGroup(Alignment.TRAILING);
		for (Component component : leftComponents)
			leftGroup.addComponent(component);
		horizontalGroup.addGroup(leftGroup);
		
		// Right vertical group
		ParallelGroup rightGroup = createParallelGroup(Alignment.LEADING);
		for (Component component : rightComponents)
			rightGroup.addComponent(component);
		horizontalGroup.addGroup(rightGroup);
		
		setHorizontalGroup(horizontalGroup);
		
		// Make the vertical group
		SequentialGroup verticalGroup = createSequentialGroup();
		for (int row = 0; row < rows; row ++)
			verticalGroup.addGroup(createParallelGroup(Alignment.CENTER)
				.addComponent(leftComponents.get(row))
				.addComponent(rightComponents.get(row)));
		setVerticalGroup(verticalGroup);
	}
	
}