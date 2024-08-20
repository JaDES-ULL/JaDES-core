/**
 * 
 */
package es.ull;

import es.ull.simulation.functions.TimeFunctionFactory;
import es.ull.simulation.experiment.BaseExperiment;
import es.ull.simulation.experiment.CommonArguments;
import es.ull.simulation.factory.SimulationFactory;
import es.ull.simulation.inforeceiver.StdInfoView;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.SimulationPeriodicCycle;
import es.ull.simulation.model.SimulationTimeFunction;
import es.ull.simulation.model.TimeUnit;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.ParallelFlow;

/**
 * A test for elements with different priorities.
 * @author Iván Castilla Rodríguez
 *
 */
public class TestPriorityElement {
	static private final int NACT = 40;
	static private final int NELEMT = 4;
	static private final int NELEM = 100;
	static private final int NRES = 20;
	static private final TimeUnit unit = TimeUnit.MINUTE;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        final CommonArguments arguments = new CommonArguments();
		new BaseExperiment("Testing priority", arguments) {

			@Override
			public void runExperiment(int ind) {
				SimulationFactory factory = new SimulationFactory(ind, "Testing Elements with priority", unit,
						0, 200);
				Simulation sim = factory.getSimulation();
				
		        ResourceType rt = factory.getResourceTypeInstance("RT0");
		        WorkGroup wg = factory.getWorkGroupInstance(new ResourceType[] {rt}, new int[] {2});
				ActivityFlow acts[] = new ActivityFlow[NACT];
				for (int i = 0; i < NACT; i++) {
					acts[i] = (ActivityFlow)factory.getFlowInstance("ActivityFlow", "ACT" + i, i / 2,
							false, false);
					acts[i].newWorkGroupAdder(wg).withDelay(10).add();
				}
				SimulationPeriodicCycle c1 = new SimulationPeriodicCycle(unit, 0, new SimulationTimeFunction(
						unit, "ConstantVariate", 200), 0);
				SimulationPeriodicCycle c2 = new SimulationPeriodicCycle(unit, 20, new SimulationTimeFunction(
						unit, "ConstantVariate", 100), 0);
				for (int i = 0; i < NRES; i++)
					factory.getResourceInstance("RES" + i).newTimeTableOrCancelEntriesAdder(rt).withDuration(
							c2, 40).addTimeTableEntry();;

				ParallelFlow meta = (ParallelFlow)factory.getFlowInstance("ParallelFlow");
				for (int i = 0; i < NACT; i++) {
					meta.link(acts[i]);
				}
				for (int i = 0; i < NELEMT; i++)
					factory.getTimeDrivenElementGeneratorInstance(TimeFunctionFactory.getInstance(
							"ConstantVariate", NELEM),
							factory.getElementTypeInstance("ET" + i, i), meta, c1);
				
				StdInfoView debugView = new StdInfoView();
				sim.addInfoReceiver(debugView);
				sim.run();
			}
			
		}.run();
	}

}
