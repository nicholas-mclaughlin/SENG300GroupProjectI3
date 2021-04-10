package org.lsmr.test;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.software.SoftwareController;
import org.lsmr.software.StorageUnitContoller;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.*;

public class StorageUnitContollerTest {
    //only banknote unload unit test is done here
    public final Currency currency = Currency.getInstance(Locale.CANADA);
    public final int[] banknoteDenominations = {5,10,20,50,100};
    public final BigDecimal[] coinDenominations = {new BigDecimal("0.05"),new BigDecimal("0.1"),
            new BigDecimal("0.25"), new BigDecimal("1"), new BigDecimal("2")};
    private SelfCheckoutStation stubStation = new SelfCheckoutStation(
            currency,banknoteDenominations,coinDenominations,300,1);
    private StorageUnitContoller storageUnitContoller = new StorageUnitContoller(stubStation);
    @Test
    public void unloadBanknoteEmptyTest(){
        int unloadAmount = storageUnitContoller.unloadBanknote();
        assertTrue(unloadAmount == 0 && stubStation.banknoteStorage.getBanknoteCount() == 0);
    }

    @Test
    public void unloadBanknoteWithValueTest(){
        Banknote twenty = new Banknote(20,currency);
        Banknote hundred = new Banknote(100,currency);
        Banknote[] list = {hundred,hundred,hundred,twenty,twenty,twenty};
        try{
            stubStation.banknoteStorage.load(list);
            int unloadAmount = storageUnitContoller.unloadBanknote();
            assertEquals(360,unloadAmount);
        }catch (Exception e){
            fail(e.getMessage());
        }

    }
}