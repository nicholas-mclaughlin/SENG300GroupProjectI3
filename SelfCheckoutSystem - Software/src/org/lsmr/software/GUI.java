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

}
