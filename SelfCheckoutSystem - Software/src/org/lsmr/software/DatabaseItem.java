package org.lsmr.software;

import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

/**
 * Only used in our database for testing, created to hold
 * a BarcodedProduct with an expected weight for the bagging area's use
 */
public class DatabaseItem {
    public BarcodedProduct barcodedProduct;
    public PLUCodedProduct pluCodedProduct;
    public double expectedWeight;

    public DatabaseItem(BarcodedProduct product, double expectedWeight) {
        barcodedProduct = product;
        this.expectedWeight = expectedWeight;
    }

    public DatabaseItem(PLUCodedProduct product, double expectedWeight) {
    	pluCodedProduct = product;
        this.expectedWeight = expectedWeight;
    }
}
