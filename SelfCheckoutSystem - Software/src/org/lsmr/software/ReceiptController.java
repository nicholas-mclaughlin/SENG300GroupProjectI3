// Armeen Rashidian
// Printing Receipt 

package org.lsmr.software;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.ReceiptPrinterListener;
import org.lsmr.selfcheckout.products.Product;

public class ReceiptController {
	
	public ReceiptPrinter printer;
	private RPListener rpListener = new RPListener(this);
	private Map<Product, Integer> productQuantities;
	private boolean outOfInk = false;
	private boolean outOfPaper = false;
	private int inkRemaining = 0;
	private int paperRemaining = 0;
	private int CHARS_PER_LINE = 0;
	private DatabaseController databaseController;


	// format of receipt to be printed
	// Left Column........RightColumn (first character of left column always index 0 at each line. 
	// Last char of right column always last position of line
	// There should always be a separation of at least three spaces between the columns for good formatting
	// If a field is too big to fit in its space, a line is skipped when max length is reached, and rest writen below with a '-' char
	// Hyphenated lines follow the same allignment formatting principals for 
	
	public String twoColumnFormat(String field1, String field2) {
		int midPoint = CHARS_PER_LINE/2;
		int leftLimit = midPoint-3;
		int rightLimit = midPoint+3;
		String res = "";

		while ((field1.length() > 0) || (field2.length() > 0)) {
			//System.out.println(res);
			int j = 0;
			int i = 0;
		
			boolean trail1 = false;
			if ((field1.length()) > leftLimit) {
				trail1 = true;
			}

			for (char c : field1.toCharArray()) {			
				
				if ((i == (leftLimit-1)) && (trail1 == true)) {
					field1 = field1.substring(j);
					res = res + '-';
					i++;
					//j = 0;
					break;
				}
								
				res = res + c;
				i++;
				j++;
				
			}
			
			if (trail1 == false) {
				field1 = "";
			}
			

			// *******
			
			
			while (i < rightLimit) {
				res = res + ' ';
				i++;
			}
			
			j = 0;

			boolean trail = false;
			if ((field2.length()+i) > CHARS_PER_LINE) {
				trail = true;
			}
			
			while ((field2.length()+i) < (CHARS_PER_LINE)) {
				res = res + ' ';
				i++;
			}
			

			for (char c : field2.toCharArray()) {			
				
				if ((i == (CHARS_PER_LINE-1)) && (trail == true)) {
					field2 = field2.substring(j);
					res = res + '-';
					i++;
					//j = 0;
					break;
				}
				
				res = res + c;
				i++;
				j++;
				
			}
			
			if (trail == false) {
				field2 = "";
			}
			res = res + "\n";
			i++;		
	
		}
		//System.out.println(res);
		return res;
	}
	
	/**
	 * Constructor
	 * @param printer
	 */
	public ReceiptController(ReceiptPrinter printer) {
		this.printer = printer;
		this.printer.register(rpListener);
		inkRemaining = this.printer.MAXIMUM_INK;
		paperRemaining = this.printer.MAXIMUM_PAPER;
		CHARS_PER_LINE = this.printer.CHARACTERS_PER_LINE;
	}

	
	/**
	 * Prints the receipt for this purchase 
	 * @param currentPurchase
	 * @throws SimulationException, if purchase is null
	 */
	public void printReceipt(Purchase currentPurchase) throws SimulationException  {
		if (currentPurchase == null) {
			throw new SimulationException(new IllegalArgumentException());
		}
	
		quantisize(currentPurchase);
		// String header = getHeader();
		String header = twoColumnFormat("Item", "Price");

		
		String body = "";
		for (Map.Entry<Product, Integer> entry: productQuantities.entrySet()) {

			String productName = entry.getKey().toString();
			String productQuantity = entry.getValue().toString() + "x " + productName;
			String productPrice = "$" + (entry.getKey().toString());
			String totalProductPrice = "$" + entry.getKey().getPrice().setScale(2, RoundingMode.CEILING).multiply(BigDecimal.valueOf(entry.getValue())).setScale(2, RoundingMode.CEILING).toString();
		//	String totalProductPrice = "$" + entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())).setScale(2, RoundingMode.CEILING).toString();
			//String entryLine = getLine(productName, productQuantity, productPrice, totalProductPrice);
			String entryLine = twoColumnFormat(productQuantity, totalProductPrice);
			body = body + entryLine + "\n";
		} 
		
		String total = currentPurchase.getSubtotal().setScale(2, RoundingMode.CEILING).toString();
		String totalFooter = "Total: " + total;
		String paidFooter = "Paid: " + total;
		String paidBalance = "Balance: " + "$0.00";
		
		String endLine1 = twoColumnFormat("", "Total: " + "$" + total);
		String endLine2 = twoColumnFormat("", "Paid: " + "$" + total);
		String endLine3 = twoColumnFormat("", "Balance: $0.00");
		
		//String endLines = getFooter(currentPurchase);
		
		String toPrint = header + "\n" + body + endLine1 + "\n" + endLine2 + "\n" + endLine3;
		//System.out.println(toPrint);
		printAll(toPrint);
		printer.cutPaper();
	}
	
