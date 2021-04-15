package org.lsmr.software;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.listeners.*;
import org.lsmr.selfcheckout.external.CardIssuer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.ZERO;

public class PaymentController {

    private static final BigDecimal GST_VALUE = new BigDecimal("1.05");
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal FIFTY = new BigDecimal("50");
    private static final BigDecimal TWENTY = new BigDecimal("20");
    private static final BigDecimal TEN = new BigDecimal("10");
    private static final BigDecimal FIVE = new BigDecimal("5");
    private static final BigDecimal TOONIE = new BigDecimal("2");
    private static final BigDecimal LOONIE = new BigDecimal("1");
    private static final BigDecimal QUARTER = new BigDecimal("0.25");
    private static final BigDecimal DIME = new BigDecimal("0.1");
    private static final BigDecimal NICKEL = new BigDecimal("0.5");
    private BigDecimal total;
    private BigDecimal amountPaid;
    private boolean isPaymentPhase = false;
    private int paymentType = 0;		// (0 is default, 1 indicates cash/coin, 2 indicates card)
    private boolean shouldEmptyCoinStorage = false;
    private boolean shouldEmptyBanknoteStorage = false;
    private Currency currency;
    
    private CoinValidator coinValidator;
    private CVListener coinValidatorListener = new CVListener();
    private CoinTray coinTray;
    private CTListener coinTrayListener = new CTListener();
    private CoinSlot coinSlot;
    private CSlotListener coinSlotListener = new CSlotListener();
    private CoinStorageUnit coinStorage;
    private CStorageListener coinStorageListener = new CStorageListener();
   // private Map<BigDecimal, CoinDispenser> coinDispensers;
    //private CDispenserListener coinDispenserListener = new CDispenserListener(this);
    private BanknoteValidator banknoteValidator;
    private BVListener banknoteValidatorListener = new BVListener();
    private BanknoteSlot banknoteSlot;
    private BSlotListener banknoteSlotListener = new BSlotListener();
    private BanknoteStorageUnit banknoteStorage;
    private BStorageListener banknoteStorageListener = new BStorageListener();
    //Map<Integer, BanknoteDispenser> banknoteDispensers;
    private SoftwareController caller;
    
    private CReaderListener cardReaderListener = new CReaderListener(this);
    private CardReader cardReader;
    
    private Map<BigDecimal, CoinDispenser> coinDispensers;
    private Map<Integer, BanknoteDispenser> banknoteDispensers;
    private Map<BigDecimal, CDispenserListener> coinDispenserListeners = new HashMap<>();
    private Map<Integer, BDispenserListener> banknoteDispenserListeners = new HashMap<>();

    public Map<String, CardIssuer> BANK_CARD_DATABASE = Database.BANK_CARD_DATABASE;
    
    public PaymentController(CoinValidator cv, CoinTray ct, CoinSlot cSlot, CoinStorageUnit cStorage, Map<BigDecimal, CoinDispenser> cDispensers,
                             BanknoteValidator bv, BanknoteSlot bSlot, BanknoteStorageUnit bStorage, Map<Integer, BanknoteDispenser> bDispensers,
                             CardReader cardReader) {
        coinValidator = cv;
        coinValidator.register(coinValidatorListener);

        coinTray = ct;
        coinTray.register(coinTrayListener);

        coinSlot = cSlot;
        coinSlot.register(coinSlotListener);

        coinStorage = cStorage;
        coinStorage.register(coinStorageListener);
       
        //create a map of coin dispensers
        coinDispensers = cDispensers;
        banknoteDispensers = bDispensers;
         
        banknoteValidator = bv;
        banknoteValidator.register(banknoteValidatorListener);

        banknoteSlot = bSlot;
        banknoteSlot.register(banknoteSlotListener);

        banknoteStorage = bStorage;
        banknoteStorage.register(banknoteStorageListener);
        
        
        // Register a listener for each coin dispenser 
        for (Map.Entry<BigDecimal, CoinDispenser> entry : coinDispensers.entrySet()) {
        	CDispenserListener cdListener = new CDispenserListener(this);
        	coinDispenserListeners.put(entry.getKey(), cdListener);
        	entry.getValue().register(cdListener);
        }
        
        
        // Register a listener for each bank note dispenser 
        for (Map.Entry<Integer, BanknoteDispenser> entry : banknoteDispensers.entrySet()) {
        	BDispenserListener bdListener = new BDispenserListener(this);
        	banknoteDispenserListeners.put(entry.getKey(), bdListener);
        	entry.getValue().register(bdListener);
        }
        
        this.cardReader = cardReader;
        cardReader.register(cardReaderListener);
        
      
    }
    
    
    private class CVListener implements CoinValidatorListener {

