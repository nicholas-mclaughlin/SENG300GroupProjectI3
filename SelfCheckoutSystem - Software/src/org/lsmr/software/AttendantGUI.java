package org.lsmr.software;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.TouchScreen;

public class AttendantGUI {

	private JFrame Attendantframe;
	private JFrame CustomerFrame;
	private SelfCheckoutStation station;
	private SoftwareController sc;
	
	public AttendantGUI(JFrame Customerframe, SelfCheckoutStation station, SoftwareController sc) {
		this.CustomerFrame = Customerframe;
		this.station = station;
		this.sc = sc;
		
		WeightDiscrepencyApproval wda = new WeightDiscrepencyApproval(station.scale);
		
		TouchScreen ts = new TouchScreen();
		JFrame frame = ts.getFrame();
		
		frame.setVisible(true);
		int frameWidth = frame.getWidth();
		int frameHeight = frame.getHeight();
		
		JPanel screen = new JPanel();
		JLabel label = new JLabel("Attendant Station", SwingConstants.CENTER);
		label.setFont(new Font("Serif", Font.BOLD, (int)(frameHeight*0.1)));
		label.setForeground(Color.BLUE);
		JPanel buttons = new JPanel();
		screen.add(Box.createRigidArea(new Dimension(0, (int)(frameHeight*0.1))));
		screen.add(label);
		screen.add(Box.createRigidArea(new Dimension(0, (int)(frameHeight*0.1))));
		screen.add(buttons);
		screen.add(Box.createRigidArea(new Dimension(0, (int)(frameHeight*0.1))));
		
		screen.setLayout(new BoxLayout(screen, BoxLayout.PAGE_AXIS));
		buttons.setLayout(new GridLayout(2, 4, (int)(frameHeight*0.02), (int)(frameWidth*0.02)));
		
		label.setMinimumSize(new Dimension(frameWidth, (int)(frameHeight*0.15)));
		label.setPreferredSize(new Dimension(frameWidth, (int)(frameHeight*0.15)));
		label.setMaximumSize(new Dimension(frameWidth, (int)(frameHeight*0.15)));
		buttons.setMinimumSize(new Dimension(frameWidth, (int)(frameHeight*0.55)));
		buttons.setPreferredSize(new Dimension(frameWidth, (int)(frameHeight*0.55)));
		buttons.setMaximumSize(new Dimension(frameWidth, (int)(frameHeight*0.55)));
		
		JButton approveWeightDisc = new JButton("Approve Weight Discrepancy");
		JButton removeProduct = new JButton("Remove Product from Purchases");
		JButton lookUp = new JButton("Look Up Product");
		JPanel empty = new JPanel();
		JButton startStation = new JButton("Start Station");
		JButton shutDownStation = new JButton("Shut Down Station");
		JButton blockStation = new JButton("Block Station");
		JButton logOut = new JButton("Log Out");
		
		buttons.add(approveWeightDisc);
		buttons.add(removeProduct);
		buttons.add(lookUp);
		buttons.add(empty);
		buttons.add(startStation);
		buttons.add(shutDownStation);
		buttons.add(blockStation);
		buttons.add(logOut);
		
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		frame.add(screen);
		
		
		// When the GUI first opens
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowOpened(WindowEvent we) {
		    	String name = JOptionPane.showInputDialog(frame,
                        "Enter Attendant ID", "1234567");
		    	String name2 = JOptionPane.showInputDialog(frame,
                        "Enter Pin", "1234");
		    }
		});
		
		
		// What happens when each button is clicked
		approveWeightDisc.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Which Customer Station?", null);
            	wda.approve();
            }
        });
		
		removeProduct.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Which Customer Station?", null);
            	String name2 = JOptionPane.showInputDialog(frame,
                        "Enter Product ID", null);
            	sc.getBaggingAreaController().startRemovingItem(0);
            }
        });
		
		lookUp.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name2 = JOptionPane.showInputDialog(frame,
                        "Enter Product ID", null);
            }
        });
		
		startStation.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Which Customer Station?", null);
            	Customerframe.setVisible(true);
            }
        });
		
		blockStation.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Which Customer Station?", null);
            	Customerframe.setVisible(false);
            }
        });
		
		shutDownStation.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Which Customer Station?", null);
            	Customerframe.setVisible(false);
            }
        });
		
		// Open new form taking attendant id and pin
		logOut.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String name = JOptionPane.showInputDialog(frame,
                        "Enter Attendant ID", "1234567");
		    	String name2 = JOptionPane.showInputDialog(frame,
                        "Enter Pin", "1234");
            }
        });
		
		
		frame.revalidate();
		frame.repaint();
		
		Attendantframe = frame;
		
		frame.setVisible(false);
	}

	public JFrame getFrame() {
		return Attendantframe;
	}
	
}
