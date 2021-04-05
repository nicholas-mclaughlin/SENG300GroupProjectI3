package org.lsmr.test;

import static org.junit.Assert.*;


import org.junit.Test;

import org.junit.Before;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.Product;
import org.lsmr.software.*;
import org.lsmr.selfcheckout.devices.SimulationException;

import org.junit.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;


public class MembershipCardControllerTest {
	
	private SelfCheckoutStation station;
    private int[] banknoteDenominations = {5, 10, 20, 50, 100};
    

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
        String cardNum1 = "1234567891011120";
        String name1 = "Ronaldo";
        Date date1 = new Date(2022, 1, 1);
        
        DatabaseMembershipCard.newMember(cardNum1, name1, date1); 
    }

    @Test
    public void test1() {
    	MembershipCardController membershipCardController = new MembershipCardController(station.mainScanner);
    	assertFalse(membershipCardController.isTypingPhase());
    	assertFalse(membershipCardController.isScanningPhase());
    	assertFalse(membershipCardController.isMemberVerificationPhase());
    	
    	membershipCardController.beginMemberVerificationPhase();
    	assertFalse(membershipCardController.isTypingPhase());
    	assertTrue(membershipCardController.isScanningPhase());
    	assertTrue(membershipCardController.isMemberVerificationPhase());
    	
    	membershipCardController.beginTypingPhase();
    	assertTrue(membershipCardController.isTypingPhase());
    	assertFalse(membershipCardController.isScanningPhase());
    	assertTrue(membershipCardController.isMemberVerificationPhase());
    	
		membershipCardController.typedMembershipValidity("124234525");
    	assertFalse(membershipCardController.isMemberVerified());
    	assertTrue(membershipCardController.isUnverifiedNumber());
    	
    	membershipCardController.typedMembershipValidity("1234567891011120");
    	assertTrue(membershipCardController.isMemberVerified());
    }
    
    @Test
    public void test2() {
    	MembershipCardController membershipCardController = new MembershipCardController(station.mainScanner);
		
    	membershipCardController.beginMemberVerificationPhase();
    	assertFalse(membershipCardController.isTypingPhase());
    	assertTrue(membershipCardController.isScanningPhase());
    	assertTrue(membershipCardController.isMemberVerificationPhase());
    	
    	membershipCardController.beginTypingPhase();
    	assertTrue(membershipCardController.isTypingPhase());
    	assertFalse(membershipCardController.isScanningPhase());
    	assertTrue(membershipCardController.isMemberVerificationPhase());
    	
    	membershipCardController.beginScanningPhase();
    	assertFalse(membershipCardController.isTypingPhase());
    	assertTrue(membershipCardController.isScanningPhase());
    	assertTrue(membershipCardController.isMemberVerificationPhase());
    	
    	Barcode b1 = new Barcode("124234525");
    	BarcodedItem card1 = new BarcodedItem(b1, 1);
    	station.mainScanner.scan(card1);
    	assertFalse(membershipCardController.isMemberVerified());
    	assertTrue(membershipCardController.isUnverifiedNumber());
    	
    	Barcode b2 = new Barcode("1234567891011120");
    	BarcodedItem card2 = new BarcodedItem(b2, 1);
    	station.mainScanner.scan(card2);
    	assertTrue(membershipCardController.isMemberVerified());
    }

}
