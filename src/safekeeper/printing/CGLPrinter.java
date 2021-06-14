
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
		JTextArea textArea = new JTextArea(text);
		textArea.setLineWrap(true);
		textArea.setFont(GUIUtils.fontPrinter);
		
		// Printer job init
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		MessageFormat topFormat = null;
		MessageFormat bottomFormat = new MessageFormat("Page {0}");
		printerJob.setPrintable(textArea.getPrintable(topFormat, bottomFormat));
		
		if (printerJob.printDialog()) {
			// Print dialog was confirmed
			printerJob.print();
			return true;
		} return false;
	}
	
	private static String getTextToPrint (CategoryGroupList cgl) {
		Object[] categories = cgl.getCategoriesAlphabetical();
		
		// No categories
		if (categories.length == 0) return "[No Categories]";
		
		// Loop through categories
		String text = "";
		for (Object category : categories)
			text += getTextToPrint((CategoryGroup)category);
		return text;
	}
	
	private static String getTextToPrint (CategoryGroup category) {
		Object[] accounts = category.getAccountsAlphabetical();
		String text = indent(0, category.name);
		
		// No accounts
		if (accounts.length == 0)
			return text + indent(1, "[No Accounts]");
		
		// Loops through accounts
		for (Object account : accounts)
			text += getTextToPrint((AccountGroup)account);
		return text;
	}
	
	private static String getTextToPrint (AccountGroup account) {
		String text = indent(1, account.company);
		text += indent(2, "Username: " + account.username);
		text += indent(2, "Password: " + account.getPassword());
		if (!account.notes.isEmpty())
			text += indent(2, "Notes:") + indent(3, account.notes);
		
		return text;
	}
	
	private static final int
		SPACES_PER_INDENT = 3,
		MAX_CHARS_IN_LINE = 50;
	
	private static String indent (int indents, String text) {
		// Get the indent space
		final String space = " ".repeat(indents * SPACES_PER_INDENT);
		
		// Keep cutting off lines until there is no more text left
		String outText = "";
		
		while (!text.equals("")) {
			
			// Get the next line of text, which should generally be the maximum number of chars
			// in a line (minus what will be added by the indentation) but can never be longer
			// than the number of chars left
			int lengthToCut = Math.min(MAX_CHARS_IN_LINE - space.length(), text.length());
			String nextLine = text.substring(0, lengthToCut);
			
			int lineBreakIndex = nextLine.indexOf('\n');
			if (lineBreakIndex == -1) {
				// The line does not have a line break already, add the entire line to the output text
				outText += space + nextLine + "\n";
			} else {
				// The line already has a line break, add only the text up until the line break
				nextLine = nextLine.substring(0, lineBreakIndex + 1);
				
				// Add the next line
				outText += space + nextLine;
			}
			
			// Remove the cut text
			text = text.substring(nextLine.length());
			
		}
		
		return outText;
	}
	
	public static boolean printCGL (CategoryGroupList cgl) throws PrinterException {
		return print(getTextToPrint(cgl));
	}
	
}