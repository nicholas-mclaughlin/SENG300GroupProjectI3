package org.lsmr.software;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import java.util.List;

public class StorageUnitContoller {
    private BanknoteStorageUnit banknoteStorageUnit;
    private CoinStorageUnit coinStorageUnit;

    public StorageUnitContoller(SelfCheckoutStation station){
        this.banknoteStorageUnit = station.banknoteStorage;
        this.coinStorageUnit = station.coinStorage;
    }

    public List<Banknote> unloadBanknote(){
        return banknoteStorageUnit.unload();
    }

    public List<Coin> unloadCoin(){
        return coinStorageUnit.unload();
    }
}
