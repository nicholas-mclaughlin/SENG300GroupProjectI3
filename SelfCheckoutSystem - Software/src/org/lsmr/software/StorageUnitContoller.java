package org.lsmr.software;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import java.math.BigDecimal;
import java.util.List;
/*
* Coin and banknote storage unit controller
 */
public class StorageUnitContoller {
    private BanknoteStorageUnit banknoteStorageUnit;
    private CoinStorageUnit coinStorageUnit;

    /*
    constructor
     @pram :
     station :selfcheckout station
     */
    public StorageUnitContoller(SelfCheckoutStation station){
        this.banknoteStorageUnit = station.banknoteStorage;
        this.coinStorageUnit = station.coinStorage;
    }

    /*
    unload the banknote from storage unit
    @return
    sum of amount of banknote unloaded
     */
    public int unloadBanknote(){
        List<Banknote> banknotes = banknoteStorageUnit.unload();
        int sum = 0;
        for(Banknote aBanknote : banknotes){
            sum += aBanknote.getValue();
        }
        return sum;
    }
    /*
    unload the coin from storage unit
    @return
    sum of amount of coin unloaded
     */
    public BigDecimal unloadCoin(){
        List<Coin> coins = coinStorageUnit.unload();
        BigDecimal sum = new BigDecimal("0");
        for(Coin aCoin : coins){
            sum = sum.add(aCoin.getValue());
        }
        return sum;
    }
}
