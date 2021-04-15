
package org.lsmr.software;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.BanknoteSlot;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.CardReader;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.CoinTray;
import org.lsmr.selfcheckout.devices.CoinValidator;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;


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

	public BlockStation(SelfCheckoutStation sc, BanknoteDispenser bnd, BanknoteSlot bns, BanknoteStorageUnit bnsu, BanknoteValidator bnv, 
			BarcodeScanner bcs, CardReader cr, CoinDispenser cd, CoinSlot cs, CoinStorageUnit csu, CoinTray ct, 
			CoinValidator cv, ElectronicScale es, ReceiptPrinter rpr) {
		
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
			this.rp = rp;
		
		
		
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
	
		
	}
	
	
	
}
