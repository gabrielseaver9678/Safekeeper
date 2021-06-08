package safekeeper;

import safekeeper.gui.util.GUIUtils;

public class Entry {
  public static void main(String[] paramArrayOfString) {
    try {
      new Program(paramArrayOfString);
    } catch (Exception exception) {
      exception.printStackTrace();
      GUIUtils.showFatalError(exception);
    } 
  }
}
