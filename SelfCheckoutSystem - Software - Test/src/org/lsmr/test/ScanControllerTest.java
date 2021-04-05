package org.lsmr.test;

import org.junit.Before;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;

import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.Product;
import org.lsmr.software.*;

import static org.junit.Assert.*;
import org.junit.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

public class ScanControllerTest {
    private SelfCheckoutStation station;
    private int[] banknoteDenominations = {5, 10, 20, 50, 100};
    private ScanController scanController;
    private BaggingAreaController baggingAreaController;
    private DatabaseController databaseController;

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

        // install scan software

        baggingAreaController = new BaggingAreaControllerStub(station.scale);
        scanController = new ScanController(station.mainScanner, databaseController, baggingAreaController);
        baggingAreaController.setScanController(scanController);
    }

    /**
     * Stub to refer to BaggingAreaController but not use any of its functionality
     * Upon being told to accept an item in the bagging area,
     * it immediately informs the controller that it can continue scanning.
     */
    private class BaggingAreaControllerStub extends BaggingAreaController {
        @Override

        public void startBaggingItem(double expectedWeight) {
            scanController.continueScanning();
        }

        public BaggingAreaControllerStub(ElectronicScale electronicScale) {
            super(electronicScale);
        }
    }

    @Test
    public void testOneValidItem() {
        // arrange
        Barcode b = new Barcode("12345");
        BarcodedItem kitKat = new BarcodedItem(b, 38);
        BarcodedProduct expectedProduct = new BarcodedProduct(b, "KitKat", new BigDecimal("2.50"));

        // act
        scanController.beginScan();
        station.mainScanner.scan(kitKat);
        Purchase purchase = scanController.endScan();

        // assert
        ArrayList<Product> productsScanned = purchase.getProducts();

        // check whether item is barcoded
        assertEquals(BarcodedProduct.class, productsScanned.get(0).getClass());
        BarcodedProduct p1 = (BarcodedProduct) productsScanned.get(0);

        // check whether item is has expected fields
        assertEquals(expectedProduct.getBarcode(), p1.getBarcode());
        assertEquals(expectedProduct.getDescription(), p1.getDescription());
        assertEquals(0, expectedProduct.getPrice().compareTo(p1.getPrice()));

        // check whether purchase has expected subtotal
        assertEquals(new BigDecimal("2.50"), purchase.getSubtotal());
    }

    @Test
    public void testTwoValidItems() {
        // arrange
        Barcode b1 = new Barcode("54321");
        Barcode b2 = new Barcode("13579");
        BarcodedItem pineapple = new BarcodedItem(b1, 310);
        BarcodedItem cheese = new BarcodedItem(b2, 250.11111);

        ArrayList<BarcodedProduct> expectedProducts = new ArrayList();
        expectedProducts.add(new BarcodedProduct(b2, "Boursin", new BigDecimal("7.50")));
        expectedProducts.add(new BarcodedProduct(b1, "Can of Diced Pineapple", new BigDecimal("1.99")));

        // act
        scanController.beginScan();
        station.mainScanner.scan(cheese);
        station.mainScanner.scan(pineapple);
        Purchase purchase = scanController.endScan();

        // assert
        ArrayList<Product> productsScanned = purchase.getProducts();
        assertEquals(2, productsScanned.size());
        assertEquals(BarcodedProduct.class, productsScanned.get(0).getClass());
        assertEquals(BarcodedProduct.class, productsScanned.get(1).getClass());

        BarcodedProduct p1 = (BarcodedProduct) productsScanned.get(0);
        BarcodedProduct p2 = (BarcodedProduct) productsScanned.get(1);

        assertEquals(expectedProducts.get(0).getBarcode(), p1.getBarcode());
        assertEquals(expectedProducts.get(0).getDescription(), p1.getDescription());
        assertEquals(0, expectedProducts.get(0).getPrice().compareTo(p1.getPrice()));

        assertEquals(expectedProducts.get(1).getBarcode(), p2.getBarcode());
        assertEquals(expectedProducts.get(1).getDescription(), p2.getDescription());
        assertEquals(0, expectedProducts.get(1).getPrice().compareTo(p2.getPrice()));

        assertEquals(new BigDecimal("9.49"), purchase.getSubtotal());
    }
    
    @Test 
    public void testInvalidItem() {
    	
    	 // arrange
        Barcode c = new Barcode("13579");
        Barcode x = new Barcode("98765");
        BarcodedItem boursin = new BarcodedItem(c, 243);
        BarcodedItem ice_breaker = new BarcodedItem(x, 125);
        BarcodedProduct expectedProduct = new BarcodedProduct(c, "Boursin", new BigDecimal("7.50"));
        BarcodedProduct unexpectedProduct = new BarcodedProduct(x, "ice_breaker", new BigDecimal("3.50"));
        boolean expectedPhase = true;
        
        // act
        scanController.beginScan();
        station.mainScanner.scan(boursin);
        station.mainScanner.scan(ice_breaker);
        assertEquals(expectedPhase, scanController.isItemScanning());
        Purchase purchase = scanController.endScan();

        // assert
        assertEquals(1,purchase.getProducts().size());
        assertEquals(new BigDecimal("7.50"), purchase.getSubtotal());
    }
}
