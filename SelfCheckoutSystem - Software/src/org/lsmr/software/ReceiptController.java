// Armeen Rashidian
// Printing Receipt 

package org.lsmr.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.ReceiptPrinterListener;
import org.lsmr.selfcheckout.products.Product;

public class ReceiptController {
	
	public ReceiptPrinter printer;
	private RPListener rpListener = new RPListener(this);
	private Map<Product, Integer> productQuantities;
	private boolean outOfInk = false;
	private boolean outOfPaper = false;
	
	public ReceiptController(ReceiptPrinter printer) {
		this.printer = printer;
		this.printer.register(rpListener);
	}

	public void quantisize(Purchase currentPurchase) {
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
	
	public void printReceipt(Purchase currentPurchase) {
		String header = getHeader();
		quantisize(currentPurchase);
		String body = "";
		for (Map.Entry<Product, Integer> entry: productQuantities.entrySet()) {
			String productName = entry.getKey().toString();
			String productQuantity = entry.getValue().toString();
			String productPrice = "$" + (entry.getKey().toString());
			String totalProductPrice = "$" + entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())).toString();
			String entryLine = getLine(productName, productQuantity, productPrice, totalProductPrice);
			body = body + entryLine;
		}
		
		String endLines = getFooter(currentPurchase);
		
		String toPrint = header + body + endLines;

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
	
	public String getHeader() {
		String itemHeader = "Item";
		String priceHeader = "Price";
		int lineLength = printer.CHARACTERS_PER_LINE; 	// 60
		
		// Assume chars per line enough for header
		
		String header = itemHeader;
		int i = header.length();
		while (i < (lineLength - priceHeader.length())) {
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
