package org.lsmr.software;

import org.lsmr.selfcheckout.devices.*;

/**
 * This overarching software controller is the software equivalent of the SelfCheckoutStation,
 * where it initializes and ties together all the single-purpose classes,
 * combining their functionalites into a single class to be instantiated.
 */
public class SoftwareController {

    private SelfCheckoutStation station;
    private ScanController scanController;
    private BaggingAreaController baggingAreaController;
    private PaymentController paymentController;
    private DatabaseController databaseController;
    private boolean transactionInProgress = false;
    private Purchase currentPurchase = null;
    private ReceiptController receiptController;
    private MembershipCardController membershipCardController;

    public SoftwareController(SelfCheckoutStation station) {
        this.station = station;

        databaseController = new DatabaseController(Database.getDatabase());
        baggingAreaController = new BaggingAreaController(station.baggingArea);
        scanController = new ScanController(station.mainScanner, databaseController, baggingAreaController);
        paymentController = new PaymentController(station.coinValidator, station.coinTray, station.coinSlot, station.coinStorage, station.coinDispensers, 
        		station.banknoteValidator, station.banknoteInput, station.banknoteStorage, station.banknoteDispensers, station.cardReader);
        baggingAreaController.setScanController(scanController);
        receiptController = new ReceiptController(station.printer);
        membershipCardController = new membershipCardController();
    }

    public void beginTransaction() {
        if (transactionInProgress) {
            System.out.println("Transaction is in progress, please finish before starting next transaction.");
        } else {
            transactionInProgress = true;

            // TODO: log in to member account somehow

            scanController.beginScan();
        }
    }

    public void moveToPayment() {
        currentPurchase = scanController.endScan();
        paymentController.beginPayment(currentPurchase.getSubtotal(), this);
    }

    public void endTransaction() {
        printReceipt();
        transactionInProgress = false;
    }

    public boolean isTransactionInProgress() {
        return transactionInProgress;
    }

    public void printReceipt() {
        receiptController.printReceipt(currentPurchase);
        currentPurchase = null;
    }

    public DatabaseController getDatabaseController() {
    	return this.databaseController;
    }
    
    public PaymentController getPaymentController() {
        return this.paymentController;
    }
    
    public ScanController getScanController() {
        return this.scanController;
    }
    
    public BaggingAreaController getBaggingAreaController {
        return this.baggingAreaController;
    }
    
    public ReceiptController getReceiptController {
        return this.receiptController;
    }
    
    public MembershipCardController getMembershipCardController {
        return this.membershipCardController;
    }

    public SelfCheckoutStation getStation(){
        return this.station;
    }
