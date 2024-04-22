package com.ull.WFP;


import com.ull.functions.TimeFunctionFactory;
import com.ull.simulation.model.ElementType;
import com.ull.simulation.model.ResourceType;
import com.ull.simulation.model.WorkGroup;
import com.ull.simulation.model.flow.ActivityFlow;
import com.ull.simulation.model.flow.ForLoopFlow;

/**
 * WFP 21. Example 2: Revelado fotogr�fico (implemented with a for structure)
 * @author Yeray Callero
 * @author Iv�n Castilla
 *
 */
public class WFP21Simulation_For extends WFPTestSimulation {

	/**
	 * @param type
	 * @param id
	 * @param detailed
	 */
	public WFP21Simulation_For(int id) {
		super(id, "WFP21: Structured Loop (For). EjReveladoFotografico");
	}

	/* (non-Javadoc)
	 * @see com.ull.simulation.test.WFP.WFPTestSimulationFactory#createModel(Model model)
	 */
	@Override
	protected void createModel() {
		ResourceType rt0 = getDefResourceType("Maquina revelado");
        
        WorkGroup wg = new WorkGroup(this, new ResourceType[] {rt0}, new int[] {1});
    	
    	ActivityFlow act0 = getDefActivity("Revelar foto", wg, false);

        getDefResource("Maquina 1", rt0);        
        getDefResource("Maquina 2", rt0);
        
        ForLoopFlow root = new ForLoopFlow(this, act0, TimeFunctionFactory.getInstance("ConstantVariate", 2));

        ElementType et = getDefElementType("Cliente");
        getDefGenerator(et, root);
	}
}
