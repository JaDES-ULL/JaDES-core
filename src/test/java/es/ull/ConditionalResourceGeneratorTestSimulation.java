/**
 * 
 */
package es.ull;

import es.ull.simulation.model.ElementType;

import java.util.ArrayList;
import java.util.TreeMap;

import es.ull.simulation.model.ElementInstance;
import es.ull.simulation.model.Simulation;
import es.ull.simulation.model.SimulationPeriodicCycle;
import es.ull.simulation.model.Resource;
import es.ull.simulation.model.ResourceType;
import es.ull.simulation.model.TimeDrivenElementGenerator;
import es.ull.simulation.model.TimeUnit;
import es.ull.simulation.model.WorkGroup;
import es.ull.simulation.model.flow.ActivityFlow;
import es.ull.simulation.model.flow.ParallelFlow;

/**
 * @author Iván Castilla Rodríguez
 *
 */
public class ConditionalResourceGeneratorTestSimulation extends Simulation {
	private final static long DURATION = 5L;
	private final ResourceType rt0;
	private final ResourceType rt1;

	class SpecialActivityFlow extends ActivityFlow {
		final private ResourceType rt1;
		
		public SpecialActivityFlow(Simulation model, String description, ResourceType rt1) {
			super(model, description, 0);
			this.rt1 = rt1;
		}
		
		@Override
		public void afterFinalize(ElementInstance fe) {
			final Resource res = new Resource(simul, "Container later 1");
			res.newTimeTableOrCancelEntriesAdder(rt1).addTimeTableEntry();
			simul.addEvent(res.onCreate(simul.getTs()));
		}
	}

	/**
	 * @param description
	 * @param nExperiments
	 */
	public ConditionalResourceGeneratorTestSimulation() {
		super(0, "Testing conditional generation of resources", TimeUnit.MINUTE, 0L, 60);
		final ElementType et = new ElementType(this, "Crane");

		final ParallelFlow pf = new ParallelFlow(this);

		rt0 = new ResourceType(this, "Container existing");
		rt1 = new ResourceType(this, "Container arriving later");
		final WorkGroup wg0 = new WorkGroup(this, rt0, 1);
		final ActivityFlow req0 = new SpecialActivityFlow(this, "Req 0", rt1);
		req0.newWorkGroupAdder(wg0).withDelay(DURATION).add();
		final WorkGroup wg1 = new WorkGroup(this, rt1, 1);
		final ActivityFlow req1 = new ActivityFlow(this, "Req 1");
		req1.newWorkGroupAdder(wg1).withDelay(DURATION).add();

		pf.link(req0);
		pf.link(req1);

		// Only the first resource is available from the beginning
		final Resource res0 = new Resource(this, "Container " + 0);
		res0.newTimeTableOrCancelEntriesAdder(rt0).addTimeTableEntry();
		
		new TimeDrivenElementGenerator(this, 1, et, pf,
				SimulationPeriodicCycle.newDailyCycle(getTimeUnit()));

		setListeners(req0, req1);
	}

	private void setListeners(ActivityFlow req0, ActivityFlow req1) {
		final ArrayList<Integer> nElems = new ArrayList<>();
		nElems.add(1);
		addInfoReceiver(new CheckElementsListener(nElems));
		final ArrayList<Long> actDuration = new ArrayList<>();
		actDuration.add(DURATION);
		actDuration.add(DURATION);
		final TreeMap<ActivityFlow, Integer> actIndex = new TreeMap<>();
		actIndex.put(req0, 0);
		actIndex.put(req1, 1);
		addInfoReceiver(new CheckActivitiesListener(1, actIndex, actDuration));
		// Prepare structures to check behavior of resources
		final ArrayList<TreeMap<Integer, Long>> roleOns = new ArrayList<>();
		final TreeMap<Integer, Long> roleOns0 = new TreeMap<>();
		roleOns0.put(rt0.getIdentifier(), 0L);
		roleOns.add(roleOns0);
		final TreeMap<Integer, Long> roleOns1 = new TreeMap<>();
		roleOns1.put(rt1.getIdentifier(), DURATION);
		roleOns.add(roleOns1);
		final ArrayList<TreeMap<Integer, Long>> roleOffs = new ArrayList<>();
		final TreeMap<Integer, Long> roleOffs0 = new TreeMap<>();
		roleOffs.add(roleOffs0);
		final TreeMap<Integer, Long> roleOffs1 = new TreeMap<>();
		roleOffs.add(roleOffs1);
		addInfoReceiver(new CheckResourcesListener(2, roleOns, roleOffs));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ConditionalResourceGeneratorTestSimulation().start();
	}

}
