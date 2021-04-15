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
import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.CoinTray;
import org.lsmr.selfcheckout.devices.CoinValidator;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.software.AttendantControlConsole;
import org.lsmr.software.BaggingAreaController;
import org.lsmr.software.Database;
import org.lsmr.software.DatabaseController;
import org.lsmr.software.DatabaseItem;
import org.lsmr.software.PaymentController;
import org.lsmr.software.ScanController;
import org.lsmr.software.SoftwareController;
import org.lsmr.software.WeightDiscrepencyApproval;

public class WeightDiscrepencyApprovalTest {
	
	private SelfCheckoutStation station;
    private int[] banknoteDenominations = {5, 10, 20, 50, 100};
    private ScanController scanController;
    private BaggingAreaController baggingAreaController;
    private DatabaseController databaseController;
    private PaymentController paymentController;
    private SoftwareController sc;
	
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
		sc = new SoftwareController(station);
	}

	@Test
	public void overloadedCaughtapproved() {
		AttendantControlConsole acc = new AttendantControlConsole(sc, station);
		ElectronicScale es = new ElectronicScale(1000, 2);
		WeightDiscrepencyApproval wd = new WeightDiscrepencyApproval(es);
		Barcode b = new Barcode("1234");
		BarcodedItem bi1 = new BarcodedItem(b, 500);
		BarcodedItem bi2 = new BarcodedItem(b, 600);
		Item i1 = bi1;
		Item i2 = bi2;
		
		es.add(i1);
		es.add(i2);
		
		boolean result = wd.approve();
		
		assertEquals(true, result);
		
	}
	
	@Test
	public void overloadedCaughtNotApproved() {
		AttendantControlConsole acc = new AttendantControlConsole(sc, station);
		ElectronicScale es = new ElectronicScale(1000, 2);
		WeightDiscrepencyApproval wd = new WeightDiscrepencyApproval(es);
		Barcode b = new Barcode("1234");
		BarcodedItem bi1 = new BarcodedItem(b, 500);
		BarcodedItem bi2 = new BarcodedItem(b, 400);
		Item i1 = bi1;
		Item i2 = bi2;
		
		es.add(i1);
		es.add(i2);
		
		boolean result = wd.dontApprove();
		
		assertEquals(false, result);
		
	}
	
	

}
