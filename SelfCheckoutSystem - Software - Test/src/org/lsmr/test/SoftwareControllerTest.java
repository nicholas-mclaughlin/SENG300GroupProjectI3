package org.lsmr.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.software.*;

//import ScanControllerTest.BaggingAreaControllerStub;

public class SoftwareControllerTest {
	
	private SelfCheckoutStation station;
    private int[] banknoteDenominations = {5, 10, 20, 50, 100};
    private ScanController scanController;
    private BaggingAreaController baggingAreaController;
    private DatabaseController databaseController;
    private PaymentController paymentController;
    

    @Before
    public void setup() {

        BigDecimal nickel = BigDecimal.valueOf(0.05);
        BigDecimal dime = BigDecimal.valueOf(0.1);
        BigDecimal quarter = BigDecimal.valueOf(0.25);
        BigDecimal loonie = BigDecimal.valueOf(1);
        BigDecimal toonie = BigDecimal.valueOf(2);
        BigDecimal[] coinDenominations = new BigDecimal[]{nickel, dime, quarter, loonie, toonie};

        // manufacture/assemble hardware, install/prepare firmware

        station = new SelfCheckoutStation(Currency.getInstance("CAD"), banknoteDenominations, coinDenominations, 300, 1);

        // create database with test items

        Barcode b1 = new Barcode("12345");
        Barcode b2 = new Barcode("54321");
        Barcode b3 = new Barcode("13579");
        Barcode b4 = new Barcode("24680");

        BarcodedProduct p1 = new BarcodedProduct(b1, "KitKat", new BigDecimal("2.50"));
        BarcodedProduct p2 = new BarcodedProduct(b2, "Can of Diced Pineapple", new BigDecimal("1.99"));
        BarcodedProduct p3 = new BarcodedProduct(b3, "Boursin", new BigDecimal("7.50"));
        BarcodedProduct p4 = new BarcodedProduct(b4, "All-Purpose Flour", new BigDecimal("10"));

        DatabaseItem d1 = new DatabaseItem(p1, 40);
        DatabaseItem d2 = new DatabaseItem(p2, 300);
        DatabaseItem d3 = new DatabaseItem(p3, 250);
        DatabaseItem d4 = new DatabaseItem(p4, 20000);

        Database.BARCODED_PRODUCT_DATABASE.put(b1, d1);
        Database.BARCODED_PRODUCT_DATABASE.put(b2, d2);
        Database.BARCODED_PRODUCT_DATABASE.put(b3, d3);
        Database.BARCODED_PRODUCT_DATABASE.put(b4, d4);

        databaseController = new DatabaseController(Database.getDatabase());
        
        //initialzing shit for payment controller
        Locale can = new Locale("en", "US");
		Currency curr = Currency.getInstance(can);
		List<BigDecimal> den = new ArrayList<BigDecimal>();
		den.add(toonie);
		den.add(loonie);
		den.add(quarter);
		den.add(dime);
		den.add(nickel);
		
		CoinDispenser coinDispenserToonie = new CoinDispenser(10);
		CoinDispenser coinDispenserLoonie = new CoinDispenser(10);
		CoinDispenser coinDispenserQuarter = new CoinDispenser(10);
		CoinDispenser coinDispenserDime = new CoinDispenser(10);
		CoinDispenser coinDispenserNickle = new CoinDispenser(10);
		
		CoinSlot coinSlot = new CoinSlot();
		CoinValidator coinValidator = new CoinValidator(curr, den);
		CoinTray coinTray = new CoinTray(100);
		CoinStorageUnit coinStorage = new CoinStorageUnit(100);
		Map<BigDecimal, CoinDispenser> coinDispensers = new HashMap<BigDecimal, CoinDispenser>();
		coinDispensers.put(toonie, coinDispenserToonie);
		coinDispensers.put(loonie, coinDispenserLoonie);
		coinDispensers.put(quarter, coinDispenserQuarter);
		coinDispensers.put(dime, coinDispenserDime);
		coinDispensers.put(nickel, coinDispenserNickle);
		System.out.println();
		
		
		
		

        // install scan software

        baggingAreaController = new BaggingAreaController(station.baggingArea);
        scanController = new ScanController(station.mainScanner, databaseController, baggingAreaController);
        baggingAreaController.setScanController(scanController);
        PaymentController paymentController = new PaymentController(station.coinValidator, station.coinTray, station.coinSlot, station.coinStorage, station.coinDispensers, 
        		station.banknoteValidator, station.banknoteInput, station.banknoteStorage, station.banknoteDispensers, station.cardReader);
        
        
    }

	@Test
	public void constructor() {
		
		
		 
		/*
		DatabaseController databaseController = new DatabaseController(Database.getDatabase());
        BaggingAreaController baggingAreaController = new BaggingAreaController(station.baggingArea);
        ScanController scanController = new ScanController(station.mainScanner, databaseController, baggingAreaController);
        PaymentController paymentController = new PaymentController(station.coinValidator, station.coinTray, station.coinSlot, station.coinStorage, station.coinDispensers, 
        		station.banknoteValidator, station.banknoteInput, station.banknoteStorage, station.banknoteDispensers);
        baggingAreaController.setScanController(scanController);
        ReceiptController receiptController = new ReceiptController(station.printer);
	    */
		 
		 SoftwareController sc = new SoftwareController(station);
		 
		 assertNotNull(sc);
	   
		
	}
	
	@Test
	public void beginTransInProgress() {
		
		SoftwareController sc = new SoftwareController(station);
		sc.beginTransaction();
		boolean inProg = sc.isTransactionInProgress();
		assertEquals(true, inProg);
		sc.beginTransaction();
		inProg = sc.isTransactionInProgress();
		assertEquals(true, inProg);
		
		
	}
	
	//@Test
	public void beginTransNotInProgress() {
		
		SoftwareController sc = new SoftwareController(station);
		
		sc.beginTransaction();
		boolean scan = scanController.isItemScanning();
		boolean inProg = sc.isTransactionInProgress();
		assertEquals(true, inProg);
		assertEquals(true, scan);
		
	}
	
	//@Test
	public void moveToPaymentTestEndScan() {
		
		SoftwareController sc = new SoftwareController(station);
		sc.beginTransaction();
		sc.moveToPayment();
		boolean scan = scanController.isItemScanning();
		
		assertEquals(false, scan);
		
	}
	
	//@Test
	public void moveToPaymentTestStartPayment() {
		
		SoftwareController sc = new SoftwareController(station);
		sc.beginTransaction();
		sc.moveToPayment();
		boolean payment = paymentController.isPaymentPhase();
		
		assertEquals(true, payment);
		
	}
	
	//@Test
	public void endTransactionTest() {
		SoftwareController sc = new SoftwareController(station);
		sc.beginTransaction();
		sc.moveToPayment();
		//how do I test end transaction if it is protected
		sc.endTransaction();
		boolean inProg = sc.isTransactionInProgress();
		assertEquals(false, inProg);
		
	}
	
	//@Test
	public void printReceiptTest() {
		SoftwareController sc = new SoftwareController(station);
		sc.beginTransaction();
		sc.moveToPayment();
		sc.endTransaction();
		sc.printReceipt();
		boolean printed = paymentController.isPaymentPhase();
		boolean printed2 = sc.isTransactionInProgress();
		
		assertEquals(false, printed);
		assertEquals(false, printed2);
		
	}
	
	
	

}
