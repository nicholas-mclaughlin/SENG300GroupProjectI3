package org.lsmr.software;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Currency;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.TouchScreen;


public class GUI {
	
	public final static Currency currency = Currency.getInstance(Locale.CANADA);
    public final static int[] banknoteDenominations = {5,10,20,50,100};
    public final static BigDecimal[] coinDenominations = {new BigDecimal("0.05"),new BigDecimal("0.1"),
    new BigDecimal("0.25"), new BigDecimal("1"), new BigDecimal("2")};
    private static int scaleMaximumWeight = 50000;// just a meaningless magic number. Please fix - Randomly set limit to 50kg
    private static int scaleSensitivity = 50; // just a meaningless magic number. Please fix - Randomly set sensitivity to 50g
    
    
   
	public static void main(String[] args) {
		TouchScreen ts = new TouchScreen();
		JFrame frame = ts.getFrame();
		frame.setVisible(true);
		int frameWidth = frame.getWidth();
		int frameHeight = frame.getHeight();
		
		
		SelfCheckoutStation station = new SelfCheckoutStation(currency,banknoteDenominations,coinDenominations,scaleMaximumWeight,scaleSensitivity);
		SoftwareController sc = new SoftwareController(station);
		
		ExitScreen exit = new ExitScreen(frameWidth, frameHeight, frame);
		JPanel exitScreen = exit.getScreen();
		
		CreditScreen credit = new CreditScreen(frameWidth, frameHeight, frame, exitScreen, sc);		
		JPanel creditScreen = credit.getScreen();
		
		DebitScreen debit = new DebitScreen(frameWidth, frameHeight, frame, exitScreen, sc);
		JPanel debitScreen = debit.getScreen();
		
		CashScreen cash = new CashScreen(frameWidth, frameHeight, frame, exitScreen, sc);
		JPanel cashScreen = cash.getScreen();
		
		GiftCardScreen gift = new GiftCardScreen(frameWidth, frameHeight, frame, exitScreen,sc);
		JPanel giftScreen = gift.getScreen();
		
		MembershipScreen member = new MembershipScreen(frameWidth, frameHeight, frame, exitScreen,sc);
		JPanel memberScreen = member.getScreen();
		

		
		PaymentScreen payment = new PaymentScreen(frameWidth, frameHeight, frame, creditScreen, debitScreen, cashScreen, giftScreen, memberScreen,sc);
		JPanel paymentScreen = payment.getScreen();
		
		BagNumberScreen bagScreen = new BagNumberScreen(frameWidth, frameHeight, frame,  paymentScreen);
		JPanel bsScreen = bagScreen.getScreen();
	
		ScanningScreen scanning = new ScanningScreen(frameWidth, frameHeight, frame, bsScreen, sc,paymentScreen);
		JPanel scanningScreen = scanning.getScreen();
		
		StartScreen start = new StartScreen(frameWidth, frameHeight, frame, scanningScreen);
		JPanel startScreen = start.getScreen();

		
		
		frame.add(startScreen);
		
		frame.revalidate();
		frame.repaint();
	}

}
