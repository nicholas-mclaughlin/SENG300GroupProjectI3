package org.lsmr.software;

import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.listeners.ElectronicScaleListener;

public class WeightDiscrepencyApproval {
	
	private ElectronicScale es;
	private ElectronicScaleListener esl;	// is not used
	
	
	public WeightDiscrepencyApproval(ElectronicScale es, ElectronicScaleListener esl) {
		
		this.es = es;
		this.esl = esl;
		
		
	}
	
	//manage overload exception
	
	public void overloadCatch() {
		
		try {
		
			es.getCurrentWeight();
			//int sensitivty = es.getSensitivity();
			
		}catch(OverloadException e) {
			
			if ( !(approve())) {
				
				// error: Overload is unhandled
				throw new OverloadException();
				
			}
			
		}
			
	}
	
	public boolean approve() {
		
		//from attendant console
		// error: Login is undfined
		return login();
		
	}
	
	

}
