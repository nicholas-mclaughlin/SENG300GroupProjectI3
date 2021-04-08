package org.lsmr.software;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class DispenserController {
    private SelfCheckoutStation station;
    private Currency currency = Currency.getInstance("CAD");
    private int[] banknoteDenominations;
    private List<BigDecimal> coinDenominations;

    public DispenserController(SelfCheckoutStation station){
        this.coinDenominations = station.coinDenominations;
        this.banknoteDenominations = station.banknoteDenominations;

    }

    public DispenserController(SelfCheckoutStation station,Currency currency){
        this.station = station;
        this.coinDenominations = station.coinDenominations;
        this.banknoteDenominations = station.banknoteDenominations;
        this.currency = currency;
    }
    public int loadBanknote(Banknote banknote, int number)
            throws SoftwareException, OverloadException, SimulationException{
        if(banknote.getCurrency() ==this.currency
                && validateBanknoteDenomination(banknote.getValue())){
            Banknote[] aBanknote = {banknote};
            for (int i = 0; i < number; i++){
                station.banknoteDispensers.get(banknote.getValue()).load(aBanknote);
            }
            return banknote.getValue()*number;
        }else{
            throw new SoftwareException("Illegal Banknote");
        }
    }

    public BigDecimal loadCoin(Coin coin,int number)
            throws SoftwareException, OverloadException, SimulationException{
        if(coin.getCurrency() == this.currency
                && validateCoinDenomination(coin.getValue())){
            Coin[] aCoin = {coin};
            for(int i = 0; i< number; i++ ){
                station.coinDispensers.get(coin.getValue()).load(aCoin);
            }
            return coin.getValue().multiply(new BigDecimal(number));
        }else{
            throw new SoftwareException("Illegal Coin");
        }
    }

    private boolean validateBanknoteDenomination(int value){
        for(int aDenomination : banknoteDenominations){
            if(aDenomination == value){
                return true;
            }
        }
        return  false;
    }
    private boolean validateCoinDenomination(BigDecimal value){
        return  coinDenominations.contains(value);
    }

    /*
    *   Rest are for testing usage
     */
    public void emptyCoinDispenser(BigDecimal coinDenomination){
        station.coinStorage.unload();
    }


    public void emptyBanknoteDispenser(int banknoteDenomination){
        station.banknoteDispensers.get(banknoteDenomination).unload();
    }

    public void emptyAllCoinDispenser(){
        for(int i = 0; i< coinDenominations.size(); i++){
            emptyCoinDispenser(coinDenominations.get(i));
        }
    }

    public void emptyAllBanknoteDispenser(){
        for(int i = 0; i < banknoteDenominations.length; i++){
            int banknoteDenomination = banknoteDenominations[i];
            emptyBanknoteDispenser(banknoteDenomination);
        }
    }







}
