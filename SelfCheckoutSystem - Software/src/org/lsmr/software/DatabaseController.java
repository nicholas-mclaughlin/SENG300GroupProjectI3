package org.lsmr.software;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.products.BarcodedProduct;

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

    public double getExpectedWeight(Barcode barcode) {
        try {
            return database.BARCODED_PRODUCT_DATABASE.get(barcode).expectedWeight;
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
}
