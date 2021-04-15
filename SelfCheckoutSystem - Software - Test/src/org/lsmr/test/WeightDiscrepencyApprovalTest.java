

import static org.junit.Assert.*;

import org.junit.Test;
import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.ElectronicScale;

public class WeightDiscrepencyApprovalTest {
	
	

	@Test
	public void overloadedCaughtapproved() {
		
		AttendantControleConsole acc = new AttendantControlConsole();
		ElectronicScale es = new ElectronicScale(1000, 2);
		WeightDiscrepencyApproval wd = new WeightDiscrepencyApproval(es, acc);
		Barcode b = new Barcode("1234");
		BarcodedItem bi1 = new BarcodedItem(b, 500);
		BarcodedItem bi2 = new BarcodedItem(b, 600);
		Item i1 = bi1;
		Item i2 = bi2;
		
		es.add(i1);
		es.add(i2);
		
		boolean result = wd.overloadCatch();
		
		assertEquals(true, result);
		
	}
	
	@Test
	public void overloadedCaughtNotApproved() {
		
		AttendantControleConsole acc = new AttendantControlConsole();
		ElectronicScale es = new ElectronicScale(1000, 2);
		WeightDiscrepencyApproval wd = new WeightDiscrepencyApproval(es, acc);
		Barcode b = new Barcode("1234");
		BarcodedItem bi1 = new BarcodedItem(b, 500);
		BarcodedItem bi2 = new BarcodedItem(b, 400);
		Item i1 = bi1;
		Item i2 = bi2;
		
		es.add(i1);
		es.add(i2);
		
		boolean result = wd.overloadCatch();
		
		assertEquals(false, result);
		
	}
	
	

}
