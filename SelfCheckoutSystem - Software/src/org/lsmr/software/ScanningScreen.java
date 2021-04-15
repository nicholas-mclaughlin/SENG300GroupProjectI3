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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.lsmr.selfcheckout.devices.TouchScreen;

public class ScanningScreen {
	
	private JPanel screen;
	private JFrame frame;
	private JPanel paymentScreen;
	private JPanel bagNumScreen;
	boolean haveBag = StartScreen.isHaveBag();
	private SoftwareController sc;
	
	public ScanningScreen(int screenWidth, int screenHeight, JFrame frame, JPanel bagNumScreen, SoftwareController sc ,JPanel paymentScreen) {
		this.sc = sc;
		this.frame = frame;
		this.paymentScreen = paymentScreen;
		this.bagNumScreen = bagNumScreen;
		
		screen = new JPanel();
		screen.setSize(screenWidth, screenHeight);
		screen.setLayout(null);

		int listWidth = (int)(screenWidth*0.45);
		int listHeight = screenHeight;
			
		JPanel list = new JPanel();
		list.setBounds(0, 0, listWidth, listHeight);
		screen.add(list);
		
		list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
		
		JPanel listPanel = new JPanel();
		
		JLabel yourShopping = new JLabel("Your Shopping");
		JPanel items = new JPanel();
		items.setLayout(new BoxLayout(items, BoxLayout.LINE_AXIS));
//		JTextArea items = new JTextArea();
		//items.setText("asdfghjklpoiuytrewasdfghjklmnbvcxzasdfghjkloiuytrewqasdfghjklmnbvcx");
		JPanel total = new JPanel();
		JPanel finish = new JPanel();
		
		JTextArea item = new JTextArea();
		JTextArea price = new JTextArea();
//		item.setBackground(Color.red);
//		price.setBackground(Color.blue);
		item.setMinimumSize(new Dimension((int)(listWidth*0.75), (int)(listHeight*0.6)));
		item.setPreferredSize(new Dimension((int)(listWidth*0.75), (int)(listHeight*0.6)));
		item.setMaximumSize(new Dimension((int)(listWidth*0.75), (int)(listHeight*0.6)));
		price.setMinimumSize(new Dimension((int)(listWidth*0.75), (int)(listHeight*0.6)));
		price.setPreferredSize(new Dimension((int)(listWidth*0.75), (int)(listHeight*0.6)));
		price.setMaximumSize(new Dimension((int)(listWidth*0.75), (int)(listHeight*0.6)));
		items.add(item);
		items.add(price);
		
		yourShopping.setOpaque(true);
		yourShopping.setBackground(Color.GRAY);
		total.setBackground(Color.GRAY);
		
		
		yourShopping.setMinimumSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.05)));
		yourShopping.setPreferredSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.05)));
		yourShopping.setMaximumSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.05)));
		items.setMinimumSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.6)));
		items.setPreferredSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.6)));
		items.setMaximumSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.6)));
		total.setMinimumSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.1)));
		total.setPreferredSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.1)));
		total.setMaximumSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.1)));
		finish.setMinimumSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.15)));
		finish.setPreferredSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.15)));
		finish.setMaximumSize(new Dimension((int)(listWidth*0.95), (int)(listHeight*0.15)));
		
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
		listPanel.add(yourShopping);
		listPanel.add(items);
		listPanel.add(total);
		listPanel.setBorder(new LineBorder(Color.gray, 8, true));
		
		yourShopping.setForeground(Color.white);
		yourShopping.setFont(new Font("Serif", Font.BOLD, (int)(listHeight*0.04)));
		list.add(Box.createRigidArea(new Dimension(0, (int)(listHeight*0.06))));
		list.add(listPanel);
		list.add(Box.createRigidArea(new Dimension(0, (int)(listHeight*0.02))));
		list.add(finish);
		list.add(Box.createRigidArea(new Dimension(0, (int)(listHeight*0.01))));
		yourShopping.setAlignmentX(Component.CENTER_ALIGNMENT);
		items.setAlignmentX(Component.CENTER_ALIGNMENT);
		total.setAlignmentX(Component.CENTER_ALIGNMENT);
		finish.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		total.setLayout(new BoxLayout(total, BoxLayout.LINE_AXIS));
		JLabel totalLabel = new JLabel("Total:");
		JTextField totalAmount = new JTextField();
		totalLabel.setForeground(Color.white);
		totalLabel.setFont(new Font("Serif", Font.BOLD, (int)(listHeight*0.04)));
		
		total.add(Box.createRigidArea(new Dimension((int)(listWidth*0.03), 0)));
		total.add(totalLabel);
		total.add(Box.createRigidArea(new Dimension((int)(listWidth*0.06), 0)));
		total.add(totalAmount);
		totalLabel.setMinimumSize(new Dimension((int)(listWidth*0.15), (int)(listHeight*0.08)));
		totalLabel.setPreferredSize(new Dimension((int)(listWidth*0.15), (int)(listHeight*0.08)));
		totalLabel.setMaximumSize(new Dimension((int)(listWidth*0.15), (int)(listHeight*0.08)));
		totalAmount.setMinimumSize(new Dimension((int)(listWidth*0.68), (int)(listHeight*0.08)));
		totalAmount.setPreferredSize(new Dimension((int)(listWidth*0.68), (int)(listHeight*0.08)));
		totalAmount.setMaximumSize(new Dimension((int)(listWidth*0.68), (int)(listHeight*0.08)));
		
		
		finish.setLayout(new BoxLayout(finish, BoxLayout.LINE_AXIS));
		JButton finishButton = new JButton("Finish and Pay");
		
		finishButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	 if(haveBag == true) {
		    		 //System.out.print(haveBag);
		    		 frame.getContentPane().removeAll();
			    	 frame.add(paymentScreen);
			    	 frame.revalidate();
			    	 frame.repaint();
		    	 }
		    	 else {
		    		 //System.out.print(haveBag);
		    		 frame.getContentPane().removeAll();
			    	 frame.add(bagNumScreen);
			    	 frame.revalidate();
			    	 frame.repaint();
		    	 }
		    	 
		    }
		});
		
		finish.add(Box.createHorizontalGlue());
		finish.add(finishButton);
		finishButton.setMinimumSize(new Dimension((int)(listWidth*0.6), (int)(listHeight*0.13)));
		finishButton.setPreferredSize(new Dimension((int)(listWidth*0.6), (int)(listHeight*0.13)));
		finishButton.setMaximumSize(new Dimension((int)(listWidth*0.6), (int)(listHeight*0.13)));
		finishButton.setBackground(Color.GREEN);
		finishButton.setBorder(new LineBorder(Color.GREEN, 1, true));
		finishButton.setFont(new Font("Arial", Font.PLAIN, (int)(listHeight*0.07)));
		finishButton.setFont(new Font("Arial", Font.PLAIN, 60));
		
		
		
		int rightPanelWidth = (int)(screenWidth*0.55);
		int rightPanelHeight = screenHeight;
		int rightPanelX = (int)(screenWidth*0.45);
		JPanel rightPanel = new JPanel();
		
		rightPanel.setBounds(rightPanelX, 0, rightPanelWidth, rightPanelHeight);
		screen.add(rightPanel);
		
		list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		Path currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/scanning.jpg");
		String path = currentRelativePath.toAbsolutePath().toString();
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel scanningImage = new JLabel();
		int scanningImageWidth = (int)(rightPanelWidth*0.55);
		int scanningImageHeight = (int)(rightPanelHeight*0.44);
		
		Image sImage = img.getScaledInstance(scanningImageWidth, scanningImageHeight,
		        Image.SCALE_SMOOTH);
		scanningImage.setIcon(new ImageIcon(sImage));
		
		JLabel scanItemLabel = new JLabel("Please Scan an Item");
		scanItemLabel.setMinimumSize(new Dimension((int)(rightPanelWidth*0.55), (int)(rightPanelHeight*0.03)));
		scanItemLabel.setPreferredSize(new Dimension((int)(rightPanelWidth*0.55), (int)(rightPanelHeight*0.03)));
		scanItemLabel.setMaximumSize(new Dimension((int)(rightPanelWidth*0.55), (int)(rightPanelHeight*0.03)));
		scanItemLabel.setForeground(Color.gray);
		scanItemLabel.setFont(new Font("Serif", Font.PLAIN, (int)(rightPanelHeight*0.03)));
		
		
		JPanel selectionsPanels = new JPanel();
		selectionsPanels.setLayout(new GridLayout(2, 3));
		selectionsPanels.setMinimumSize(new Dimension((int)(rightPanelWidth*0.75), (int)(rightPanelHeight*0.43)));
		selectionsPanels.setPreferredSize(new Dimension((int)(rightPanelWidth*0.75), (int)(rightPanelHeight*0.43)));
		selectionsPanels.setMaximumSize(new Dimension((int)(rightPanelWidth*0.75), (int)(rightPanelHeight*0.43)));
		
		int selectionImageWidth = (int)(rightPanelWidth*0.17);
		int selectionImageHeight = (int)(rightPanelHeight*0.17);
		
		JPanel fruitsPanel = new JPanel();
		fruitsPanel.setLayout(new BoxLayout(fruitsPanel, BoxLayout.PAGE_AXIS));
		
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/banana.jpg");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage fimg = null;
		try {
			fimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton fruitsImage = new JButton();
		
		Image fImage = fimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		fruitsImage.setIcon(new ImageIcon(fImage));
		
		JLabel fruitsLabel = new JLabel("Fruits and Vegetables");
		
		fruitsLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		fruitsPanel.add(fruitsImage);
		fruitsPanel.add(fruitsLabel);
		selectionsPanels.add(fruitsPanel);
		fruitsImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		fruitsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		fruitsImage.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Please enter barcode?", null);
            }
        });
		

		JPanel bakeryPanel = new JPanel();
		bakeryPanel.setLayout(new BoxLayout(bakeryPanel, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/bread.jpg");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton bakeryImage = new JButton();
		
		Image bImage = bimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		bakeryImage.setIcon(new ImageIcon(bImage));
		
		JLabel bakeryLabel = new JLabel("Bakery");
		
		bakeryLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		bakeryPanel.add(bakeryImage);
		bakeryPanel.add(bakeryLabel);

		selectionsPanels.add(bakeryPanel);
		bakeryImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		bakeryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		bakeryImage.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Please enter barcode?", null);
            }
        });
		
		
		JPanel otherPanel = new JPanel();
		otherPanel.setLayout(new BoxLayout(otherPanel, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/qmark.png");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage oimg = null;
		try {
			oimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton otherImage = new JButton();
		
		Image oImage = oimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		otherImage.setIcon(new ImageIcon(oImage));
		
		JLabel otherLabel = new JLabel("Other");
		otherLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		otherPanel.add(otherImage);
		otherPanel.add(otherLabel);
		
		selectionsPanels.add(otherPanel);
		otherImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		otherLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		otherImage.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Please enter barcode?", null);
            }
        });
		
		
		JPanel barcodePanel = new JPanel();
		barcodePanel.setLayout(new BoxLayout(barcodePanel, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/barcode.jpg");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage barimg = null;
		try {
			barimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton barcodeImage = new JButton();
		
		Image barImage = barimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		barcodeImage.setIcon(new ImageIcon(barImage));
		
		JLabel barcodeLabel = new JLabel("Enter Barcode");
		barcodeLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		barcodePanel.add(barcodeImage);
		barcodePanel.add(barcodeLabel);
		
		selectionsPanels.add(barcodePanel);
		barcodeImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		barcodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		barcodeImage.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Please enter barcode?", null);
            }
        });
		
		
		JPanel bagPanel = new JPanel();
		bagPanel.setLayout(new BoxLayout(bagPanel, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/bag.jpg");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage bagimg = null;
		try {
			bagimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton bagImage = new JButton();
		
		Image baImage = bagimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		bagImage.setIcon(new ImageIcon(baImage));
		
		JLabel bagLabel = new JLabel("Add Own Bag");
		bagLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		bagPanel.add(bagImage);
		bagPanel.add(bagLabel);
		
		selectionsPanels.add(bagPanel);
		bagImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		bagLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		bagImage.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Object[] options = { "DONE"};
                int name = JOptionPane.showOptionDialog(null, "Click DONE when finished", "Add Own Bags to Bagging Area",
                		JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION,
                		null, options, options[0]);
            }
        });
		
		
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		rightPanel.add(Box.createRigidArea(new Dimension(0, (int)(rightPanelHeight*0.06))));
		rightPanel.add(scanningImage);
		rightPanel.add(Box.createRigidArea(new Dimension(0, (int)(rightPanelHeight*0.02))));
		rightPanel.add(scanItemLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, (int)(rightPanelHeight*0.01))));
		rightPanel.add(selectionsPanels);
		
		scanningImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		scanItemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectionsPanels.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	public JPanel getScreen() {
		return screen;
	}
}
