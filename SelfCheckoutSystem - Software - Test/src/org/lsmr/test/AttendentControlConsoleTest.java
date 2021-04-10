package org.lsmr.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;
import org.lsmr.software.AttendantControlConsole;
import org.lsmr.software.AttendantDataBase;
import org.lsmr.software.Database;
import org.lsmr.software.DatabaseController;
import org.lsmr.software.DatabaseItem;
import org.lsmr.software.Purchase;
import org.lsmr.software.SoftwareController;
import org.lsmr.test.PaymentControllerTest.SoftwareControllerStub;

public class AttendentControlConsoleTest {
	private SelfCheckoutStation station;
	private int[] banknoteDenominations = {5, 10, 20, 50, 100};
//	private AttendantDataBase attendantDataBase; //??
	private AttendantControlConsole attendantControlConsole;
	private SoftwareControllerStub softwareControllerStub;
//	private DatabaseController databaseController;
//	private SoftwareController softwareController;

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
        softwareControllerStub = new SoftwareControllerStub(station);
        //softwareController = new SoftwareController(station);
        
        attendantControlConsole = new AttendantControlConsole(softwareControllerStub, station);
        
		attendantControlConsole.attendantDataBase.addEntry("employee1", "1234");

	}
	
	class SoftwareControllerStub extends SoftwareController {
		public SoftwareControllerStub(SelfCheckoutStation station) {
			super(station);
		}
    }

	// Testing login test
	@Test
	public void testLoginSuccessful() {
		boolean expected = true;
		
		boolean result = attendantControlConsole.logIn("employee1", "1234");
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testLoginWrongPassword() {
		boolean expected = false;
		
		boolean result = attendantControlConsole.logIn("employee1", "4321");
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testLoginWrongUsername() {
		boolean expected = false;
		
		boolean result = attendantControlConsole.logIn("name", "1234");
		
		assertEquals(expected, result);
	}

	// Testing logout 
	@Test
	public void testLogoutSuccessful() {
		boolean expected = true;
		
		attendantControlConsole.logIn("employee1", "1234");
		boolean result = attendantControlConsole.logOut();
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testLogoutFailed() {
		boolean expected = false;
		
		boolean result = attendantControlConsole.logOut();
		
		assertEquals(expected, result);
	}
	
	// Attendant looks up product
	// Test attendant looks up barcoded item
	@Test
	public void testLookUpBarcodedProduct(){
		attendantControlConsole.logIn("employee1", "1234");
        Barcode code = new Barcode("12345");
        BarcodedProduct product = new BarcodedProduct(code, "item1", new BigDecimal("10.00"));
        DatabaseItem item = new DatabaseItem(product, 50);
        Database.BARCODED_PRODUCT_DATABASE.put(code, item);
		
		Product result = attendantControlConsole.attendantLookforBarcodedProdcut(code);
		
		assertEquals(product, result);
	}
	
	// Test attendant looks up PLU coded product
	@Test
	public void testLookUpPLUCode(){
		attendantControlConsole.logIn("employee1", "1234");
        PriceLookupCode code = new PriceLookupCode("0987");
        PLUCodedProduct product = new PLUCodedProduct(code, "item1", new BigDecimal("10.00"));
        Database.PLU_PRODUCT_DATABASE.put(code, product);
		
		Product result = attendantControlConsole.attendantLookforPLUcodedProdcut(code);
		
		assertEquals(product, result);
	}
	
	// Attendant removes product
	@Test
	public void testRemoveProduct() {
		attendantControlConsole.logIn("employee1", "1234");
        Barcode code = new Barcode("12345");
        BarcodedProduct product = new BarcodedProduct(code, "item1", new BigDecimal("10.00"));
       
        Purchase purchase = new Purchase();
        purchase.addItem(product);
        attendantControlConsole.removeProdcutFromorder(purchase, product);
        
        assertEquals(new BigDecimal("0"), purchase.getSubtotal());
	}
	
	// Attendant adds paper
	@Test
	public void testAddPaper() {
		attendantControlConsole.logIn("employee1", "1234");
		attendantControlConsole.addPaper(1000);
	}
	
	@Test
	public void testEmptyBanknote() {
		attendantControlConsole.logIn("employee1", "1234");
		
		
	}

	// Attendant stats up station
	@Test
	public void testStartUp() {
		attendantControlConsole.logIn("employee1", "1234");
		boolean expected = true;
		
		attendantControlConsole.startUpStation();
		
		assertEquals(expected,attendantControlConsole.getStationStatus());
	}
	
	// Attendant shuts down station
	@Test
	public void testShutDown() {
		attendantControlConsole.logIn("employee1", "1234");
		boolean expected = false;
		
		attendantControlConsole.shutDownStation();
		
		assertEquals(expected,attendantControlConsole.getStationStatus());
	}
	
	
	
	
}
