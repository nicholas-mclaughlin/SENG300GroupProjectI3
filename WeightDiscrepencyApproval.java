package org.lsmr.software;
import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.*;

public class WeightDiscrepencyApproval {
	
	private ElectronicScale es;
	private ElectronicScale esl;
	
	
	public WeightDiscrepencyApproval(ElectronicScale es, ElectronicScaleListener esl) {
		
		this.es = es;
		this.esl = esl;
		
		
	}
	
	//manage overload exception
	
	public void overloadCatch() {
		
		try {
		
			es.getCurrentWeight()
			//int sensitivty = es.getSensitivity();
			
		}catch(OverloadException e) {
			
			if ( !(approve())) {
				
				throw new OverLoadException();
				
			}
			
		}
			
	}
	
	public boolean approve() {
		
		//from attendant console
		return logIn();
		
	}
	
	

}
