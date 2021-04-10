package org.lsmr.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.Product;
import org.lsmr.software.AttendantControlConsole;
import org.lsmr.software.AttendantDataBase;
import org.lsmr.software.SoftwareController;
import org.lsmr.test.PaymentControllerTest.SoftwareControllerStub;

public class AttendentControlConsoleTest {
	private SelfCheckoutStation station;
	private int[] banknoteDenominations = {5, 10, 20, 50, 100};
	private AttendantDataBase attendantDataBase; //??
	private AttendantControlConsole attendantControlConsole;
	private SoftwareControllerStub softwareControllerStub;

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
        
        attendantControlConsole = new AttendantControlConsole(softwareControllerStub, station);
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
		
		attendantControlConsole.attendantDataBase.addEntry("employee1", "1234");
		boolean result = attendantControlConsole.logIn("employee1", "1234");
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testLoginWrongPassword() {
		boolean expected = false;
		
		attendantControlConsole.attendantDataBase.addEntry("employee1", "1234");
		boolean result = attendantControlConsole.logIn("employee1", "4321");
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testLoginWrongUsername() {
		boolean expected = false;
		
		attendantControlConsole.attendantDataBase.addEntry("employee1", "1234");
		boolean result = attendantControlConsole.logIn("name", "1234");
		
		assertEquals(expected, result);
	}

	// Testing logout 
	@Test
	public void testLogoutSuccessful() {
		boolean expected = true;
		
		attendantControlConsole.attendantDataBase.addEntry("employee1", "1234");
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
}
