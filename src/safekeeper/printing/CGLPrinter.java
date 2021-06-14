
// CGLPrinter.java, Gabriel Seaver, 2021

package safekeeper.printing;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;

import javax.swing.JTextArea;

import safekeeper.groupings.AccountGroup;
import safekeeper.groupings.CategoryGroup;
import safekeeper.groupings.CategoryGroupList;
import safekeeper.gui.util.GUIUtils;

public class CGLPrinter {
	
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
	
	private static String getTextToPrint (CategoryGroupList cgl) {
		Object[] categories = cgl.getCategoriesAlphabetical();
		
		// No categories
		if (categories.length == 0)
			return "[No Categories]";
		
		// Loop through categories
		String text = "";
		for (Object category : categories)
			text += getTextToPrint((CategoryGroup)category);
		return text;
	}
	
	private static String getTextToPrint (CategoryGroup category) {
		Object[] accounts = category.getAccountsAlphabetical();
		String text = category.name + "\n";
		
		// No accounts
		if (accounts.length == 0)
			return text + "  [No Accounts]\n";
		
		// Loops through accounts
		for (Object account : accounts)
			text += getTextToPrint((AccountGroup)account);
		return text;
	}
	
	private static String getTextToPrint (AccountGroup account) {
		String text = "  " + account.getDisplayName() + "\n";
		// If the email isn't blank, add it as a line
		if (!account.email.isBlank())
			text += "    Email:    " + account.email + "\n";
			text += "    Password: " + account.getPassword() + "\n";
		return text;
	}
	
	public static boolean printCGL (CategoryGroupList cgl) throws PrinterException {
		return print(getTextToPrint(cgl));
	}
	
}