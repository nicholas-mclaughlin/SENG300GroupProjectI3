package org.lsmr.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.Product;
import org.lsmr.software.DatabaseController;
import org.lsmr.software.Purchase;
import org.lsmr.software.ReceiptController;

public class ReceiptControllerTest {
	
	public ReceiptController receiptController;
	public ReceiptPrinter receiptPrinter;
	
	public class ProductStub extends BarcodedProduct {
		public String name;
		
		public ProductStub(Barcode barcode, String description, BigDecimal price) {
			super(barcode, description, price);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public String toString() {
			return name;
		}

	}
	
	@Before
	public void setup() {
		receiptPrinter = new ReceiptPrinter();
		receiptController = new ReceiptController(receiptPrinter);
	}
	
	@Test
	public void testConstruction() {
		ReceiptPrinter printer = new ReceiptPrinter();
		ReceiptController stub = new ReceiptController(printer);
		
		assertEquals(printer, stub.printer);
	}
	
	@Test(expected = SimulationException.class)
	public void receiptNullPurhcase() {
		receiptController.printReceipt(null);
	}
	
	@Test
	public void receiptEmptyPurchase() {
		ReceiptPrinter printerStub = new ReceiptPrinter();
		ReceiptController stub = new ReceiptController(printerStub);
		Purchase purchaseStub = new Purchase();
		printerStub.addInk(10000);
		printerStub.addPaper(1000);
		
		String expected = 
				"Item                                                   Price\n" +
				"\n"															 +			
				"                                                Total: $0.00\n" + 
				"\n"															 +		
				"                                                 Paid: $0.00\n" + 
				"\n"															 +		
				"                                              Balance: $0.00\n";
		

		stub.printReceipt(purchaseStub);
		String receipt = printerStub.removeReceipt();
		System.out.println(receipt);
		//System.out.println(expected);
		assertEquals(receipt, expected);
	}
	
	@Test
	public void receiptSinglePurchase() {
		ReceiptPrinter printerStub = new ReceiptPrinter();
		ReceiptController stub = new ReceiptController(printerStub);
		Purchase purchaseStub = new Purchase();
		ProductStub productStub =  new ProductStub(new Barcode("00000"), "Random description", BigDecimal.valueOf(5.00));
		productStub.name = "TestName";
		purchaseStub.addItem(productStub);
	
		printerStub.addInk(10000);
		printerStub.addPaper(1000);
		
		String expected = 
				"Item                                                   Price\n" +
				"\n"															 +
				"1x TestName                                            $5.00\n" +			
				"\n"															 +			
				"                                                Total: $5.00\n" + 
				"\n"															 +		
				"                                                 Paid: $5.00\n" + 
				"\n"															 +		
				"                                              Balance: $0.00\n";
		

		stub.printReceipt(purchaseStub);
		String receipt = printerStub.removeReceipt();
		System.out.println(receipt);
		System.out.println(expected);
		assertEquals(receipt, expected);
	}
	
	
	
	
	

}
