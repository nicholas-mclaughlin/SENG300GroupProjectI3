package org.lsmr.software;

import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.ElectronicScaleListener;
import org.lsmr.selfcheckout.devices.listeners.TouchScreenListener;

import java.lang.Object;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BaggingAreaController {

	private boolean baggingNotStarted = true;
	private PersonalBaggingListener personalBaggingListener = new PersonalBaggingListener();
	private boolean addPersonalBagsPhase = false;
	private Timer baggingTimer = new Timer();
	private TimerTask baggingReminder;
	private long REMIND_TIME = 5000;
	private long QUIT_TIME = 20000;
	private double MAX_PERSONAL_BAG_WEIGHT = 20.0;
	private boolean bagItemOverdue = false;

	private boolean isBaggingPhase = false;
	private double expectedItemWeight = 0;
	private boolean isRemovingPhase = false;
	private double totalWeight = 0.0;

	private double unknownWeight = 0.0;
	private boolean overloaded = false;
	
	private ElectronicScale electronicScale;
	private ESListener electronicScaleListener = new ESListener();
	private ScanController scanController;
	
	
	public BaggingAreaController(ElectronicScale electronicScale) {
		this.electronicScale = electronicScale;
		this.electronicScale.register(electronicScaleListener);	
	}

	public void setScanController(ScanController scanController) {
		this.scanController = scanController;
	}

	public boolean getIsAddPersonalBagsPhase() {
		return addPersonalBagsPhase;
	}
	
	public boolean getBagItemOverdue() {
		return bagItemOverdue;
	}
	
	public boolean getOverloaded() {
		return overloaded;
	}
	
	public boolean getIsBaggingPhase() {
		return isBaggingPhase;
	}
	
	public boolean getIsRemovingPhase() {
		return isRemovingPhase;
	}

	public double getUnknownWeight() {
		return unknownWeight;
	}
	
	public double getTotalWeight() {
		return totalWeight;
	}
	
	public double getExpectedItemWeight() {
		return expectedItemWeight;
	}	
	public void setExpectedItemWeight(double w) {
		this.expectedItemWeight = w;
	}
	
	public PersonalBaggingListener getPersonalBaggingListener() {
		return personalBaggingListener;
	}
	
	
	public void addPersonalBag(double changeInWeight) {
		if (changeInWeight > MAX_PERSONAL_BAG_WEIGHT || unknownWeight != 0) {
			System.out.println("Excess weight added to bagging area. Please wait for attendant.");
			totalWeight += changeInWeight;
			unknownWeight += changeInWeight;
		}
		else {
			totalWeight += changeInWeight;
		}
	}


	public void startBaggingItem(double expectedWeight) {
		if (!isRemovingPhase && !isBaggingPhase) {
			baggingNotStarted = false;
			isBaggingPhase = true;
			expectedItemWeight = expectedWeight;
			baggingReminder = new addItemReminder();
			baggingTimer.schedule(baggingReminder, REMIND_TIME);
		}
		else
			System.out.println("Can't bag item until current item is removed from bagging area.");
	}
	
	public void bagItem(double changeInWeight) {
		if (expectedItemWeight*0.95 > changeInWeight || expectedItemWeight*1.05 < changeInWeight) {
			System.out.println("Item weight doesn't match expected weight. Please remove item and try again");
			unknownWeight += changeInWeight;
		}
		else {
			isBaggingPhase = false;
			totalWeight += changeInWeight;
			baggingReminder.cancel();
			bagItemOverdue = false;
			scanController.continueScanning();
		}
    }
	
	public void skipBaggingItem (BarcodedItem i) {
		double changeInWeight = i.getWeight();
		if(isBaggingPhase == true) {
			if ( (expectedItemWeight - changeInWeight) < 0.01) { // change in weight close to 0.0
				totalWeight += 0; // total Weight does not change 
				isBaggingPhase = false; // set bagging phase to false
				baggingReminder.cancel();
				bagItemOverdue = false;
				scanController.continueScanning();
			}
			else {
				System.out.println("Item weight doesn't match expected weight. Please remove item and try again");
				unknownWeight += changeInWeight;				
			}
		}
		
	}
	
	
	public void startRemovingItem(double expectedWeight) {
		if (!isBaggingPhase && !isRemovingPhase) {
			isRemovingPhase = true;
			expectedItemWeight = expectedWeight;
		}
		else
			System.out.println("Can't remove item until current item is added to bagging area.");
	}

    public void removeItem(double changeInWeight) {
    	if (expectedItemWeight*-0.95 < changeInWeight || expectedItemWeight*-1.05 > changeInWeight) {
			System.out.println("Removed item weight doesn't match expected weight. Please return to item bagging area then try again");
			unknownWeight += changeInWeight;
			
		}
    	else {
			isRemovingPhase = false;
			totalWeight += changeInWeight;
			scanController.continueScanning();
		}
    }
    

    /*
    We will make sure to match this stub to whatever is required in the third iteration.
    Basically simulates if a user taps on the touch screen if they want to add their
    own bags to bagging area. 
    */
	public class PersonalBaggingListener implements TouchScreenListener {
		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {};

    	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {};

		public void startAddingPersonalBags() {
			if (baggingNotStarted) {
				addPersonalBagsPhase = true;
				System.out.println("Please add bags to the bagging area.");
			}
		}

		public void endAddingPersonalBags() {
			if (addPersonalBagsPhase)
				addPersonalBagsPhase = false;
		}
	}


    public class ESListener implements ElectronicScaleListener{
    	
    	public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {};

    	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {};
    	
    	public void weightChanged(ElectronicScale scale, double weightInGrams) {
    		if (!overloaded) {
	    		double changeInWeight = weightInGrams-totalWeight;
	    		
				if (addPersonalBagsPhase) {
					addPersonalBag(changeInWeight);
				}
	
	    		// If the weight on the scale changes when it is not bagging phase
	    		else if (!isBaggingPhase && !isRemovingPhase && unknownWeight == 0) {
	    			unknownWeight += changeInWeight;
	    			totalWeight += changeInWeight;
	    			if (changeInWeight > 0) {
	    				System.out.println("Unknown item in the bagging area. Please remove.");
	    			}
	    			else if (changeInWeight < 0) {
	    				System.out.println("Item removed from the bagging area. Please put back.");
	    			}
	    		}
	    		
	    		// If there was already an unknown weight in the bagging area
	    		else if (unknownWeight != 0) {
	    			unknownWeight += changeInWeight;
	    			totalWeight += changeInWeight;
	    			if (unknownWeight == 0) {
	    				System.out.println("Unknown item removed. Please Continue.");
	    			}
	    			else if (unknownWeight > 0 && changeInWeight > 0) {
	    				System.out.println("Unknown item added to the bagging area. Please remove.");
	    			}
	    			else if (unknownWeight > 0 && changeInWeight < 0) {
	    				System.out.println("There is still an unknown item in the bagging area.");
	    			}
	    			else if (unknownWeight < 0 && changeInWeight > 0) {
	    				System.out.println("Bagging area still missing some items.");
	    			}
	    			else if (unknownWeight < 0 && changeInWeight < 0) {
	    				System.out.println("Item removed from the bagging area. Please put back.");
	    			}
	    		}
	    		
	    		// If item is removed from scale during the bagging phase
	    		else if (changeInWeight < 0 && isBaggingPhase) {
	    			unknownWeight += changeInWeight;
	    			totalWeight += changeInWeight;
	    			System.out.println("Item removed from the bagging area. Please put back.");
	    		}
				// If item is added to scale during the removing phase
	    		else if (changeInWeight > 0 && isRemovingPhase) {
	    			unknownWeight += changeInWeight;
	    			totalWeight += changeInWeight;
	    			System.out.println("Item added to the bagging area. Please remove.");
	    		}
	    		
	    		else if (isBaggingPhase)
	    			bagItem(changeInWeight);
	    	
	    		else if (isRemovingPhase)
	    			removeItem(changeInWeight); 	
    		}
    	}

    	public void overload(ElectronicScale scale) {
    		System.out.println("Bagging area weight limit exceeded.");
    		overloaded = true;
    	}

    	public void outOfOverload(ElectronicScale scale) {
    		System.out.println("Excessive weight removed. Please continue.");
    		overloaded = false; 
    	}
    }

	public class addItemReminder extends TimerTask {
		public addItemReminder() {}

		public synchronized void run() {
			if (isBaggingPhase) {
				System.out.println("Please add item to the bagging area!");
				bagItemOverdue = true;
			}
		}
	}
	
}