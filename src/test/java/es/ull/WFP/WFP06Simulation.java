package es.ull.WFP;

import es.ull.StandardTestSimulation;
import es.ull.simulation.condition.AbstractCondition;
import es.ull.simulation.condition.NotCondition;
import es.ull.simulation.condition.TrueCondition;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.MultiChoiceFlow;

/**
 * WFP 6. Example 1: Llamadas emergencia
 * @author Yeray Callero
 * @author Iván Castilla Rodríguez
 *
 */
public class WFP06Simulation extends StandardTestSimulation {
	
	public WFP06Simulation(int id, TestWFP.TestWFPArguments args) {
		super(id, "WFP6: Multichoice. EjLamadasEmergencia", args);
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
        AbstractCondition<ElementInstance> falseCond = new NotCondition<ElementInstance>(new TrueCondition<ElementInstance>());
        mulcho1.link(act1);
        mulcho1.link(act2, falseCond);
        mulcho1.link(act3);

        getDefGenerator(getDefElementType("Emergencia"), act0);
//        getSimulation().addInfoReceiver(new WFP06CheckView(getSimulation(), detailed));
    }
	
}

