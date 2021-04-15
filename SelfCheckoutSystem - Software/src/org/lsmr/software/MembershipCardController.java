package org.lsmr.software;

import org.lsmr.selfcheckout.BarcodedItem;


import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.listeners.*;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.*;
import java.util.*;

public class MembershipCardController {
	
	private BarcodeScanner barcodeScanner;
	private ScanListener scanListener = new ScanListener();
//	private MembershipCardTouchScreenListener MCTSListener = new MembershipCardTouchScreenListener();
	private boolean isMemberVerificationPhase = false; 
	private boolean isScanningPhase = false; 
	private boolean isTypingPhase = false;
	private boolean isUnverifiedNumber = false;
	private boolean memberVerified = false;
	
	public boolean isScanningPhase() {
		return isScanningPhase;
	}
	
	public boolean isTypingPhase() {
		return isTypingPhase;
	}
	
	public boolean isMemberVerificationPhase() {
		return isMemberVerificationPhase;
	}
	
	public boolean isUnverifiedNumber() {
		return isUnverifiedNumber;
	}
	
	public boolean isMemberVerified() {
		return memberVerified;
	}
	
	public void beginMemberVerificationPhase() {
		if (!isMemberVerificationPhase) {
			isMemberVerificationPhase = true; 
			beginScanningPhase();
		}
	}
	
	public void beginScanningPhase() {
		isScanningPhase = true;
		isTypingPhase = false;
	}
	
	
	public void beginTypingPhase() {
		isTypingPhase = true;
		isScanningPhase = false;
	}

	public BarcodeScanner getBarcodeScanner() {
    	return barcodeScanner;
    }
	
	
	public MembershipCardController(BarcodeScanner barcodeScanner) {
		this.barcodeScanner = barcodeScanner;
		barcodeScanner.register(scanListener);
	}
	
	public void typedMembershipValidity(String cardNumber) {
		if (isMemberVerificationPhase && isTypingPhase) {
			if(!DatabaseMembershipCard.isCardInDatabase(cardNumber)) {
				System.out.println("Invalid MemberShip card : Card not in Database"); 
				isUnverifiedNumber = true;
			}
			else {
				System.out.println("Valid Membership Card");
				memberVerified = true;
				isTypingPhase=false;
				isMemberVerificationPhase = false;
			}
		}
	}
	
	public void scannedMembershipValidity(BarcodeScanner barcodeScanner, Barcode barcode) {
		if (isMemberVerificationPhase && isScanningPhase) {
			if(!DatabaseMembershipCard.isCardInDatabase(barcode.toString())) {
				System.out.println("Invalid MemberShip card : Card not in Database"); 
				isUnverifiedNumber = true;
			}
			else {
				System.out.println("Valid Membership Card");
				memberVerified= true;
				isScanningPhase=false;
				isMemberVerificationPhase = false;
			}
		}
	}

	private class ScanListener implements BarcodeScannerListener {

		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {			
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {			
		}

		@Override
		public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
			if(isScanningPhase) {
				scannedMembershipValidity(barcodeScanner, barcode);
			}
		}
	}
	
	/*
    We will make sure to match this stub to whatever is required in the third iteration.
    Basically simulates if a user taps on the touch screen if they have a membership card,
    if they want to type the membership card number, or if they want to scan their card.
    */
//	private class MembershipCardTouchScreenListener implements TouchScreenListener {
//		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {};
//
//    	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {};
//
//    	public void beginMemberValidation() {
//    		isMemberVerificationPhase = true;
//    		beginScanningPhase();
//    	}
//    	
//    	public void beginTypingCardNumber() {
//    		beginTypingPhase();
//    		endScanningPhase();
//    	}
//    	
//    	public void beginScanningCard() {
//    		endTypingPhase();
//    		beginScanningPhase();
//    	}
//    	
//		public void enterTypedBarcode(String cardNumber) {
//			if (isTypingPhase) {
//				typedMembershipValidity(cardNumber);
//			}
//		}
//	}
}
