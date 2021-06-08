package safekeeper.printing;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import javax.swing.JTextArea;
import safekeeper.groupings.AccountGroup;
import safekeeper.groupings.ServiceGroup;
import safekeeper.groupings.ServiceGroupList;
import safekeeper.gui.util.GUIUtils;

public class SGLPrinter {
  private static boolean print(String paramString) throws PrinterException {
    JTextArea jTextArea = new JTextArea(paramString);
    jTextArea.setLineWrap(true);
    jTextArea.setFont(GUIUtils.fontPrinter);
    PrinterJob printerJob = PrinterJob.getPrinterJob();
    MessageFormat messageFormat1 = null;
    MessageFormat messageFormat2 = new MessageFormat("Page {0}");
    printerJob.setPrintable(jTextArea.getPrintable(messageFormat1, messageFormat2));
    if (printerJob.printDialog()) {
      printerJob.print();
      return true;
    } 
    return false;
  }
  
  private static String getTextToPrint(ServiceGroupList paramServiceGroupList) {
    Object[] arrayOfObject = paramServiceGroupList.getServicesAlphabetical();
    if (arrayOfObject.length == 0)
      return "[No Services]"; 
    String str = "";
    for (Object object : arrayOfObject)
      str = str + str; 
    return str;
  }
  
  private static String getTextToPrint(ServiceGroup paramServiceGroup) {
    Object[] arrayOfObject = paramServiceGroup.getAccountsAlphabetical();
    String str = paramServiceGroup.name + "\n";
    if (arrayOfObject.length == 0)
      return "  [No Accounts]\n"; 
    for (Object object : arrayOfObject)
      str = str + str; 
    return str;
  }
  
  private static String getTextToPrint(AccountGroup paramAccountGroup) {
    String str = "  " + paramAccountGroup.username + "\n";
    if (!paramAccountGroup.email.equals(""))
      str = str + "    Email:    " + str + "\n"; 
    str = str + "    Password: " + str + "\n";
    return str;
  }
  
  public static boolean printSGL(ServiceGroupList paramServiceGroupList) throws PrinterException {
    return print(getTextToPrint(paramServiceGroupList));
  }
}
