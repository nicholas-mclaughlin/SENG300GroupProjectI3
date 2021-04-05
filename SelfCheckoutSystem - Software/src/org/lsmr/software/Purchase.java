package org.lsmr.software;

import org.lsmr.selfcheckout.products.Product;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Keeps track of all the items bought in a single purchase (one interaction at the checkout station)
 */
public class Purchase {

    private ArrayList<Product> products = new ArrayList<>();

    public void addItem(Product product) {
        products.add(product);
    }

    /**
     * Removes one instance of item from list of items, if present.
     * If not present, no change occurs.
     */
    public void removeItem(Product item) {
        products.remove(item);
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for(Product item : products) {
            subtotal = subtotal.add(item.getPrice());
        }
        return subtotal;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