        @Override
        public void validCoinDetected(CoinValidator validator, BigDecimal value) {
            addToPayment(value);
        }

        @Override
        public void invalidCoinDetected(CoinValidator validator) {
            System.out.println("Not a valid coin. Try again.");
        }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) { }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) { }
    }

    private class CTListener implements CoinTrayListener {

        @Override
        public void coinAdded(CoinTray tray) {
            System.out.println("Please retrieve your coin(s).");
        }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) { }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) { }
    }

    private class CSlotListener implements CoinSlotListener {

        @Override
        public void coinInserted(CoinSlot slot) { }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) { }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) { }
    }
    
    private class CDispenserListener implements CoinDispenserListener {
    	
       	private PaymentController paymentController;
    	private boolean empty = false;
    	private boolean success = false;
    	
    	
    	public CDispenserListener(PaymentController paymentController) {
    		this.paymentController = paymentController;
    	}
    	
		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void coinsFull(CoinDispenser dispenser) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void coinsEmpty(CoinDispenser dispenser) {
			// TODO Auto-generated method stub
			empty = true;
			success = true;
		}

		@Override
		public void coinAdded(CoinDispenser dispenser, Coin coin) {
			// TODO Auto-generated method stub
			empty = false;
			
		}

		@Override
		public void coinRemoved(CoinDispenser dispenser, Coin coin) {
			success = true;
			
		}

		@Override
		public void coinsLoaded(CoinDispenser dispenser, Coin... coins) {
			// TODO Auto-generated method stub
			empty = false;
			
		}

		@Override
		public void coinsUnloaded(CoinDispenser dispenser, Coin... coins) {
			// TODO Auto-generated method stub
			
		}
		
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return empty;
		}
		
		public boolean isSucess() {
			boolean tmp = success;
			success = false;
			return tmp;
		}
    	
    	
    	
    }
    
    private class BDispenserListener implements BanknoteDispenserListener {

    	private PaymentController paymentController;
    	private boolean empty = false;
    	private boolean success = false;
    	
    	public BDispenserListener(PaymentController paymentController) {
    		this.paymentController = paymentController;
    	}
    	
    	
		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void banknotesFull(BanknoteDispenser dispenser) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void banknotesEmpty(BanknoteDispenser dispenser) {
			empty = true;
			success = true;
			
		}

		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return empty;
		}
		
		public boolean isSucess() {
			boolean tmp = success;
			success = false;
			return tmp;
		}

		@Override
		public void banknoteAdded(BanknoteDispenser dispenser, Banknote banknote) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void banknoteRemoved(BanknoteDispenser dispenser, Banknote banknote) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void banknotesLoaded(BanknoteDispenser dispenser, Banknote... banknotes) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void banknotesUnloaded(BanknoteDispenser dispenser, Banknote... banknotes) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    

    private class CStorageListener implements CoinStorageUnitListener {

        @Override
        public void coinsFull(CoinStorageUnit unit) {
            shouldEmptyCoinStorage = true;
            System.out.println("Warning: Coin storage unit is full. Please wait for attendant.");
        }

        @Override
        public void coinAdded(CoinStorageUnit unit) {
            if (unit.getCoinCount() >= unit.getCapacity() - 10) {
                shouldEmptyCoinStorage = true;
                System.out.println("Warning: Coin storage unit is nearly full.");
            }
        }

        @Override
        public void coinsLoaded(CoinStorageUnit unit) {
            if (unit.getCapacity() == unit.getCoinCount()) {
                shouldEmptyCoinStorage = true;
                System.out.println("Warning: Coin storage unit is full. Please wait for attendant.");
            } else if(unit.getCoinCount() >= unit.getCapacity() - 10) {
                shouldEmptyCoinStorage = true;
                System.out.println("Warning: Coin storage unit is nearly full.");
            }
        }

        @Override
        public void coinsUnloaded(CoinStorageUnit unit) {
            if (unit.getCoinCount() == unit.getCapacity()) {
                shouldEmptyCoinStorage = true;
                System.out.println("Warning: Coin storage unit is full. Please wait for attendant.");
            } else if (unit.getCoinCount() >= unit.getCapacity() - 10) {
                shouldEmptyCoinStorage = true;
                System.out.println("Warning: Coin storage unit is nearly full.");
            } else {
                shouldEmptyCoinStorage = false;
            }
        }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) { }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) { }
    }

    private class BVListener implements BanknoteValidatorListener {
        @Override
        public void validBanknoteDetected(BanknoteValidator validator, Currency currency, int value) {
            addToPayment(new BigDecimal(value));
        }

        @Override
        public void invalidBanknoteDetected(BanknoteValidator validator) {
            System.out.println("Not a valid banknote. Try again.");
        }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) { }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) { }
    }

    private class BSlotListener implements BanknoteSlotListener {

        @Override
        public void banknoteInserted(BanknoteSlot slot) { }

        @Override
        public void banknoteEjected(BanknoteSlot slot) {
            System.out.println("Please remove banknote.");
        }

        @Override
        public void banknoteRemoved(BanknoteSlot slot) { }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) { }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) { }
    }

    private class BStorageListener implements BanknoteStorageUnitListener {

        @Override
        public void banknotesFull(BanknoteStorageUnit unit) {
            shouldEmptyBanknoteStorage = true;
            System.out.println("Warning: Banknote storage unit is full. Please wait for attendant.");
        }

        @Override
        public void banknoteAdded(BanknoteStorageUnit unit) {
            if (unit.getBanknoteCount() >= unit.getCapacity() - 10) {
                shouldEmptyBanknoteStorage = true;
                System.out.println("Warning: Banknote storage unit is nearly full.");
            }
        }

        @Override
        public void banknotesLoaded(BanknoteStorageUnit unit) {
            if (unit.getCapacity() == unit.getBanknoteCount()) {
                shouldEmptyBanknoteStorage = true;
                System.out.println("Warning: Banknote storage unit is full. Please wait for attendant.");
            } else if(unit.getBanknoteCount() >= unit.getCapacity() - 10) {
                shouldEmptyBanknoteStorage = true;
                System.out.println("Warning: Banknote storage unit is nearly full.");
            }
        }

        @Override
        public void banknotesUnloaded(BanknoteStorageUnit unit) {
            if (unit.getBanknoteCount() == unit.getCapacity()) {
                shouldEmptyBanknoteStorage = true;
                System.out.println("Warning: Banknote storage unit is full. Please wait for attendant.");
            } else if (unit.getBanknoteCount() >= unit.getCapacity() - 10) {
                shouldEmptyBanknoteStorage = true;
                System.out.println("Warning: Banknote storage unit is nearly full.");
            } else {
                shouldEmptyBanknoteStorage = false;
            }
        }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) { }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) { }
    }
    
    public class CReaderListener implements CardReaderListener {

    	private PaymentController paymentController;
    	private CardData dataDetected;
    	
    	public CReaderListener(PaymentController paymentController) {
    		this.paymentController = paymentController;
    	}
    	
		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void cardInserted(CardReader reader) {
			paymentController.informCardInserted();
		}

		@Override
		public void cardRemoved(CardReader reader) {
			// TODO Auto-generated method stub
		}

		@Override
		public void cardTapped(CardReader reader) {
			paymentController.informCardTapped();
			
		}

		@Override
		public void cardSwiped(CardReader reader) {
			paymentController.informCardSwiped();
			
		}

		@Override
		public void cardDataRead(CardReader reader, CardData data) {
			dataDetected = data;
		}
		
		public CardData transmitCardData() {
			CardData tmp = dataDetected;
			dataDetected = null;
			return tmp;
		}
    	
    }
    
    // Payment type (0 is default, 1 is coin, 2 is card)
    // Can't change payment type once you begin making that payment
    // In full implementation, customer would indicate payment type
    public void setPaymentType(int type) {
    	paymentType = type;
    }
    
    // Informed of card inserted in card reader event
    // Ignore if not in payment phase, or if payment type is not set to card
    public void informCardInserted() {
    	if ((isPaymentPhase) && (paymentType == 2)) {
    		
    	}
    }
    
    
    // Informed of card tapped event
    // Ignore if not in payment phase
    public void informCardTapped() {
    	if ((isPaymentPhase) && (paymentType == 2)) {
    		
    	}
    }
    
    // Informed of card swiped event
    // Ignore if not in payment phase
    public void informCardSwiped() {
    	if ((isPaymentPhase) && (paymentType == 2)) {
    		
    	}
    }
    
    public void analyseCardData() {
    	CardData data = cardReaderListener.transmitCardData();
    	if (data == null) {
    		// data cannot be read
    		System.out.println("Error reading card. Please try again.");
    	}
    	
    	// Check if card is recognized, if not, 
    	CardIssuer issuer = BANK_CARD_DATABASE.get(data.getType());
    	if (issuer == null) {
    		System.out.println("Card type is not recognized. Please use a different card.");
    	}
    	
    	// Try to place hold
    	int holdReference = issuer.authorizeHold(data.getNumber(), total);
    	if (holdReference == -1) {
    		
    	}
    	
    	// Try to complete transaction
    	if (issuer.postTransaction(data.getNumber(), holdReference, total) == false) {
    		
    	}
    	
    	// Otherwise, pay and complete transaction
    	total = BigDecimal.ZERO;
    	paymentType = 0;
    	endPayment();
    }

    /**
     * Initiates the payment process by calculating the total to be paid
     * and enabling the payment-related devices.
     *
     * NOTE: because the only supported payment option is cash,
     * the total is automatically rounded to the nearest 5 cents.
     * @param subtotal the sum of the prices of the items being purchased
     * @throws NullPointerException if subtotal is null
     * @throws IllegalArgumentException if subtotal <= 0
     */
    public void beginPayment(BigDecimal subtotal, SoftwareController caller) {
        if (subtotal == null)
            throw new NullPointerException("Amount to be paid cannot be null.");

        if (subtotal.compareTo(ZERO) <= 0)
            throw new IllegalArgumentException("Amount to be paid must be greater than zero.");

        amountPaid = ZERO;
        BigDecimal t = ZERO;
        t = subtotal.multiply(GST_VALUE);
        t = t.multiply(TWENTY);
        t = t.setScale(0, RoundingMode.HALF_UP);
        total = t.divide(TWENTY, 2, RoundingMode.HALF_UP);
        isPaymentPhase = true;
        System.out.println("Please insert cash, coin or card.");
        this.caller = caller;
    }

    
    /**
     * Refunds payment and ends payment phase
     */
    public void cancelPayment() {
        if (isPaymentPhase) {
            total = amountPaid.negate();
            returnChange();
            isPaymentPhase = false;
        }
    }

    public void endPayment() {
        if (total.compareTo(ZERO) == 0) {
            System.out.println("Payment complete.");
            isPaymentPhase = false;
            caller.endTransaction();
        } else if (total.compareTo(ZERO) > 0) {
            System.out.println("Payment is not complete, please provide more payment.");
        } else {
            System.out.println("Change has not been provided, please contact employee.");
        }
    }

    public boolean isPaymentPhase() {
        return isPaymentPhase;
    }

    public void addToPayment(BigDecimal payment) {
        if(payment.compareTo(ZERO) > 0) {
            total = total.subtract(payment);
            amountPaid = amountPaid.add(payment);
        } else {
            // negative (or 0) payment has been provided... that's weird
            // this should never happen when denominations are properly configured
            System.out.println("Error: Payment has value less than or equal to zero.");
        }

        if(total.compareTo(ZERO) <= 0) {
            returnChange();
        }
    }

    public BigDecimal getTotal() {
        return new BigDecimal(String.valueOf(total));
    }
    
 
 /*  
    public void addCoin(Coin coin) {
    	
    	cs.accept(coin);
    	cv.accept(coin);
    	this.currency = coin.getCurrency();
    	BigDecimal coinAmount = coin.getValue();
    	addToPayment(coinAmount);
    	
    }

*/

    public boolean shouldEmptyCoinStorage() {
        return shouldEmptyCoinStorage;
    }

    public boolean shouldEmptyBanknoteStorage() {
        return shouldEmptyBanknoteStorage;
    }

    private void returnChange() {
      
    	//implement the listener so that when a coin is emitted the listener is is notified
    	//the listener is subtract the total for what has been emitted and what is left to emit
    	//call return again and continue to do so
    	//keep track of when the dispenser runs out
    	// so that is we do not have something it doesnt continue to keep trying to emit that specific coin
    	//instead tries to emit the next denomination
    	//ex. no toonies, 
    	

        while (total.compareTo(ZERO) < 0) {
            // not sure because no banknote dispenser/coin dispenser is part of the hardware
            if ((total.add(HUNDRED).compareTo(ZERO) <= 0) && (dispense(HUNDRED) == true)) {
                // dispense $100 bill
                total = total.add(HUNDRED);
            } else if ((total.add(FIFTY).compareTo(ZERO) <= 0) && (dispense(FIFTY) == true)) {
                // dispense $50 bill
                total = total.add(FIFTY);
            } else if ((total.add(TWENTY).compareTo(BigDecimal.ZERO) <= 0) && (dispense(TWENTY) == true)) {
                // dispense $20 bill
                total = total.add(TWENTY);
            } else if ((total.add(TEN).compareTo(BigDecimal.ZERO) <= 0)  && (dispense(TEN) == true)) {
                // dispense $10 bill
                total = total.add(TEN);
            } else if ((total.add(FIVE).compareTo(BigDecimal.ZERO) <= 0) && (dispense(FIVE) == true)) {
                // dispense $5 bill
                total = total.add(FIVE);
            } else if ((total.add(TOONIE).compareTo(BigDecimal.ZERO) <= 0) && (dispense(TOONIE) == true)) {
                // dispense $2 coin
                total = total.add(TOONIE);
            } else if ((total.add(LOONIE).compareTo(BigDecimal.ZERO) <= 0) && (dispense(LOONIE) == true))  {
                // dispense $1 coin
                total = total.add(LOONIE);
            } else if ((total.add(QUARTER).compareTo(BigDecimal.ZERO) <= 0) && (dispense(QUARTER) == true)) {
                // dispense $0.25 coin
                total = total.add(QUARTER);
            } else if ((total.add(DIME).compareTo(BigDecimal.ZERO) <= 0) && (dispense(DIME) == true))  {
                // dispense $0.10 coin
                total = total.add(DIME);
            } else if ((total.add(NICKEL).compareTo(BigDecimal.ZERO) <= 0) && (dispense(NICKEL) == true))  {
                // dispense $0.05 coin
                total = total.add(NICKEL);
            } else {
            	if (total.compareTo(NICKEL) <= 0) {
            		// Indicates all dispensers are out of change, non-existent or not able to emit coin/banknote
                    // should never happen but may need to improve this else case
            		System.out.println("Unable to return change.");
            		break;
            	}
            	
            	else {
            		// Change to be returned is less than 5 cents
            		 System.out.println("Cancelling change value < 5 cents");
                     total = ZERO;
                     break;
            	}
            }
        }
        // money has been exchanged that corresponds exactly to the original total
        endPayment();
    }
    
    /**
     * dispense causes a banknote or coin to be emitted
     * @param value
     * If dispenser for that denomination is not hooked up to software, return false to indicate nothing emitted
     * If dispenser for that denomination is empty, return false to indicate nothing emitted
     * If dispenser for that denomination is disabled or overloaded, return false to indicate nothing emitted
     * Otherwise, return true if dispenser for that value successfuly emits the coin or banknote 
     * @return
     */
    public boolean dispense(BigDecimal value) {
    	
        // Banknote Dispensers
    	if (value.intValue() > 2) {
        	if (banknoteDispenserListeners.get(value.intValue()) == null) {
        		return false;
        	}
        		
        	if (banknoteDispenserListeners.get(value.intValue()).isEmpty()) {
        		return false;
        	}
        		
        	else {
        		// try to do it
        		try {
    				banknoteDispensers.get(value.intValue()).emit();
    				if (banknoteDispenserListeners.get(value.intValue()).isSucess()) {
    					// indicates successfully emitted
    					return true;
    				
    				} else {
    					
    					return false;
    					
    				}
    				
        		} catch (EmptyException e) {
        			return false;
    				
        		} catch (DisabledException d) {
        			return false;
        				
    			} catch (OverloadException e) {
    				return false;
    				// indicates output channel is overloaded, shouldn't happen 
    			}
        	}
        }
    	
    	// CoinDispesers 
    	else {
        	if (coinDispenserListeners.get(value) == null) {
        		return false;
        	}
        		
        	if (coinDispenserListeners.get(value).isEmpty()) {
        		return false;
        	}
        		
        	else {
        		// try to do it
        		try {
    				coinDispensers.get(value).emit();
    				if (coinDispenserListeners.get(value).isSucess()) {
    					// indicates successfully emitted
    					return true;
    				
    				} else {
    					
    					return false;
    					
    				}
    				
        		} catch (EmptyException e) {
        			return false;
    				
        		} catch (DisabledException d) {
        			return false;
        				
    			} catch (OverloadException e) {
    				return false;
    				// indicates output channel is overloaded, shouldn't happen 
    			}
        	}
        }
    }

	public static BigDecimal getGSTValue() {
		// TODO Auto-generated method stub
		return GST_VALUE;
	}
}