	public void printAll(String msg ) {
		int i = 0;
		while (i < msg.length()) {
			if (outOfInk == true)  {
				// prematurely stop printing and cut paper 
				break;
			}
			
			if (outOfPaper == true) {
				// prematurely stop printing and cut paper 
				break;
			}
			
			printer.print(msg.charAt(i));
			i++;
		}
	}
	
	/**
	 * Takes purchase and maps each product to quantity
	 * @param currentPurchase
	 * 
	 */
	private void quantisize(Purchase currentPurchase)  {
		productQuantities = new HashMap<>();
		
		for (Product product : currentPurchase.getProducts()) {
			if (productQuantities.containsKey(product) == false) {
				productQuantities.put(product, 1);
			}
			
			else {
				productQuantities.put(product, productQuantities.get(product) + 1); 
			}
		}
		
	}
	
	public String getLine(String productName, String productQuantity, String productPrice, String totalProductPrice) {
		String line = "";
		String column1 = productQuantity + "X" + " " + productName;
		line = column1;
		String column2 = totalProductPrice;
		

		int i = line.length();
		int lineLength = printer.CHARACTERS_PER_LINE; 	// 60
		
		while (i < (lineLength - column2.length())) {
			line = line + ' ';
			i++;
		}
		
		line = line + column2 + "\n";
		
		return line;
	}
	
	// Generate header string for receipt
	// format: "Item <      >  Price"
	private String getHeader() {
		String itemHeader = "Item";
		String priceHeader = "Price";
		
		// Assume chars per line enough for header
		
		String header = itemHeader;
		int i = header.length();
		while (i < (CHARS_PER_LINE - priceHeader.length())) {
			header = header + ' ';
		}
		
		header = header + priceHeader + "\n";
		
		return header;
	}
	
	 
	
	public String getFooter(Purchase currentPurchase) {
		String line = "";
		String total = currentPurchase.getSubtotal().toString();
		String totalFooter = "Total: " + total;
		String paidFooter = "Paid: " + total;
		String paidBalance = "Balance: " + "$0.00";
		
		int i =  0;
		while (i < (printer.CHARACTERS_PER_LINE - totalFooter.length())) {
			line = line + ' ';
		}
		
		line = line + totalFooter + "\n";
		
		i =  0;
		while (i < (printer.CHARACTERS_PER_LINE - paidFooter.length())) {
			line = line + ' ';
		}
		
		line = line + paidFooter + "\n";
		
		i =  0;
		while (i < (printer.CHARACTERS_PER_LINE - paidBalance.length())) {
			line = line + ' ';
		}
		
		line = line + paidBalance + "\n";
		
		return line;
		
		
	}
	
	public void informOutOfInk() {
		outOfInk = true;
	}
	
	public void informInkAdded() {
		outOfInk = false;
	}
	
	public void informOutOfPaper() {
		outOfPaper = true;
	}
	
	public void informPaperAdded() {
		outOfPaper = false;
	}

	public class RPListener implements ReceiptPrinterListener {

		private ReceiptPrinter detectedPrinter;
		private ReceiptController controller;
		
		public RPListener(ReceiptController controller) {
			this.controller = controller;
		}
		
		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void outOfPaper(ReceiptPrinter printer) {
			controller.informOutOfPaper();
		}

		@Override
		public void outOfInk(ReceiptPrinter printer) {
			controller.informOutOfInk();
			
		}

		@Override
		public void paperAdded(ReceiptPrinter printer) {
			controller.informPaperAdded();
			
		}

		@Override
		public void inkAdded(ReceiptPrinter printer) {
			controller.informInkAdded();
		}	
	}
	


	
}
