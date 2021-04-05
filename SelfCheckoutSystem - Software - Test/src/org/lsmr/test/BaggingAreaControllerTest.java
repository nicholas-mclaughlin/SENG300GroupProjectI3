package org.lsmr.test;

import static org.junit.Assert.*;


import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.software.BaggingAreaController;
import org.lsmr.software.Database;
import org.lsmr.software.DatabaseController;
import org.lsmr.software.DatabaseItem;
import org.lsmr.software.PaymentController;
import org.lsmr.software.ScanController;
import java.util.concurrent.TimeUnit;
import java.lang.Object;
import java.lang.Thread;

public class BaggingAreaControllerTest {

	private SelfCheckoutStation station;
    private int[] banknoteDenominations = {5, 10, 20, 50, 100};
    private ScanController scanController;
    private BaggingAreaController baggingAreaController;
    private DatabaseController databaseController;
	
	@Before
	public void setUp() throws Exception {
		BigDecimal nickel = BigDecimal.valueOf(0.05);
        BigDecimal dime = BigDecimal.valueOf(0.1);
        BigDecimal quarter = BigDecimal.valueOf(0.25);
        BigDecimal loonie = BigDecimal.valueOf(1);
        BigDecimal toonie = BigDecimal.valueOf(2);
        BigDecimal[] coinDenominations = new BigDecimal[]{nickel, dime, quarter, loonie, toonie};

        // manufacture/assemble hardware, install/prepare firmware

        station = new SelfCheckoutStation(Currency.getInstance("CAD"), banknoteDenominations, coinDenominations, 300, 1);

        scanController = new ScanControllerStub(station.mainScanner, databaseController, baggingAreaController);
	}

	private class ScanControllerStub extends ScanController {
        public ScanControllerStub(BarcodeScanner bs, DatabaseController dc, BaggingAreaController bac) {
            super(bs, dc, bac);
        }
    }
	
	private class Bag extends Item {

		protected Bag(double weightInGrams) {
			super(weightInGrams);
		}
		
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddPersonalBagging1() {
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
		
		baggingAreaController.getPersonalBaggingListener().startAddingPersonalBags();
		
		assertTrue(baggingAreaController.getIsAddPersonalBagsPhase());
		
		Item bag1 = new Bag(15);
		station.scale.add(bag1);
		assertEquals(baggingAreaController.getTotalWeight(), 15, 0);
		
		Item bag2 = new Bag(200);
		station.scale.add(bag2);
		
		assertEquals(baggingAreaController.getTotalWeight(), 215, 0);
		assertEquals(baggingAreaController.getUnknownWeight(), 200, 0);
		
		Item bag3 = new Bag(5);
		station.scale.add(bag3);
		
		assertEquals(baggingAreaController.getUnknownWeight(), 205, 0);
		
		station.scale.remove(bag2);
		station.scale.remove(bag3);
		assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
		
		station.scale.add(bag3);
		assertEquals(baggingAreaController.getTotalWeight(), 20, 0);
		
		baggingAreaController.getPersonalBaggingListener().endAddingPersonalBags();
		assertFalse(baggingAreaController.getIsAddPersonalBagsPhase());
	}
	

	
	@Test
	public void testAddItemReminder1() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startBaggingItem(i1.getWeight());
        
        try {
			Thread.currentThread().sleep(5500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        assertTrue(baggingAreaController.getBagItemOverdue());
        
        station.scale.add(i1);
        
        assertFalse(baggingAreaController.getBagItemOverdue());
	}
	
	@Test
	public void testAddItemReminder2() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startBaggingItem(i1.getWeight());
        
        try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        assertFalse(baggingAreaController.getBagItemOverdue());
        
        station.scale.add(i1);
        
        assertFalse(baggingAreaController.getBagItemOverdue());
        
        try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        assertFalse(baggingAreaController.getBagItemOverdue());
	}

	@Test
	public void testStartBaggingItem1() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
		baggingAreaController.startBaggingItem(i1.getWeight());
		assertTrue(baggingAreaController.getIsBaggingPhase());
		assertFalse(baggingAreaController.getIsRemovingPhase());
		assertEquals(baggingAreaController.getExpectedItemWeight(), 40, 0);
	}

