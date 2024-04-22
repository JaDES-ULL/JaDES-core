package com.ull.WFP;

import com.ull.simulation.condition.Condition;
import com.ull.simulation.condition.NotCondition;
import com.ull.simulation.condition.TrueCondition;
import com.ull.simulation.model.ElementInstance;
import com.ull.simulation.model.ResourceType;
import com.ull.simulation.model.WorkGroup;
import com.ull.simulation.model.flow.ActivityFlow;
import com.ull.simulation.model.flow.MultiChoiceFlow;

/**
 * WFP 6. Example 1: Llamadas emergencia
 * @author Yeray Callero
 * @author Iv�n Castilla
 *
 */
public class WFP06Simulation extends WFPTestSimulation {
	
	public WFP06Simulation(int id) {
		super(id, "WFP6: Multichoice. EjLamadasEmergencia");
    }
    
    protected void createModel() {
        ResourceType rt = getDefResourceType("Operador");
        WorkGroup wg = new WorkGroup(this, new ResourceType[] {rt}, new int[] {1});

        ActivityFlow act0 = getDefActivity("Recepcion de llamada", wg, false);
        ActivityFlow act1 = getDefActivity("Envio policias", wg, false);
        ActivityFlow act2 = getDefActivity("Envio ambulancias", wg, false);
        ActivityFlow act3 = getDefActivity("Envio bomberos", wg, false);
        
        getDefResource("Operador1", rt);
        getDefResource("Operador2", rt);
        
        MultiChoiceFlow mulcho1 = new MultiChoiceFlow(this);
        
        act0.link(mulcho1);
        Condition<ElementInstance> falseCond = new NotCondition<ElementInstance>(new TrueCondition<ElementInstance>());
        mulcho1.link(act1);
        mulcho1.link(act2, falseCond);
        mulcho1.link(act3);

        getDefGenerator(getDefElementType("Emergencia"), act0);
//        getSimulation().addInfoReceiver(new WFP06CheckView(getSimulation(), detailed));
    }
	
}

