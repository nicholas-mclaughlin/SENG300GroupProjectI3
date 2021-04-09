package org.lsmr.software;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.external.CardIssuer;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

/**
 * This database is provided for testing purposes,
 * and is very similar to the ProductDatabase
 * with a few changes to make it support expected weights.
 * We will replace this database with whatever database
 * is provided/specified in future iterations.
 */
public class Database {
    private static Database database = new Database();

    public static Database getDatabase() {
        return database;
    }

    /**
     * Instances of this class are not needed, so the constructor is private.
     */
    private Database() {}

    /**
     * The known barcoded products, indexed by barcode.
     */
    public static final Map<Barcode, DatabaseItem> BARCODED_PRODUCT_DATABASE = new HashMap<>();

    /**
     * The know PLU products, indexed by barcode.
     */
    public static final Map<PriceLookupCode, PLUCodedProduct> PLU_PRODUCT_DATABASE = new HashMap<>();

    /**
     * A count of the items of the given product that are known to exist in the
     * store. Of course, this does not account for stolen items or items that were
     * not correctly recorded, but it helps management to track inventory.
     */
    public static final Map<Product, Integer> INVENTORY = new HashMap<>();
    

    public static final Map<String, CardIssuer> BANK_CARD_DATABASE = new HashMap<>();
    
}
