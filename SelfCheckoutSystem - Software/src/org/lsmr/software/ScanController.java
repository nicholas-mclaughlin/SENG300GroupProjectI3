package org.lsmr.software;

import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.listeners.*;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.products.BarcodedProduct;

/**
 * Controls the functionality around the BarcodeScanner,
 * listening for scanned barcodes whenever the customer is in the scanning phase,
 * and passing control to the BaggingAreaController after each item is scanned.
 */
public class ScanController {
	
	private ScanListener scanListener = new ScanListener();
	private BarcodeScanner barcodeScanner; 
	private DatabaseController databaseController;
	private BaggingAreaController baggingAreaController;

	private boolean isItemScanningPhase;
	private boolean isScanningPhase;
	private Purchase current_purchase;

	public ScanController(BarcodeScanner bs, DatabaseController dc, BaggingAreaController bac) {

		barcodeScanner = bs;
		barcodeScanner.register(scanListener);
		
		databaseController = dc;

		baggingAreaController = bac;
	}
	
	public void beginScan() {
		current_purchase = new Purchase();
		isScanningPhase = true;
		isItemScanningPhase = true;
	}

	public boolean isScanning() {
		return isScanningPhase;
	}
	
	public boolean isItemScanning() {
		return isScanningPhase;
	}
	
	//Called when the customer requests the scanning phase ends and we shift to payment phase.
	public Purchase endScan(){
		isScanningPhase = false;
		return current_purchase;
	}
	
	//Called by the Bagging software to continue next scan.
	public void continueScanning() {
		isItemScanningPhase = true;
	}
	
	private class ScanListener implements BarcodeScannerListener {

		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {			
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {			
		}

		/**
		 * @param barcodeScanner the device on which the event occurred.
		 * @param barcode the barcode that was just scanned
		 */
		@Override
		public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
			if(isScanningPhase) {
				if(isItemScanningPhase) {
					double expectedWeight = databaseController.getExpectedWeight(barcode);
					if (expectedWeight == -1) return; // barcode was not in database
					BarcodedProduct currentProduct = databaseController.getBarcodedProduct(barcode);
					if (currentProduct == null) return; // barcode was not in database
					current_purchase.addItem(currentProduct);
					isItemScanningPhase = false;
					baggingAreaController.startBaggingItem(expectedWeight);
				}
			}
		}
	}	
}
