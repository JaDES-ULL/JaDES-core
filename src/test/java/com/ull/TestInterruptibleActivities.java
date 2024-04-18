/**
 * 
 */
package com.ull;

import com.ull.functions.TimeFunctionFactory;
import com.ull.simulation.factory.SimulationFactory;
import com.ull.simulation.inforeceiver.StdInfoView;
import com.ull.simulation.model.Experiment;
import com.ull.simulation.model.ResourceType;
import com.ull.simulation.model.Simulation;
import com.ull.simulation.model.SimulationPeriodicCycle;
import com.ull.simulation.model.SimulationTimeFunction;
import com.ull.simulation.model.TimeStamp;
import com.ull.simulation.model.TimeUnit;
import com.ull.simulation.model.WorkGroup;
import com.ull.simulation.model.flow.ActivityFlow;
import com.ull.simulation.model.flow.ParallelFlow;

/**
 * @author Iv�n Castilla Rodr�guez
 *
 */
public class TestInterruptibleActivities {
	static final TimeUnit unit = TimeUnit.MINUTE;
	static final int NACT = 1;
	static final int NELEMT = 1;
	static final int NELEM = 2;
	static final int NRES = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Experiment("Testing interruptible activities", 1) {

			@Override
			public Simulation getSimulation(int ind) {
				Simulation sim = null;
				SimulationFactory factory = new SimulationFactory(ind, "Testing interruptible activities", unit, TimeStamp.getZero(), new TimeStamp(TimeUnit.MINUTE, 400));
				sim = factory.getSimulation();
				
		        ResourceType rt = factory.getResourceTypeInstance("RT0");
		        WorkGroup wg = factory.getWorkGroupInstance(new ResourceType[] {rt}, new int[] {1});

				ActivityFlow acts[] = new ActivityFlow[NACT];
				for (int i = 0; i < NACT; i++) {
					acts[i] = (ActivityFlow)factory.getFlowInstance("ActivityFlow", "ACT" + i, i / 2, false, true);
			    	acts[i].newWorkGroupAdder(wg).withDelay(101).add();
				}
				SimulationPeriodicCycle c1 = new SimulationPeriodicCycle(unit, 0, new SimulationTimeFunction(unit, "ConstantVariate", 200), 0);
				SimulationPeriodicCycle c2 = new SimulationPeriodicCycle(unit, 20, new SimulationTimeFunction(unit, "ConstantVariate", 100), 0);
				for (int i = 0; i < NRES; i++)
					factory.getResourceInstance("RES" + i).newTimeTableOrCancelEntriesAdder(rt).withDuration(c2, 40).addTimeTableEntry();;
				ParallelFlow meta = (ParallelFlow)factory.getFlowInstance("ParallelFlow");
				for (int i = 0; i < NACT; i++) {
					meta.link(acts[i]);
				}
				for (int i = 0; i < NELEMT; i++)
					factory.getTimeDrivenElementGeneratorInstance(TimeFunctionFactory.getInstance("ConstantVariate", NELEM), 
									factory.getElementTypeInstance("ET" + i, i), meta, c1);
				
				sim.addInfoReceiver(new StdInfoView());
				return sim;
			}
			
		}.start();
	}

}
