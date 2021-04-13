
package org.lsmr.software;
import org.lsmr.selfcheckout.devices.Acceptor;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.BanknoteSlot;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.BidirectionalChannel;
import org.lsmr.selfcheckout.devices.CardReader;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.CoinTray;
import org.lsmr.selfcheckout.devices.CoinValidator;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import a3Test.BaggingAreaController;
import a3Test.DatabaseController;
import a3Test.PaymentController;
import a3Test.ScanController;

//Class to block a self checkout station
// functionality to disable and enable all devices for that specific station

public class BlockStation {
	
	private SelfCheckoutStation sc;
	private BanknoteDispenser bnd;
	private BanknoteSlot bns;
	private BanknoteStorageUnit bnsu; 
	private BanknoteValidator bnv; 
	private BarcodeScanner bcs;
	private CardReader cr;
	private CoinDispenser cd;
	private CoinSlot cs;
	private CoinStorageUnit csu;
	private CoinTray ct; 
	private CoinValidator cv;
	private ElectronicScale es;
	private ReceiptPrinter rp;
	private ScanController scanController;
    private BaggingAreaController baggingAreaController;
    private DatabaseController databaseController;
    private PaymentController paymentController;

	public BlockStation(SelfCheckoutStation sc, BanknoteDispenser bnd, Acceptor a, BanknoteSlot bns, BanknoteStorageUnit bnsu, BanknoteValidator bnv, 
			BarcodeScanner bcs, BidirectionalChannel bic, CardReader cr, CoinDispenser cd, CoinSlot cs, CoinStorageUnit csu, CoinTray ct, 
			CoinValidator cv, ElectronicScale es, ReceiptPrinter rp, ScanController scanController, BaggingAreaController baggingAreaController, DatabaseController
			databaseController, PaymentController paymentController) {
		
			this.sc = sc;
			this.bnd = bnd;
			this.bns = bns;
			this.bnsu = bnsu;
			this.bnv = bnv;
			this.bcs = bcs;
			this.cr = cr;
			this.cd =  cd;
			this.cs = cs;
			this.csu = csu;
			this.ct = ct;
			this.cv = cv;
			this.es = es;
			this.ee = ee;	// ee cannot to resolved or is not a field
			this.fte = fte;	// fte cannot to resolved or is not a field
			this.fse = fse;	// fse cannot to resolved or is not a field
			this.oe = oe;	// ee cannot to resolved or is not a field
			this.rp = rp;
			this.se = se;	// se cannot to resolved or is not a field
			this.ts = ts;	// ts cannot to resolved or is not a field
			this.udc = udc;	// udc cannot to resolved or is not a field
			this.scanController = scanController;
			this.baggingAreaController = baggingAreaController;
			this.databaseController = databaseController;
			this.paymentController = paymentController;
		
		
		
	}
	
	public void disableStation() {
		
		bnd.disable();
		bns.disable();
		bnsu.disable();
		bnv.disable();
		bcs.disable();
		cr.disable();
		cd.disable();
		cs.disable();
		csu.disable();
		ct.disable();
		cv.disable();
		es.disable();
		rp.disable();
		// error on .disable() as they are undefined for the ones below this comment
		scanController.disable();
		baggingAreaController.disable();
		databaseController.disable();
		paymentController.disable();
		
		
		
		
	}
	
	public void enableStation() {
		
		bnd.enable();
		bns.enable();
		bnsu.enable();
		bnv.enable();
		bcs.enable();
		cr.enable();
		cd.enable();
		cs.enable();
		csu.enable();
		ct.enable();
		cv.enable();
		es.enable();
		rp.enable();
		// error on .enable() as they are undefined for the ones below this comment
		scanController.enable();
		baggingAreaController.enable();
		databaseController.enable();
		paymentController.enable();
		
	}
	
	
	
}
