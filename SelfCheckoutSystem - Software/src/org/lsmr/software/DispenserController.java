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

    //consturctor
    public DispenserController(SelfCheckoutStation station){
        this.station = station;
        this.coinDenominations = station.coinDenominations;
        this.banknoteDenominations = station.banknoteDenominations;

    }

    ///constructor in case of currecny is not cad
    public DispenserController(SelfCheckoutStation station,Currency currency){
        this.station = station;
        this.coinDenominations = station.coinDenominations;
        this.banknoteDenominations = station.banknoteDenominations;
        this.currency = currency;
    }

    /*
    load a batch of same banknote
    @param
    Banknote banknoe : banknote object
    int number: number of the bannknote in the batch

    @return
    the sum of values of load banknotes
    */
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

    /*
    load varieties of different banknote
    @param
    banknotes: varieties of different banknotes, represented as array

    @return
    sum of total banknote load
     */
    public int loadBanknote(Banknote[] banknotes)
            throws SoftwareException, OverloadException, SimulationException{
        int sum = 0;
        for(Banknote banknote:banknotes){
            if(banknote.getCurrency() ==this.currency
                    && validateBanknoteDenomination(banknote.getValue())){
                Banknote[] aBanknote = {banknote};
                station.banknoteDispensers.get(banknote.getValue()).load(aBanknote);
                sum += banknote.getValue();
            }else{
                throw new SoftwareException("Illegal Banknote");
            }
        }
        return sum;
    }

    /*
    load varieties of different coin
    @param
    coins: varieties of different coins, represented as array

    @return
    sum of total coin load
    */
    public BigDecimal loadCoin(Coin[] coins)
            throws SoftwareException, OverloadException, SimulationException{
        BigDecimal sum = new BigDecimal("0");
        for(Coin coin:coins){
            if(coin.getCurrency() == this.currency
                    && validateCoinDenomination(coin.getValue())){
                Coin[] aCoin = {coin};
                station.coinDispensers.get(coin.getValue()).load(aCoin);
                sum = sum.add(coin.getValue());
            }else{
                throw new SoftwareException("Illegal Coin");
            }
        }
        return sum;
    }

    /*
     load a batch of same coin
     @param
     Banknote banknoe : coin object
     int number: number of the coins in the batch

     @return
      the sum of values of load coins
     */
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

    /*
    verify the value is a legal banknote denomination of the station

    @param
    value: the value want to be test

    @return
    true if legal value of a banknote, false if not
     */
    private boolean validateBanknoteDenomination(int value){
        for(int aDenomination : banknoteDenominations){
            if(aDenomination == value){
                return true;
            }
        }
        return  false;
    }

    /*
    verify the value is a legal coin denomination of the station

    @param
    value: the value want to be test

    @return
    true if legal value of a coin, false if not
     */
    private boolean validateCoinDenomination(BigDecimal value){
        return  coinDenominations.contains(value);
    }

    /*
    *   Rest are for testing usage only
    */
    public void emptyCoinDispenser(BigDecimal coinDenomination){
        station.coinStorage.unload();
    }


    public void emptyBanknoteDispenser(int banknoteDenomination){
        station.banknoteDispensers.get(banknoteDenomination).unload();
    }

    public void emptyAllCoinDispenser(){
        for (BigDecimal coinDenomination : coinDenominations) {
            emptyCoinDispenser(coinDenomination);
        }
    }

    public void emptyAllBanknoteDispenser(){
        for (int banknoteDenomination : banknoteDenominations) {
            emptyBanknoteDispenser(banknoteDenomination);
        }
    }

    //return the number of storages of different dispenser.
    public int[] updateStorage(){
        int[] storages = new int[coinDenominations.size()+banknoteDenominations.length];
        //update storage of coins
        for(int i = 0; i< coinDenominations.size(); i++){
            BigDecimal coinDenomination = coinDenominations.get(i);
            int number = station.coinDispensers.get(coinDenomination).size();
            storages[i] = number;
        }

        //update storage of banknotes
        for(int i = 0; i < banknoteDenominations.length; i++){
            int banknoteDenomination = banknoteDenominations[i];
            int number = station.banknoteDispensers.get(banknoteDenomination).size();
            storages[i+coinDenominations.size()] = number;
        }
        return storages;
    }







}
