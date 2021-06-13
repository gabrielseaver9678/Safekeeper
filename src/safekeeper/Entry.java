
// Entry.java, Gabriel Seaver, 2021

package safekeeper;

import safekeeper.gui.util.GUIUtils;

public class Entry {
	
	public static void main (String[] args) {
		try {
			new Program(args);
		} catch (Exception e) {
			e.printStackTrace();
			GUIUtils.showFatalError(e);
		}
	}
	
}