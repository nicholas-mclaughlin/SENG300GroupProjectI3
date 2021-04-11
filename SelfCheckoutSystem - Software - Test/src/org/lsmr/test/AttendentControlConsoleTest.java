package org.lsmr.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;
import org.lsmr.software.AttendantControlConsole;
import org.lsmr.software.AttendantDataBase;
import org.lsmr.software.Database;
import org.lsmr.software.DatabaseController;
import org.lsmr.software.DatabaseItem;
import org.lsmr.software.DispenserController;
import org.lsmr.software.Purchase;
import org.lsmr.software.SoftwareController;
import org.lsmr.software.SoftwareException;
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
		attendantControlConsole.attendantDataBase.addEntry("employee2", "4321");

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
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.attendantLookforBarcodedProdcut(code);
		});  
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
		
		attendantControlConsole.logOut();
			
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.attendantLookforPLUcodedProdcut(code);
		});  
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
        
        attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			 attendantControlConsole.removeProdcutFromorder(purchase, product);
		});  
	}
	
	// Attendant adds paper
	@Test
	public void testAddPaper() {
		attendantControlConsole.logIn("employee1", "1234");
		attendantControlConsole.addPaper(10);
		
		
		assertThrows(SimulationException.class, () -> {
			 attendantControlConsole.addPaper(2000);
		});  
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.addPaper(10);
		});  
	}
	
	@Test
	public void testAddInk() {
		attendantControlConsole.logIn("employee1", "1234");
		attendantControlConsole.addInk(10);
		
		
		assertThrows(SimulationException.class, () -> {
			 attendantControlConsole.addInk(2000000);
		});  
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.addInk(10);
		});  
	}
	
	// Attendant unloads banknotes
	@Test
	public void testUnloadBanknote() throws SimulationException, OverloadException {
		attendantControlConsole.logIn("employee1", "1234");
		Banknote fives = new Banknote(5, Currency.getInstance("CAD"));
		Banknote tens = new Banknote(10, Currency.getInstance("CAD"));
		Banknote[] banknote = {fives, tens, fives, tens};
		station.banknoteStorage.load(banknote);
		
		int result = attendantControlConsole.emptyBanknoteStorage();
		
		assertEquals(0, result);
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.emptyBanknoteStorage();
		});  
	}
	
	// Attendant unloads coins
	@Test
	public void testUnloadCoin() throws SimulationException, OverloadException {
		attendantControlConsole.logIn("employee1", "1234");
		Coin dime = new Coin(BigDecimal.valueOf(0.10), Currency.getInstance("CAD"));
		Coin nickle = new  Coin(BigDecimal.valueOf(0.05), Currency.getInstance("CAD"));
		Coin[] coin = {dime, nickle, dime, nickle};
		station.coinStorage.load(coin);
		
		int result = attendantControlConsole.emptyCoinStorage();
		
		assertEquals(0, result);
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.emptyCoinStorage();
		});  
	}
	
	// Attendant loads banknotes
	@Test
	public void testLoadSameBanknote() throws SimulationException, OverloadException {
		attendantControlConsole.logIn("employee1", "1234");
		Banknote fives = new Banknote(5, Currency.getInstance("CAD"));
		
		int result = attendantControlConsole.loadBanknote(fives, 10);
		
		assertEquals(50, result);
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.loadBanknote(fives, 10);
		});  
	}
	
	@Test
	public void testLoadDifferentBanknote() throws SimulationException, OverloadException {
		attendantControlConsole.logIn("employee1", "1234");
		Banknote fives = new Banknote(5, Currency.getInstance("CAD"));
		Banknote tens = new Banknote(10, Currency.getInstance("CAD"));
		Banknote[] banknote = {fives, tens, fives};
		
		int result = attendantControlConsole.loadBanknote(banknote);
		
		assertEquals(20, result);
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.loadBanknote(banknote);
		});  
	}
	
	@Test
	public void testOverloadBanknote() throws SimulationException, OverloadException {
		attendantControlConsole.logIn("employee1", "1234");
		Banknote fives = new Banknote(5, Currency.getInstance("CAD"));
		
		assertThrows(OverloadException.class, () -> {
			attendantControlConsole.loadBanknote(fives, 1000);
		});  
		
	}
	
	// Attendant loads coins
	@Test
	public void testLoadSameCoin() throws SimulationException, OverloadException {
		attendantControlConsole.logIn("employee1", "1234");
		Coin dime = new Coin(BigDecimal.valueOf(0.10), Currency.getInstance("CAD"));
		
		BigDecimal result = attendantControlConsole.loadCoin(dime, 20);
		
		assertEquals(BigDecimal.valueOf(2.00), result);
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.loadCoin(dime, 20);
		});  
	}
	
	@Test
	public void testLoadDifferentCoin() throws SimulationException, OverloadException {
		attendantControlConsole.logIn("employee1", "1234");
		Coin dime = new Coin(BigDecimal.valueOf(0.10), Currency.getInstance("CAD"));
		Coin nickle = new  Coin(BigDecimal.valueOf(0.05), Currency.getInstance("CAD"));
		Coin[] coin = {dime, nickle, nickle, nickle};
		
		BigDecimal result = attendantControlConsole.loadCoin(coin);
		
		assertEquals(BigDecimal.valueOf(0.25), result);
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.loadCoin(coin);
		});  
	}
	
	@Test
	public void testOverLoadCoin() throws SimulationException, OverloadException {
		attendantControlConsole.logIn("employee1", "1234");
		Coin dime = new Coin(BigDecimal.valueOf(0.10), Currency.getInstance("CAD"));
		
		assertThrows(OverloadException.class, () -> {
			attendantControlConsole.loadCoin(dime, 1000);
		});  
	}

	// Attendant stats up station
	@Test
	public void testStartUp() {
		attendantControlConsole.logIn("employee1", "1234");
		boolean expected = true;
		
		attendantControlConsole.startUpStation();
		
		assertEquals(expected,attendantControlConsole.getStationStatus());
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.startUpStation();
		});  
	}
	
	// Attendant shuts down station
	@Test
	public void testShutDown() {
		attendantControlConsole.logIn("employee1", "1234");
		boolean expected = false;
		
		attendantControlConsole.shutDownStation();
		
		assertEquals(expected,attendantControlConsole.getStationStatus());
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.shutDownStation();
		});  
	}
	
	// Attendant adds new attendant to system
	@Test
	public void testAddEntry() {
		boolean expected = true;
		
		attendantControlConsole.logIn("employee1", "1234");
		boolean result = attendantControlConsole.addEntry("newEmployee", "4321");
		
		assertEquals(expected, result);
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.addEntry("newEmployee", "4321");
		});  
	}
	
	// Attendant adds new attendant to system
	@Test
	public void testRemoveEntry() {
		boolean expected = true;
		
		attendantControlConsole.logIn("employee1", "1234");
		boolean result = attendantControlConsole.removeEntry("employee2");
		
		assertEquals(expected, result);
		
		attendantControlConsole.logOut();
		
		assertThrows(SoftwareException.class, () -> {
			attendantControlConsole.removeEntry("employee2");
		});  
	}
	
	
	
}
