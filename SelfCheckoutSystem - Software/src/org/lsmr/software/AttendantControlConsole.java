package org.lsmr.software;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.*;

import java.math.BigDecimal;
/*
    Main backbone of attendant control console
    All attendant methods should be registered here
 */

public class AttendantControlConsole {
    private SoftwareController centre;
    public final AttendantDataBase attendantDataBase = new AttendantDataBase();
    private DispenserController dispenserController;
    private StorageUnitContoller storageUnitContoller;
    private SelfCheckoutStation station;
    private String currentAttendant = null;
    private boolean stationActive;

    /*constructor
    @pram :
     centre :software controller
     station: selfcheckoutstation
    */
    public AttendantControlConsole(SoftwareController centre ,SelfCheckoutStation station ){
        this.centre = centre;
        this.station = station;
        this.dispenserController = new DispenserController(station);
        this.storageUnitContoller = new StorageUnitContoller(station);
    }

    //log in
    // return true for login scuessfully, false for invalid login
    public boolean logIn(String username,String password){
        if(attendantDataBase.logIn(username,password)){
            currentAttendant = username;
            return true;
        }else{
            return false;
        }
    }

    //log out
    // return true for logout scuessfully, false for no attendant can log  out
    public boolean logOut(){
        if(currentAttendant == null){
            return false;
        }else{
            currentAttendant = null;
            return true;
        }
    }

    //add a new account for attendant console
    //return true for adding sucuessfully, false for cannot add
    public boolean addEntry(String username,String password) {
        if (currentAttendant == null) throw new SoftwareException("Log in required");
        return attendantDataBase.addEntry(username,password);
    }

    //remove a account for attendant console
    //the current working account cannot be removed
    //return true for removing sucuessfully, false for cannot remove
    public boolean removeEntry(String username) {
        if (currentAttendant == null) throw new SoftwareException("Log in required");
        return attendantDataBase.removeEntry(currentAttendant,username);
    }

    //add paper, exceptions will from devices will be passed to here
    public void addPaper(int units) throws SimulationException,SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        //Other Exception will be thrown by printer as simulationException
        station.printer.addPaper(units);
    }

    //add ink, exceptions will from devices will be passed to here
    public void addInk(int quantity) throws SimulationException,SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        //Other Exception will be thrown by printer as simulationException
        station.printer.addInk(quantity);
    }

    //empty banknote storage unit
    //return the number banknote storage unit rest
    public int emptyBanknoteStorage() throws SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        storageUnitContoller.unloadBanknote();
        return station.banknoteStorage.getBanknoteCount();
    }

    //empty coin storage unit
    //return the number coin storage unit rest
    public int emptyCoinStorage() throws SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        storageUnitContoller.unloadCoin();
        return station.coinStorage.getCoinCount();
    }

    // load a batch of same banknote
    // return loaded sum
    public int loadBanknote(Banknote banknote, int number)
            throws SoftwareException, OverloadException, SimulationException {
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return dispenserController.loadBanknote(banknote,number);
    }

    //load varieties of banknotes
    // return loaded sum
    public int loadBanknote(Banknote[] banknotes)
            throws SoftwareException, OverloadException, SimulationException {
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return dispenserController.loadBanknote(banknotes);
    }

    // load a batch of same coins
    // return loaded sum
    public BigDecimal loadCoin (Coin coin, int number)throws SoftwareException, OverloadException, SimulationException {
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return dispenserController.loadCoin(coin,number);
    }

    //load varieties of coins
    // return loaded sum
    public BigDecimal loadCoin(Coin[] coins)
            throws SoftwareException, OverloadException, SimulationException {
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return dispenserController.loadCoin(coins);
    }

    //get station on /off status
    public boolean getStationStatus(){
        return stationActive;
    }

    //shutdown station, return local flag of station active or not
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
            return stationActive;
        }else{
            throw new SoftwareException("Log in required");
        }
    }

    //shutdown station, return local flag of station active or not
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
            return stationActive;
        }else{
            throw new SoftwareException("Log in required");
        }
    }
}
