package org.lsmr.software;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

/**
 * Only used with our database for testing right now
 * Currently only works with our custom database class,
 * but can (and will) be modified to work with
 * whatever database is eventually required.
 */
public class DatabaseController {
    private Database database;

    public DatabaseController(Database database) {
        this.database = database;
    }
    
    public Barcode getBarcodeOfItem(String itemName) {
        try {
            return  Database.PRODUCT_NAME_DATABASE.get(itemName).barcodedProduct.getBarcode();
        } catch (NullPointerException n) {
            return null;
        }
    }
        
    
    public double getExpectedWeight(Barcode barcode) {
        try {
            return database.BARCODED_PRODUCT_DATABASE.get(barcode).expectedWeight;
        } catch (NullPointerException n) {
            return -1;
        }
    }
    
    public double getExpectedWeight(PriceLookupCode pluProductCode) {
        try {
            return database.PLU_PRODUCT_DATABASE.get(pluProductCode).expectedWeight;
        } catch (NullPointerException n) {
            return -1;
        }
    }

    public BarcodedProduct getBarcodedProduct(Barcode barcode) {
        try {
            return database.BARCODED_PRODUCT_DATABASE.get(barcode).barcodedProduct;
        } catch (NullPointerException n) {
            return null;
        }
    }

    public PLUCodedProduct getpluCodedProduct(PriceLookupCode pluProductCode) {
        try {
            return database.PLU_PRODUCT_DATABASE.get(pluProductCode).pluCodedProduct;
        } catch (NullPointerException n) {
            return null;
        }
    }
    
    public PLUCodedProduct getPLUcodedProduct(PriceLookupCode pluCode) {
        try {
            return  database.PLU_PRODUCT_DATABASE1.get(pluCode);
        } catch (NullPointerException n) {
            return null;
        }
    }
}
