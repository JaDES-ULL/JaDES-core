package com.ull.WFP;

import com.ull.simulation.condition.Condition;
import com.ull.simulation.condition.NotCondition;
import com.ull.simulation.condition.TrueCondition;
import com.ull.simulation.model.ElementInstance;
import com.ull.simulation.model.ResourceType;
import com.ull.simulation.model.WorkGroup;
import com.ull.simulation.model.flow.ActivityFlow;
import com.ull.simulation.model.flow.ExclusiveChoiceFlow;

/**
 * WFP 4 Example 1: Sistema Votaci�n
 * @author Yeray Callero
 * @author Iv�n Castilla
 *
 */
public class WFP04Simulation extends WFPTestSimulation {
	int ndays;
	
	public WFP04Simulation(int id) {
		super(id, "WFP4: Exclusive Choice. EjSistemaVotacion");
    }
    
    protected void createModel() {
        ResourceType rt = getDefResourceType("Encargado");
        WorkGroup wg = new WorkGroup(this, new ResourceType[] {rt}, new int[] {1});
        
        ActivityFlow act0 = getDefActivity("Celebrar elecciones", wg, false);
        ActivityFlow act1 = getDefActivity("Recuentos de votos", wg, false);
        ActivityFlow act2 = getDefActivity("Declarar resultados", wg, false);
        
        getDefResource("Encargado 1", rt); 

        ExclusiveChoiceFlow excho1 = new ExclusiveChoiceFlow(this);
        
        act0.link(excho1);
        Condition<ElementInstance> falseCond = new NotCondition<ElementInstance>(new TrueCondition<ElementInstance>());
        excho1.link(act1);
        excho1.link(act2, falseCond);

        getDefGenerator(getDefElementType("Votante"), act0);

    }
	
}
