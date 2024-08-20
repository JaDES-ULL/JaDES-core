/**
 * 
 */
package es.ull;

import es.ull.simulation.functions.TimeFunctionFactory;
import es.ull.simulation.inforeceiver.StdInfoView;

import java.util.ArrayList;
import java.util.TreeMap;

import es.ull.CheckResourcesListener.ResourceUsageTimeStamps;
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
import es.ull.simulation.model.flow.ITaskFlow;

/**
 * Checks interruptible activities. Activities are interrupted when a resource stops being available before the activity has finalized.
 * The test involves a single activity that lasts more than the available time of a resource. The resource is available cyclically, so
 * the activity resumes every time the resource is available again. 
 * @author Iván Castilla Rodríguez
 *
 */
public class InterruptibleActivitiesTestSimulation extends Simulation {
	static private final TimeUnit unit = TimeUnit.MINUTE;
	static private final int NELEM = 1;
	static private final long ACT_DURATION = 101;
	static private final long RES_START = 20L;
	static private final long RES_DURATION = 40L;
	static private final long RES_CYCLE = 100L;
	static private final long ELEM_CYCLE = 800L;
	static private final long END = 400L;


	public InterruptibleActivitiesTestSimulation() {
		super(0, "Testing interruptible activities", unit, TimeStamp.getZero(), new TimeStamp(TimeUnit.MINUTE, END));
		final ResourceType rt = new ResourceType(this, "RT0");
		final WorkGroup wg = new WorkGroup(this, new ResourceType[] {rt}, new int[] {1});

		final ActivityFlow act = new ActivityFlow(this, "ACT0", 0, false, true);
		act.newWorkGroupAdder(wg).withDelay(ACT_DURATION).add();
		final SimulationPeriodicCycle creatorCycle = new SimulationPeriodicCycle(unit, 0, new SimulationTimeFunction(
				unit, "ConstantVariate", ELEM_CYCLE), 0);
		final SimulationPeriodicCycle resCycle = new SimulationPeriodicCycle(unit, RES_START, new SimulationTimeFunction(
				unit, "ConstantVariate", RES_CYCLE), 0);
		new Resource(this, "RES0").newTimeTableOrCancelEntriesAdder(rt).withDuration(resCycle, RES_DURATION).addTimeTableEntry();
		new TimeDrivenElementGenerator(this, TimeFunctionFactory.getInstance("ConstantVariate", NELEM),
							new ElementType(this, "ET0", 0), act, creatorCycle);
	
		setListeners(act, rt);
	}

	private void setListeners(final ActivityFlow act, final ResourceType rt) {
		ArrayList<Integer> nElems = new ArrayList<>();
		nElems.add(NELEM);
		addInfoReceiver(new CheckElementsListener(nElems));
		final ArrayList<Long> actDuration = new ArrayList<>();
		actDuration.add(ACT_DURATION);
		final TreeMap<ITaskFlow, Integer> actIndex = new TreeMap<>();
		actIndex.put(act, 0);
		addInfoReceiver(new CheckActivitiesListener(1, actIndex, actDuration));
		// Prepare structures to check behavior of resources
		final ArrayList<ResourceUsageTimeStamps> roleOns = new ArrayList<>();
		final ArrayList<ResourceUsageTimeStamps> roleOffs = new ArrayList<>();
		long start = RES_START;
		int activations = (int) ((END - RES_START) / RES_CYCLE) + 1;
		long [] roleOnTimestamps = new long[activations];
		activations = (int) ((END - RES_START - RES_DURATION) / RES_CYCLE) + 1; 
		long [] roleOffTimestamps = new long[activations];
		for (int i = 0; i < (int) ((END - RES_START) / RES_CYCLE) + 1; i++) {
			roleOnTimestamps[i] = start;
			if (start + RES_DURATION < END)
				roleOffTimestamps[i] = start + RES_DURATION;
			start += RES_CYCLE;
		}
		roleOns.add(new ResourceUsageTimeStamps(0, rt.getIdentifier(), roleOnTimestamps));
		roleOffs.add(new ResourceUsageTimeStamps(0, rt.getIdentifier(), roleOffTimestamps));	
		addInfoReceiver(new CheckResourcesListener(roleOns, roleOffs));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Simulation sim = new InterruptibleActivitiesTestSimulation();
		sim.addInfoReceiver(new StdInfoView());
		sim.run();
	}

}
