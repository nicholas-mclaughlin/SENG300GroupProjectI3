package a3Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.CoinTray;
import org.lsmr.selfcheckout.devices.CoinValidator;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.software.BaggingAreaController;
import org.lsmr.software.Database;
import org.lsmr.software.DatabaseController;
import org.lsmr.software.DatabaseItem;
import org.lsmr.software.PaymentController;
import org.lsmr.software.ScanController;

//Checking to see if all devices have been disabled properly

public class BlockStationTest {
	
		
		private SelfCheckoutStation station;
	    private int[] banknoteDenominations = {5, 10, 20, 50, 100};
	    private ScanController scanController;
	    private BaggingAreaController baggingAreaController;
	    private DatabaseController databaseController;
	    private PaymentController paymentController;
	    private BanknoteDispenser bnd = new BanknoteDispenser(100);
		private BanknoteSlot bns = new BanknoteSlot(true);
		private BanknoteStorageUnit bnsu = new BanknoteStorageUnit(100);
		private BanknoteValidator bnv;
		private BarcodeScanner bcs = new BarcodeScanner();
		private CardReader cr = new CardReader();
		private ElectronicScale es = new ElectronicScale(10000, 20);
		private ReceiptPrinter rp = new ReceiptPrinter();
		CoinDispenser coinDispenserToonie = new CoinDispenser(10);
		CoinSlot coinSlot = new CoinSlot();
		CoinValidator coinValidator;
		CoinTray coinTray = new CoinTray(100);
		CoinStorageUnit coinStorage = new CoinStorageUnit(100);
	    

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
			
			//this is initialized as local variable
			//CoinDispenser coinDispenserToonie = new CoinDispenser(10);
			CoinDispenser coinDispenserLoonie = new CoinDispenser(10);
			CoinDispenser coinDispenserQuarter = new CoinDispenser(10);
			CoinDispenser coinDispenserDime = new CoinDispenser(10);
			CoinDispenser coinDispenserNickle = new CoinDispenser(10);
			
			Map<BigDecimal, CoinDispenser> coinDispensers = new HashMap<BigDecimal, CoinDispenser>();
			coinDispensers.put(toonie, coinDispenserToonie);
			coinDispensers.put(loonie, coinDispenserLoonie);
			coinDispensers.put(quarter, coinDispenserQuarter);
			coinDispensers.put(dime, coinDispenserDime);
			coinDispensers.put(nickel, coinDispenserNickle);
			System.out.println();
			
			BanknoteValidator bnv = new BanknoteValidator(curr, banknoteDenominations);
			coinValidator = new CoinValidator(curr, den);
			
			
			
			
			

	        // install scan software

