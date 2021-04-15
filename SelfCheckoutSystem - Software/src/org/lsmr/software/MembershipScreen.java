package org.lsmr.software;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MembershipScreen {
	
	private JPanel screen;
	private JPanel exitScreen;
	private JFrame frame;
	SoftwareController station;
	
	public MembershipScreen(int screenWidth, int screenHeight, JFrame frame, JPanel exitScreen, SoftwareController station) {
		this.frame = frame;
		this.exitScreen = exitScreen;
		this.station = station;
		
		screen = new JPanel();

		screen.setSize(screenWidth, screenHeight);

		screen.setLayout(null);
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		Path currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/giftscreen.jpg");
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
		
		JLabel bagLabel = new JLabel("Please Enter the Membership Card Number");
	
		//check validity of membership
		
		MembershipCardController mcControl = station.getMembershipCardController();
		mcControl.beginScanningPhase();
		mcControl.barcodeScanner.scan();
		String membership = null;
		mcControl.beginTypingPhase();
		if (membership != null)
			mcControl.typedMembershipValidity(membership);
		
		
		bagLabel.setForeground(Color.BLUE);
		bagLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.045)));
		
		
		JTextField bagNum = new JTextField();
		bagNum.setHorizontalAlignment(SwingConstants.CENTER);
		bagNum.setFont(new Font("Serif", Font.PLAIN, (int)(rightPanelHeight*0.05)));
		
		bagNum.setMinimumSize(new Dimension((int)(rightPanelWidth*0.35), (int)(rightPanelHeight*0.08)));
		bagNum.setPreferredSize(new Dimension((int)(rightPanelWidth*0.35), (int)(rightPanelHeight*0.08)));
		bagNum.setMaximumSize(new Dimension((int)(rightPanelWidth*0.35), (int)(rightPanelHeight*0.08)));
		
		
		JPanel numPanel = new JPanel();
				
		numPanel.setLayout(new GridLayout(4,3));
		
		numPanel.setMinimumSize(new Dimension((int)(rightPanelWidth*0.35), (int)(rightPanelHeight*0.30)));
		numPanel.setPreferredSize(new Dimension((int)(rightPanelWidth*0.35), (int)(rightPanelHeight*0.30)));
		numPanel.setMaximumSize(new Dimension((int)(rightPanelWidth*0.35), (int)(rightPanelHeight*0.30)));
		
		
		
		
		
		
		JButton button;
		
		JButton button1 =  new JButton();
		
		button1.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "1");
		    }
		});
		
		
		JButton button2 =  new JButton();
		
		button2.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "2");
		    }
		});
		
		JButton button3 =  new JButton();
		
		button3.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "3");
		    }
		});
		
		JButton button4 =  new JButton();
		
		button4.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "4");
		    }
		});
		
		JButton button5 =  new JButton();
		
		button5.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "5");
		    }
		});
		
		JButton button6 =  new JButton();
		button6.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "6");
		    }
		});
		
		JButton button7 =  new JButton();
		button7.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "7");
		    }
		});
		
		JButton button8 =  new JButton();
		button8.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "8");
		    }
		});
		
		JButton button9 =  new JButton();
		button9.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "9");
		    }
		});
		
		JButton button0 =  new JButton();
		button0.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText(bagNum.getText() + "0");
		    }
		});
		
		JButton buttonClear =  new JButton();
		buttonClear.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    		bagNum.setText("");
		    }
		});
		
		JButton buttonEnter =  new JButton();

		buttonEnter.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	 String result = new String(bagNum.getText());	
		    	 frame.getContentPane().removeAll();
		    	 frame.add(exitScreen);
		    	 frame.revalidate();
		    	 frame.repaint();
		    }
		});
		
		Font newButtonFont=new Font(button1.getFont().getName(),button1.getFont().getStyle(),(int)(rightPanelHeight*0.05));
		Font smallButtonFont=new Font(buttonClear.getFont().getName(),buttonClear.getFont().getStyle(),(int)(rightPanelHeight*0.02));

		
		button1.setFont(newButtonFont);
		button2.setFont(newButtonFont);
		button3.setFont(newButtonFont);
		button4.setFont(newButtonFont);
		button5.setFont(newButtonFont);
		button6.setFont(newButtonFont);
		button7.setFont(newButtonFont);
		button8.setFont(newButtonFont);
		button9.setFont(newButtonFont);
		button0.setFont(newButtonFont);
		buttonClear.setFont(smallButtonFont);
		buttonEnter.setFont(smallButtonFont);
		
		button1.setBounds(150, 50, 100, 60);
		button2.setBounds(150, 50, 100, 60);
		button3.setBounds(150, 50, 100, 60);
		button4.setBounds(150, 50, 100, 60);
		button5.setBounds(150, 50, 100, 60);
		button6.setBounds(150, 50, 100, 60);
		button7.setBounds(150, 50, 100, 60);
		button8.setBounds(150, 50, 100, 60);
		button9.setBounds(150, 50, 100, 60);
		button0.setBounds(150, 50, 100, 60);
		buttonClear.setBounds(150, 50, 100, 60);
		buttonEnter.setBounds(150, 50, 100, 60);
		
		button1.setPreferredSize(new Dimension(80,80));
		button2.setPreferredSize(new Dimension(80,80));
		button3.setPreferredSize(new Dimension(80,80));
		button4.setPreferredSize(new Dimension(80,80));
		button5.setPreferredSize(new Dimension(80,80));
		button6.setPreferredSize(new Dimension(80,80));
		button7.setPreferredSize(new Dimension(80,80));
		button8.setPreferredSize(new Dimension(80,80));
		button9.setPreferredSize(new Dimension(80,80));
		button0.setPreferredSize(new Dimension(80,80));
		buttonClear.setPreferredSize(new Dimension(80,80));
		buttonEnter.setPreferredSize(new Dimension(80,80));
		
		button1.setText("1");
		button2.setText("2");
		button3.setText("3");
		button4.setText("4");
		button5.setText("5");
		button6.setText("6");
		button7.setText("7");
		button8.setText("8");
		button9.setText("9");
		button0.setText("0");
		buttonClear.setText("Clear");
		buttonEnter.setText("Enter");	
				
		numPanel.add(button1);
		numPanel.add(button2);
		numPanel.add(button3);
		numPanel.add(button4);
		numPanel.add(button5);
		numPanel.add(button6);
		numPanel.add(button7);
		numPanel.add(button8);
		numPanel.add(button9);
		numPanel.add(buttonClear);
		numPanel.add(button0);
		numPanel.add(buttonEnter);		
		
		
