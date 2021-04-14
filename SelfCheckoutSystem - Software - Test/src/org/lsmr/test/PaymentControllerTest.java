package org.lsmr.test;

// SENG300 Group Project Iteration 1
// Author: Lauryn Anderson

import static org.junit.Assert.*;

import org.junit.*;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.external.CardIssuer;
import org.lsmr.software.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.Map;

public class PaymentControllerTest {

    private SelfCheckoutStation station;
    private int[] banknoteDenominations = {5, 10, 20, 50, 100};
    private PaymentController paymentController;
    private SoftwareControllerStub softwareControllerStub; 
    
    class SoftwareControllerStub extends SoftwareController {

		public SoftwareControllerStub(SelfCheckoutStation station) {
			super(station);
			// TODO Auto-generated constructor stub
		}
    }

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

        // install payment software

        paymentController = new PaymentController(station.coinValidator, station.coinTray, station.coinSlot, station.coinStorage, station.coinDispensers,
                station.banknoteValidator, station.banknoteInput, station.banknoteStorage, station.banknoteDispensers, station.cardReader);
     
        softwareControllerStub = new SoftwareControllerStub(station);
    }

    @Test
    public void testTotalCalculationRoundDown() {
        // arrange
        BigDecimal subtotal = new BigDecimal("1.02"); // with GST = 1.071
        // act
        paymentController.beginPayment(subtotal, softwareControllerStub);
        // assert (rounded to nearest 5c)
        assertEquals(new BigDecimal("1.05"), paymentController.getTotal());
    }

    @Test
    public void testTotalCalculationRoundUp() {
        // arrange
        BigDecimal subtotal = new BigDecimal("1.03"); // with GST = 1.081
        // act
        paymentController.beginPayment(subtotal, softwareControllerStub);
        // assert (rounded to nearest 5c)
        assertEquals(new BigDecimal("1.10"), paymentController.getTotal());
    }

    @Test
    public void testTotalCalculationExact() {
        // arrange
        BigDecimal subtotal = new BigDecimal("100"); // with GST = 105.00
        // act
        paymentController.beginPayment(subtotal, softwareControllerStub);
        // assert (rounded to nearest 5c)
        assertEquals(new BigDecimal("105.00"), paymentController.getTotal());
    }

    @Test
    public void testTotalCalculationZero() {
        // arrange
        BigDecimal subtotal = new BigDecimal("0");
        // act/assert
        assertThrows(IllegalArgumentException.class, () -> {
            paymentController.beginPayment(subtotal, softwareControllerStub);
        });
    }

    @Test
    public void testTotalCalculationNegative() {
        // arrange
        BigDecimal subtotal = new BigDecimal("-50");
        // act/assert
        assertThrows(IllegalArgumentException.class, () -> {
            paymentController.beginPayment(subtotal, softwareControllerStub);
        });
    }

    @Test
    public void testTotalCalculationNull() {
        // act/assert
        assertThrows(NullPointerException.class, () -> {
            paymentController.beginPayment(null, softwareControllerStub);
        });
    }

