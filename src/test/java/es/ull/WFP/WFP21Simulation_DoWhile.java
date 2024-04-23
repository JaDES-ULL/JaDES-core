package es.ull.WFP;

import es.ull.simulation.condition.AbstractCondition;
import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.ElementType;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.DoWhileFlow;

/**
 * WFP 21. Example 2: Revelado fotográfico (implemented with a do-while structure)
 * @author Yeray Callero
 * @author Iván Castilla Rodríguez
 *
 */
public class WFP21Simulation_DoWhile extends WFPTestSimulation {

	/**
	 * @param type
	 * @param id
	 * @param detailed
	 */
	public WFP21Simulation_DoWhile(int id) {
		super(id, "WFP21: Structured Loop (DoWhile). EjReveladoFotografico");
	}

	class WFP21Condition extends AbstractCondition<ElementInstance> {
		
		public WFP21Condition() {
			super();
		}
		
    	@Override
    	public boolean check(ElementInstance fe) {
    		return (fe.getElement().getVar("fotosReveladas").getValue(fe).intValue() < 10);
    	}
		
	}
	/* (non-Javadoc)
	 * @see com.ull.simulation.test.WFP.WFPTestSimulationFactory#createModel(Model model)
	 */
	@Override
	protected void createModel() {
		final ResourceType rt0 = getDefResourceType("Maquina revelado");
        
        final WorkGroup wg = new WorkGroup(this, new ResourceType[] {rt0}, new int[] {1});
    	
        getDefResource("Maquina 1", rt0);        
        getDefResource("Maquina 2", rt0);
        
        final AbstractCondition<ElementInstance> cond = new WFP21Condition();
        
    	ActivityFlow act0 = new TestActivityFlow("Revelar foto", 0, wg, false) {
    		@Override
    		public void afterFinalize(ElementInstance fe) {
    			fe.getElement().putVar("fotosReveladas", fe.getElement().getVar(
						"fotosReveladas").getValue(fe).intValue() + 1);
//    			System.out.println(fe.getElement() + ": " + fe.getElement().getVar("fotosReveladas").getValue(fe) +
//    			" fotos reveladas.");
    		}
    	};

        final DoWhileFlow root = new DoWhileFlow(this, act0, cond);

        final ElementType et = getDefElementType("Cliente");
        et.addElementVar("fotosReveladas", 0);
        getDefGenerator(et, root);
 	}

}
