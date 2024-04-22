/**
 * 
 */
package es.ull.WFP;

import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.SynchronizedMultipleInstanceFlow;

/**
 * WFP 13. Multiple Instances with a priori design-time knowledge
 * @author Yeray Callero
 * @author Iv�n Castilla
 *
 */
public class WFP13Simulation extends WFPTestSimulation {
	final static int RES = 6;

	/**
	 * @param type
	 * @param id
	 * @param detailed
	 */
	public WFP13Simulation(int id) {
		super(id, "WFP13: Multiple Instances with a priori design-time knowledge");
	}

	@Override
	protected void createModel() {
		ResourceType rt0 = getDefResourceType("Director");
    	WorkGroup wg = new WorkGroup(this, new ResourceType[] {rt0}, new int[] {1});
    	
    	ActivityFlow act0 = getDefActivity("Sign Annual Report", wg);
    	ActivityFlow act1 = getDefActivity("Check acceptance", wg);
    	
    	for (int i = 0; i < RES; i++)
    		getDefResource("Director" + i, rt0);

		SynchronizedMultipleInstanceFlow root = new SynchronizedMultipleInstanceFlow(this, 6);
    	root.addBranch(act0);
    	root.link(act1);

    	getDefGenerator(getDefElementType("ET0"), root);
	}

}
