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
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.devices.TouchScreen;


public class GUI {
	public SoftwareController softwareController;
	public ScanController scanController;
	public DatabaseController databaseController;
	public Purchase currentPurchase;
	public BaggingAreaController baggingAreaController;

	public static void main(String[] args) {
		TouchScreen ts = new TouchScreen();
		JFrame frame = ts.getFrame();
		frame.setVisible(true);
		int frameWidth = frame.getWidth();
		int frameHeight = frame.getHeight();
		
		ScanningScreen scanning = new ScanningScreen(frameWidth, frameHeight, frame);
		JPanel scanningScreen = scanning.getScreen();
		
		StartScreen start = new StartScreen(frameWidth, frameHeight, frame, scanningScreen);
		JPanel startScreen = start.getScreen();
		
		
		frame.add(startScreen);
		
		frame.revalidate();
		frame.repaint();
	}
	
	public class TSListener implements TouchScreenListener {

		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
			// TODO Auto-generated method stub
		}
		
		
		// If the customer enters the product look up code on the touch screen, the listener should be notified
		public void notifyProductCodeEntered(String code) {
			PriceLookupCode productCode = null;
			try {
				productCode = new PriceLookupCode(code);
				
				if(scanController.isScanning()) {
					if(scanController.isItemScanning()) {
						double expectedWeight = databaseController.getExpectedWeight(productCode);
						if (expectedWeight == -1) return; // pluproduct code was not in database
						PLUCodedProduct currentProduct = databaseController.getpluCodedProduct(productCode);
						if (currentProduct == null) return; // pluproduct was not in database
						currentPurchase.addItem(currentProduct);
						scanController.blockNextScan();
						baggingAreaController.startBaggingItem(expectedWeight);
					}
				}
			} catch (Exception e) {
				// Should inform user to enter valid code
			}
		}
		
	}

}
