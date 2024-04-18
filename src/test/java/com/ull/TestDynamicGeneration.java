/**
 * 
 */
package com.ull;

import com.ull.simulation.condition.Condition;
import com.ull.simulation.condition.NotCondition;
import com.ull.simulation.factory.SimulationFactory;
import com.ull.simulation.model.ElementInstance;
import com.ull.simulation.model.Experiment;
import com.ull.simulation.model.Resource;
import com.ull.simulation.model.ResourceType;
import com.ull.simulation.model.Simulation;
import com.ull.simulation.model.SimulationPeriodicCycle;
import com.ull.simulation.model.TimeUnit;
import com.ull.simulation.model.WorkGroup;
import com.ull.simulation.model.flow.ActivityFlow;

class TestDynamicGenerationExperiment extends Experiment {
	
	public TestDynamicGenerationExperiment() {
		super("Test Dynamic Generation", 1);
	}

	@Override
	public Simulation getSimulation(int ind) {
		TimeUnit unit = TimeUnit.MINUTE;
		SimulationFactory factory = new SimulationFactory(ind, "Test Dynamic", unit, 0, 1);
		
		ResourceType rt0 = factory.getResourceTypeInstance("RT0");
		ResourceType rt1 = factory.getResourceTypeInstance("RT1");
		
		Resource r0 =  factory.getResourceInstance("Res0");
		r0.newTimeTableOrCancelEntriesAdder(rt0).withDuration(SimulationPeriodicCycle.newDailyCycle(unit), 1).addTimeTableEntry();
		Resource r1 = factory.getResourceInstance("Res1");
		r1.newTimeTableOrCancelEntriesAdder(rt1).withDuration(SimulationPeriodicCycle.newDailyCycle(unit), 1).addTimeTableEntry();
		
		WorkGroup wg0 = factory.getWorkGroupInstance(new ResourceType [] {rt0, rt1}, new int[] {1,1});
		
		Condition<ElementInstance> cond = factory.getCustomizedConditionInstance(null, "false");
		ActivityFlow act0 = (ActivityFlow)factory.getFlowInstance("ActivityFlow", "ACT0");
    	act0.newWorkGroupAdder(wg0).withDelay(10).withCondition(new NotCondition<ElementInstance>(cond)).add();
		
		factory.getElementTypeInstance("ET0");
		factory.getFlowInstance("SingleFlow", act0);
		return factory.getSimulation();
	}
}

/**
 * @author Iv�n Castilla Rodr�guez
 *
 */
public class TestDynamicGeneration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestDynamicGenerationExperiment().start();

	}

}