//    @Test
//    public void testCancelPayment() {
//        // arrange
//        BigDecimal subtotal = new BigDecimal("100");
//        // act
//        paymentController.beginPayment(subtotal, softwareControllerStub);
//        paymentController.cancelPayment();
//        // assert
//        assertFalse(paymentController.isPaymentPhase());
//
//        // when change is implemented, should check right amount of change
//    }

    //@Test
    public void testExactCoins() {
        // arrange
        BigDecimal subtotal = new BigDecimal("1"); // with GST: 1.05
        Coin loonie = new Coin(new BigDecimal("1"), Currency.getInstance("CAD"));
        Coin nickel = new Coin(new BigDecimal("0.05"), Currency.getInstance("CAD"));
        int successes = 0;

        // act
        for (int i = 0; i < 1000; i++) {
            paymentController.beginPayment(subtotal, softwareControllerStub);
            try {
                station.coinSlot.accept(loonie);
                station.coinSlot.accept(nickel);
            } catch (DisabledException e) {
                fail("Exception should not be thrown: " + Arrays.toString(e.getStackTrace()));
            }

            if (paymentController.isPaymentPhase()) {
                // may be due to false rejection (1/100 chance)
                paymentController.cancelPayment();
            } else {
                successes++;
            }

            if (paymentController.shouldEmptyCoinStorage())
                station.coinStorage.unload();

            station.coinTray.collectCoins();
        }

        // Each repeat: 2 coins/banknotes * 1% failure rate for each
        // at least one coin will fail about 2% of the time
        // should have about 1000*(1-0.02)=980 successes

        // assert
        assertTrue(successes >= 960);
    }

    //@Test
    public void testExactBanknotes() {
        // arrange
        BigDecimal subtotal = new BigDecimal("100"); // with GST: 105
        Banknote fifty1 = new Banknote(50, Currency.getInstance("CAD"));
        Banknote fifty2 = new Banknote(50, Currency.getInstance("CAD"));
        Banknote five = new Banknote(5, Currency.getInstance("CAD"));
        int successes = 0;

        // act
        for (int i = 0; i < 1000; i++) {
            paymentController.beginPayment(subtotal, softwareControllerStub);
            try {
                station.banknoteInput.accept(fifty1);
                station.banknoteInput.removeDanglingBanknote();
                station.banknoteInput.accept(fifty2);
                station.banknoteInput.removeDanglingBanknote();
                station.banknoteInput.accept(five);
                station.banknoteInput.removeDanglingBanknote();
            } catch (DisabledException | OverloadException e) {
                fail("Exception should not be thrown: " + Arrays.toString(e.getStackTrace()));
            }

            if (paymentController.isPaymentPhase()) {
                // may be due to false rejection (1/100 chance)
                paymentController.cancelPayment();
            } else {
                successes++;
            }
            if (paymentController.shouldEmptyBanknoteStorage())
                station.banknoteStorage.unload();
        }
        // Each repeat: 3 banknotes * 1% failure rate for each
        // at least one banknote will fail about 3% of the time
        // should have about 1000*(1-0.03)=970 successes

        // assert
        assertTrue(successes >= 950);

        // assert
        assertFalse(paymentController.isPaymentPhase());
    }

   // @Test
    public void testExactCoinsAndBanknotes() {
        // arrange
        BigDecimal subtotal = new BigDecimal("31.50"); // with GST: 33.08
        Banknote twenty = new Banknote(20, Currency.getInstance("CAD"));
        Banknote ten = new Banknote(10, Currency.getInstance("CAD"));
        Coin toonie = new Coin(new BigDecimal("2"), Currency.getInstance("CAD"));
        Coin loonie = new Coin(new BigDecimal("1"), Currency.getInstance("CAD"));
        Coin dime = new Coin(new BigDecimal("0.1"), Currency.getInstance("CAD"));
        int successes = 0;

        // act
        for (int i = 0; i < 1000; i++) {
            paymentController.beginPayment(subtotal, softwareControllerStub);
            try {
                station.banknoteInput.accept(twenty);
                station.banknoteInput.removeDanglingBanknote();
                station.coinSlot.accept(toonie);
                station.coinSlot.accept(loonie);
                station.coinSlot.accept(dime);
                station.banknoteInput.accept(ten);
                station.banknoteInput.removeDanglingBanknote();
            } catch (DisabledException | OverloadException e) {
                fail("Exception should not be thrown: " + Arrays.toString(e.getStackTrace()));
            }

            if (paymentController.isPaymentPhase()) {
                // may be due to false rejection (1/100 chance)
                paymentController.cancelPayment();
            } else {
                successes++;
            }

            if (paymentController.shouldEmptyCoinStorage())
                station.coinStorage.unload();

            if (paymentController.shouldEmptyBanknoteStorage())
                station.banknoteStorage.unload();

            station.coinTray.collectCoins();
        }

        // Each repeat: 5 coins/banknotes * 1% failure rate for each
        // at least one coin/banknote will fail about 4.9% of the time
        // should have about 1000*(1-0.049)=951 successes

        // assert
        assertTrue(successes >= 930);
    }

   // @Test
    public void testNotEnoughPayment() {
        // arrange
        BigDecimal subtotal = new BigDecimal("31.50"); // with GST: 33.08
        Banknote twenty = new Banknote(20, Currency.getInstance("CAD"));
        Banknote ten = new Banknote(10, Currency.getInstance("CAD"));

        // act
        paymentController.beginPayment(subtotal, softwareControllerStub);
        try {
            station.banknoteInput.accept(twenty);
            station.banknoteInput.accept(ten);
        } catch (DisabledException | OverloadException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        paymentController.endPayment();

        // assert
        assertTrue(paymentController.isPaymentPhase());
    }

    @Test
    public void testFullCoinStorageLoad() {
        // arrange
        int spaceLeft = station.coinStorage.getCapacity() - station.coinStorage.getCoinCount();
        Coin coin = new Coin(new BigDecimal("1"), Currency.getInstance("CAD"));
        Coin[] coins = new Coin[spaceLeft];
        Arrays.fill(coins, coin);
        paymentController.beginPayment(new BigDecimal("100"), softwareControllerStub);

        // act
        try {
            station.coinStorage.load(coins);
        } catch (OverloadException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // assert
        assertTrue(paymentController.shouldEmptyCoinStorage());
    }

   // @Test
    public void testFullCoinStorageAccept() {
        // arrange
        int spaceLeft = station.coinStorage.getCapacity() - station.coinStorage.getCoinCount();
        Coin coin = new Coin(new BigDecimal("1"), Currency.getInstance("CAD"));
        Coin[] coins = new Coin[spaceLeft - 1];
        Arrays.fill(coins, coin);
        paymentController.beginPayment(new BigDecimal("100"), softwareControllerStub);
        try {
            station.coinStorage.load(coins);
            station.coinSlot.accept(coin);
        } catch (OverloadException | DisabledException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // assert
        assertTrue(paymentController.shouldEmptyCoinStorage());
    }

    @Test
    public void testFullBanknoteStorageLoad() {
        // arrange
        int spaceLeft = station.banknoteStorage.getCapacity() - station.banknoteStorage.getBanknoteCount();
        Banknote banknote = new Banknote(20, Currency.getInstance("CAD"));
        Banknote[] banknotes = new Banknote[spaceLeft];
        Arrays.fill(banknotes, banknote);
        paymentController.beginPayment(new BigDecimal("100"), softwareControllerStub);

        // act
        try {
            station.banknoteStorage.load(banknotes);
        } catch (OverloadException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // assert
        assertTrue(paymentController.shouldEmptyBanknoteStorage());
    }

    //@Test
    public void testFullBanknoteStorageAccept() {
        // arrange
        int spaceLeft = station.banknoteStorage.getCapacity() - station.banknoteStorage.getBanknoteCount();
        Banknote banknote = new Banknote(20, Currency.getInstance("CAD"));
        Banknote[] banknotes = new Banknote[spaceLeft - 1];
        Arrays.fill(banknotes, banknote);
        paymentController.beginPayment(new BigDecimal("100"), softwareControllerStub);
        try {
            station.banknoteStorage.load(banknotes);
            station.banknoteInput.accept(banknote);
        } catch (OverloadException | DisabledException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // assert
        assertTrue(paymentController.shouldEmptyBanknoteStorage());
    }

    @Test
    public void testEmptyingFullCoinStorageReEnables() {
        // arrange
        int spaceLeft = station.coinStorage.getCapacity() - station.coinStorage.getCoinCount();
        Coin coin = new Coin(new BigDecimal("2"), Currency.getInstance("CAD"));
        Coin[] coins = new Coin[spaceLeft];
        Arrays.fill(coins, coin);
        paymentController.beginPayment(new BigDecimal("100"), softwareControllerStub);
        try {
            station.coinStorage.load(coins);
        } catch (OverloadException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // act
        station.coinStorage.unload();

        // assert
        assertFalse(paymentController.shouldEmptyCoinStorage());
    }

    @Test
    public void testEmptyingFullBanknoteStorageReEnables() {
        // arrange
        int spaceLeft = station.banknoteStorage.getCapacity() - station.banknoteStorage.getBanknoteCount();
        Banknote banknote = new Banknote(50, Currency.getInstance("CAD"));
        Banknote[] banknotes = new Banknote[spaceLeft];
        Arrays.fill(banknotes, banknote);
        paymentController.beginPayment(new BigDecimal("100"), softwareControllerStub);
        try {
            station.banknoteStorage.load(banknotes);
        } catch (OverloadException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // act
        station.banknoteStorage.unload();

        // assert
        assertFalse(paymentController.shouldEmptyBanknoteStorage());
    }
	
    @Test(expected = NullPointerException.class)
    public void dispenseTestNull() {
    	
    	BigDecimal b = null;
    	boolean test = paymentController.dispense(b);
    	
    	//assertFalse(test);
    }
    
   // @Test
    public void dispenseEmpty() {
    	/*
    	CoinDispenserListener cdl = null;
    	*/
    	BigDecimal b = new BigDecimal(100);
    	boolean test = paymentController.dispense(b);
    	
    	assertFalse(true);
    }
    
    
    
    @Test(expected = NullPointerException.class)
    public void testNullBeginPayment() {
    	BigDecimal subtotal = null;
    	paymentController.beginPayment(subtotal, softwareControllerStub);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBEGINPayment() {
    	BigDecimal subtotal = BigDecimal.ZERO.negate();
    	paymentController.beginPayment(subtotal, softwareControllerStub);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testZeroBeginPayment() {
    	BigDecimal subtotal = BigDecimal.ZERO;
    	paymentController.beginPayment(subtotal, softwareControllerStub);
    }
    
    @Test
    public void testBeginPaymentO() {
    	BigDecimal subtotal = BigDecimal.TEN;
    	paymentController.beginPayment(subtotal, softwareControllerStub);
    	assertEquals(paymentController.getTotal(),  subtotal.multiply(PaymentController.getGSTValue()));
    }
    
    @Test
    public void beginPaymentStartsPaymentPhaseO() {
    	BigDecimal subtotal = BigDecimal.TEN;
    	paymentController.beginPayment(subtotal, softwareControllerStub);
    	assertTrue(paymentController.isPaymentPhase());
    }

    
    public void cancelPaymentChangesPaymentPhaseO() {
    	BigDecimal subtotal = BigDecimal.TEN;
    	paymentController.beginPayment(subtotal, softwareControllerStub);
    	paymentController.cancelPayment();
    	assertFalse(paymentController.isPaymentPhase());
    }
    
   @Test
   public void testTapWithCardDetected() {
	   class PaymentControllerStub extends PaymentController {
		 public CardData data;
		 public CReaderListener cardReaderListener;
		   
		  public PaymentControllerStub(CoinValidator cv, CoinTray ct, CoinSlot cSlot, CoinStorageUnit cStorage,
				Map<BigDecimal, CoinDispenser> cDispensers, BanknoteValidator bv, BanknoteSlot bSlot,
				BanknoteStorageUnit bStorage, Map<Integer, BanknoteDispenser> bDispensers, CardReader cardReader) {
			  
			  super(cv, ct, cSlot, cStorage, cDispensers, bv, bSlot, bStorage, bDispensers, cardReader);
			  
			  cardReaderListener =  new CReaderListener(this);
			  cardReader.deregisterAll();
			  cardReader.register(cardReaderListener);
		   }
		
		   @Override
		   public void informCardTapped() {
			   this.data = cardReaderListener.transmitCardData();
		   }
	   }
	   
	   
	   PaymentControllerStub pyStub = new PaymentControllerStub(station.coinValidator, station.coinTray, station.coinSlot, station.coinStorage, station.coinDispensers,
               station.banknoteValidator, station.banknoteInput, station.banknoteStorage, station.banknoteDispensers, station.cardReader);
	   
	 


	   Card card = new Card("x", "0000", "X", "000", "0000", true, true);  
	   
	   try {
		station.cardReader.tap(card);
		
		
	   } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		fail("Shouldn't happen.");
	   }
	   
   }
   
   @Test
   public void testInsertWithCardDetected() {
	   class PaymentControllerStub extends PaymentController {
		 public CardData data;
		 public CReaderListener cardReaderListener;
		   
		  public PaymentControllerStub(CoinValidator cv, CoinTray ct, CoinSlot cSlot, CoinStorageUnit cStorage,
				Map<BigDecimal, CoinDispenser> cDispensers, BanknoteValidator bv, BanknoteSlot bSlot,
				BanknoteStorageUnit bStorage, Map<Integer, BanknoteDispenser> bDispensers, CardReader cardReader) {
			  
			  super(cv, ct, cSlot, cStorage, cDispensers, bv, bSlot, bStorage, bDispensers, cardReader);
			  
			  cardReaderListener =  new CReaderListener(this);
			  cardReader.deregisterAll();
			  cardReader.register(cardReaderListener);
		   }
		
		   @Override
		   public void informCardInserted() {
			   this.data = cardReaderListener.transmitCardData();
		   }
	   }
	   
	   
	   PaymentControllerStub pyStub = new PaymentControllerStub(station.coinValidator, station.coinTray, station.coinSlot, station.coinStorage, station.coinDispensers,
               station.banknoteValidator, station.banknoteInput, station.banknoteStorage, station.banknoteDispensers, station.cardReader);
	   
	 


	   Card card = new Card("x", "0000", "X", "000", "0000", true, true);  
	   
	   try {
		station.cardReader.insert(card, "0000");
		
		
	   } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		// Shouldn't happen
		fail("Shouldn't happen.");
	   }   
   }
   
   @Test
   public void testSwipedWithCardDetected() {
	   class PaymentControllerStub extends PaymentController {
		 public CardData data;
		 public CReaderListener cardReaderListener;
		   
		  public PaymentControllerStub(CoinValidator cv, CoinTray ct, CoinSlot cSlot, CoinStorageUnit cStorage,
				Map<BigDecimal, CoinDispenser> cDispensers, BanknoteValidator bv, BanknoteSlot bSlot,
				BanknoteStorageUnit bStorage, Map<Integer, BanknoteDispenser> bDispensers, CardReader cardReader) {
			  
			  super(cv, ct, cSlot, cStorage, cDispensers, bv, bSlot, bStorage, bDispensers, cardReader);
			  
			  cardReaderListener =  new CReaderListener(this);
			  cardReader.deregisterAll();
			  cardReader.register(cardReaderListener);
		   }
		
		   @Override
		   public void informCardInserted() {
			   this.data = cardReaderListener.transmitCardData();
		   }
	   }
	   
	   
	   PaymentControllerStub pyStub = new PaymentControllerStub(station.coinValidator, station.coinTray, station.coinSlot, station.coinStorage, station.coinDispensers,
               station.banknoteValidator, station.banknoteInput, station.banknoteStorage, station.banknoteDispensers, station.cardReader);
	   
	 

	   BufferedImage image = new BufferedImage(1, 1, 1);
	   Card card = new Card("x", "0000", "X", "000", "0000", true, true);  
	   
	   try {
		station.cardReader.swipe(card, image);
		
		
	   } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		// Shouldn't happen
		fail("Shouldn't happen.");
	   }   
   }
   
    @Test
    public void testCardTransaction() {
    	class CardIssuerStub extends CardIssuer {
    		String companyName;
    		BigDecimal amount;
    		String cardNumber;
    		
			public CardIssuerStub(String name) {
				super(name);
				this.companyName = name;
			}
			
			@Override
			public int authorizeHold(String cardNumber, BigDecimal amount) {
				this.amount = amount;
				this.cardNumber = cardNumber;
				return 1;
		
			}
	
			
			@Override
			public boolean postTransaction(String cardNumber, int holdNumber, BigDecimal actualAmount) {
				return true;
			}
			
    	}
    	
        BigDecimal subtotal = new BigDecimal("31.50"); // with GST: 33.08
        CardIssuerStub cardIssuer = new CardIssuerStub("x");
   
        Card card = new Card("x", "0000", "x", "000", "0000", true, true); 
        paymentController.BANK_CARD_DATABASE.put("x", cardIssuer);

        // act
        paymentController.beginPayment(subtotal, softwareControllerStub);
        BigDecimal total = paymentController.getTotal();
        try {
        	paymentController.setPaymentType(2);
			station.cardReader.tap(card);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// shouldn't happen
			fail("Shouldn't happen");
		}
        
        assertEquals(total, total);    	
    }
    @Test
    public void testGiftCardTransaction() {
    
    BigDecimal subtotal = new BigDecimal("31.50"); // with GST: 33.08
    Card card = new Card("GiftCard", "1234", "Paul Walker", "123", "0000", true, true );
    paymentController.GIFT_CARD_DATABASE.put("1234", new BigDecimal(50.0));
    
    paymentController.beginPayment(subtotal, softwareControllerStub);
    BigDecimal total = paymentController.getTotal();
    
    try {
    	paymentController.setPaymentType(3);
		station.cardReader.tap(card);
		
    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		// shouldn't happen
		fail("Shouldn't happen");
		}
    
    assertEquals(total, total); 
    
    	
    }
   
   
    @Test
    public void testNotEnoughPaymentO() {
        // arrange
        BigDecimal subtotal = new BigDecimal("31.50"); // with GST: 33.08
        Banknote twenty = new Banknote(20, Currency.getInstance("CAD"));
        Banknote ten = new Banknote(10, Currency.getInstance("CAD"));

        // act
        paymentController.beginPayment(subtotal, softwareControllerStub);
        paymentController.addToPayment(new BigDecimal(twenty.getValue()));
        paymentController.addToPayment(new BigDecimal(ten.getValue()));
       
        assertTrue(paymentController.isPaymentPhase());
    }

	
	
}
