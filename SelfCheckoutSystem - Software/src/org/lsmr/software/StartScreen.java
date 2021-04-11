package org.lsmr.software;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

public class StartScreen {

	private JPanel screen;
	private JPanel scanningScreen;
	private JFrame frame;
	
	public StartScreen(int screenWidth, int screenHeight, JFrame frame, JPanel scanningScreen) {
		this.frame = frame;
		this.scanningScreen = scanningScreen;
		
		screen = new JPanel();

		screen.setSize(screenWidth, screenHeight);

		screen.setLayout(null);
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		Path currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/entry.jpg");
		String path = currentRelativePath.toAbsolutePath().toString();
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel leftLabel = new JLabel();
		
		
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		
		int leftPanelWidth = screenWidth/2;
		int leftPanelHeight = screenHeight;
		int rightPanelWidth = screenWidth/2;
		int rightPanelHeight = screenHeight;
		int rightPanelX = (int)screenWidth/2;
		
		leftPanel.setBounds(0, 0, leftPanelWidth, leftPanelHeight);
		rightPanel.setBounds(rightPanelX, 0, rightPanelWidth, rightPanelHeight);
		
		screen.add(leftPanel);
		screen.add(rightPanel);

		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.add(Box.createVerticalGlue());
		leftPanel.add(leftLabel);
		leftPanel.add(Box.createVerticalGlue());
		leftLabel.setMinimumSize(new Dimension(leftPanelWidth, leftPanelHeight));
		leftLabel.setPreferredSize(new Dimension(leftPanelWidth, leftPanelHeight));
		leftLabel.setMaximumSize(new Dimension(leftPanelWidth, leftPanelHeight));
		leftLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Image image = img.getScaledInstance(leftPanelWidth, leftPanelHeight,
		        Image.SCALE_SMOOTH);
		leftLabel.setIcon(new ImageIcon(image));
		
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		
		JLabel openLabel = new JLabel("Lane Open.");
		
		openLabel.setForeground(Color.BLUE);
		openLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.1)));
		
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	 frame.getContentPane().removeAll();
		    	 frame.add(scanningScreen);
		    	 frame.revalidate();
		    	 frame.repaint();
		    }
		});
		
		JButton ownBaggingButton = new JButton("Start - I'm using my own bags");
		ownBaggingButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	 frame.getContentPane().removeAll();
		    	 frame.add(scanningScreen);
		    	 frame.revalidate();
		    	 frame.repaint();
		    }
		});
		
		rightPanel.add(Box.createRigidArea(new Dimension(0, (int)(rightPanelHeight*0.25))));
		rightPanel.add(openLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(0,(int)(rightPanelHeight*0.1))));
		rightPanel.add(startButton);
		rightPanel.add(Box.createRigidArea(new Dimension(0,(int)(rightPanelHeight*0.05))));
		rightPanel.add(ownBaggingButton);
		
		openLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		ownBaggingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		startButton.setMinimumSize(new Dimension((int)(rightPanelWidth*0.5), (int)(rightPanelHeight*0.15)));
		startButton.setPreferredSize(new Dimension((int)(rightPanelWidth*0.5), (int)(rightPanelHeight*0.15)));
		startButton.setMaximumSize(new Dimension((int)(rightPanelWidth*0.5), (int)(rightPanelHeight*0.15)));
		ownBaggingButton.setMinimumSize(new Dimension((int)(rightPanelWidth*0.5), (int)(rightPanelHeight*0.15)));
		ownBaggingButton.setPreferredSize(new Dimension((int)(rightPanelWidth*0.5), (int)(rightPanelHeight*0.15)));
		ownBaggingButton.setMaximumSize(new Dimension((int)(rightPanelWidth*0.5), (int)(rightPanelHeight*0.15)));
		
		startButton.setFont(new Font("Arial", Font.PLAIN, (int)(rightPanelHeight*0.03)));
		ownBaggingButton.setFont(new Font("Arial", Font.PLAIN, (int)(rightPanelHeight*0.03)));
	}
	
	public JPanel getScreen() {
		return screen;
	}
}
