/**
 * 
 */
package es.ull;

import es.ull.simulation.functions.TimeFunctionFactory;
import es.ull.simulation.inforeceiver.StdInfoView;

import java.util.ArrayList;
import java.util.TreeMap;

import es.ull.simulation.experiment.BaseExperiment;
import es.ull.simulation.experiment.CommonArguments;
import es.ull.simulation.model.ElementType;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.SimulationPeriodicCycle;
import es.ull.simulation.model.SimulationTimeFunction;
import es.ull.simulation.model.TimeDrivenElementGenerator;
import es.ull.simulation.model.TimeStamp;
import es.ull.simulation.model.TimeUnit;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class InterruptibleActivitiesTestSimulation extends Simulation {
	static private final TimeUnit unit = TimeUnit.MINUTE;
	static private final int NELEM = 1;
	static private final long ACT_DURATION = 101;
	static private final long RES_START = 20L;
	static private final long RES_DURATION = 40L;

	public InterruptibleActivitiesTestSimulation() {
		super(0, "Testing interruptible activities", unit, TimeStamp.getZero(), new TimeStamp(TimeUnit.MINUTE, 400));
		final ResourceType rt = new ResourceType(this, "RT0");
		final WorkGroup wg = new WorkGroup(this, new ResourceType[] {rt}, new int[] {1});

		final ActivityFlow act = new ActivityFlow(this, "ACT0", 0, false, true);
		act.newWorkGroupAdder(wg).withDelay(ACT_DURATION).add();
		final SimulationPeriodicCycle c1 = new SimulationPeriodicCycle(unit, 0, new SimulationTimeFunction(
				unit, "ConstantVariate", 800), 0);
		final SimulationPeriodicCycle c2 = new SimulationPeriodicCycle(unit, RES_START, new SimulationTimeFunction(
				unit, "ConstantVariate", 100), 0);
		new Resource(this, "RES0").newTimeTableOrCancelEntriesAdder(rt).withDuration(c2, RES_DURATION).addTimeTableEntry();
		new TimeDrivenElementGenerator(this, TimeFunctionFactory.getInstance("ConstantVariate", NELEM),
							new ElementType(this, "ET0", 0), act, c1);
	
		setListeners(act, rt);
	}

	private void setListeners(final ActivityFlow act, final ResourceType rt) {
		ArrayList<Integer> nElems = new ArrayList<>();
		nElems.add(NELEM);
		addInfoReceiver(new CheckElementsListener(nElems));
/*		final ArrayList<Long> actDuration = new ArrayList<>();
		actDuration.add(ACT_DURATION);
		final TreeMap<ActivityFlow, Integer> actIndex = new TreeMap<>();
		actIndex.put(act, 0);
		addInfoReceiver(new CheckActivitiesListener(1, actIndex, actDuration));
		// Prepare structures to check behavior of resources
/* 		final ArrayList<TreeMap<Integer, Long>> roleOns = new ArrayList<>();
		final TreeMap<Integer, Long> roleOns0 = new TreeMap<>();
		roleOns0.put(rt.getIdentifier(), RES_START);
		roleOns.add(roleOns0);
		final ArrayList<TreeMap<Integer, Long>> roleOffs = new ArrayList<>();
		final TreeMap<Integer, Long> roleOffs0 = new TreeMap<>();
		roleOffs0.put(rt.getIdentifier(), RES_START + RES_DURATION);
		roleOffs.add(roleOffs0);
		addInfoReceiver(new CheckResourcesListener(2, roleOns, roleOffs));
	*/	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        final CommonArguments arguments = new CommonArguments();
		new BaseExperiment("Testing interruptible activities", arguments) {

			@Override
			public void runExperiment(int ind) {
				Simulation sim = new InterruptibleActivitiesTestSimulation();
				sim.addInfoReceiver(new StdInfoView());
				sim.run();
			}
			
		}.run();
	}

}