//		JButton startButton = new JButton("Start");
//		startButton.addActionListener(new ActionListener() {
//		    @Override
//		    public void actionPerformed(ActionEvent e) {
//		    	 frame.getContentPane().removeAll();
//		    	 frame.add(paymentScreen);
//		    	 frame.revalidate();
//		    	 frame.repaint();
//		    }
//		});
//		
//		JButton ownBaggingButton = new JButton("Start - I'm using my own bags");
//		ownBaggingButton.addActionListener(new ActionListener() {
//		    @Override
//		    public void actionPerformed(ActionEvent e) {
//		    	 frame.getContentPane().removeAll();
//		    	 frame.add(paymentScreen);
//		    	 frame.revalidate();
//		    	 frame.repaint();
//		    }
//		});
		
		rightPanel.add(Box.createRigidArea(new Dimension(0, (int)(rightPanelHeight*0.25))));
		rightPanel.add(bagLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, (int)(rightPanelHeight*0.15))));
		
		rightPanel.add(bagNum);		
		rightPanel.add(numPanel);
		rightPanel.add(Box.createRigidArea(new Dimension(0,(int)(rightPanelHeight*0.30))));
		
		
		bagLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		bagNum.setAlignmentX(Component.CENTER_ALIGNMENT);
		numPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
	}
	
	public JPanel getScreen() {
		return screen;
	}

}
