
// SGLPrinter.java, Gabriel Seaver, 2021

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
	
	private static boolean print (String text) throws PrinterException {
		// Makes JTextArea for printing
		JTextArea jTextArea = new JTextArea(text);
		jTextArea.setLineWrap(true);
		jTextArea.setFont(GUIUtils.fontPrinter);
		
		// Printer job init
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		MessageFormat topFormat = null;
		MessageFormat bottomFormat = new MessageFormat("Page {0}");
		printerJob.setPrintable(jTextArea.getPrintable(topFormat, bottomFormat));
		
		if (printerJob.printDialog()) {
			// Print dialog was confirmed
			printerJob.print();
			return true;
		} return false;
	}
	
	private static String getTextToPrint (ServiceGroupList sgl) {
		Object[] services = sgl.getServicesAlphabetical();
		
		// No services
		if (services.length == 0)
			return "[No Services]";
		
		// Loop through services
		String text = "";
		for (Object service : services)
			text += getTextToPrint((ServiceGroup)service);
		return text;
	}
	
	private static String getTextToPrint (ServiceGroup service) {
		Object[] accounts = service.getAccountsAlphabetical();
		String text = service.name + "\n";
		
		// No accounts
		if (accounts.length == 0)
			return "  [No Accounts]\n";
		
		// Loops through accounts
		for (Object account : accounts)
			text += getTextToPrint((AccountGroup)account);
		return text;
	}
	
	private static String getTextToPrint (AccountGroup account) {
		String text = "  " + account.username + "\n";
		// If the email isn't blank, add it as a line
		if (!account.email.isBlank())
			text += "    Email:    " + account.email + "\n";
			text += "    Password: " + account.getPassword() + "\n";
		return text;
	}
	
	public static boolean printSGL (ServiceGroupList sgl) throws PrinterException {
		return print(getTextToPrint(sgl));
	}
	
}