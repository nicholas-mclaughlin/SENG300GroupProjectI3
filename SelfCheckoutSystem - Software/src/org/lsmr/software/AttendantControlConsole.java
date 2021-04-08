package org.lsmr.software;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.*;

import java.math.BigDecimal;

import java.util.List;

public class AttendantControlConsole {
    private SoftwareController centre;
    public final AttendantDataBase attendantDataBase = new AttendantDataBase();
    private DispenserController dispenserController;
    private StorageUnitContoller storageUnitContoller;
    private SelfCheckoutStation station;
    private String currentAttendant = null;
    private boolean stationActive;

    public AttendantControlConsole(SoftwareController centre ,SelfCheckoutStation station ){
        this.centre = centre;
        this.station = station;
        this.dispenserController = new DispenserController(station);
        this.storageUnitContoller = new StorageUnitContoller(station);
    }

    public boolean addEntry(String username,String password) {
        if (currentAttendant == null) throw new SoftwareException("Log in required");
        return attendantDataBase.addEntry(username,password);
    }

    public boolean logIn(String username,String password){
        if(attendantDataBase.logIn(username,password)){
            currentAttendant = username;
            return true;
        }else{
            return false;
        }
    }

    public boolean logOut(){
        if(currentAttendant == null){
            return false;
        }else{
            currentAttendant = null;
            return true;
        }
    }

    public void addPaper(int units) throws SimulationException,SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        //Other Exception will be thrown by printer as simulationException
        station.printer.addPaper(units);
    }

    public void addInk(int quantity) throws SimulationException,SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        //Other Exception will be thrown by printer as simulationException
        station.printer.addInk(quantity);
    }

    public int emptyBanknoteStorage() throws SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        List<Banknote> banknotes = storageUnitContoller.unloadBanknote();
        int sum = 0;
        for(Banknote aBanknote : banknotes){
            sum += aBanknote.getValue();
        }
        return sum;
    }

    public BigDecimal emptyCoinStorage() throws SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        List<Coin> coins = storageUnitContoller.unloadCoin();
        BigDecimal sum = new BigDecimal("0");
        for(Coin aCoin : coins){
            sum.add(aCoin.getValue());
        }
        return sum;
    }

    public int loadBanknote(Banknote banknote, int number)
            throws SoftwareException, OverloadException, SimulationException {
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return dispenserController.loadBanknote(banknote,number);
    }

    public BigDecimal loadCoin (Coin coin, int number)throws SoftwareException, OverloadException, SimulationException {
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return dispenserController.loadCoin(coin,number);
    }

    public boolean getStationStatus(){
        return stationActive;
    }
    public boolean startUpStation()throws SoftwareException{
        if(currentAttendant != null){
            station.banknoteValidator.enable();
            station.banknoteInput.enable();
            station.banknoteOutput.enable();
            station.banknoteStorage.enable();
            for(CoinDispenser coinDispenser: station.coinDispensers.values()){
                coinDispenser.enable();
            }

            station.coinSlot.enable();
            station.coinTray.enable();
            station.coinStorage.enable();
            station.coinValidator.enable();
            for(BanknoteDispenser banknoteDispenser: station.banknoteDispensers.values()){
                banknoteDispenser.enable();
            }

            station.scale.enable();
            station.baggingArea.enable();
            station.cardReader.enable();
            station.handheldScanner.enable();
            station.mainScanner.enable();
            station.printer.enable();
            station.screen.enable();
            this.stationActive = true;
            return true;
        }else{
            throw new SoftwareException("Log in required");
        }
    }

    public boolean shutDownStation()throws SoftwareException{
        if(currentAttendant != null){
            station.banknoteValidator.disable();
            station.banknoteInput.disable();
            station.banknoteOutput.disable();
            station.banknoteStorage.disable();
            for(CoinDispenser coinDispenser: station.coinDispensers.values()){
                coinDispenser.disable();
            }

            station.coinSlot.disable();
            station.coinTray.disable();
            station.coinStorage.disable();
            station.coinValidator.disable();
            for(BanknoteDispenser banknoteDispenser: station.banknoteDispensers.values()){
                banknoteDispenser.disable();
            }

            station.scale.disable();
            station.baggingArea.disable();
            station.cardReader.disable();
            station.handheldScanner.disable();
            station.mainScanner.disable();
            station.printer.disable();
            station.screen.disable();
            this.stationActive = false;
            return false;
        }else{
            throw new SoftwareException("Log in required");
        }
    }
}