	@Test
	public void testStartBaggingItem2() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startRemovingItem(i1.getWeight());
		baggingAreaController.startBaggingItem(i1.getWeight());
		assertFalse(baggingAreaController.getIsBaggingPhase());
	}
	
	@Test
	public void testBagItem1() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
		baggingAreaController.startBaggingItem(500);
		station.scale.add(i1);
		assertEquals(baggingAreaController.getUnknownWeight(), 40, 0);
	}
	
	@Test
	public void testBagItem2() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
		baggingAreaController.startBaggingItem(41);
		station.scale.add(i1);
		assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
		assertFalse(baggingAreaController.getIsBaggingPhase());
	}
	
	@Test
	public void testBagItem3() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 50);
		Barcode b2 = new Barcode("54321");
		BarcodedItem i2 = new BarcodedItem(b1, 10);
		
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        station.scale.add(i1);
        assertEquals(baggingAreaController.getTotalWeight(), 50, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 50, 0);
        
        station.scale.remove(i1);
        assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
        
        station.scale.add(i1);
        station.scale.add(i2);
        assertEquals(baggingAreaController.getTotalWeight(), 60, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 60, 0);
        
        station.scale.remove(i1);
        assertEquals(baggingAreaController.getUnknownWeight(), 10, 0);
        station.scale.remove(i2);
        assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);

        baggingAreaController.startBaggingItem(i1.getWeight());
        station.scale.add(i1);
        assertEquals(baggingAreaController.getTotalWeight(), 50, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
	}
	
	@Test
	public void testBagItem4() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 50);
		Barcode b2 = new Barcode("54321");
		BarcodedItem i2 = new BarcodedItem(b1, 10);
		
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startBaggingItem(i1.getWeight());
        station.scale.add(i1);
        assertEquals(baggingAreaController.getTotalWeight(), 50, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
        
        station.scale.remove(i1);
        assertEquals(baggingAreaController.getTotalWeight(), 0, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), -50, 0);
        
        station.scale.add(i2);
        assertEquals(baggingAreaController.getTotalWeight(), 10, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), -40, 0);
        
        station.scale.remove(i2);
        station.scale.add(i1);
        
        baggingAreaController.startBaggingItem(i2.getWeight());
        station.scale.add(i2);
        assertEquals(baggingAreaController.getTotalWeight(), 60, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
	}
	
	@Test
	public void testBagItem5() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 50);
		Barcode b2 = new Barcode("54321");
		BarcodedItem i2 = new BarcodedItem(b1, 10);
		
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startBaggingItem(i1.getWeight());
        station.scale.add(i1);
        baggingAreaController.startBaggingItem(i2.getWeight());
        station.scale.add(i2);
        assertEquals(baggingAreaController.getTotalWeight(), 60, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
	
        station.scale.remove(i2);
        assertEquals(baggingAreaController.getTotalWeight(), 50, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), -10, 0);
        
        station.scale.remove(i1);
        assertEquals(baggingAreaController.getTotalWeight(), 0, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), -60, 0);
        
        station.scale.add(i1);
        station.scale.add(i2);
        assertEquals(baggingAreaController.getTotalWeight(), 60, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
	}

	@Test
	public void testBagItem6() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 50);
		Barcode b2 = new Barcode("54321");
		BarcodedItem i2 = new BarcodedItem(b1, 10);
		Barcode b3 = new Barcode("43247");
		BarcodedItem i3 = new BarcodedItem(b1, 20);
		
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startBaggingItem(i1.getWeight());
        station.scale.add(i1);
        baggingAreaController.startBaggingItem(i2.getWeight());
        station.scale.remove(i1);
        assertEquals(baggingAreaController.getTotalWeight(), 0, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), -50, 0);
        
        
        station.scale.add(i1);
        station.scale.add(i2);
        assertEquals(baggingAreaController.getTotalWeight(), 60, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
	}
	
	@Test
	public void testBagItem7() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 50);
		Barcode b2 = new Barcode("54321");
		BarcodedItem i2 = new BarcodedItem(b1, 10);
		Barcode b3 = new Barcode("43247");
		BarcodedItem i3 = new BarcodedItem(b1, 20);
		
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startBaggingItem(i1.getWeight());
        station.scale.add(i1);
        baggingAreaController.startRemovingItem(i1.getWeight());
        station.scale.add(i2);
        assertEquals(baggingAreaController.getTotalWeight(), 60, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 10, 0);
        
        
        station.scale.remove(i2);
        station.scale.remove(i1);
        assertEquals(baggingAreaController.getTotalWeight(), 0, 0);
        assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
	}
	
	@Test
	public void testStartRemoveItem1() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
		baggingAreaController.startRemovingItem(i1.getWeight());
		assertTrue(baggingAreaController.getIsRemovingPhase());
		assertFalse(baggingAreaController.getIsBaggingPhase());
		assertEquals(baggingAreaController.getExpectedItemWeight(), 40, 0);
	}

	@Test
	public void testStartRemoveItem2() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startBaggingItem(i1.getWeight());
        baggingAreaController.startRemovingItem(i1.getWeight());
		assertFalse(baggingAreaController.getIsRemovingPhase());
	}
	
	@Test
	public void testRemoveItem1() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startBaggingItem(40);
		station.scale.add(i1);
		baggingAreaController.startRemovingItem(500);
		station.scale.remove(i1);
		assertEquals(baggingAreaController.getUnknownWeight(), -40, 0);
	}
	
	@Test
	public void testRemoveItem2() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 40);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        baggingAreaController.startBaggingItem(40);
		station.scale.add(i1);
		baggingAreaController.startRemovingItem(40);
		station.scale.remove(i1);
		assertEquals(baggingAreaController.getUnknownWeight(), 0, 0);
		assertFalse(baggingAreaController.getIsRemovingPhase());
	}

	@Test
	public void testOverloaded() {
		Barcode b1 = new Barcode("12345");
		BarcodedItem i1 = new BarcodedItem(b1, 4000);
		baggingAreaController = new BaggingAreaController(station.scale);
        baggingAreaController.setScanController(scanController);
        
        station.scale.add(i1);
        assertTrue(baggingAreaController.getOverloaded());
        
        station.scale.remove(i1);
        assertFalse(baggingAreaController.getOverloaded());
	}
	
	
}