	        baggingAreaController = new BaggingAreaController(station.baggingArea);
	        scanController = new ScanController(station.mainScanner, databaseController, baggingAreaController);
	        baggingAreaController.setScanController(scanController);
	        PaymentController paymentController = new PaymentController(station.coinValidator, station.coinTray, station.coinSlot, station.coinStorage, station.coinDispensers, 
	        		station.banknoteValidator, station.banknoteInput, station.banknoteStorage, station.banknoteDispensers);
	        
	        
	    }
	    
	    
	    @Test
	    public void banknoteDispenserDisable() {
	        
	    	bnd.disable();
	    	boolean result = bnd.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void banknoteDispenserEnable() {
	        
	    	bnd.disable();
	    	bnd.enable();
	    	boolean result = bnd.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    
	    @Test
	    public void banknoteSlotDisable() {
	        
	    	bns.disable();
	    	boolean result = bns.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void banknoteSlotEnable() {
	        
	    	bns.disable();
	    	bns.enable();
	    	boolean result = bns.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	
	    @Test
	    public void banknoteStorageDisable() {
	        
	    	bnsu.disable();
	    	boolean result = bnsu.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void banknoteStorageEnable() {
	        
	    	bnsu.disable();
	    	bnsu.enable();
	    	boolean result = bnsu.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    
	    @Test
	    public void banknoteValidatorDisable() {
	        
	    	bnv.disable();
	    	boolean result = bnv.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void banknoteValidatorEnable() {
	        
	    	bnv.disable();
	    	bnv.enable();
	    	boolean result = bnv.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    @Test
	    public void barcodeScannerDisable() {
	        
	    	bcs.disable();
	    	boolean result = bcs.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void BarcodeScannerEnable() {
	        
	    	bcs.disable();
	    	bcs.enable();
	    	boolean result = bcs.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    
	    @Test
	    public void cardReaderDisable() {
	        
	    	cr.disable();
	    	boolean result = cr.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void cardReaderEnable() {
	        
	    	cr.disable();
	    	cr.enable();
	    	boolean result = cr.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    @Test
	    public void coinDispenserDisable() {
	        
	    	coinDispenserToonie.disable();
	    	boolean result = coinDispenserToonie.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void coinDispenserEnable() {
	        
	    	coinDispenserToonie.disable();
	    	coinDispenserToonie.enable();
	    	boolean result = coinDispenserToonie.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    @Test
	    public void coinSlotDisable() {
	        
	    	coinSlot.disable();
	    	boolean result = coinSlot.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void coinSlotEnable() {
	        
	    	coinSlot.disable();
	    	coinSlot.enable();
	    	boolean result = coinSlot.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    @Test
	    public void coinStorageDisable() {
	        
	    	coinStorage.disable();
	    	boolean result = coinStorage.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void coinStorageEnable() {
	        
	    	coinStorage.disable();
	    	coinStorage.enable();
	    	boolean result = coinStorage.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    @Test
	    public void coinTrayDisable() {
	        
	    	coinTray.disable();
	    	boolean result = coinTray.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void coinTrayEnable() {
	        
	    	coinTray.disable();
	    	coinTray.enable();
	    	boolean result = coinTray.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    @Test
	    public void coinValidatorDisable() {
	        
	    	coinValidator.disable();
	    	boolean result = coinValidator.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void coinValidatorEnable() {
	        
	    	coinValidator.disable();
	    	coinValidator.enable();
	    	boolean result = coinValidator.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    
	    @Test
	    public void scaleDisable() {
	        
	    	es.disable();
	    	boolean result = es.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void scaleEnable() {
	        
	    	es.disable();
	    	es.enable();
	    	boolean result = es.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    
	    @Test
	    public void printerDisable() {
	        
	    	rp.disable();
	    	boolean result = rp.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void printerEnable() {
	        
	    	rp.disable();
	    	rp.enable();
	    	boolean result = rp.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    @Test
	    public void scanControllerDisable() {
	        
	    	scanController.disable();
	    	boolean result = scanController.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void scanControllerEnable() {
	        
	    	scanController.disable();
	    	scanController.enable();
	    	boolean result = scanController.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    @Test
	    public void BaggingAreaControllerDisable() {
	        
	    	baggingAreaController.disable();
	    	boolean result = baggingAreaController.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void BaggingAreaControllerEnable() {
	        
	    	baggingAreaController.disable();
	    	baggingAreaController.enable();
	    	boolean result = baggingAreaController.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    
	    @Test
	    public void DatabaseControllerDisable() {
	        
	    	databaseController.disable();
	    	boolean result = databaseController.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void DatabaseControllerEnable() {
	        
	    	databaseController.disable();
	    	databaseController.enable();
	    	boolean result = databaseController.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    
	    @Test
	    public void PaymentControllerDisable() {
	        
	    	paymentController.disable();
	    	boolean result = paymentController.isDisabled();
	    	
	    	
	        assertEquals(false, result);
	        
	    }
	    
	
	    @Test
	    public void PaymentControllerEnable() {
	        
	    	paymentController.disable();
	    	paymentController.enable();
	    	boolean result = paymentController.isDisabled();
	    	
	    	
	        assertEquals(true, result);
	        
	    }
	    
	    
	    

	
	
}
