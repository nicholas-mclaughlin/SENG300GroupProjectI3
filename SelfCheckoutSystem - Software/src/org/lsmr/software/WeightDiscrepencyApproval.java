package org.lsmr.software;
import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.listeners.ElectronicScaleListener;

public class WeightDiscrepencyApproval {
	
	private ElectronicScale es;
	private ElectronicScaleListener esl;
	
	
	public WeightDiscrepencyApproval(ElectronicScale es) {		
		this.es = es;
		
	}
	
	//manage overload exception
	
	public void overloadCatch() throws Exception {
		
		try {
		
			es.getCurrentWeight();
			//int sensitivty = es.getSensitivity();
			
		}catch(OverloadException e) {
			
			if ( !(approve())) {
				
				throw new Exception();
				
			}
			
		}
			
	}
	
	public boolean approve() {
		System.out.println("Please continue bagging items");
//		from attendant console
		return true;
		
	}
	
	
	public boolean dontApprove() {
//		from attendant console
		return false;
		
	}
	

}
