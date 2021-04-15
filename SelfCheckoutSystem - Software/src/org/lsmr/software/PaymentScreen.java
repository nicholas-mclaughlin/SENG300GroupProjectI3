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
import javax.swing.border.LineBorder;

import org.lsmr.selfcheckout.devices.TouchScreen;

public class PaymentScreen {
	
	private JPanel screen;
	private JFrame frame;
	JPanel creditScreen;
	JPanel debitScreen;
	JPanel cashScreen;
	JPanel giftCardScreen;
	JPanel memberScreen;
	SoftwareController station;
	
	
	public PaymentScreen(int screenWidth, int screenHeight, JFrame frame, JPanel creditScreen, JPanel debitScreen, JPanel cashScreen, JPanel giftCardScreen, JPanel memberScreen, SoftwareController station) {
		
		this.frame = frame;
		this.creditScreen = creditScreen;
		this.debitScreen = debitScreen;
		this.cashScreen = cashScreen;
		this.giftCardScreen = giftCardScreen;
		this.memberScreen = memberScreen;
		this.station = station;
		
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
		JTextField items = new JTextField();
		JPanel total = new JPanel();
		JPanel finish = new JPanel();
		
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
		
			
		
		int rightPanelWidth = (int)(screenWidth*0.55);
		int rightPanelHeight = screenHeight;
		int rightPanelX = (int)(screenWidth*0.45);
		JPanel rightPanel = new JPanel();
		
		rightPanel.setBounds(rightPanelX, 0, rightPanelWidth, rightPanelHeight);
		screen.add(rightPanel);
		
		list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		Path currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/paymentOptions.jpg");
		String path = currentRelativePath.toAbsolutePath().toString();
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel paymentImage = new JLabel();
		int paymentImageWidth = (int)(rightPanelWidth*0.80);
		int paymentImageHeight = (int)(rightPanelHeight*0.44);
		
		Image sImage = img.getScaledInstance(paymentImageWidth, paymentImageHeight,
		        Image.SCALE_SMOOTH);
		paymentImage.setIcon(new ImageIcon(sImage));
		
		JLabel paymentMethodLabel = new JLabel("Please Choose a Payment method");
		paymentMethodLabel.setMinimumSize(new Dimension((int)(rightPanelWidth*0.55), (int)(rightPanelHeight*0.03)));
		paymentMethodLabel.setPreferredSize(new Dimension((int)(rightPanelWidth*0.55), (int)(rightPanelHeight*0.03)));
		paymentMethodLabel.setMaximumSize(new Dimension((int)(rightPanelWidth*0.55), (int)(rightPanelHeight*0.03)));
		paymentMethodLabel.setForeground(Color.gray);
		paymentMethodLabel.setFont(new Font("Serif", Font.PLAIN, (int)(rightPanelHeight*0.03)));
		
		
		JPanel selectionsPanels = new JPanel();
		selectionsPanels.setLayout(new GridLayout(2, 3));
		selectionsPanels.setMinimumSize(new Dimension((int)(rightPanelWidth*0.75), (int)(rightPanelHeight*0.43)));
		selectionsPanels.setPreferredSize(new Dimension((int)(rightPanelWidth*0.75), (int)(rightPanelHeight*0.43)));
		selectionsPanels.setMaximumSize(new Dimension((int)(rightPanelWidth*0.75), (int)(rightPanelHeight*0.43)));
		
		int selectionImageWidth = (int)(rightPanelWidth*0.17);
		int selectionImageHeight = (int)(rightPanelHeight*0.17);
		
		JPanel creditPanel = new JPanel();
		creditPanel.setLayout(new BoxLayout(creditPanel, BoxLayout.PAGE_AXIS));
		
		station.moveToPayment();
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/credit.jpg");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage cimg = null;
		try {
			cimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton creditImage = new JButton();
		creditImage.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	station.paymentController.setPaymentType(2); //payment type 2 denotes card
		    	 frame.getContentPane().removeAll();
		    	 frame.add(creditScreen);
		    	 frame.revalidate();
		    	 frame.repaint();
		    }
		});
		
		Image cImage = cimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		creditImage.setIcon(new ImageIcon(cImage));
		
		JLabel creditLabel = new JLabel("Credit Card");
		creditLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		creditPanel.add(creditImage);
		creditPanel.add(creditLabel);
		selectionsPanels.add(creditPanel);
		creditImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		creditLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		


		JPanel debitPanel = new JPanel();
		debitPanel.setLayout(new BoxLayout(debitPanel, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/debit.jpg");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage dimg = null;
		try {
			dimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton debitImage = new JButton();
		debitImage.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	station.paymentController.setPaymentType(2); //type 2 denotes card
		    	 frame.getContentPane().removeAll();
		    	 frame.add(debitScreen);
		    	 frame.revalidate();
		    	 frame.repaint();
		    }
		});
		
		Image bImage = dimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		debitImage.setIcon(new ImageIcon(bImage));
		
		JLabel debitLabel = new JLabel("Debit Card");
		debitLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		debitPanel.add(debitImage);
		debitPanel.add(debitLabel);

		selectionsPanels.add(debitPanel);
		debitImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		debitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		
		JPanel cashPanel = new JPanel();
		cashPanel.setLayout(new BoxLayout(cashPanel, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/cash.jpg");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage chimg = null;
		try {
			chimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton cashImage = new JButton();
		cashImage.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	 frame.getContentPane().removeAll();
		    	 frame.add(cashScreen);
		    	 frame.revalidate();
		    	 frame.repaint();
		    }
		});
		
		Image casImage = chimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		cashImage.setIcon(new ImageIcon(casImage));
		
		JLabel cashLabel = new JLabel("Pay With Cash");
		cashLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		cashPanel.add(cashImage);
		cashPanel.add(cashLabel);
		
		selectionsPanels.add(cashPanel);
		cashImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		cashLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		
		JPanel membershipPanel = new JPanel();
		membershipPanel.setLayout(new BoxLayout(membershipPanel, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/membership.jpg");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage mimg = null;
		try {
			mimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton membershipImage = new JButton();
		membershipImage.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	 frame.getContentPane().removeAll();
		    	 frame.add(memberScreen);
		    	 frame.revalidate();
		    	 frame.repaint();
		    }
		});
		
		Image memImage = mimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		membershipImage.setIcon(new ImageIcon(memImage));
		
		JLabel membershipLabel = new JLabel("MemberShip Card");
		membershipLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		membershipPanel.add(membershipImage);
		membershipPanel.add(membershipLabel);
		
		selectionsPanels.add(membershipPanel);
		membershipImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		membershipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		
		JPanel giftcardPanel = new JPanel();
		giftcardPanel.setLayout(new BoxLayout(giftcardPanel, BoxLayout.PAGE_AXIS));
		
		// Image from: <a href="http://www.freepik.com">Designed by stories / Freepik</a>
		currentRelativePath = Paths.get("SENG300GroupProjectI3-main/SelfCheckoutSystem - Software/src/org/lsmr/software/GUI_Images/giftcard.jpg");
		path = currentRelativePath.toAbsolutePath().toString();
		BufferedImage gcimg = null;
		try {
			gcimg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton giftcardImage = new JButton();
		giftcardImage.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	station.paymentController.setPaymentType(3);
		    	frame.getContentPane().removeAll();
		    	frame.add(giftCardScreen);
		    	frame.revalidate();
		    	frame.repaint();
		    }
		});
		
		Image gcImage = gcimg.getScaledInstance(selectionImageWidth, selectionImageHeight,
		        Image.SCALE_SMOOTH);
		giftcardImage.setIcon(new ImageIcon(gcImage));
		
		JLabel giftcardLabel = new JLabel("Gift Card");
		giftcardLabel.setFont(new Font("Serif", Font.BOLD, (int)(rightPanelHeight*0.02)));
		
		giftcardPanel.add(giftcardImage);
		giftcardPanel.add(giftcardLabel);
		
		selectionsPanels.add(giftcardPanel);
		giftcardImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		giftcardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		rightPanel.add(Box.createRigidArea(new Dimension((int)(rightPanelHeight*0.1), (int)(rightPanelHeight*0.06))));
		rightPanel.add(paymentImage);
		rightPanel.add(Box.createRigidArea(new Dimension(0, (int)(rightPanelHeight*0.02))));
		rightPanel.add(paymentMethodLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, (int)(rightPanelHeight*0.01))));
		rightPanel.add(selectionsPanels);
		
		paymentImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		paymentMethodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectionsPanels.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	public JPanel getScreen() {
		return screen;
	}


}
